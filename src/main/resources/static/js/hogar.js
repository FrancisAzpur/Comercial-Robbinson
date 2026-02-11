// ==========================================
// CAT\u00c1LOGO DE PRODUCTOS HOGAR (SIN CATEGOR\u00cdAS)
// ==========================================

// Cat\u00e1logo completo de productos del hogar
const productosHogar = [
    // Menaje - S\u00e1banas
    {
        id: 1,
        nombre: 'Juego de s\u00e1banas 200 hilos | 1 plaza',
        precio: 89,        categoria: 'menaje',        imagen: '/img/sabanas1.webp',
        descripcion: 'S\u00e1banas de algod\u00f3n suave'
    },
    {
        id: 2,
        nombre: 'Juego de s\u00e1banas 200 hilos | 2 plazas',
        precio: 120,        categoria: 'menaje',        imagen: '/img/sabana2.jpg',
        descripcion: 'Ideal para cama matrimonial'
    },
    {
        id: 3,
        nombre: 'Edred\u00f3n reversible cama matrimonial',
        precio: 200,        categoria: 'menaje',        imagen: '/img/sabanas3.webp',
        descripcion: 'Dise\u00f1o elegante y moderno'
    },
    {
        id: 4,
        nombre: 'Almohada hotelera premium (unidad)',
        precio: 60,        categoria: 'menaje',        imagen: '/img/almohada.avif',
        descripcion: 'M\u00e1ximo confort'
    },
    
    // Menaje - Toallas
    {
        id: 5,
        nombre: 'Juego de toallas 4 piezas algod\u00f3n',
        precio: 140,        categoria: 'menaje',        imagen: '/img/toallas1.webp',
        descripcion: 'Suaves y absorbentes'
    },
    {
        id: 6,
        nombre: 'Set x4 Toallas Mano/Ba\u00f1o Roberta Allen Lollipop',
        precio: 80,        categoria: 'menaje',        imagen: '/img/toallas2.avif',
        descripcion: 'Dise\u00f1o exclusivo'
    },
    {
        id: 7,
        nombre: 'Toalla Cl\u00e1sica Ba\u00f1o',
        precio: 75,
        imagen: '/img/toallas3.avif',
        descripcion: 'Algod\u00f3n 100%'
    },
    {
        id: 8,
        nombre: 'Toalla Premium Ba\u00f1o',
        precio: 99,        categoria: 'menaje',        imagen: '/img/toallas4.avif',
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
        precio: 199.90,        categoria: 'cocina',        imagen: '/img/vajilla2.webp',
        descripcion: 'Dise\u00f1o elegante Paula'
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
        precio: 49.90,        categoria: 'cocina',        imagen: '/img/vajilla4.jpg',
        descripcion: 'Delicado dise\u00f1o rosa'
    },
    
    // Decoraci\u00f3n - Plantas
    {
        id: 13,
        nombre: 'Planta Olivo Artificial 30\u00d7132 cm',
        precio: 129.90,        categoria: 'decoracion',        imagen: '/img/planta1.webp',
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
// FUNCIONES DE RENDERIZADO Y FILTRADO
// ==========================================

let categoriaActual = 'todos';

// Renderizar productos según filtro de categoría
function renderizarProductos() {
    const catalogo = document.querySelector('.row.g-4');
    if (!catalogo) return;

    // Filtrar productos según categoría
    const productosFiltrados = categoriaActual === 'todos' 
        ? productosHogar 
        : productosHogar.filter(p => p.categoria === categoriaActual);

    catalogo.innerHTML = productosFiltrados.map(producto => `
        <div class="col-sm-6 col-md-4 col-lg-3 product-item">
            <div class="card product-card h-100">
                <div class="image-container">
                    <img src="${producto.imagen}" class="card-img-top" alt="${producto.nombre}">
                </div>
                <div class="card-body">
                    <h6 class="card-title">${producto.nombre}</h6>
                    <p class="small text-muted mb-2">${producto.descripcion}</p>
                    <p class="fw-bold text-danger mb-3">S/ ${producto.precio.toFixed(2)}</p>
                    <button class="btn btn-agregar w-100" onclick="addToCartHogar(${producto.id})">
                        <i class="fas fa-cart-plus me-2"></i>AGREGAR
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

// Filtrar por categoría
function filtrarPorCategoria(categoria) {
    categoriaActual = categoria;
    renderizarProductos();
}

// Configurar filtros de categoría
function configurarFiltros() {
    document.querySelectorAll('.category-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            // Remover clase active de todos los botones
            document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
            // Agregar clase active al botón clickeado
            boton.classList.add('active');
            
            // Filtrar por categoría
            const categoria = boton.dataset.filter;
            filtrarPorCategoria(categoria);
        });
    });
}

// Inicializar catálogo al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    configurarFiltros();
    renderizarProductos();
    
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
            
            // Aplicar ordenamiento sobre los productos filtrados
            if (categoriaActual !== 'todos') {
                productosOrdenados = productosOrdenados.filter(p => p.categoria === categoriaActual);
            }
            
            const catalogo = document.querySelector('.row.g-4');
            if (!catalogo) return;
            
            catalogo.innerHTML = productosOrdenados.map(producto => `
                <div class="col-sm-6 col-md-4 col-lg-3 product-item">
                    <div class="card product-card h-100">
                        <div class="image-container">
                            <img src="${producto.imagen}" class="card-img-top" alt="${producto.nombre}">
                        </div>
                        <div class="card-body">
                            <h6 class="card-title">${producto.nombre}</h6>
                            <p class="small text-muted mb-2">${producto.descripcion}</p>
                            <p class="fw-bold text-danger mb-3">S/ ${producto.precio.toFixed(2)}</p>
                            <button class="btn btn-agregar w-100" onclick="addToCartHogar(${producto.id})">
                                <i class="fas fa-cart-plus me-2"></i>AGREGAR
                            </button>
                        </div>
                    </div>
                </div>
            `).join('');
        });
    }
});

// ==========================================
// FUNCIÓN PARA AGREGAR AL CARRITO
// Usa la API REST /api/carrito/* del backend
// ==========================================

// Esta función ya no se usa directamente, se delega a addToCartHogar
function agregarAlCarritoHogar(productoId) {
    addToCartHogar(productoId);
}

function mostrarNotificacion(mensaje) {
    // Crear notificaci\u00f3n temporal
    const notif = document.createElement('div');
    notif.className = 'alert alert-success position-fixed top-0 start-50 translate-middle-x mt-3';
    notif.style.zIndex = '9999';
    notif.innerHTML = `<i class="fas fa-check-circle me-2"></i>${mensaje}`;
    document.body.appendChild(notif);
    
    setTimeout(() => {
        notif.remove();
    }, 2000);
}

// Actualizar contador al cargar la página (usa la función global de carrito.js)
document.addEventListener('DOMContentLoaded', function() {
    if (window.actualizarContadorCarrito) {
        window.actualizarContadorCarrito();
    }
});
// Función para agregar desde la vista Hogar usando la API del backend
function addToCartHogar(productoId) {
    console.log('addToCartHogar invoked with', productoId);
    productoId = Number(productoId);

    const producto = productosHogar.find(p => p.id === productoId);
    if (!producto) { console.warn('Producto no encontrado en productosHogar:', productoId); return; }

    const payload = {
        id: producto.id,
        nombre: producto.nombre,
        precio: producto.precio,
        imagen: producto.imagen
    };

    // Usar la API central del backend
    if (window && typeof window.agregarAlCarrito === 'function') {
        window.agregarAlCarrito(payload).then(exito => {
            if (exito) {
                mostrarNotificacion('Producto agregado al carrito');
            }
        }).catch(e => {
            console.error('Error ejecutando agregarAlCarrito:', e);
        });
    } else {
        console.error('agregarAlCarrito no disponible');
    }
}