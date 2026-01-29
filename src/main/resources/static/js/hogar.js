// ==========================================
// CATÁLOGO DE PRODUCTOS HOGAR
// ==========================================

// Catálogo completo de productos del hogar
const productosHogar = [
    // Menaje - Sábanas
    {
        id: 1,
        nombre: 'Juego de sábanas 200 hilos | 1 plaza',
        precio: 89,
        categoria: 'menaje',
        imagen: '/img/sabanas1.webp',
        descripcion: 'Sábanas de algodón suave'
    },
    {
        id: 2,
        nombre: 'Juego de sábanas 200 hilos | 2 plazas',
        precio: 120,
        categoria: 'menaje',
        imagen: '/img/sabana2.jpg',
        descripcion: 'Ideal para cama matrimonial'
    },
    {
        id: 3,
        nombre: 'Edredón reversible cama matrimonial',
        precio: 200,
        categoria: 'menaje',
        imagen: '/img/sabanas3.webp',
        descripcion: 'Diseño elegante y moderno'
    },
    {
        id: 4,
        nombre: 'Almohada hotelera premium (unidad)',
        precio: 60,
        categoria: 'menaje',
        imagen: '/img/almohada.avif',
        descripcion: 'Máximo confort'
    },
    
    // Menaje - Toallas
    {
        id: 5,
        nombre: 'Juego de toallas 4 piezas algodón',
        precio: 140,
        categoria: 'menaje',
        imagen: '/img/toallas1.webp',
        descripcion: 'Suaves y absorbentes'
    },
    {
        id: 6,
        nombre: 'Set x4 Toallas Mano/Baño Roberta Allen Lollipop',
        precio: 80,
        categoria: 'menaje',
        imagen: '/img/toallas2.avif',
        descripcion: 'Diseño exclusivo'
    },
    {
        id: 7,
        nombre: 'Toalla Clásica Baño',
        precio: 75,
        categoria: 'menaje',
        imagen: '/img/toallas3.avif',
        descripcion: 'Algodón 100%'
    },
    {
        id: 8,
        nombre: 'Toalla Premium Baño',
        precio: 99,
        categoria: 'menaje',
        imagen: '/img/toallas4.avif',
        descripcion: 'Extra absorbente'
    },
    
    // Cocina - Vajillas
    {
        id: 9,
        nombre: 'Juego de Vajilla Porcelana Combo 60 Piezas',
        precio: 249.90,
        categoria: 'cocina',
        imagen: '/img/vajilla1.webp',
        descripcion: 'Juego completo para 12 personas'
    },
    {
        id: 10,
        nombre: 'Juego de Vajilla Porcelana 30 Piezas Paula',
        precio: 199.90,
        categoria: 'cocina',
        imagen: '/img/vajilla2.webp',
        descripcion: 'Diseño elegante Paula'
    },
    {
        id: 11,
        nombre: 'Vajilla x16 Piezas Porcelana con Textura',
        precio: 99.90,
        categoria: 'cocina',
        imagen: '/img/vajilla3.webp',
        descripcion: 'Textura moderna'
    },
    {
        id: 12,
        nombre: 'Set Vajilla Decal Rosa 16 Piezas',
        precio: 49.90,
        categoria: 'cocina',
        imagen: '/img/vajilla4.jpg',
        descripcion: 'Delicado diseño rosa'
    },
    
    // Decoración - Plantas
    {
        id: 13,
        nombre: 'Planta Olivo Artificial 30×132 cm',
        precio: 129.90,
        categoria: 'decoracion',
        imagen: '/img/planta1.webp',
        descripcion: 'Planta artificial grande'
    },
    {
        id: 14,
        nombre: 'Planta Grande Eucalipto 120 cm',
        precio: 99.90,
        categoria: 'decoracion',
        imagen: '/img/planta2.webp',
        descripcion: 'Eucalipto decorativo'
    },
    {
        id: 15,
        nombre: 'Planta Ficus artificial 154 cm',
        precio: 179.90,
        categoria: 'decoracion',
        imagen: '/img/planta3.avif',
        descripcion: 'Ficus realista'
    },
    {
        id: 16,
        nombre: 'Planta Sansevieria Artificial con maceta',
        precio: 83.70,
        categoria: 'decoracion',
        imagen: '/img/planta4.avif',
        descripcion: 'Perfecta para interiores'
    }
];

// ==========================================
// FUNCIONES DE RENDERIZADO
// ==========================================

// Renderizar productos
function renderizarProductos(productosFiltrados) {
    const catalogo = document.querySelector('.row.g-4');
    if (!catalogo) return;

    catalogo.innerHTML = productosFiltrados.map(producto => `
        <div class="col-sm-6 col-md-4 col-lg-3 product-item" data-category="${producto.categoria}">
            <div class="card product-card h-100">
                <div class="image-container">
                    <img src="${producto.imagen}" class="card-img-top" alt="${producto.nombre}">
                </div>
                <div class="card-body">
                    <h6 class="card-title">${producto.nombre}</h6>
                    <p class="small text-muted mb-2">${producto.descripcion}</p>
                    <p class="fw-bold text-danger mb-3">S/ ${producto.precio.toFixed(2)}</p>
                    <button class="btn btn-agregar w-100" onclick="agregarAlCarrito(${producto.id}, 'hogar')">
                        <i class="fas fa-cart-plus me-2"></i>AGREGAR
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

// Inicializar catálogo al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    renderizarProductos(productosHogar);
    
    // Configurar filtros de categoría
    const categoryButtons = document.querySelectorAll('.category-btn');
    categoryButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Actualizar botón activo
            categoryButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            
            // Filtrar productos
            const filter = button.dataset.filter;
            const productosFiltrados = filter === 'all' 
                ? productosHogar 
                : productosHogar.filter(p => p.categoria === filter);
            
            renderizarProductos(productosFiltrados);
        });
    });
    
    // Configurar ordenamiento
    const sortSelect = document.querySelector('#sortSelect');
    if (sortSelect) {
        sortSelect.addEventListener('change', (e) => {
            let productosOrdenados = [...productosHogar];
            
            switch(e.target.value) {
                case 'menor-precio':
                    productosOrdenados.sort((a, b) => a.precio - b.precio);
                    break;
                case 'mayor-precio':
                    productosOrdenados.sort((a, b) => b.precio - a.precio);
                    break;
                default: // Relevancia
                    productosOrdenados = [...productosHogar];
            }
            
            renderizarProductos(productosOrdenados);
        });
    }
});

// ==========================================
// FUNCIÓN PARA AGREGAR AL CARRITO
// ==========================================

function agregarAlCarrito(productoId, tipo) {
    const producto = productosHogar.find(p => p.id === productoId);
    if (!producto) return;

    // Obtener carrito del localStorage
    let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
    
    // Verificar si el producto ya existe en el carrito
    const productoExistente = carrito.find(item => item.id === productoId && item.tipo === tipo);
    
    if (productoExistente) {
        productoExistente.cantidad += 1;
    } else {
        carrito.push({
            id: producto.id,
            nombre: producto.nombre,
            precio: producto.precio,
            imagen: producto.imagen,
            cantidad: 1,
            tipo: tipo
        });
    }
    
    // Guardar en localStorage
    localStorage.setItem('carrito', JSON.stringify(carrito));
    
    // Actualizar contador del carrito
    actualizarContadorCarrito();
    
    // Mostrar notificación
    mostrarNotificacion('Producto agregado al carrito');
}

function actualizarContadorCarrito() {
    const carrito = JSON.parse(localStorage.getItem('carrito')) || [];
    const totalItems = carrito.reduce((sum, item) => sum + item.cantidad, 0);
    
    const badges = document.querySelectorAll('.cart-count');
    badges.forEach(badge => {
        badge.textContent = totalItems;
    });
}

function mostrarNotificacion(mensaje) {
    // Crear notificación temporal
    const notif = document.createElement('div');
    notif.className = 'alert alert-success position-fixed top-0 start-50 translate-middle-x mt-3';
    notif.style.zIndex = '9999';
    notif.innerHTML = `<i class="fas fa-check-circle me-2"></i>${mensaje}`;
    document.body.appendChild(notif);
    
    setTimeout(() => {
        notif.remove();
    }, 2000);
}

// Actualizar contador al cargar la página
document.addEventListener('DOMContentLoaded', actualizarContadorCarrito);
