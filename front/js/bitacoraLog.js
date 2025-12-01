const usuarioActual = JSON.parse(localStorage.getItem("usuario"));

function volver() {
    if (!usuarioActual) return window.location.href = "login.html";

    if (usuarioActual.rol === "ADMIN") {
        window.location.href = "admin.html";
    } else {
        window.location.href = "anonimo.html";
    }
}

function cargarHistorial() {
    const id = document.getElementById("idProducto").value;

    if (!id) {
        alert("Ingresa un ID de producto.");
        return;
    }

    fetch(`http://localhost:8080/api/productos/${id}/historial`)
        .then(r => r.json())
        .then(data => {
            let html = "";

            if (data.length === 0) {
                html = `
                    <tr>
                        <td colspan="4" class="text-center text-muted">
                            No hay historial para este producto.
                        </td>
                    </tr>`;
            } else {
                data.forEach(log => {
                    html += `
                        <tr>
                            <td>${log.fecha}</td>
                            <td>${log.accion}</td>
                            <td>${log.descripcion}</td>
                            <td>${log.idUsuario}</td>
                        </tr>
                    `;
                });
            }

            document.getElementById("tbodyHistorial").innerHTML = html;
        })
        .catch(err => {
            console.error(err);
            alert("Error obteniendo historial");
        });
}
