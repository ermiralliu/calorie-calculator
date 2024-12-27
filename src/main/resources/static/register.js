class Registration {
    constructor() {
        this.registerForm = document.querySelector('form');
        this.registerForm.addEventListener('submit', (e) => this.handleRegister(e));
    }

    handleRegister(event) {
        event.preventDefault();

        // Get user inputs
        const name = this.registerForm.name.value.trim();
        const email = this.registerForm.email.value.trim();
        const password = this.registerForm.password.value.trim();

        if (!name || !email || !password) {
            alert('All fields are required!');
            return;
        }

        const users = JSON.parse(localStorage.getItem('users')) || [];

        const emailAlreadyRegistered = users.some((user) => user.email === email);

        if (emailAlreadyRegistered) {
            // Display dialog for already registered email
            this.showDialog('Email is already registered. Please use a different email or log in.');
            return;
        }

        users.push({ name, email, password });
        localStorage.setItem('users', JSON.stringify(users));

        // Display dialog for successful registration
        this.showDialog('Registration successful! You can now log in.');

        // Redirect to the login page
        window.location.href = 'login.html';
    }

    showDialog(message) {
        const dialog = document.getElementById('dial');
        dialog.querySelector('h1').textContent = message;  // Set the message
        dialog.showModal();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new Registration();
});
