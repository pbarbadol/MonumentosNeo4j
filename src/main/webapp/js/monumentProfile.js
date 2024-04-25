// Obtener el ID del monumento desde la URL
const urlParams = new URLSearchParams(window.location.search);
const monumentId = urlParams.get('uri');

// Cargar detalles del monumento
function loadMonumentDetails() {
    fetch('http://localhost:8080/monumentos/' + encodeURIComponent(monumentId))
        .then(response => response.json())
        .then(monumento => {
            document.getElementById('monumentDetails').innerHTML = `
                <strong>${monumento.clase} ${monumento.rdfsLabel}</strong><br>
                Latitud: ${monumento.geoLat}<br>
                Longitud: ${monumento.geoLong}<br>
            `;
        });
}

// Añadir a favoritos
document.getElementById('favoriteButton').addEventListener('click', function() {
    fetch('http://localhost:8080/usuarios/favoritos/add/' + encodeURIComponent(monumentId), {
        method: 'POST',
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            alert("Añadido a favoritos");
        } else {
            alert("Error al añadir a favoritos");
        }
    });
});

window.onload = loadMonumentDetails;
