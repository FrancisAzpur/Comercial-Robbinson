package com.Robbinson.ComRobinson.controladores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Robbinson.ComRobinson.modelo.Categoria;
import com.Robbinson.ComRobinson.modelo.Producto;

/**
 * Controlador principal para Comercial Robinson
 */
@Controller
public class HomeController {

    /**
     * Página principal
     */
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("titulo", "Inicio");
        model.addAttribute("categorias", obtenerCategorias());
        model.addAttribute("productosDestacados", obtenerProductosDestacados());
        return "index";
    }

    /**
     * Página de electrodomésticos
     */
    @GetMapping("/electrodomesticos")
    public String electrodomesticos(Model model) {
        model.addAttribute("titulo", "Electrodomésticos");
        return "electrodomesticos";
    }

    /**
     * Página de productos para el hogar
     */
    @GetMapping("/hogar")
    public String hogar(Model model) {
        model.addAttribute("titulo", "Hogar");
        model.addAttribute("productos", obtenerProductosPorCategoria("Hogar"));
        return "hogar";
    }

    /**
     * Página de ofertas
     */
    @GetMapping("/ofertas")
    public String ofertas(Model model) {
        model.addAttribute("titulo", "Ofertas Especiales");
        model.addAttribute("productos", obtenerProductosEnOferta());
        return "ofertas";
    }

    /**
     * Página de contacto
     */
    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("titulo", "Contacto");
        return "contacto";
    }

    /**
     * Página de categorías
     */
    @GetMapping("/categorias")
    public String categorias(Model model) {
        model.addAttribute("titulo", "Categorías");
        model.addAttribute("categorias", obtenerCategorias());
        return "categorias";
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

    // ============ MÉTODOS AUXILIARES (simulan base de datos) ============

    private List<Categoria> obtenerCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria(1L, "Electrodomésticos", "Todo para tu cocina y hogar", 
                "fa-blender", "SmartTv.jpeg", 25));
        categorias.add(new Categoria(2L, "Hogar", "Decoración y confort", 
                "fa-couch", "Moderno.jpg", 30));
        categorias.add(new Categoria(3L, "Refrigeración", "Refrigeradoras y congeladores", 
                "fa-snowflake", "Refri1.jpg", 15));
        categorias.add(new Categoria(4L, "Lavado", "Lavadoras y secadoras", 
                "fa-soap", "Lavadora1.jpg", 12));
        return categorias;
    }

    private List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        
        // Electrodomésticos - TVs
        productos.add(new Producto(1L, "Smart TV OLED 55\"", 
                "Televisor OLED 4K con HDR10+ y Dolby Vision. Sistema operativo Android TV con Google Assistant.",
                new BigDecimal("2499.00"), "tv_oled_55.avif", "Electrodomésticos", true, 
                true, new BigDecimal("2999.00"), 10));
        
        productos.add(new Producto(2L, "Smart TV LED 43\"", 
                "Televisor LED Full HD con WiFi integrado y aplicaciones pre-instaladas.",
                new BigDecimal("899.00"), "smart_tv_2.webp", "Electrodomésticos", true, 
                false, null, 15));
        
        productos.add(new Producto(3L, "Smart TV 50\" 4K", 
                "Pantalla 4K Ultra HD con tecnología HDR. Conexión WiFi y Bluetooth.",
                new BigDecimal("1299.00"), "SmartTv.jpeg", "Electrodomésticos", false, 
                true, new BigDecimal("1599.00"), 8));

        // Refrigeración
        productos.add(new Producto(4L, "Refrigeradora Inverter 420L", 
                "Refrigeradora No Frost con tecnología Inverter. Eficiencia energética A+++",
                new BigDecimal("1899.00"), "Refri1.jpg", "Electrodomésticos", true, 
                false, null, 12));
        
        productos.add(new Producto(5L, "Refrigeradora Side by Side", 
                "Diseño moderno con dispensador de agua y hielo. Capacidad 600L",
                new BigDecimal("3499.00"), "refrigerador.webp", "Electrodomésticos", true, 
                true, new BigDecimal("3999.00"), 5));
        
        productos.add(new Producto(6L, "Refrigeradora Compacta", 
                "Perfecta para espacios pequeños. Eficiente y silenciosa.",
                new BigDecimal("799.00"), "refrigeradora_2.jpg", "Electrodomésticos", false, 
                false, null, 20));

        // Lavado
        productos.add(new Producto(7L, "Lavadora Inverter 16kg", 
                "Lavadora de carga frontal con tecnología Inverter Direct Drive",
                new BigDecimal("1599.00"), "Lavadora1.jpg", "Electrodomésticos", true, 
                true, new BigDecimal("1999.00"), 7));
        
        productos.add(new Producto(8L, "Lavadora Automática 10kg", 
                "Carga superior con 12 programas de lavado. Eficiencia energética A+",
                new BigDecimal("999.00"), "lavadora_2.webp", "Electrodomésticos", false, 
                false, null, 15));
        
        productos.add(new Producto(9L, "Lavadora-Secadora Combo", 
                "Lavadora y secadora en un solo equipo. Capacidad 9kg/6kg",
                new BigDecimal("2299.00"), "lavadora_inverter.webp", "Electrodomésticos", true, 
                true, new BigDecimal("2699.00"), 6));

        // Hogar
        productos.add(new Producto(10L, "Juego de Sala Moderno", 
                "Sala de 3 piezas tapizado en tela de alta calidad. Diseño contemporáneo",
                new BigDecimal("2999.00"), "Moderno.jpg", "Hogar", true, 
                false, null, 8));

        return productos;
    }

    private List<Producto> obtenerProductosDestacados() {
        return obtenerTodosLosProductos().stream()
                .filter(Producto::isDestacado)
                .limit(6)
                .collect(Collectors.toList());
    }

    private List<Producto> obtenerProductosPorCategoria(String categoria) {
        return obtenerTodosLosProductos().stream()
                .filter(p -> p.getCategoria().equals(categoria))
                .collect(Collectors.toList());
    }

    private List<Producto> obtenerProductosEnOferta() {
        return obtenerTodosLosProductos().stream()
                .filter(Producto::isEnOferta)
                .collect(Collectors.toList());
    }

    private Producto obtenerProductoPorId(Long id) {
        return obtenerTodosLosProductos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Página de carrito de compras
     */
    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("titulo", "Mi Carrito");
        return "carrito";
    }

    /**
     * Panel de administración (Operarios)
     */
    @GetMapping("/admin-panel")
    public String adminPanel(Model model) {
        model.addAttribute("titulo", "Panel de Operarios");
        return "admin-panel";
    }
}
