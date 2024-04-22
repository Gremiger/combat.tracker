document.addEventListener("DOMContentLoaded", function() {
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