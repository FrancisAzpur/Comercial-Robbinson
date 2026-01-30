// carrito.js - VERSIÓN CORREGIDA
// Funcionalidades del carrito de compras

// Variable global para el carrito
let carrito = [];

// Constante para la clave del localStorage (DEBE SER LA MISMA)
const CARRO_KEY = 'carritoRobinson';

// Inicializar carrito al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    console.log('Carrito.js inicializando...');
    cargarCarrito();
    actualizarContadorCarrito();
    
    // Configurar evento para el botón del carrito en el navbar
    const btnCarrito = document.querySelector('.carrito-btn');
    if (btnCarrito) {
        btnCarrito.addEventListener('click', function(e) {
            e.preventDefault();
            window.location.href = '/carrito';
        });
    }
    
    // También actualizar contador periódicamente por si acaso
    setInterval(actualizarContadorCarrito, 2000);
});

// Función para cargar el carrito desde localStorage
function cargarCarrito() {
    // Preferir la clave nueva
    const carritoGuardado = localStorage.getItem(CARRO_KEY);
    console.log('Cargando carrito, localStorage:', carritoGuardado);

    if (carritoGuardado) {
        try {
            carrito = JSON.parse(carritoGuardado);
        } catch (error) {
            console.error('Error al cargar el carrito:', error);
            carrito = [];
        }
    } else {
        // Compatibilidad hacia atrás: migrar desde clave antigua 'carrito' si existe
        const antiguo = localStorage.getItem('carrito');
        if (antiguo) {
            try {
                carrito = JSON.parse(antiguo);
                localStorage.setItem(CARRO_KEY, JSON.stringify(carrito));
                localStorage.removeItem('carrito');
                console.log('Carrito migrado desde clave antigua `carrito` a `carritoRobinson`.');
            } catch (e) {
                console.error('Error migrando carrito antiguo:', e);
                carrito = [];
            }
        }
    }

    console.log('Carrito cargado:', carrito);
    // Asegurar que el contador del navbar se actualice tras cargar
    actualizarContadorCarrito();
}

// Función para guardar el carrito en localStorage
function guardarCarrito() {
    localStorage.setItem(CARRO_KEY, JSON.stringify(carrito));
    console.log('Carrito guardado:', carrito);
}

// Función para agregar un producto al carrito
function agregarAlCarrito(producto) {
    console.log('Agregando al carrito:', producto);
    
    // Verificar si el producto ya está en el carrito
    const itemExistenteIndex = carrito.findIndex(item => item.id === producto.id);
    
    if (itemExistenteIndex !== -1) {
        // Incrementar cantidad si ya existe
        carrito[itemExistenteIndex].cantidad += 1;
        console.log('Producto existente, nueva cantidad:', carrito[itemExistenteIndex].cantidad);
    } else {
        // Agregar nuevo producto al carrito
        carrito.push({
            ...producto,
            cantidad: 1
        });
        console.log('Nuevo producto agregado');
    }
    
    // Guardar y actualizar
    guardarCarrito();
    actualizarContadorCarrito();
    
    return true;
}

// Función para actualizar el contador del carrito en el navbar
function actualizarContadorCarrito() {
    const totalItems = carrito.reduce((total, item) => total + item.cantidad, 0);
    
    console.log('Actualizando contador, total items:', totalItems);
    
    // BUSCAR TODOS LOS POSIBLES CONTADORES (actualizado)
    const selectores = [
        '.cart-count',           // Tu navbar usa esta clase
        '.contador-carrito',     // Clase que teníamos antes
        '.badge.bg-danger',      // Badge rojo
        '.navbar .badge'         // Cualquier badge en navbar
    ];
    
    selectores.forEach(selector => {
        const elementos = document.querySelectorAll(selector);
        elementos.forEach(elemento => {
            // Solo actualizar si parece un contador (número o clase específica)
            if (elemento.textContent.match(/^\d+$/) || 
                elemento.classList.contains('cart-count') || 
                elemento.classList.contains('contador-carrito')) {
                elemento.textContent = totalItems;
                elemento.style.display = totalItems > 0 ? 'inline-block' : 'none';
                console.log(`Contador actualizado (${selector}):`, totalItems);
            }
        });
    });
    
    return totalItems;
}

// Función para eliminar un producto del carrito
function eliminarDelCarrito(productoId) {
    carrito = carrito.filter(item => item.id !== productoId);
    guardarCarrito();
    actualizarContadorCarrito();
    
    // Si estamos en la página del carrito, recargar la vista
    if (window.location.pathname.includes('carrito')) {
        window.location.reload();
    }
}

// Función para actualizar la cantidad de un producto en el carrito
function actualizarCantidadCarrito(productoId, nuevaCantidad) {
    if (nuevaCantidad < 1) {
        eliminarDelCarrito(productoId);
        return;
    }
    
    const itemIndex = carrito.findIndex(item => item.id === productoId);
    if (itemIndex !== -1) {
        carrito[itemIndex].cantidad = nuevaCantidad;
        guardarCarrito();
        actualizarContadorCarrito();
        
        // Si estamos en la página del carrito, recargar la vista
        if (window.location.pathname.includes('carrito')) {
            window.location.reload();
        }
    }
}

// Función para obtener el total del carrito
function obtenerTotalCarrito() {
    return carrito.reduce((total, item) => total + (item.precio * item.cantidad), 0);
}

// Función para vaciar el carrito
function vaciarCarrito() {
    carrito = [];
    guardarCarrito();
    actualizarContadorCarrito();
    
    // Si estamos en la página del carrito, recargar la vista
    if (window.location.pathname.includes('carrito')) {
        window.location.reload();
    }
}

// Función para obtener el carrito completo
function obtenerCarritoCompleto() {
    return [...carrito]; // Copia del carrito
}

// Exportar funciones para uso global
window.agregarAlCarrito = agregarAlCarrito;
window.eliminarDelCarrito = eliminarDelCarrito;
window.actualizarCantidadCarrito = actualizarCantidadCarrito;
window.vaciarCarrito = vaciarCarrito;
window.obtenerTotalCarrito = obtenerTotalCarrito;
window.actualizarContadorCarrito = actualizarContadorCarrito;
window.obtenerCarritoCompleto = obtenerCarritoCompleto;
window.CARRO_KEY = CARRO_KEY; // Exportar la clave también

// Añadir función de debug
window.debugCarrito = function() {
    console.log('=== DEBUG CARRITO ===');
    console.log('Clave localStorage:', CARRO_KEY);
    console.log('Contenido localStorage:', localStorage.getItem(CARRO_KEY));
    console.log('Carrito en memoria:', carrito);
    console.log('Total items:', carrito.reduce((total, item) => total + item.cantidad, 0));
    
    const contadores = document.querySelectorAll('.cart-count, .contador-carrito, .badge');
    console.log('Contadores encontrados:', contadores.length);
    contadores.forEach((cont, i) => {
        console.log(`Contador ${i}:`, {
            clase: cont.className,
            texto: cont.textContent,
            display: cont.style.display
        });
    });
};