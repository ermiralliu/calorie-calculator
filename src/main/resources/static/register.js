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


        if (users.some((user) => user.email === email)) {
            alert('Email is already registered. Please use a different email or log in.');
            return;
        }


        users.push({ name, email, password });
        localStorage.setItem('users', JSON.stringify(users));

        alert('Registration successful! You can now log in.');
        // Redirect to the login page
        window.location.href = 'login.html';
    }
}


document.addEventListener('DOMContentLoaded', () => {
    new Registration();
});
