package com.Robbinson.ComRobinson.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.servicios.ProductoService;

/**
 * Controlador para las páginas públicas del sitio web
 * Maneja las rutas principales: inicio, catálogos, contacto, carrito, login, registro
 */
@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;

    // ==================== PÁGINA DE INICIO ====================

    /**
     * Muestra la página principal con productos destacados (etiqueta "NUEVO")
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Producto> productosDestacados = productoService.obtenerPorEtiqueta("NUEVO");
        model.addAttribute("productos", productosDestacados);
        return "index";
    }

    // ==================== CATÁLOGOS ====================

    /**
     * Muestra el catálogo de electrodomésticos (todos los productos activos)
     */
    @GetMapping("/electrodomesticos")
    public String electrodomesticos(Model model) {
        List<Producto> productos = productoService.obtenerProductosActivos();
        model.addAttribute("productos", productos);
        return "electrodomesticos";
    }

    /**
     * Muestra el catálogo de artículos del hogar (todos los productos activos)
     */
    @GetMapping("/hogar")
    public String hogar(Model model) {
        List<Producto> productos = productoService.obtenerProductosActivos();
        model.addAttribute("productos", productos);
        return "hogar";
    }

    /**
     * Muestra los productos en oferta (etiqueta "OFERTA")
     */
    @GetMapping("/ofertas")
    public String ofertas(Model model) {
        List<Producto> productos = productoService.obtenerPorEtiqueta("OFERTA");
        model.addAttribute("productos", productos);
        return "ofertas";
    }

    // ==================== PÁGINAS ESTÁTICAS ====================

    /** Página de contacto */
    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    /** Página del carrito de compras */
    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }

    // ==================== AUTENTICACIÓN ====================

    /** Página de inicio de sesión */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** Página de registro de nuevo usuario */
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    // ==================== ADMINISTRACIÓN ====================

    /** Panel de administración principal */
    @GetMapping("/admin")
    public String adminPanel() {
        return "admin-panel";
    }
}
