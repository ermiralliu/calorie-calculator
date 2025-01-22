class Auth {
    constructor() {
        this.loginForm = document.querySelector('form');
        this.loginForm.addEventListener('submit', (e) => this.handleLogin(e));
    }


    handleLogin(event) {
        event.preventDefault();

        // Get email and password from the form
        const email = this.loginForm.email.value.trim();
        const password = this.loginForm.password.value.trim();


        const users = JSON.parse(localStorage.getItem('users')) || [];
        const user = users.find((u) => u.email === email && u.password === password);

        if (user) {
            alert('Login successful!');
            // Redirect or perform actions after successful login
            window.location.href = '/dashboard.html'; // Example
        } else {
            alert('Invalid email or password. Please try again.');
        }
    }
}


document.addEventListener('DOMContentLoaded', () => {
    new Auth();
});
