async function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const msg = document.getElementById("msg");

    try {
        const response = await fetch("http://localhost:8080/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            msg.innerText = "Email o contraseña incorrectos";
            return;
        }

        const usuario = await response.json();
        localStorage.setItem("usuario", JSON.stringify(usuario));

        if (usuario.rol === "ADMIN") {
            window.location.href = "admin.html";
        } else {
            window.location.href = "anonimo.html";
        }

    } catch (error) {
        msg.innerText = "Error conectando al servidor";
        console.error(error);
    }
}
