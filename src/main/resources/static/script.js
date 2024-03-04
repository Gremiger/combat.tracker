document.addEventListener("DOMContentLoaded", function() {
            // Get a reference to the image element
            const themeIcon = document.getElementById('theme-icon');

            // Check if the theme preference is stored in localStorage
            const themePreference = localStorage.getItem('themePreference');

            // Apply the theme based on the stored preference
            if (themePreference === 'light-theme') {
                // Apply light theme
                document.body.classList.add('light-theme');
            } else {
                // Apply dark theme (default)
                document.body.classList.remove('light-theme');
            }

            // Apply the filter to the image if dark theme is applied
            if (themePreference === 'dark-theme') {
                themeIcon.style.filter = 'invert(1)';
            }


            // Toggle theme function
            function toggleTheme() {
                const body = document.body;
                const currentTheme = body.classList.contains('light-theme') ? 'light-theme' : 'dark-theme';

                // Toggle between light and dark themes
                body.classList.toggle('light-theme');

                // Update the theme preference in localStorage
                const newTheme = body.classList.contains('light-theme') ? 'light-theme' : 'dark-theme';
                localStorage.setItem('themePreference', newTheme);

                // Apply or remove the filter based on the new theme
                if (newTheme === 'dark-theme') {
                    themeIcon.style.filter = 'invert(1)';
                } else {
                    themeIcon.style.filter = '';
                }
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

        function healCombatant(){
            console.log('healing');
        }

        function dmgCombatant(){
            console.log('damaging');
        }