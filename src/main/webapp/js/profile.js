document.addEventListener("DOMContentLoaded", function() {
    fetchUserData();
    document.getElementById('userProfileForm').addEventListener('submit', updateUserProfile);
    document.getElementById('logoutButton').addEventListener('click', logoutUser);
    document.getElementById('deleteAccountButton').addEventListener('click', deleteAccount);
});

function fetchUserData() {
    fetch('http://localhost:8080/usuarios/perfil', {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById('nombre').value = data.nombre || '';
            document.getElementById('email').value = data.email || '';
        })
        .catch(error => console.error('Error al cargar datos del usuario:', error));
}

function updateUserProfile(event) {
    event.preventDefault();
    const nombre = document.getElementById('nombre').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('http://localhost:8080/usuarios/actualizar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ nombre: nombre, email: email, password: password}),
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                alert('Perfil actualizado correctamente.');
            } else {
                alert('Error al actualizar perfil.');
            }
        })
        .catch(error => {
            console.error('Error al actualizar perfil:', error);
            alert('Error al actualizar perfil: ' + error.message);
        });
}

function logoutUser() {
    fetch('http://localhost:8080/usuarios/logout', {
        method: 'POST',
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                alert('Sesión cerrada con éxito.');
                window.location.href = 'index.html'; // Redirige al usuario a la página de inicio
            } else {
                alert('Error al cerrar sesión.');
            }
        })
        .catch(error => {
            console.error('Error al cerrar sesión:', error);
            alert('Error al cerrar sesión: ' + error.message);
        });
}

function deleteAccount() {
    if (confirm("¿Está seguro de que desea eliminar su cuenta? Esta acción es irreversible.")) {
        fetch('http://localhost:8080/usuarios/eliminar', {
            method: 'POST',
            credentials: 'include'
        })
            .then(response => {
                if (response.ok) {
                    alert('Cuenta eliminada con éxito.');
                    window.location.href = 'index.html'; // Redirige al usuario a la página de inicio
                } else {
                    alert('Error al eliminar la cuenta.');
                }
            })
            .catch(error => {
                console.error('Error al eliminar la cuenta:', error);
                alert('Error al eliminar la cuenta: ' + error.message);
            });
    }
}
