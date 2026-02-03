package com.Robbinson.ComRobinson.controladores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.servicios.ProductoService;

/**
 * Controlador principal para la página web pública de Comercial Robinson
 * Maneja las rutas de la tienda online que ven los clientes
 */
@Controller
public class HomeController {

    private final ProductoService productoService;

    public HomeController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // ==================== PÁGINAS PÚBLICAS DE LA TIENDA ====================
    
    /**
     * Página principal (Home)
     */
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("titulo", "Inicio");
        model.addAttribute("productosDestacados", obtenerProductosDestacados());
        return "index";
    }

    /**
     * Página de Electrodomésticos
     */
    @GetMapping("/electrodomesticos")
    public String electrodomesticos(Model model) {
        model.addAttribute("titulo", "Electrodomésticos");
        return "electrodomesticos";
    }

    /**
     * Página de Hogar
     */
    @GetMapping("/hogar")
    public String hogar(Model model) {
        model.addAttribute("titulo", "Hogar");
        return "hogar";
    }

    /**
     * Página de Ofertas
     */
    @GetMapping("/ofertas")
    public String ofertas(Model model) {
        model.addAttribute("titulo", "Ofertas Especiales");
        model.addAttribute("productos", obtenerProductosEnOferta());
        return "ofertas";
    }

    /**
     * Página de Contacto
     */
    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("titulo", "Contacto");
        return "contacto";
    }

    /**
     * Detalle de producto
     */
    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        Producto producto = obtenerProductoPorId(id);
        if (producto != null) {
            model.addAttribute("titulo", producto.getNombre());
            model.addAttribute("producto", producto);
            return "detalle-producto";
        }
        return "redirect:/";
    }

    /**
     * Página de Inicio de Sesión
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("titulo", "Iniciar Sesión");
        return "login";
    }
    
    @PostMapping("/login")
    public String procesarLogin() {
        return "redirect:/";
    }

    /**
     * Página de Registro de Usuario
     */
    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("titulo", "Registro");
        return "registro";
    }

    /**
     * Página del Carrito de Compras
     */
    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("titulo", "Mi Carrito");
        return "carrito";
    }

    /**
     * Panel de Administración
     */
    @GetMapping("/admin-panel")
    public String adminPanel(Model model) {
        model.addAttribute("titulo", "Panel de Operarios");
        return "admin-panel";
    }

    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Obtiene todos los productos disponibles (datos de demostración)
     */
    private List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        
        // ========== ELECTRODOMÉSTICOS ==========
        productos.add(crearProducto(1L, "ELECTRO-001", "Refrigerador 420L", 
                "Refrigerador No Frost con eficiencia energética A++. Tecnología Inverter.",
                new BigDecimal("2899.00"), new BigDecimal("2200.00"), "refrigerador.webp", null, true, 8));
        
        productos.add(crearProducto(2L, "ELECTRO-002", "Lavadora Secadora 10kg", 
                "Lavadora y secadora en uno. Tecnología Inverter silenciosa y eficiente.",
                new BigDecimal("1799.00"), new BigDecimal("1400.00"), "lavadora_inverter.webp", "OFERTA", true, 7));
        
        productos.add(crearProducto(3L, "ELECTRO-003", "Smart TV OLED 55\"", 
                "Televisor OLED 4K UHD con HDR. Negros perfectos y colores infinitos.",
                new BigDecimal("4499.00"), new BigDecimal("3500.00"), "tv_oled_55.avif", "OFERTA", true, 5));
        
        productos.add(crearProducto(4L, "ELECTRO-004", "Refrigerador 690L Samsung", 
                "Dispensador de agua y hielo. Capacidad 690L ideal para familias grandes.",
                new BigDecimal("5599.00"), new BigDecimal("4500.00"), "Refrigueradora_Samsung_690L.jpg", "OFERTA", true, 4));
        
        productos.add(crearProducto(5L, "ELECTRO-005", "Televisor SAMSUNG QLED 75\"", 
                "UHD 4K Smart TV QN75Q60DAGXPE. Quantum Dot para colores vibrantes.",
                new BigDecimal("1899.00"), new BigDecimal("1500.00"), "Televisor SAMSUNG QLED  UHD 75 4K.jpg", "OFERTA", true, 6));
        
        productos.add(crearProducto(6L, "ELECTRO-006", "Televisor LG LED 43\" HD", 
                "Smart TV Modelo 43LM6300PLA. Conectividad WiFi y apps inteligentes.",
                new BigDecimal("2999.00"), new BigDecimal("2400.00"), "Televisor LG LED 43.jpg", null, true, 10));
        
        productos.add(crearProducto(10L, "ELECTRO-010", "Cocina a Gas 6 Hornillas Indurama", 
                "Acero inoxidable resistente. 6 quemadores para cocinar en grande.",
                new BigDecimal("1299.00"), new BigDecimal("1000.00"), "Cocina a Gas 6 Hornillas Indurama.jpg", null, true, 11));
        
        productos.add(crearProducto(15L, "ELECTRO-015", "Lavavajillas 14 Servicios", 
                "Lavavajillas de alta eficiencia. Capacidad para 14 servicios completos.",
                new BigDecimal("1999.00"), new BigDecimal("1600.00"), "lavavajillas.jpg", "OFERTA", true, 5));

        // ========== HOGAR ==========
        productos.add(crearProducto(17L, "HOGAR-001", "Juego de sábanas 200 hilos | 1 plaza", 
                "Sábanas de algodón suave. Perfectas para descanso individual.",
                new BigDecimal("89.00"), new BigDecimal("60.00"), "sabanas1.webp", null, false, 15));
        
        productos.add(crearProducto(18L, "HOGAR-002", "Juego de sábanas 200 hilos | 2 plazas", 
                "Ideal para cama matrimonial. 200 hilos de algodón premium.",
                new BigDecimal("120.00"), new BigDecimal("85.00"), "sabana2.jpg", null, true, 12));
        
        productos.add(crearProducto(20L, "HOGAR-004", "Almohada hotelera premium (unidad)", 
                "Máximo confort. Relleno especial para soporte cervical.",
                new BigDecimal("60.00"), new BigDecimal("40.00"), "almohada.avif", null, false, 20));
        
        productos.add(crearProducto(21L, "HOGAR-005", "Juego de toallas 4 piezas algodón", 
                "Suaves y absorbentes. Set completo para baño.",
                new BigDecimal("140.00"), new BigDecimal("95.00"), "toallas1.webp", null, true, 10));
        
        productos.add(crearProducto(25L, "HOGAR-009", "Juego de Vajilla Porcelana Combo 60 Piezas", 
                "Juego completo para 12 personas. Porcelana de alta calidad.",
                new BigDecimal("249.90"), new BigDecimal("180.00"), "vajilla1.webp", null, true, 6));
        
        productos.add(crearProducto(31L, "HOGAR-015", "Planta Ficus artificial 154 cm", 
                "Ficus realista de gran tamaño. 154cm de altura.",
                new BigDecimal("179.90"), new BigDecimal("120.00"), "planta3.avif", null, true, 5));

        return productos;
    }

    /**
     * Método helper para crear productos con el nuevo modelo
     */
    private Producto crearProducto(Long id, String codigo, String nombre, String descripcion,
                                   BigDecimal precioVenta, BigDecimal precioCompra, 
                                   String imagen, String etiqueta, boolean activo, int stock) {
        Producto p = new Producto();
        p.setIdProducto(id);
        p.setCodigoProducto(codigo);
        p.setNombreProducto(nombre);
        p.setDescripcion(descripcion);
        p.setPrecioVenta(precioVenta);
        p.setPrecioCompra(precioCompra);
        p.setImagenPrincipal(imagen);
        p.setEtiqueta(etiqueta);
        p.setActivo(activo);
        p.setStockActual(stock);
        return p;
    }

    /**
     * Filtra y obtiene solo los productos destacados
     */
    private List<Producto> obtenerProductosDestacados() {
        return obtenerTodosLosProductos().stream()
                .filter(Producto::getActivo)
                .limit(6)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene solo los productos que están en oferta
     */
    private List<Producto> obtenerProductosEnOferta() {
        return obtenerTodosLosProductos().stream()
                .filter(Producto::isEnOferta)
                .collect(Collectors.toList());
    }

    /**
     * Busca un producto específico por su ID
     */
    private Producto obtenerProductoPorId(Long id) {
        return obtenerTodosLosProductos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
