// ofertas.js - Funcionalidades específicas para página de ofertas

document.addEventListener('DOMContentLoaded', function() {
    // Datos de ejemplo para productos en oferta
    const productosOferta = [
        {
            id: 1,
            nombre: "Refrigeradora Samsung Top Mount 255Lt Silver",
            descripcion: "Refrigerador de 255L con tecnología Digital Inverter y diseño elegante",
            precioAnterior: 1299.99,
            precioOferta: 999.99,
            descuento: 23,
            imagen: "/img/Refrig_Samsung.webp",
            categoria: "destacados",
            stock: 8,
            destacado: true
        },
        {
            id: 2,
            nombre: "Lavadora 19 Kg Turbodrum Carga Superior Negro Claro LG",
            descripcion: "Lavadora de 19kg con tecnología TurboDrum y SmartDiagnosis",
            precioAnterior: 899.99,
            precioOferta: 699.99,
            descuento: 22,
            imagen: "/img/Lav_LG.webp",
            categoria: "rebajados",
            stock: 15,
            destacado: false
        },
        {
            id: 3,
            nombre: "Televisor Samsung QLED 55 4K Ultra HD Tizen",
            descripcion: "Televisor de 55 pulgadas con tecnología QLED y resolución 4K",
            precioAnterior: 1299.99,
            precioOferta: 899.99,
            descuento: 31,
            imagen: "/img/Tv_Samsung_55.webp",
            categoria: "rebajados",
            stock: 12,
            destacado: false
        },
        {
            id: 4,
            nombre: "Horno Microonda Panasonic",
            descripcion: "Horno de microondas de 23L con funcion Quick 30 y bloqueo para niños",
            precioAnterior: 899.99,
            precioOferta: 699.99,
            descuento: 22,
            imagen: "/img/Micro_Panasonic_27.webp",
            categoria: "rebajados",
            stock: 16,
            destacado: false
        },
        // ... (agrega más productos según necesites)
    ];

    // Inicializar contador
    inicializarContadorOferta();
    
    // Cargar productos
    cargarProductos(productosOferta);
    
    // Configurar filtros
    configurarFiltros();
    
    // Configurar eventos de carrito
    configurarEventosCarrito();
});

function inicializarContadorOferta() {
    // Contador regresivo de 72 horas
    let horas = 72;
    let minutos = 0;
    let segundos = 0;
    
    function actualizarContador() {
        if (segundos === 0) {
            if (minutos === 0) {
                if (horas === 0) {
                    // Oferta terminada
                    clearInterval(intervalo);
                    document.getElementById('contador-oferta').textContent = "00:00:00";
                    document.getElementById('contador-oferta').classList.add('text-danger');
                    return;
                }
                horas--;
                minutos = 59;
            } else {
                minutos--;
            }
            segundos = 59;
        } else {
            segundos--;
        }
        
        // Formatear y mostrar
        const formato = `${horas.toString().padStart(2, '0')}:${minutos.toString().padStart(2, '0')}:${segundos.toString().padStart(2, '0')}`;
        document.getElementById('contador-oferta').textContent = formato;
    }
    
    // Actualizar cada segundo
    const intervalo = setInterval(actualizarContador, 1000);
    actualizarContador(); // Ejecutar inmediatamente
}

function cargarProductos(productos) {
    const container = document.getElementById('productos-oferta-container');
    
    if (!container) return;
    
    container.innerHTML = '';
    
    productos.forEach((producto, index) => {
        const col = document.createElement('div');
        col.className = 'col-md-4 col-lg-3';
        col.innerHTML = `
            <div class="card card-oferta h-100 producto-animado" 
                 style="animation-delay: ${index * 0.1}s;">
                <div class="position-relative">
                    <span class="oferta-badge">-${producto.descuento}%</span>
                    <img src="${producto.imagen}" class="card-img-top" alt="${producto.nombre}" 
                         style="height: 200px; object-fit: cover;">
                </div>
                <div class="card-body d-flex flex-column">
                    <h5 class="card-title">${producto.nombre}</h5>
                    <p class="card-text text-muted flex-grow-1">${producto.descripcion}</p>
                    <div class="mt-auto">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="precio-anterior text-decoration-line-through">S/ ${producto.precioAnterior.toLocaleString()}</span>
                            <span class="precio-oferta">S/ ${producto.precioOferta.toLocaleString()}</span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="stock ${producto.stock < 5 ? 'stock-bajo' : 'text-success'}">
                                <i class="fas ${producto.stock < 5 ? 'fa-exclamation-triangle' : 'fa-check-circle'} me-1"></i>
                                ${producto.stock < 5 ? `Solo ${producto.stock} unidades` : 'En stock'}
                            </span>
                        </div>
                        <button class="btn btn-primary w-100 btn-agregar-carrito" 
                                data-id="${producto.id}"
                                data-nombre="${producto.nombre}"
                                data-precio="${producto.precioOferta}"
                                data-imagen="${producto.imagen}">
                            <i class="fas fa-cart-plus me-2"></i>Agregar al carrito
                        </button>
                    </div>
                </div>
            </div>
        `;
        container.appendChild(col);
    });
}

function configurarFiltros() {
    const selectOrden = document.getElementById('ordenar');
    
    // Filtros de categoría eliminados - ya no se utilizan
    
    if (selectOrden) {
        selectOrden.addEventListener('change', function() {
            console.log(`Ordenar por: ${this.value}`);
        });
    }
}

function configurarEventosCarrito() {
    // Eventos para agregar productos al carrito
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-agregar-carrito')) {
            const boton = e.target.closest('.btn-agregar-carrito');
            agregarProductoAlCarrito(boton);
        }
        
        if (e.target.closest('.btn-combo-agregar')) {
            const boton = e.target.closest('.btn-combo-agregar');
            agregarComboAlCarrito(boton);
        }
    });
    
    // Newsletter
    const formNewsletter = document.getElementById('form-newsletter');
    if (formNewsletter) {
        formNewsletter.addEventListener('submit', function(e) {
            e.preventDefault();
            const email = this.querySelector('input[type="email"]').value;
            
            // Simular envío
            this.innerHTML = `
                <div class="alert alert-success" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    ¡Gracias por suscribirte! Te enviaremos nuestras mejores ofertas a: ${email}
                </div>
            `;
            
            // En un caso real, aquí harías una petición AJAX al servidor
        });
    }
}

function agregarProductoAlCarrito(boton) {
    const producto = {
        id: boton.dataset.id,
        nombre: boton.dataset.nombre,
        precio: parseFloat(boton.dataset.precio),
        imagen: boton.dataset.imagen,
        cantidad: 1
    };
    
    // Usar la función del carrito si existe
    if (typeof agregarAlCarrito === 'function') {
        agregarAlCarrito(producto);
        mostrarNotificacionCarrito(producto.nombre);
    } else {
        // Fallback a localStorage
        agregarAlCarritoFallback(producto);
    }
}

function agregarComboAlCarrito(boton) {
    const comboId = boton.dataset.combo;
    let combo;
    
    if (comboId === '1') {
        combo = {
            id: 'combo-1',
            nombre: 'Combo Cocina Completa',
            precio: 1999.99,
            imagen: 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80',
            cantidad: 1,
            esCombo: true
        };
    } else {
        combo = {
            id: 'combo-2',
            nombre: 'Combo Lavandería',
            precio: 1399.99,
            imagen: 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80',
            cantidad: 1,
            esCombo: true
        };
    }
    
    // Usar la función del carrito si existe
    if (typeof agregarAlCarrito === 'function') {
        agregarAlCarrito(combo);
        mostrarNotificacionCarrito(combo.nombre);
    } else {
        // Fallback a localStorage
        agregarAlCarritoFallback(combo);
    }
}

function agregarAlCarritoFallback(item) {
    let carrito = JSON.parse(localStorage.getItem('carritoRobinson')) || [];
    
    // Verificar si ya existe
    const index = carrito.findIndex(p => p.id === item.id);
    
    if (index !== -1) {
        carrito[index].cantidad += 1;
    } else {
        carrito.push(item);
    }
    
    localStorage.setItem('carritoRobinson', JSON.stringify(carrito));
    actualizarContadorCarritoFallback();
    mostrarNotificacionCarrito(item.nombre);
}

function actualizarContadorCarritoFallback() {
    const carrito = JSON.parse(localStorage.getItem('carritoRobinson')) || [];
    const total = carrito.reduce((sum, item) => sum + item.cantidad, 0);
    
    // Actualizar en el navbar
    const contador = document.querySelector('.cart-count');
    if (contador) {
        contador.textContent = total;
        contador.style.display = total > 0 ? 'inline-block' : 'none';
    }
}

function mostrarNotificacionCarrito(nombreProducto) {
    // Crear notificación temporal
    const notificacion = document.createElement('div');
    notificacion.className = 'position-fixed top-0 end-0 m-3 p-3 bg-success text-white rounded shadow-lg';
    notificacion.style.zIndex = '9999';
    notificacion.innerHTML = `
        <div class="d-flex align-items-center">
            <i class="fas fa-check-circle fa-2x me-3"></i>
            <div>
                <strong>¡Producto agregado!</strong><br>
                ${nombreProducto} se agregó al carrito.
            </div>
        </div>
    `;
    
    document.body.appendChild(notificacion);
    
    // Remover después de 3 segundos
    setTimeout(() => {
        notificacion.classList.add('animate__animated', 'animate__fadeOutRight');
        setTimeout(() => {
            if (notificacion.parentNode) {
                notificacion.parentNode.removeChild(notificacion);
            }
        }, 500);
    }, 3000);
}