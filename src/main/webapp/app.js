var map = L.map('map').setView([39.4749936, -6.3722448], 14);
L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
    attribution: 'Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, Esri China (Hong Kong), Esri (Thailand), TomTom, 2012'
}).addTo(map);

// Función para cargar y mostrar los monumentos
function loadMonuments() {
    fetch('http://localhost:8080/monumentos')
        .then(response => {
            // Imprimir la respuesta antes de convertirla a JSON
            console.log('Response:', response);
            return response.json();
        })
        .then(data => {
            const listElement = document.getElementById('monumentList');

            data.forEach(monumento => {
                var marker = L.marker([monumento.geoLat, monumento.geoLong]).addTo(map);
                marker.bindPopup(`<b>${monumento.rdfsLabel}</b><br>${monumento.clase}`);


            });
        })
        .catch(error => console.error('Error fetching monument data:', error));
}


// Cargar los monumentos al cargar la página
window.onload = loadMonuments;

// Función para verificar la sesión del usuario
function checkSession() {
    fetch('http://localhost:8080/usuarios/logueado')
        .then(response => response.json())
        .then(data => {
            const loginBtn = document.getElementById('loginButton');
            const signupBtn = document.getElementById('signupButton');
            const profileBtn = document.getElementById('profileButton');
            const welcomeMessage = document.getElementById('welcomeMessage');
            const userNameSpan = document.getElementById('userName');
            const monumentActions = document.getElementById('monumentActions');

            if (data.logueado) {
                loginBtn.style.display = 'none';
                signupBtn.style.display = 'none';
                profileBtn.style.display = 'inline';
                welcomeMessage.style.display = 'block';
                userNameSpan.textContent = data.usuario;
                monumentActions.style.display = 'block'; // Muestra los botones de acciones de monumentos
            } else {
                loginBtn.style.display = 'inline';
                signupBtn.style.display = 'inline';
                profileBtn.style.display = 'none';
                welcomeMessage.style.display = 'none';
                monumentActions.style.display = 'none'; // Oculta los botones de acciones de monumentos
            }
        })
        .catch(error => console.error('Error checking session:', error));
}

// Añadir Monumento
function addMonument() {
    // Aquí se puede añadir un formulario o similar para capturar los datos del nuevo monumento
    // Ejemplo de cómo enviar un POST con datos ficticios
    fetch('http://localhost:8080/monumentos/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombre: "Nuevo Monumento",
            geoLat: 39.47,
            geoLong: -6.37,
            clase: "Historia"
        })
    })
        .then(response => response.json())
        .then(data => {
            console.log('Monumento añadido:', data);
            // Recargar los monumentos para mostrar el nuevo
            loadMonuments();
        })
        .catch(error => console.error('Error al añadir monumento:', error));
}

// Editar Monumento
function editMonument() {
    // Supone que tienes un identificador para el monumento que quieres editar
    let monumentId = '123'; // Ejemplo de ID, debería ser dinámico
    fetch(`http://localhost:8080/monumentos/edit/${monumentId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombre: "Monumento Editado",
            geoLat: 39.47,
            geoLong: -6.37,
            clase: "Cultural"
        })
    })
        .then(response => response.json())
        .then(data => {
            console.log('Monumento editado:', data);
            // Recargar los monumentos para mostrar los cambios
            loadMonuments();
        })
        .catch(error => console.error('Error al editar monumento:', error));
}

// Eliminar Monumento
function deleteMonument() {
    let monumentId = '123'; // Ejemplo de ID, debería ser dinámico
    fetch(`http://localhost:8080/monumentos/delete/${monumentId}`, {
        method: 'DELETE'
    })
        .then(response => {
            console.log('Monumento eliminado');
            // Recargar los monumentos para actualizar la lista
            loadMonuments();
        })
        .catch(error => console.error('Error al eliminar monumento:', error));
}

// Añadir Monumento a Favoritos
function addFavorite() {
    let monumentId = '123'; // Ejemplo de ID, debería ser dinámico
    fetch(`http://localhost:8080/usuarios/favoritos/add/${monumentId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            console.log('Monumento añadido a favoritos:', data);
        })
        .catch(error => console.error('Error al añadir a favoritos:', error));
}



// Verifica la sesión al cargar la página
window.onload = function() {
    loadMonuments();
    checkSession();
    document.getElementById('addMonumentButton').addEventListener('click', addMonument);
    document.getElementById('editMonumentButton').addEventListener('click', editMonument);
    document.getElementById('deleteMonumentButton').addEventListener('click', deleteMonument);
    document.getElementById('addFavoriteButton').addEventListener('click', addFavorite);
};



