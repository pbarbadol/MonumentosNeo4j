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

            if (data.logueado) {
                loginBtn.style.display = 'none';
                signupBtn.style.display = 'none';
                profileBtn.style.display = 'inline';
                welcomeMessage.style.display = 'block'; // Muestra el mensaje de bienvenida
                userNameSpan.textContent = data.usuario; // Actualiza el nombre del usuario
            } else {
                loginBtn.style.display = 'inline';
                signupBtn.style.display = 'inline';
                profileBtn.style.display = 'none';
                welcomeMessage.style.display = 'none'; // Oculta el mensaje de bienvenida
            }
        })
        .catch(error => console.error('Error checking session:', error));
}

// Verifica la sesión al cargar la página
window.onload = function() {
    loadMonuments();
    checkSession();
};


