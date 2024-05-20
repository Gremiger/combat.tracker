// Declare combatStarted globally
let combatStarted = false;
let currentTurn = 0;

document.addEventListener("DOMContentLoaded", function() {
        // Listen clicks in all the document
        document.addEventListener('click', function(event) {
            // Obtain elements with 'modal-content' class
            const modalsContent = document.querySelectorAll('.modal-content');

            // Check if the click was inside any modal content
            let isClickInsideModalContent = Array.from(modalsContent).some(modalContent => {
                return modalContent.contains(event.target);
            });

            // Only execute it if the click was outside any modal content (and the click was not in a btn
            if (!isClickInsideModalContent && isAnyModalOpen() && event.target.className !== 'btn-medieval-primary') {
                closeAllModals();
            }
        });

        // Get a reference to the image element
        const themeIcon = document.getElementById('theme-icon');

        // Check if the theme preference is stored in localStorage
        const themePreference = localStorage.getItem('themePreference');

        // Apply the theme based on the stored preference
        if (themePreference === 'light-theme') {
            // Apply light theme
            document.body.classList.add('light-theme');
            document.body.classList.remove('dark-theme');
        } else {
            // Apply dark theme (default)
            document.body.classList.add('dark-theme');
            themeIcon.style.filter = 'invert(1)';
            document.body.classList.remove('light-theme');
        }

        // Toggle theme function
        function toggleTheme() {
            const body = document.body;
            const currentTheme = body.classList.contains('light-theme') ? 'light-theme' : 'dark-theme';

            // Toggle between light and dark themes
            body.classList.toggle('light-theme');
            body.classList.toggle('dark-theme');

            // Update the theme preference in localStorage
            const newTheme = body.classList.contains('light-theme') ? 'light-theme' : 'dark-theme';
            localStorage.setItem('themePreference', newTheme);

            // Apply or remove the filter based on the new theme
            themeIcon.style.filter = newTheme === 'dark-theme' ? 'invert(1)' : '';
        }

        // Add event listener to the theme switcher button
        const themeSwitcher = document.querySelector('.theme-switcher');
        themeSwitcher.addEventListener('click', toggleTheme);

        // Function to parse query parameters from URL
        function getQueryParam(param) {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get(param);
        }

        // Check if combat is already started based on query parameter
        const isCombatStartedParam = getQueryParam('combatStarted');
        if (isCombatStartedParam === 'true') {
            // Set combatStarted flag to true
            combatStarted = true;
            // Get the current turn from query parameter
            currentTurn = parseInt(getQueryParam('currentTurn'));
            // Highlight the current combatant
            highlightCurrentCombatant(false, false);
            // Enable/disable buttons based on combat state
            toggleNextButton();
            let startButton = document.getElementById("startButton");
            startButton.innerHTML = "Stop Combat";
        }

        // Check if there are at least two combatants and enable the "Start Combat" button accordingly
        var combatants = document.querySelectorAll("#current-order li");
        var startButton = document.getElementById("startButton");
        if (combatants.length >= 2) {
            startButton.disabled = false;
        }
});

// Function to open a modal
function openModal(modalName) {
    document.getElementById(modalName).style.display = "block";
}

// Function to close a modal
function closeModal(modalName) {
    document.getElementById(modalName).style.display = "none";
}

function isAnyModalOpen() {
    const modals = document.querySelectorAll('.modal');
    return Array.from(modals).some(modal => modal.style.display !== 'none');
}

// Function to close all modals
function closeAllModals() {
    var modals = document.querySelectorAll('.modal');
    modals.forEach(function(modal) {
        modal.style.display = 'none';
    });
}

/**
* This function is used to heal a combatant in a game.
* It sends a POST request to the '/healCombatant' endpoint with the combatant's ID and the amount to heal.
* If the request is successful, it updates the combatant's health in the DOM.
* If the request fails, it logs the error message.
*
* @param {Object} button - The button element that triggered the function.
*/
function healCombatant(button){
    var parent = button.parentElement;
    var combatantID = parent.id;
    var amount = parent.children.dmg_heal.value
    console.log('healing combatant ' + combatantID + " by: " + amount);
    axios.post('/healCombatant', 'combatantId=' + encodeURIComponent(combatantID) + '&amount=' + encodeURIComponent(amount), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    .then(function(response) {
        console.log('Combatant Healed: ', response.data);
        // Update the temporal health in the DOM
        var temporalHealthElement = parent.querySelector('.temporal-health');
        if (temporalHealthElement) {
            temporalHealthElement.innerText = response.data.newTemporalHealth;
        }
    })
    .catch(function(error) {
        console.error('Error in healing: ', error.response.data);
    });
}

/**
* This function is used to damage a combatant in a game.
* It sends a POST request to the '/dmgCombatant' endpoint with the combatant's ID and the amount of damage.
* If the request is successful, it updates the combatant's health in the DOM.
* If the request fails, it logs the error message.
*
* @param {Object} button - The button element that triggered the function.
*/
function dmgCombatant(button){
    const parent = button.parentElement;
    const combatantID = parent.id;
    const amount = parent.children.dmg_heal.value;

    axios.post('/dmgCombatant', `combatantId=${encodeURIComponent(combatantID)}&amount=${encodeURIComponent(amount)}`, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        console.log('Combatant Damaged: ', response.data);
        const temporalHealthElement = parent.querySelector('.temporal-health');
        if (temporalHealthElement) {
            temporalHealthElement.innerText = response.data.newTemporalHealth;
        }
    })
    .catch(error => {
        console.error('Error in damaging: ', error.response.data);
    });
}

function toggleCombat(){
    let startButton = document.getElementById("startButton");
    if (combatStarted){
        if (confirm("Are you sure you want to end the combat?")){
            startButton.innerHTML = "Start Combat";
            combatStarted = false;
            axios.post('/combat/stop');
            currentTurn = -1;
            //Redirect to /dnd
            window.location.href = "/dnd";
        }
    } else {
        startButton.innerHTML = "Stop Combat";
        combatStarted = true;
        axios.post('/combat/start');
        currentTurn = 0;
        highlightCurrentCombatant(true);
    }
    toggleNextButton();
}

// Function to toggle buttons based on combat status
function toggleNextButton() {
    var nextButton = document.getElementById("nextButton");
    nextButton.disabled = !combatStarted;
}

function nextTurn() {
    axios.post('/combat/next')
     .then(response => {
        console.log('next turn:', response.data);
        highlightCurrentCombatant();
     })
     .catch(error => {
        console.error('Error starting turn:', error);
     });
}



function highlightCurrentCombatant(firstTurn = false, nextTurn = true) {
    // Remove highlight from previously highlighted combatant
    let previousCombatant = document.querySelector('.combatant.current');
    if (previousCombatant) {
        previousCombatant.classList.remove('current');
    }
    // Highlight the current combatant
    let combatants = document.querySelectorAll('.combatant');
    if (firstTurn) {
        currentTurn = 0;
    } else if (nextTurn) {
        currentTurn = (currentTurn + 1) % combatants.length;
    } else {
        currentTurn = currentTurn % combatants.length;
    }
    let currentCombatant = combatants[currentTurn];
    currentCombatant.classList.add('current');
}