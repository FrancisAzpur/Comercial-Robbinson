// ==========================================
// INICIALIZACIÓN DE AOS (Animate On Scroll)
// ==========================================
AOS.init({ 
    duration: 1200, 
    once: true 
});

// ==========================================
// NAVBAR SCROLL EFFECT
// ==========================================
window.addEventListener('scroll', () => {
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        navbar.classList.toggle('scrolled', window.scrollY > 50);
    }
});

// ==========================================
// GESTIÓN DE CARRITO CON LOCALSTORAGE
// ==========================================

/**
 * Obtiene el carrito desde localStorage
 * @returns {Array} Array de productos en el carrito
 */
function getCarrito() {
    // Priorizar la clave actual
    const actual = localStorage.getItem('carritoRobinson');
    if (actual) {
        return JSON.parse(actual);
    }

    // Migrar desde la clave antigua 'carrito' si existe (compatibilidad hacia atrás)
    const antiguo = localStorage.getItem('carrito');
    if (antiguo) {
        try {
            const parsed = JSON.parse(antiguo);
            localStorage.setItem('carritoRobinson', JSON.stringify(parsed));
            localStorage.removeItem('carrito');
            return parsed;
        } catch (e) {
            console.error('Error migrando carrito antiguo:', e);
        }
    }

    return [];
}

/**
 * Guarda el carrito en localStorage
 * @param {Array} carrito - Array de productos
 */
function setCarrito(carrito) {
    localStorage.setItem('carritoRobinson', JSON.stringify(carrito));
}

/**
 * Agrega un producto al carrito
 * @param {Object} producto - Objeto con datos del producto
 */
function agregarAlCarrito(producto) {
    // Si la API central está disponible y no es esta misma función, delegar
    if (window && typeof window.agregarAlCarrito === 'function' && window.agregarAlCarrito !== agregarAlCarrito) {
        return window.agregarAlCarrito(producto);
    }

    // Fallback local (compatibilidad): manejar con la clave 'carritoRobinson'
    let carrito = getCarrito();
    
    // Buscar si el producto ya existe en el carrito (considerar tipo si existe)
    const productoExistente = carrito.find(item => item.id === producto.id && (producto.tipo ? item.tipo === producto.tipo : true));
    
    if (productoExistente) {
        productoExistente.cantidad++;
    } else {
        carrito.push({
            ...producto,
            cantidad: 1
        });
    }
    
    setCarrito(carrito);
    actualizarContadorCarrito();
    
    // Mostrar mensaje de éxito
    mostrarNotificacion(`✓ ${producto.nombre} agregado al carrito`);
}

/**
 * Actualiza el contador del carrito en el navbar
 */
function actualizarContadorCarrito() {
    const carrito = getCarrito();
    const totalItems = carrito.reduce((total, item) => total + item.cantidad, 0);
    
    // Actualizar todos los contadores en la página (puede haber varios badges)
    const elementos = document.querySelectorAll('.cart-count');
    elementos.forEach(el => {
        el.textContent = totalItems;
        el.style.display = totalItems > 0 ? 'inline-block' : 'none';
    });
}

/**
 * Muestra una notificación temporal
 * @param {string} mensaje - Mensaje a mostrar
 */
function mostrarNotificacion(mensaje) {
    // Crear elemento de notificación
    const notificacion = document.createElement('div');
    notificacion.className = 'alert alert-success position-fixed';
    notificacion.style.cssText = 'top: 80px; right: 20px; z-index: 9999; min-width: 250px;';
    notificacion.textContent = mensaje;
    
    document.body.appendChild(notificacion);
    
    // Eliminar después de 3 segundos
    setTimeout(() => {
        notificacion.remove();
    }, 3000);
}

// ==========================================
// EVENTO PARA BOTONES DE AÑADIR AL CARRITO
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    // Preferir la función centralizada si existe
    if (window.actualizarContadorCarrito) {
        window.actualizarContadorCarrito();
    } else {
        actualizarContadorCarrito();
    }

    // Agregar event listeners a todos los botones de "Añadir al carrito"
    const botonesAgregar = document.querySelectorAll('.btn-add-cart');
    
    botonesAgregar.forEach(boton => {
        boton.addEventListener('click', (e) => {
            e.preventDefault();
            
            // Obtener datos del producto desde atributos data-*
            const producto = {
                id: parseInt(boton.dataset.id),
                nombre: boton.dataset.nombre,
                precio: parseFloat(boton.dataset.precio),
                imagen: boton.dataset.imagen,
                tipo: boton.dataset.tipo || 'general'
            };
            
            if (window.agregarAlCarrito) {
                window.agregarAlCarrito(producto);
            } else {
                // Fallback: operación local mínima si la API central no está cargada
                let carrito = JSON.parse(localStorage.getItem('carritoRobinson') || '[]');
                const idx = carrito.findIndex(item => item.id === producto.id && item.tipo === producto.tipo);
                if (idx !== -1) carrito[idx].cantidad += 1; else carrito.push({...producto, cantidad:1});
                localStorage.setItem('carritoRobinson', JSON.stringify(carrito));
                // Actualizar contador localmente
                const elementos = document.querySelectorAll('.cart-count');
                elementos.forEach(el => el.textContent = carrito.reduce((s,i)=>s+i.cantidad,0));
            }
        });
    });
});

// ==========================================
// FUNCIONES AUXILIARES
// ==========================================

/**
 * Vacía completamente el carrito
 */
function vaciarCarrito() {
    localStorage.removeItem('carritoRobinson');
    actualizarContadorCarrito();
}

/**
 * Elimina un producto específico del carrito
 * @param {number} productoId - ID del producto a eliminar
 */
function eliminarDelCarrito(productoId) {
    let carrito = getCarrito();
    carrito = carrito.filter(item => item.id !== productoId);
    setCarrito(carrito);
    actualizarContadorCarrito();
}

/**
 * Obtiene el total del carrito
 * @returns {number} Total en soles
 */
function calcularTotalCarrito() {
    const carrito = getCarrito();
    return carrito.reduce((total, item) => total + (item.precio * item.cantidad), 0);
}

// ==========================================
// CONSOLA - DEBUG (solo en desarrollo)
// ==========================================
console.log('🛒 Sistema de carrito con localStorage inicializado');
console.log('📦 Productos en carrito:', getCarrito());
