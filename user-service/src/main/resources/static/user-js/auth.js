
const loginTab = document.getElementById('login-tab');
const registerTab = document.getElementById('register-tab');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');

loginTab.addEventListener('click', function() {
    loginTab.classList.add('active');
    registerTab.classList.remove('active');
    loginForm.classList.add('active');
    registerForm.classList.remove('active');
});

registerTab.addEventListener('click', function() {
    registerTab.classList.add('active');
    loginTab.classList.remove('active');
    registerForm.classList.add('active');
    loginForm.classList.remove('active');
});


const registerFormEl = registerForm.querySelector('form');
registerFormEl.addEventListener('submit', function(e) {
    const password = document.getElementById('register-password').value;
    const confirmPassword = document.getElementById('register-confirm-password').value;

    if (password !== confirmPassword) {
        e.preventDefault(); 
        alert('Les mots de passe ne correspondent pas.');
    }
});
