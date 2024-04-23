document.getElementById('login').addEventListener('submit', function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('http://localhost:8080/usuarios/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, password: password })
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Failed to login');
    }).then(data => {
        console.log('Logged in:', data);
        alert('Login successful!');
        window.location.href = 'index.html'; // Redirige a la página principal después del inicio de sesión
    }).catch(error => {
        console.error('Login failed:', error);
        alert('Login failed');
    });
});
