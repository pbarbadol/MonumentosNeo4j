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

function showLoginForm() {
    alert('Mostrar formulario de inicio de sesión');
    // Aquí iría la lógica para mostrar un formulario de inicio de sesión real
}

function showSignupForm() {
    alert('Mostrar formulario de registro');
    // Aquí iría la lógica para mostrar un formulario de registro real
}
