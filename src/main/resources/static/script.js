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
 * Updates the combatants list in the DOM based on the updated combatants data from the server.
 * @param {Array} combatants - The updated combatants list.
 */
function updateCombatantsList(combatants) {
    const combatantsContainer = document.getElementById('combatants-container');
    combatantsContainer.innerHTML = ''; // Clear existing combatants list

    combatants.forEach(combatant => {
        const combatantCard = document.createElement('div');
        combatantCard.id = combatant.id;
        combatantCard.classList.add('combatant-card');

        const combatantInfo = `
            <div class="combatant-header">
                <span class="combatant-name">${combatant.name}</span>
                <span class="combatant-type">${combatant.type}</span>
            </div>
            <div class="combatant-stats">
                <div class="stat-group">
                    <div class="stat-label">Initiative</div>
                    <div class="stat-value currentInitiative">${combatant.initiative}</div>
                </div>
                <div class="stat-group">
                    <div class="stat-label">Max HP</div>
                    <div class="stat-value max-health">${combatant.health}</div>
                </div>
                <div class="stat-group">
                    <div class="stat-label">Current HP</div>
                    <div class="stat-value temporal-health">${combatant.temporalHealth}</div>
                </div>
            </div>
            <div class="combatant-actions">
                <div class="action-row">
                    <input type="number" class="updatedInit" min="0" placeholder="Init">
                    <button onclick="setInitiative(this)" class="btn-action btn-change">Change Initiative</button>
                </div>
                <div class="action-row">
                    <input type="number" class="dmg-heal" min="0" placeholder="HP">
                    <button onclick="healCombatant(this)" class="btn-action btn-heal">HEAL</button>
                    <button onclick="dmgCombatant(this)" class="btn-action btn-damage">DAMAGE</button>
                </div>
                <button onclick="deleteRow(this)" class="btn-action btn-delete">DELETE ROW</button>
            </div>
        `;
        combatantCard.innerHTML = combatantInfo;

        combatantsContainer.appendChild(combatantCard);
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
function healCombatant(button) {
    var parent = button.closest('.combatant-card');
    var combatantID = parent.id;
    var amount = parent.querySelector('.dmg-heal').value;
    console.log('healing combatant ' + combatantID + " by: " + amount);
    axios.post('/healCombatant', 'combatantId=' + encodeURIComponent(combatantID) + '&amount=' + encodeURIComponent(amount), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    .then(function(response) {
        console.log('Combatant Healed: ', response.data);
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
function dmgCombatant(button) {
    const parent = button.closest('.combatant-card');
    const combatantID = parent.id;
    const amount = parent.querySelector('.dmg-heal').value;

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


/**
 * Sets the initiative of a combatant by sending a POST request to the server.
 * @param {HTMLElement} button - The button element triggering the initiative change.
 */
function setInitiative(button) {
    var parent = button.closest('.combatant-card');
    var combatantID = parent.id;
    var amount = parent.querySelector('.updatedInit').value;
    axios.post('/changeInitiative', 'combatantId=' + encodeURIComponent(combatantID) + '&amount=' + encodeURIComponent(amount), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    .then(function(response) {
        console.log('Combatant Initiative: ', response.data);
        var initiativeElement = parent.querySelector('.currentInitiative');
        if (initiativeElement) {
            initiativeElement.innerText = response.data.newInitiative;
        }
        updateCombatantsList(response.data.combatants);
    })
    .catch(function(error) {
        console.error('Error setting initiative: ', error.response.data);
    });
}

/**
 * Deletes the row corresponding to a combatant from the UI and sends a POST request to the server to mark the combatant as dead.
 * @param {HTMLElement} button - The button element triggering the deletion.
 */
function deleteRow(button) {
    var parent = button.closest('.combatant-card');
    const combatantID = parent.id;
    axios.post('/deadMan', 'combatantId=' + encodeURIComponent(combatantID), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
    .then(function(response) {
        console.log('Combatant deleted: ', response.data);
        parent.remove();
    })
    .catch(function(error) {
        console.error('Error setting initiative: ', error.response.data);
    });
}

// Make functions global
window.healCombatant = healCombatant;
window.dmgCombatant = dmgCombatant;
window.setInitiative = setInitiative;
window.deleteRow = deleteRow;