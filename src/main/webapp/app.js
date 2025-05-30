var map = L.map('map').setView([39.4749936, -6.3722448], 14);
L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
    attribution: 'Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, Esri China (Hong Kong), Esri (Thailand), TomTom, 2012'
}).addTo(map);

// Función para cargar y mostrar los monumentos
// Función para cargar y mostrar los monumentos
// Función para cargar y mostrar los monumentos
function loadMonuments() {
    fetch('http://localhost:8080/monumentos')
        .then(response => response.json())
        .then(data => {
            data.forEach(monumento => {
                var marker = L.marker([monumento.geoLat, monumento.geoLong]).addTo(map);
                marker.bindPopup(`<b>${monumento.clase} ${monumento.rdfsLabel}</b><br><a href='monumentProfile.html?uri=${encodeURIComponent(monumento.uri)}'>Ver Perfil</a>`);
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

function loadFavoriteMonuments() {
    fetch('http://localhost:8080/usuarios/favoritos') // Asegúrate de que esta URL es correcta
        .then(response => response.json())
        .then(data => {
            map.eachLayer(layer => {
                if (layer instanceof L.Marker) {
                    map.removeLayer(layer); // Limpia los marcadores actuales
                }
            });
            data.forEach(monumento => {
                var marker = L.marker([monumento.geoLat, monumento.geoLong]).addTo(map);
                marker.bindPopup(`<b>${monumento.clase} ${monumento.rdfsLabel}</b><br><a href='monumentProfile.html?uri=${encodeURIComponent(monumento.uri)}'>Ver Perfil</a>`);
            });
        })
        .catch(error => console.error('Error fetching favorite monument data:', error));
}
document.getElementById('loadFavoritesButton').addEventListener('click', loadFavoriteMonuments);

function loadFilteredMonuments() {
    let selectedType = document.getElementById('typeFilter').value;
    fetch(`http://localhost:8080/monumentos?type=${encodeURIComponent(selectedType)}`) // Asumiendo que el backend puede manejar este filtro
        .then(response => response.json())
        .then(data => {
            map.eachLayer(layer => {
                if (layer instanceof L.Marker) {
                    map.removeLayer(layer); // Limpia los marcadores actuales
                }
            });
            data.forEach(monumento => {
                if (!selectedType || monumento.clase === selectedType) {
                    var marker = L.marker([monumento.geoLat, monumento.geoLong]).addTo(map);
                    marker.bindPopup(`<b>${monumento.clase} ${monumento.rdfsLabel}</b><br><a href='monumentProfile.html?uri=${encodeURIComponent(monumento.uri)}'>Ver Perfil</a>`);
                }
            });
        })
        .catch(error => console.error('Error fetching filtered monument data:', error));
}

// Cargar los monumentos en los selectores del formulario
function loadMonumentsInForm() {
    fetch('http://localhost:8080/monumentos')
        .then(response => response.json())
        .then(data => {
            let startSelect = document.getElementById('startMonument');
            let endSelect = document.getElementById('endMonument');
            data.forEach(monumento => {
                let option = document.createElement('option');
                option.value = monumento.uri;
                option.text = `${monumento.clase} ${monumento.rdfsLabel}`;
                startSelect.add(option);
                let optionClone = option.cloneNode(true);
                endSelect.add(optionClone);
            });
        })
        .catch(error => console.error('Error fetching monument data:', error));
}

// Función para encontrar y mostrar la ruta más corta entre dos monumentos
function findShortestPath() {
    let startUri = document.getElementById('startMonument').value;
    let endUri = document.getElementById('endMonument').value;

    if (startUri && endUri) {
        fetch(`http://localhost:8080/monumentos/shortestPath?startUri=${encodeURIComponent(startUri)}&endUri=${encodeURIComponent(endUri)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                if (data.length > 0) {
                    let latlngs = data.map(monumento => [monumento.geoLat, monumento.geoLong]);
                    let polyline = L.polyline(latlngs, { color: 'red' }).addTo(map);
                    map.fitBounds(polyline.getBounds());
                }
            })
            .catch(error => console.error('Error fetching shortest path:', error));
    }
}

// Cargar los monumentos en los selectores al cargar la página
window.onload = function() {
    loadMonuments();
    checkSession();
    loadMonumentsInForm();
};




