function checkSession() {
    return fetch('http://localhost:8080/usuarios/logueado')
        .then(response => response.json())
        .then(data => data.logueado)
        .catch(error => {
            console.error('Error checking session:', error);
            return false;
        });
}

function loadMonumentos() {
    checkSession().then(isLoggedIn => {
        fetch('http://localhost:8080/monumentos')
            .then(response => response.json())
            .then(data => {
                const container = document.getElementById('monumentosContainer');
                data.forEach(monumento => {
                    const div = document.createElement('div');
                    div.className = 'monumento';
                    let buttonsHtml = isLoggedIn ? `<button onclick="addToFavorites('${monumento.id}')">Añadir a Favoritos</button>` : '';
                    div.innerHTML = `<strong>${monumento.clase} ${monumento.rdfsLabel}</strong><br>
                                     Latitud: ${monumento.geoLat} <br>
                                     Longitud: ${monumento.geoLong}<br>
                                     Clase: ${monumento.clase}<br>
                                     ${buttonsHtml}`;
                    container.appendChild(div);
                });
            })
            .catch(error => console.error('Error fetching monument data:', error));
    });
}

function addToFavorites(monumentId) {
    console.log("Adding to favorites:", monumentId);
    // Aquí implementamos la lógica para añadir el monumento a los favoritos del usuario
    fetch(`http://localhost:8080/usuarios/favoritos/add/${monumentId}`, {
        method: 'POST',
        credentials: 'include',  // Asegúrate de que las cookies de la sesión se envían con la solicitud
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                console.log("Monumento añadido a favoritos con éxito");
                alert("Monumento añadido a favoritos");
            } else {
                console.error("Error al añadir a favoritos", response.status);
                alert("Error al añadir a favoritos");
            }
        })
        .catch(error => {
            console.error("Error en la red al añadir a favoritos", error);
            alert("Error en la red al añadir a favoritos");
        });
}

window.onload = loadMonumentos;
