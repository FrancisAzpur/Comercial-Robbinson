package com.Robbinson.ComRobinson.controladores;

import java.math.BigDecimal; // Import necesario para BigDecimal para precios de productos
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Import necesario para streams para filtrar y limitar listas

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Robbinson.ComRobinson.modelo.Producto;

/**
 * Controlador principal para la página web pública de Comercial Robinson
 * Maneja las rutas de la tienda online que ven los clientes
 * 
 * IMPORTANTE: Este controlador solo muestra las páginas HTML.
 * Los productos se cargan dinámicamente desde JavaScript (electrodomesticos.js, hogar.js)
 * El carrito funciona con localStorage del navegador (sin backend)
 */
@Controller  // Indica a Spring que esta clase maneja páginas web
public class HomeController {

    // ==================== PÁGINAS PÚBLICAS DE LA TIENDA ====================
    
    /**
     * Página principal (Home)
     * Ruta: GET / o GET /home
     * Muestra el inicio con categorías y productos destacados
     */
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // Pasar título de la página
        model.addAttribute("titulo", "Inicio");
        
        // Pasar productos destacados (simulados, para demostración)
        model.addAttribute("productosDestacados", obtenerProductosDestacados());
        
        return "index";  // Retorna templates/index.html
    }

    /**
     * Página de Electrodomésticos
     * Ruta: GET /electrodomesticos
     * IMPORTANTE: Los productos se cargan dinámicamente desde static/js/electrodomesticos.js
     * Este método solo muestra la página HTML vacía
     */
    @GetMapping("/electrodomesticos")
    public String electrodomesticos(Model model) {
        model.addAttribute("titulo", "Electrodomésticos");
        return "electrodomesticos";  // templates/electrodomesticos.html
    }

    /**
     * Página de Hogar
     * Ruta: GET /hogar
     * IMPORTANTE: Los productos se cargan dinámicamente desde static/js/hogar.js
     * Este método solo muestra la página HTML vacía
     */
    @GetMapping("/hogar")
    public String hogar(Model model) {
        model.addAttribute("titulo", "Hogar");
        // Nota: productos se pasan pero no se usan, el JS carga los productos
        model.addAttribute("productos", obtenerProductosPorCategoria("Hogar"));
        return "hogar";  // templates/hogar.html
    }

    /**
     * Página de Ofertas
     * Ruta: GET /ofertas
     * Muestra productos en oferta
     */
    @GetMapping("/ofertas")
    public String ofertas(Model model) {
        model.addAttribute("titulo", "Ofertas Especiales");
        model.addAttribute("productos", obtenerProductosEnOferta());
        return "ofertas";  // templates/ofertas.html
    }

    /**
     * Página de Contacto
     * Ruta: GET /contacto
     * Formulario de contacto con información de la empresa
     */
    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("titulo", "Contacto");
        return "contacto";  // templates/contacto.html
    }

    /**
     * Detalle de producto
     */
    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        Producto producto = obtenerProductoPorId(id);
        model.addAttribute("titulo", producto.getNombre());
        model.addAttribute("producto", producto);
        return "detalle-producto";
    }

    // ==================== MÉTODOS AUXILIARES ====================
    // NOTA: Estos métodos simulan una base de datos.
    // Los datos están hardcodeados en el código (no en BD real)
    // En producción, estos datos vendrían de un repositorio con JPA/Hibernate
    
    /**
     * Obtiene todos los productos disponibles (SIMULADO)
     * 
     * IMPORTANTE: Estos productos coinciden EXACTAMENTE con los archivos JavaScript:
     * - static/js/electrodomesticos.js (15 productos: refrigeración, TV, lavado, cocina)
     * - static/js/hogar.js (16 productos: menaje, cocina, decoración)
     * 
     * Este método se usa principalmente para:
     * - Mostrar productos destacados en la página principal (index.html)
     * - Página de ofertas (/ofertas)
     * - Detalle de producto individual (/producto/{id})
     * 
     * NOTA: Las páginas /electrodomesticos y /hogar cargan productos desde JS directamente
     * 
     * @return Lista completa de 31 productos (15 electrodomésticos + 16 hogar)
     */
    private List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        
        // ========== ELECTRODOMÉSTICOS (15 productos) ==========
        
        // REFRIGERACIÓN (2)
        productos.add(new Producto(1L, "Refrigerador 420L", 
                "Refrigerador No Frost con eficiencia energética A++. Tecnología Inverter.",
                new BigDecimal("2899.00"), "refrigerador.webp", "Electrodomésticos", true, 
                false, null, 8));
        
        productos.add(new Producto(4L, "Refrigerador 690L Samsung", 
                "Dispensador de agua y hielo. Capacidad 690L ideal para familias grandes.",
                new BigDecimal("5599.00"), "Refrigueradora_Samsung_690L.jpg", "Electrodomésticos", true, 
                true, new BigDecimal("6299.00"), 4));

        // TV (5)
        productos.add(new Producto(5L, "Televisor SAMSUNG QLED 75\"", 
                "UHD 4K Smart TV QN75Q60DAGXPE. Quantum Dot para colores vibrantes.",
                new BigDecimal("1899.00"), "Televisor SAMSUNG QLED  UHD 75 4K.jpg", "Electrodomésticos", true, 
                true, new BigDecimal("2399.00"), 6));
        
        productos.add(new Producto(6L, "Televisor LG LED 43\" HD", 
                "Smart TV Modelo 43LM6300PLA. Conectividad WiFi y apps inteligentes.",
                new BigDecimal("2999.00"), "Televisor LG LED 43.jpg", "Electrodomésticos", true, 
                false, null, 10));
        
        productos.add(new Producto(3L, "Smart TV OLED 55\"", 
                "Televisor OLED 4K UHD con HDR. Negros perfectos y colores infinitos.",
                new BigDecimal("4499.00"), "tv_oled_55.avif", "Electrodomésticos", true, 
                true, new BigDecimal("5299.00"), 5));
        
        productos.add(new Producto(8L, "Smart TV 65\" QLED Samsung", 
                "Quantum Dot, 120Hz. Ideal para gaming y deportes. Panel OLED premium.",
                new BigDecimal("5999.00"), "Samsung TV 65 OLED.jpg", "Electrodomésticos", true, 
                true, new BigDecimal("7499.00"), 3));
        
        productos.add(new Producto(9L, "Soundbar 5.1 Dolby Atmos", 
                "Sistema de audio envolvente. Subwoofer inalámbrico incluido.",
                new BigDecimal("999.00"), "Soundbar Dolby Atmos.jpg", "Electrodomésticos", false, 
                false, null, 12));

        // LAVADO (2)
        productos.add(new Producto(2L, "Lavadora Secadora 10kg", 
                "Lavadora y secadora en uno. Tecnología Inverter silenciosa y eficiente.",
                new BigDecimal("1799.00"), "lavadora_inverter.webp", "Electrodomésticos", true, 
                true, new BigDecimal("2199.00"), 7));
        
        productos.add(new Producto(7L, "Secadora de Ropa 10kg", 
                "Secadora con sensor de humedad. Cuida tus prendas con tecnología inteligente.",
                new BigDecimal("1499.00"), "Secadora de ropa 10kg.jpg", "Electrodomésticos", false, 
                false, null, 9));

        // COCINA ELECTRODOMÉSTICOS (6)
        productos.add(new Producto(10L, "Cocina a Gas 6 Hornillas Indurama", 
                "Acero inoxidable resistente. 6 quemadores para cocinar en grande.",
                new BigDecimal("1299.00"), "Cocina a Gas 6 Hornillas Indurama.jpg", "Electrodomésticos", true, 
                false, null, 11));
        
        productos.add(new Producto(11L, "Horno Microondas 28L", 
                "Fácil modo de uso. 28 litros de capacidad con múltiples funciones.",
                new BigDecimal("499.00"), "Horno Microondas 28L.jpg", "Electrodomésticos", false, 
                false, null, 15));
        
        productos.add(new Producto(12L, "Licuadora Industrial 2L Jhumy", 
                "1200W de potencia. Perfecta para batidos y preparaciones profesionales.",
                new BigDecimal("349.00"), "Licuadora Industrial 2L Jhumy.jpg", "Electrodomésticos", false, 
                false, null, 20));
        
        productos.add(new Producto(13L, "Campana Extractora 90cm Sole", 
                "3 velocidades de extracción. Diseño moderno en acero inoxidable.",
                new BigDecimal("799.00"), "Campana Extractora 90cm Sole.jpg", "Electrodomésticos", false, 
                false, null, 8));
        
        productos.add(new Producto(14L, "Horno Eléctrico 60L", 
                "Empotrable con sistema de convección. 60L para preparaciones grandes.",
                new BigDecimal("899.00"), "Horno Eléctrico 60L.jpg", "Electrodomésticos", false, 
                false, null, 6));
        
        productos.add(new Producto(15L, "Lavavajillas 14 Servicios", 
                "Lavavajillas de alta eficiencia. Capacidad para 14 servicios completos.",
                new BigDecimal("1999.00"), "lavavajillas.jpg", "Electrodomésticos", true, 
                true, new BigDecimal("2499.00"), 5));

        // ========== HOGAR (16 productos) ==========
        
        // MENAJE - Sábanas (4)
        productos.add(new Producto(17L, "Juego de sábanas 200 hilos | 1 plaza", 
                "Sábanas de algodón suave. Perfectas para descanso individual.",
                new BigDecimal("89.00"), "sabanas1.webp", "Hogar", false, 
                false, null, 15));
        
        productos.add(new Producto(18L, "Juego de sábanas 200 hilos | 2 plazas", 
                "Ideal para cama matrimonial. 200 hilos de algodón premium.",
                new BigDecimal("120.00"), "sabana2.jpg", "Hogar", true, 
                false, null, 12));
        
        productos.add(new Producto(19L, "Edredón reversible cama matrimonial", 
                "Diseño elegante y moderno. Reversible con dos acabados diferentes.",
                new BigDecimal("200.00"), "sabanas3.webp", "Hogar", true, 
                false, null, 8));
        
        productos.add(new Producto(20L, "Almohada hotelera premium (unidad)", 
                "Máximo confort. Relleno especial para soporte cervical.",
                new BigDecimal("60.00"), "almohada.avif", "Hogar", false, 
                false, null, 20));

        // MENAJE - Toallas (4)
        productos.add(new Producto(21L, "Juego de toallas 4 piezas algodón", 
                "Suaves y absorbentes. Set completo para baño.",
                new BigDecimal("140.00"), "toallas1.webp", "Hogar", true, 
                false, null, 10));
        
        productos.add(new Producto(22L, "Set x4 Toallas Mano/Baño Roberta Allen Lollipop", 
                "Diseño exclusivo. Marca Roberta Allen con estilo moderno.",
                new BigDecimal("80.00"), "toallas2.avif", "Hogar", false, 
                false, null, 14));
        
        productos.add(new Producto(23L, "Toalla Clásica Baño", 
                "Algodón 100%. Diseño clásico y atemporal.",
                new BigDecimal("75.00"), "toallas3.avif", "Hogar", false, 
                false, null, 18));
        
        productos.add(new Producto(24L, "Toalla Premium Baño", 
                "Extra absorbente. Calidad premium para uso diario.",
                new BigDecimal("99.00"), "toallas4.avif", "Hogar", true, 
                false, null, 11));

        // COCINA HOGAR - Vajillas (4)
        productos.add(new Producto(25L, "Juego de Vajilla Porcelana Combo 60 Piezas", 
                "Juego completo para 12 personas. Porcelana de alta calidad.",
                new BigDecimal("249.90"), "vajilla1.webp", "Hogar", true, 
                false, null, 6));
        
        productos.add(new Producto(26L, "Juego de Vajilla Porcelana 30 Piezas Paula", 
                "Diseño elegante Paula. Set de 30 piezas para 6 personas.",
                new BigDecimal("199.90"), "vajilla2.webp", "Hogar", true, 
                false, null, 8));
        
        productos.add(new Producto(27L, "Vajilla x16 Piezas Porcelana con Textura", 
                "Textura moderna. Set compacto de 16 piezas.",
                new BigDecimal("99.90"), "vajilla3.webp", "Hogar", false, 
                false, null, 12));
        
        productos.add(new Producto(28L, "Set Vajilla Decal Rosa 16 Piezas", 
                "Delicado diseño rosa. Perfecto para decoración moderna.",
                new BigDecimal("49.90"), "vajilla4.jpg", "Hogar", false, 
                false, null, 15));

        // DECORACIÓN - Plantas (4)
        productos.add(new Producto(29L, "Planta Olivo Artificial 30×132 cm", 
                "Planta artificial grande. Diseño realista sin mantenimiento.",
                new BigDecimal("129.90"), "planta1.webp", "Hogar", false, 
                false, null, 7));
        
        productos.add(new Producto(30L, "Planta Grande Eucalipto 120 cm", 
                "Eucalipto artificial de 120cm. Ideal para espacios amplios.",
                new BigDecimal("99.90"), "planta2.webp", "Hogar", false, 
                false, null, 9));
        
        productos.add(new Producto(31L, "Planta Ficus artificial 154 cm", 
                "Ficus realista de gran tamaño. 154cm de altura.",
                new BigDecimal("179.90"), "planta3.avif", "Hogar", true, 
                false, null, 5));
        
        productos.add(new Producto(32L, "Planta Sansevieria Artificial con maceta", 
                "Perfecta para interiores. Incluye maceta decorativa.",
                new BigDecimal("83.70"), "planta4.avif", "Hogar", false, 
                false, null, 13));

        return productos;
    }

    /**
     * Filtra y obtiene solo los productos destacados
     * Usa Java Streams para filtrar la lista
     */
    private List<Producto> obtenerProductosDestacados() {
        return obtenerTodosLosProductos().stream()  // Convertir lista a stream
                .filter(Producto::isDestacado)       // Filtrar solo destacados
                .limit(6)                            // Limitar a 6 productos
                .collect(Collectors.toList());       // Convertir de vuelta a lista
    }

    /**
     * Obtiene productos de una categoría específica
     * @param categoria Nombre de la categoría ("Hogar", "Electrodomésticos", etc.)
     */
    private List<Producto> obtenerProductosPorCategoria(String categoria) {
        return obtenerTodosLosProductos().stream()
                .filter(p -> p.getCategoria().equals(categoria))  // Filtrar por categoría
                .collect(Collectors.toList());
    }

    /**
     * Obtiene solo los productos que están en oferta
     */
    private List<Producto> obtenerProductosEnOferta() {
        return obtenerTodosLosProductos().stream()
                .filter(Producto::isEnOferta)  // Filtrar solo ofertas
                .collect(Collectors.toList());
    }

    /**
     * Busca un producto específico por su ID
     * @param id ID del producto a buscar
     * @return Producto encontrado o null si no existe
     */
    private Producto obtenerProductoPorId(Long id) {
        return obtenerTodosLosProductos().stream()
                .filter(p -> p.getId().equals(id))  // Filtrar por ID
                .findFirst()                         // Tomar el primero que coincida
                .orElse(null);                       // Si no existe, retornar null
    }

    /**
     * Página del Carrito de Compras
     * Ruta: GET /carrito
     * 
     *  El carrito funciona completamente con JavaScript y localStorage
     * - Los productos se guardan en el navegador del cliente
     * - El proceso de pago es simulado (sin backend real)
     * - Los datos del cliente NO se guardan en la base de datos
     * 
     * Archivos relacionados:
     * - templates/carrito.html (vista)
     * - static/js/main.js (funciones del carrito)
     */
    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("titulo", "Mi Carrito");
        return "carrito";  // templates/carrito.html
    }

    /**
     * Panel de Administración (Vista de Operarios)
     * Ruta: GET /admin-panel
     * Página de acceso rápido a las funciones de gestión
     * Enlaza a: /gestion/clientes, /gestion/pedidos, /gestion/ventas
     */
    @GetMapping("/admin-panel")
    public String adminPanel(Model model) {
        model.addAttribute("titulo", "Panel de Operarios");
        return "admin-panel";  // templates/admin-panel.html
    }
}
