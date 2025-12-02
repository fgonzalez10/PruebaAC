function obtenerUsuario() {
    return JSON.parse(localStorage.getItem("usuario"));
}

function requiereSesion() {
    const usuario = obtenerUsuario();
    if (!usuario) {
        window.location.href = "login.html";
    }
    return usuario;
}

function requiereAdmin() {
    const usuario = requiereSesion();
    if (usuario.rol !== "ADMIN") {
        alert("No tienes permisos.");
        window.location.href = "anonimo.html";
    }
    return usuario;
}

function logout() {
    localStorage.removeItem("usuario");
    window.location.href = "login.html";
}