// ==========================================
// CATÁLOGO DE ELECTRODOMÉSTICOS
// ==========================================

// Catálogo completo de productos
const productosCompletos = [
    // Refrigeración
    {
        id: 1,
        nombre: 'Refrigerador 420L',
        precio: 2899,
        categoria: 'refrigeracion',
        imagen: '/img/refrigerador.webp',
        descripcion: 'Eficiencia A++'
    },
    {
        id: 4,
        nombre: 'Refrigerador 690L',
        precio: 5599,
        categoria: 'refrigeracion',
        imagen: '/img/Refrigueradora_Samsung_690L.jpg',
        descripcion: 'Dispensador de agua y hielo'
    },
    
    // TV
    {
        id: 5,
        nombre: 'Televisor SAMSUNG QLED 75"',
        precio: 1899,
        categoria: 'tv',
        imagen: '/img/Televisor SAMSUNG QLED  UHD 75 4K.jpg',
        descripcion: 'UHD 4K Smart TV QN75Q60DAGXPE'
    },
    {
        id: 6,
        nombre: 'Televisor LG LED 43" HD',
        precio: 2999,
        categoria: 'tv',
        imagen: '/img/Televisor LG LED 43.jpg',
        descripcion: 'Smart TV Modelo 43LM6300PLA'
    },
    {
        id: 3,
        nombre: 'Smart TV OLED 55"',
        precio: 4499,
        categoria: 'tv',
        imagen: '/img/tv_oled_55.avif',
        descripcion: '4K UHD + HDR'
    },
    {
        id: 8,
        nombre: 'Smart TV 65" QLED',
        precio: 5999,
        categoria: 'tv',
        imagen: '/img/Samsung QLED 65 OLED.jpg',
        descripcion: 'Quantum Dot, 120Hz'
    },
    {
        id: 9,
        nombre: 'Soundbar 5.1 Dolby Atmos',
        precio: 999,
        categoria: 'tv',
        imagen: '/img/Soundbar Dolby Atmos.jpg',
        descripcion: 'Audio envolvente'
    },
    
    // Lavado
    {
        id: 2,
        nombre: 'Lavadora Secadora 10kg',
        precio: 1799,
        categoria: 'lavado',
        imagen: '/img/lavadora_inverter.webp',
        descripcion: 'Silenciosa y eficiente'
    },
    {
        id: 7,
        nombre: 'Secadora de Ropa 10kg',
        precio: 1499,
        categoria: 'lavado',
        imagen: '/img/Secadora de ropa 10kg.jpg',
        descripcion: 'Sensor de humedad'
    },
    
    // Cocina
    {
        id: 10,
        nombre: 'Cocina a Gas 6 Hornillas Indurama',
        precio: 1299,
        categoria: 'cocina',
        imagen: '/img/Cocina a Gas 6 Hornillas Indurama.jpg',
        descripcion: 'Acero inoxidable'
    },
    {
        id: 11,
        nombre: 'Horno Microondas 28L',
        precio: 499,
        categoria: 'cocina',
        imagen: '/img/Horno Microondas 28L.jpg',
        descripcion: 'Fácil modo de uso'
    },
    {
        id: 12,
        nombre: 'Licuadora Industrial 2L Jhumy',
        precio: 349,
        categoria: 'cocina',
        imagen: '/img/Licuadora Industrial 2L Jhumy.jpg',
        descripcion: '1200W de potencia'
    },
    {
        id: 13,
        nombre: 'Campana Extractora 90cm Sole',
        precio: 799,
        categoria: 'cocina',
        imagen: '/img/Campana Extractora 90cm Sole.jpg',
        descripcion: '3 velocidades'
    },
    {
        id: 14,
        nombre: 'Horno Eléctrico 60L',
        precio: 899,
        categoria: 'cocina',
        imagen: '/img/Horno Eléctrico 60L.jpg',
        descripcion: 'Empotrable, convección'
    },
    {
        id: 15,
        nombre: 'Lavavajillas 14 Servicios',
        precio: 1999,
        categoria: 'cocina',
        imagen: '/img/Lavavajillas 14 Servicios.jpg',
        descripcion: '6 programas de lavado'
    }
];

// ==========================================
// FILTRADO DE PRODUCTOS
// ==========================================

let categoriaActual = 'all';

/**
 * Filtra productos por categoría
 * @param {string} categoria - La categoría a filtrar
 */
function filtrarCategoria(categoria) {
    categoriaActual = categoria;
    renderizarProductos();
    
    // Actualizar botones activos
    document.querySelectorAll('.category-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');
}

/**
 * Renderiza los productos en el DOM
 */
function renderizarProductos() {
    const container = document.getElementById('catalogo-productos');
    const productosFiltrados = categoriaActual === 'all' 
        ? productosCompletos 
        : productosCompletos.filter(p => p.categoria === categoriaActual);

    container.innerHTML = productosFiltrados.map((producto, index) => `
        <div class="col-md-4 col-lg-3 product-item" data-category="${producto.categoria}" data-aos="fade-up" data-aos-delay="${index * 50}">
            <div class="card product-card h-100">
                <div class="image-container">
                    <img src="${producto.imagen}" 
                         class="card-img-top" 
                         alt="${producto.nombre}"
                         onerror="this.src='https://via.placeholder.com/280x280/cccccc/666666?text=Sin+Imagen'">
                </div>
                <div class="card-body text-center d-flex flex-column">
                    <h5 class="card-title">${producto.nombre}</h5>
                    <p class="text-muted flex-grow-1">${producto.descripcion}</p>
                    <h4 class="fw-bold text-danger mb-3">S/ ${producto.precio.toLocaleString()}</h4>
                    <button class="btn btn-agregar w-100 btn-add-cart" 
                            data-id="${producto.id}" 
                            data-nombre="${producto.nombre}" 
                            data-precio="${producto.precio}" 
                            data-imagen="${producto.imagen}">
                        Añadir al carrito
                    </button>
                </div>
            </div>
        </div>
    `).join('');

    // Re-inicializar AOS después de renderizar
    if (typeof AOS !== 'undefined') {
        AOS.refresh();
    }
    
    // Re-vincular eventos de botones
    vincularEventosCarrito();
}

/**
 * Vincula eventos a los botones de añadir al carrito
 */
function vincularEventosCarrito() {
    document.querySelectorAll('.btn-add-cart').forEach(boton => {
        boton.addEventListener('click', (e) => {
            e.preventDefault();
            const producto = {
                id: parseInt(boton.dataset.id),
                nombre: boton.dataset.nombre,
                precio: parseFloat(boton.dataset.precio),
                imagen: boton.dataset.imagen
            };
            agregarAlCarrito(producto);
        });
    });
}

/**
 * Configura los botones de filtro
 */
function configurarFiltros() {
    document.querySelectorAll('.category-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const categoria = this.dataset.filter;
            filtrarCategoria(categoria);
        });
    });
}

// ==========================================
// INICIALIZACIÓN
// ==========================================

document.addEventListener('DOMContentLoaded', () => {
    renderizarProductos();
    configurarFiltros();
    actualizarContadorCarrito();
});

console.log('🛍️ Módulo de electrodomésticos cargado correctamente');
