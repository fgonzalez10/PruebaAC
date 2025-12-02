
const usuario = JSON.parse(localStorage.getItem("usuario"));
if (!usuario) {
    window.location.href = "login.html";
}
// Ocultar columna Acciones si no es ADMIN
if (usuario.rol !== "ADMIN") {
    document.getElementById("colAdmin").style.display = "none";
}
const tbody = document.getElementById("tbodyProductos");
const btnNuevo = document.getElementById("btnNuevo");

let editandoId = null;

if (usuario.rol !== "ADMIN") {
    btnNuevo.style.display = "none";
}


function cargarProductos() {
    fetch("http://localhost:8080/api/productos")
        .then(r => r.json())
        .then(data => {
            let html = "";
            data.forEach(p => {
                html += `
                    <tr>
                        <td>${p.sku}</td>
                        <td>${p.nombre}</td>
                        <td>${p.marca}</td>
                        <td>${p.precio}</td>
                        ${usuario.rol === "ADMIN" ?
                        `  <td>
                                <button class="btn btn-warning btn-sm" onclick="editarProducto(${p.id})">Editar</button>
                                <button class="btn btn-danger btn-sm" onclick="eliminarProducto(${p.id})">Eliminar</button></td>
                            ` : ``}

                    </tr>
                `;
            });

            tbody.innerHTML = html;
        });
}

btnNuevo.onclick = () => {
    editandoId = null;

    document.getElementById("tituloModal").innerText = "Nuevo Producto";
    document.getElementById("sku").value = "";
    document.getElementById("nombre").value = "";
    document.getElementById("marca").value = "";
    document.getElementById("precio").value = "";

    new bootstrap.Modal(document.getElementById("modalProducto")).show();
};


function editarProducto(id) {
    fetch(`http://localhost:8080/api/productos/${id}`)
        .then(r => r.json())
        .then(p => {
            editandoId = id;

            document.getElementById("tituloModal").innerText = "Editar Producto";
            document.getElementById("sku").value = p.sku;
            document.getElementById("nombre").value = p.nombre;
            document.getElementById("marca").value = p.marca;
            document.getElementById("precio").value = p.precio;

            new bootstrap.Modal(document.getElementById("modalProducto")).show();
        });
}


function guardarProducto() {
    const sku = document.getElementById("sku").value;
    const nombre = document.getElementById("nombre").value;
    const marca = document.getElementById("marca").value;
    const precio = parseFloat(document.getElementById("precio").value);

    const datos = { sku, nombre, marca, precio };

    const esNuevo = editandoId === null;

    const url = esNuevo
        ? `http://localhost:8080/api/productos?usuarioId=${usuario.id}`
        : `http://localhost:8080/api/productos/${editandoId}?usuarioId=${usuario.id}`;

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

            bootstrap.Modal.getInstance(document.getElementById("modalProducto")).hide();
            cargarProductos();
        });
};


function eliminarProducto(id) {
    if (!confirm("¿Eliminar este producto?")) return;

    fetch(`http://localhost:8080/api/productos/${id}?usuarioId=${usuario.id}`, {
        method: "DELETE"
    })
        .then(r => r.text())
        .then(() => cargarProductos());
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

function buscar() {
    const texto = document.getElementById("busqueda").value.trim();
    const tipo = document.getElementById("tipoBusqueda").value;

    if (texto === "") {
        cargarProductos();
        return;
    }

    let url = "";

    if (tipo === "nombre") {
        url = `http://localhost:8080/api/productos/buscar/nombre?nombre=${texto}`;
    } else {
        url = `http://localhost:8080/api/productos/buscar/marca?marca=${texto}`;
    }

    fetch(url)
        .then(r => r.json())
        .then(data => {
            let html = "";
            data.forEach(p => {
                html += `
                    <tr>

                        <td>${p.sku}</td>
                        <td>${p.nombre}</td>
                        <td>${p.marca}</td>
                        <td>${p.precio}</td>
                        ${usuario.rol === "ADMIN" ? `<td>

                                <button class="btn btn-warning btn-sm" onclick="editarProducto(${p.id})">Editar</button>
                                <button class="btn btn-danger btn-sm" onclick="eliminarProducto(${p.id})">Eliminar</button>
                                </td>
                            ` : ``}

                    </tr>
                `;
            });

            tbody.innerHTML = html;
        });
}

cargarProductos();

