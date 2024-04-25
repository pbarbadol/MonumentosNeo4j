// Obtener el URI del monumento desde la URL
const urlParams = new URLSearchParams(window.location.search);
const monumentUri = decodeURIComponent(urlParams.get('uri'));

// Cargar detalles del monumento
function loadMonumentDetails() {
    fetch(`http://localhost:8080/monumentos/detalles?uri=${encodeURIComponent(monumentUri)}`)  // Asegúrate de que esta URL es correcta para tu API
        .then(response => response.json())
        .then(monumento => {
            document.getElementById('monumentDetails').innerHTML = `
                <h2>${monumento.rdfsLabel}</h2>
                <p>Clase: ${monumento.clase}</p>
                <p>Latitud: ${monumento.geoLat}</p>
                <p>Longitud: ${monumento.geoLong}</p>
                <p>Enlace SIG: <a href="${monumento.tieneEnlaceSIG}" target="_blank">Ver en SIG</a></p>
            `;
            // También puedes añadir aquí el botón para añadir o eliminar de favoritos
        })
        .catch(error => console.log('Error loading monument details:', error));
}

document.addEventListener('DOMContentLoaded', loadMonumentDetails);
