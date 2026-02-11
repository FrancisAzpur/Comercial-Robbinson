// carrito.js - VERSION CON BACKEND (API REST)
// ==============================================================================
// El carrito se almacena en la sesion del servidor (HttpSession).
// Las operaciones se realizan mediante fetch() contra /api/carrito/*.
// El localStorage ya NO se usa para almacenar el carrito.
// ==============================================================================

// Variable global para el carrito (cache local de lo que hay en el servidor)
let carrito = [];

// ==============================================================================
// INICIALIZACION
// ==============================================================================

document.addEventListener('DOMContentLoaded', function() {
    console.log('Carrito.js inicializando (modo backend)...');
    cargarCarritoDesdeServidor();

    // Configurar evento para el boton del carrito en el navbar
    const btnCarrito = document.querySelector('.carrito-btn');
    if (btnCarrito) {
        btnCarrito.addEventListener('click', function(e) {
            e.preventDefault();
            window.location.href = '/carrito';
        });
    }

    // Actualizar contador periodicamente
    setInterval(actualizarContadorCarrito, 5000);
});

// ==============================================================================
// COMUNICACION CON EL BACKEND
// ==============================================================================

/**
 * Carga el carrito desde el servidor y actualiza la cache local.
 */
function cargarCarritoDesdeServidor() {
    fetch('/api/carrito')
        .then(response => response.json())
        .then(data => {
            carrito = (data.items || []).map(item => ({
                id: item.idProducto,
                nombre: item.nombre,
                precio: parseFloat(item.precio),
                imagen: item.imagen,
                cantidad: item.cantidad
            }));
            console.log('Carrito cargado desde servidor:', carrito);
            actualizarContadorCarrito();
        })
        .catch(error => {
            console.error('Error al cargar carrito desde servidor:', error);
        });
}

/**
 * Carga el carrito desde la cache local (para uso interno).
 */
function cargarCarrito() {
    cargarCarritoDesdeServidor();
}

/**
 * Agrega un producto al carrito via API del backend.
 * @param {Object} producto - { id, nombre, precio, imagen }
 * @returns {Promise<boolean>} true si se agrego exitosamente
 */
function agregarAlCarrito(producto) {
    console.log('Agregando al carrito (backend):', producto);

    return fetch('/api/carrito/agregar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            idProducto: producto.id,
            nombre: producto.nombre,
            precio: producto.precio,
            imagen: producto.imagen
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.exito) {
            console.log('Producto agregado:', data.mensaje);
            // Actualizar cache local
            const existente = carrito.findIndex(item => item.id === producto.id);
            if (existente !== -1) {
                carrito[existente].cantidad += 1;
            } else {
                carrito.push({ ...producto, cantidad: 1 });
            }
            actualizarContadorDesdeServidor(data.totalItems);
            return true;
        } else {
            console.error('Error al agregar:', data.mensaje);
            return false;
        }
    })
    .catch(error => {
        console.error('Error de red al agregar al carrito:', error);
        return false;
    });
}

/**
 * Actualiza la cantidad de un producto en el carrito via API.
 */
function actualizarCantidadCarrito(productoId, nuevaCantidad) {
    if (nuevaCantidad < 1) {
        eliminarDelCarritoAPI(productoId);
        return;
    }

    fetch('/api/carrito/actualizar/' + productoId, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ cantidad: nuevaCantidad })
    })
    .then(response => response.json())
    .then(data => {
        if (data.exito) {
            const idx = carrito.findIndex(item => item.id === productoId);
            if (idx !== -1) {
                carrito[idx].cantidad = nuevaCantidad;
            }
            actualizarContadorDesdeServidor(data.totalItems);

            if (window.location.pathname.includes('carrito')) {
                window.location.reload();
            }
        }
    })
    .catch(error => {
        console.error('Error al actualizar cantidad:', error);
    });
}

/**
 * Elimina un producto del carrito via API.
 */
function eliminarDelCarritoAPI(productoId) {
    fetch('/api/carrito/eliminar/' + productoId, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(data => {
        if (data.exito) {
            carrito = carrito.filter(item => item.id !== productoId);
            actualizarContadorDesdeServidor(data.totalItems);

            if (window.location.pathname.includes('carrito')) {
                window.location.reload();
            }
        }
    })
    .catch(error => {
        console.error('Error al eliminar del carrito:', error);
    });
}

/**
 * Wrapper para compatibilidad con codigo existente.
 */
function eliminarDelCarrito(productoId) {
    eliminarDelCarritoAPI(productoId);
}

/**
 * Vacia todo el carrito via API.
 */
function vaciarCarrito() {
    fetch('/api/carrito/vaciar', { method: 'DELETE' })
        .then(response => response.json())
        .then(data => {
            if (data.exito) {
                carrito = [];
                actualizarContadorDesdeServidor(0);

                if (window.location.pathname.includes('carrito')) {
                    window.location.reload();
                }
            }
        })
        .catch(error => {
            console.error('Error al vaciar carrito:', error);
        });
}

// ==============================================================================
// ACTUALIZACION DEL CONTADOR EN EL NAVBAR
// ==============================================================================

/**
 * Actualiza el contador del carrito consultando al servidor.
 */
function actualizarContadorCarrito() {
    fetch('/api/carrito/contador')
        .then(response => response.json())
        .then(data => {
            actualizarContadorDesdeServidor(data.totalItems);
        })
        .catch(() => {
            // Silenciar errores de red
        });
}

/**
 * Actualiza el DOM del contador con el valor dado.
 */
function actualizarContadorDesdeServidor(totalItems) {
    const selectores = [
        '.cart-count',
        '.contador-carrito',
        '.badge.bg-danger',
        '.navbar .badge'
    ];

    selectores.forEach(selector => {
        const elementos = document.querySelectorAll(selector);
        elementos.forEach(elemento => {
            if (elemento.textContent.match(/^\d+$/) ||
                elemento.classList.contains('cart-count') ||
                elemento.classList.contains('contador-carrito')) {
                elemento.textContent = totalItems;
                elemento.style.display = totalItems > 0 ? 'inline-block' : 'none';
            }
        });
    });
}

// ==============================================================================
// FUNCIONES AUXILIARES
// ==============================================================================

function obtenerTotalCarrito() {
    return carrito.reduce((total, item) => total + (item.precio * item.cantidad), 0);
}

function obtenerCarritoCompleto() {
    return [...carrito];
}

// ==============================================================================
// EXPORTAR FUNCIONES GLOBALES
// ==============================================================================

window.agregarAlCarrito = agregarAlCarrito;
window.eliminarDelCarrito = eliminarDelCarrito;
window.eliminarDelCarritoAPI = eliminarDelCarritoAPI;
window.actualizarCantidadCarrito = actualizarCantidadCarrito;
window.vaciarCarrito = vaciarCarrito;
window.obtenerTotalCarrito = obtenerTotalCarrito;
window.actualizarContadorCarrito = actualizarContadorCarrito;
window.obtenerCarritoCompleto = obtenerCarritoCompleto;
window.cargarCarritoDesdeServidor = cargarCarritoDesdeServidor;

// Debug
window.debugCarrito = function() {
    console.log('=== DEBUG CARRITO (BACKEND) ===');
    console.log('Carrito en memoria (cache):', carrito);
    console.log('Total items:', carrito.reduce((total, item) => total + item.cantidad, 0));
    fetch('/api/carrito')
        .then(r => r.json())
        .then(data => console.log('Carrito en servidor:', data))
        .catch(e => console.error('Error consultando servidor:', e));
};
