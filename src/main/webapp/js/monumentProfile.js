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

// Eliminar de favoritos
document.getElementById('unfavoriteButton').addEventListener('click', function() {
    fetch('http://localhost:8080/usuarios/favoritos/delete/' + encodeURIComponent(monumentId), {
        method: 'POST',
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            alert("Eliminado de favoritos");
        } else {
            alert("Error al eliminar de favoritos");
        }
    });
});


window.onload = loadMonumentDetails;
