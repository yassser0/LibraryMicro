
        const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
        const navLinks = document.querySelector('.nav-links');
        const authButtons = document.querySelector('.auth-buttons');
        
        mobileMenuBtn.addEventListener('click', function() {
            if (navLinks.style.display === 'flex') {
                navLinks.style.display = 'none';
                authButtons.style.display = 'none';
            } else {
                navLinks.style.display = 'flex';
                navLinks.classList.add('mobile-menu');
                authButtons.style.display = 'flex';
                authButtons.classList.add('mobile-menu');
            }
        });