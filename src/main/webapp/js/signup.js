document.getElementById('signup').addEventListener('submit', function(event) {
    event.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('http://localhost:8080/usuarios/registro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: name, email: email, password: password })
    }).then(response => {
        if (response.status === 201) {
            return response.json();
        }
        throw new Error('Failed to register');
    }).then(data => {
        console.log('Registered:', data);
        alert('Registration successful!');
        window.location.href = 'index.html'; // Redirige a la página de inicio con la sesión iniciada
    }).catch(error => {
        console.error('Registration failed:', error);
        alert('Registration failed');
    });
});
