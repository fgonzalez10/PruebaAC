
const usuario = JSON.parse(localStorage.getItem("usuario"));
if (!usuario) {
    window.location.href = "login.html";
}

const tbody = document.getElementById("tbodyUsuarios");
const btnNuevo = document.getElementById("btnNuevo");

let editandoId = null;

if (usuario.rol !== "ADMIN") {
    btnNuevo.style.display = "none";
}


function cargarUsuarios() {
    fetch("http://localhost:8080/api/usuarios")
        .then(r => r.json())
        .then(data => {
            let html = "";
            data.forEach(p => {
                html += `
                    <tr>
                        <td>${p.nombre}</td>
                        <td>${p.email}</td>
                        <td>${p.rol}</td>
                        <td>
                            ${usuario.rol === "ADMIN" ? `
                                <button class="btn btn-warning btn-sm" onclick="editarUsuario(${p.id})">Editar</button>
                                <button class="btn btn-danger btn-sm" onclick="eliminarUsuario(${p.id})">Eliminar</button>
                            ` : `Sin permisos`}
                        </td>
                    </tr>
                `;
            });

            tbody.innerHTML = html;
        });
}


btnNuevo.onclick = () => {
    editandoId = null;

    document.getElementById("tituloModal").innerText = "Nuevo Usuario";
    document.getElementById("nombre").value = "";
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";
    document.getElementById("rol").value = "";

    new bootstrap.Modal(document.getElementById("modalUsuario")).show();
};


function editarUsuario(id) {
    fetch(`http://localhost:8080/api/usuarios/${id}`)
        .then(r => r.json())
        .then(p => {
            editandoId = id;

            document.getElementById("tituloModal").innerText = "Editar Usuario";
            document.getElementById("nombre").value = p.nombre;
            document.getElementById("email").value = p.email;
            document.getElementById("rol").value = p.rol;

            new bootstrap.Modal(document.getElementById("modalUsuario")).show();
        });
}


function guardarUsuario() {
    const nombre = document.getElementById("nombre").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const rol = document.getElementById("rol").value;

    const datos = { nombre, email, password, rol };

    const esNuevo = editandoId === null;

    const url = esNuevo
        ? `http://localhost:8080/api/usuarios?idUsuario=${usuario.id}`
        : `http://localhost:8080/api/usuarios/${editandoId}?idUsuario=${usuario.id}`;

    const metodo = esNuevo ? "POST" : "PUT";

    fetch(url, {
        method: metodo,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(datos)
    })
        .then(r => r.text())
        .then(resp => {
            if (resp.includes("No tienes permisos")) {
                alert("No tienes permisos para esta acción.");
                return;
            }

            bootstrap.Modal.getInstance(document.getElementById("modalUsuario")).hide();
            cargarUsuarios();
        });
};



function eliminarUsuario(id) {
    if (!confirm("¿Eliminar este Usuario?")) return;

    fetch(`http://localhost:8080/api/usuarios/${id}?idUsuario=${usuario.id}`, {
        method: "DELETE"
    })
        .then(r => r.text())
        .then(() => cargarUsuarios());
}
function volver() {
    if (!usuario) {
        window.location.href = "login.html";
        return;
    }

    if (usuario.rol === "ADMIN") {
        window.location.href = "admin.html";
    } else {
        window.location.href = "anonimo.html";
    }
}
function buscarPorEmail(texto) {
    if (texto.trim().length === 0) {
        cargarUsuarios();
        return;
    }

    fetch(`http://localhost:8080/api/usuarios/email-contain?email=${texto}`)
        .then(r => {
            if (!r.ok) return [];
            return r.json();
        })
        .then(data => {


            const lista = Array.isArray(data) ? data : [data];
             if (lista.length === 0) {
                            tbody.innerHTML = "<tr><td colspan='5'>Sin resultados</td></tr>";
                            return;
                        }

            let html = "";
            lista.forEach(u => {
                html += `
                    <tr>
                        <td>${u.nombre}</td>
                        <td>${u.email}</td>
                        <td>${u.rol}</td>
                        <td>
                            ${usuario.rol === "ADMIN" ? `
                                <button class="btn btn-warning btn-sm" onclick='mostrarFormulario(${JSON.stringify(u)})'>Editar</button>
                                <button class="btn btn-danger btn-sm" onclick="eliminarUsuario(${u.id})">Eliminar</button>
                            ` : `Sin permisos`}
                        </td>
                    </tr>`;
            });

            tbody.innerHTML = html;
        })
        .catch(() => {
            tbody.innerHTML = "<tr><td colspan='5'>Error de conexión</td></tr>";
        });
}

cargarUsuarios();
