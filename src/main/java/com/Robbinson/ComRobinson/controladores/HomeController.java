package com.Robbinson.ComRobinson.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.modelo.DireccionCliente;
import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.servicios.ClienteService;
import com.Robbinson.ComRobinson.servicios.DireccionClienteService;
import com.Robbinson.ComRobinson.servicios.ProductoService;
import com.Robbinson.ComRobinson.servicios.EmailService;

import jakarta.servlet.http.HttpSession;

/**
 * =========================================================================
 * CONTROLADOR HOME - Páginas públicas del sitio web
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Menús + Organización de páginas + Autenticación
 * 
 * Maneja las rutas públicas visibles para todos los usuarios:
 *   /                    → Página de inicio con productos destacados
 *   /electrodomesticos   → Catálogo de electrodomésticos
 *   /hogar               → Catálogo de artículos del hogar
 *   /ofertas             → Productos en oferta
 *   /contacto            → Página de contacto
 *   /carrito             → Carrito de compras
 *   /login + /logout     → Autenticación con sesión HTTP
 *   /registro            → Registro de nuevos clientes
 *   /registro/direccion  → Registro de dirección post-registro
 *   /admin               → Panel de administración
 * 
 * THYMELEAF: Cada método retorna el nombre del template HTML
 *   que Thymeleaf resuelve en /templates/*.html
 * 
 * SESIÓN HTTP: Al hacer login, se guarda el Cliente en la sesión.
 *   El header.html usa th:if="${session.clienteLogueado}" para
 *   mostrar/ocultar opciones del menú según el estado de login.
 * =========================================================================
 */
@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DireccionClienteService direccionClienteService;

    @Autowired
    private EmailService emailService;

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

    /** Página del carrito de compras - pasa info de sesión al template */
    @GetMapping("/carrito")
    public String carrito(HttpSession session, Model model) {
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");
        if (clienteLogueado != null) {
            model.addAttribute("clienteLogueado", clienteLogueado);
            // Obtener dirección principal para mostrar en el checkout
            Optional<DireccionCliente> dir = direccionClienteService
                    .obtenerDireccionPrincipal(clienteLogueado.getIdCliente());
            dir.ifPresent(d -> model.addAttribute("direccionPrincipal", d));
        }
        return "carrito";
    }

    // ==================== AUTENTICACIÓN ====================

    /** Página de inicio de sesión */
    @GetMapping("/login")
    public String login(HttpSession session) {
        // Si ya está logueado, redirigir al inicio
        if (session.getAttribute("clienteLogueado") != null) {
            return "redirect:/";
        }
        return "login";
    }

    /** Procesar login - valida contra la BD */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String contrasena,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Optional<Cliente> cliente = clienteService.autenticar(correo, contrasena);

        if (cliente.isPresent()) {
            // Guardar datos del cliente en la sesión
            session.setAttribute("clienteLogueado", cliente.get());
            session.setAttribute("nombreCliente", cliente.get().getNombreCompleto());
            session.setAttribute("idCliente", cliente.get().getIdCliente());
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "¡Bienvenido/a, " + cliente.get().getNombreCompleto() + "!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Correo o contraseña incorrectos, o la cuenta está inactiva");
            return "redirect:/login";
        }
    }

    /** Cerrar sesión */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensajeExito", "Has cerrado sesión correctamente");
        return "redirect:/login";
    }

    /** Página de registro de nuevo usuario (GET - muestra formulario) */
    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "registro";
    }

    /** Procesar registro de nuevo cliente (POST - guarda en BD) */
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si el correo ya existe
            if (clienteService.correoExiste(cliente.getCorreoElectronico())) {
                redirectAttributes.addFlashAttribute("mensajeError", "El correo electrónico ya está registrado");
                return "redirect:/registro";
            }

            // Verificar si el documento ya existe
            if (cliente.getDocumentoIdentidad() != null && !cliente.getDocumentoIdentidad().isEmpty()
                    && clienteService.documentoExiste(cliente.getDocumentoIdentidad())) {
                redirectAttributes.addFlashAttribute("mensajeError", "El número de documento ya está registrado");
                return "redirect:/registro";
            }

            // El cliente se crea activo por defecto (definido en el constructor del modelo)
            Cliente clienteGuardado = clienteService.guardarCliente(cliente);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Cuenta creada exitosamente! Ahora registra tu dirección de envío");
            return "redirect:/registro/direccion?idCliente=" + clienteGuardado.getIdCliente();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al crear la cuenta: " + e.getMessage());
            return "redirect:/registro";
        }
    }

    /** Formulario para registrar dirección después del registro (GET) */
    @GetMapping("/registro/direccion")
    public String formularioDireccion(@RequestParam Long idCliente, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(idCliente);
        if (cliente.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Cliente no encontrado");
            return "redirect:/registro";
        }
        model.addAttribute("direccion", new DireccionCliente());
        model.addAttribute("cliente", cliente.get());
        return "registro-direccion";
    }

    /** Procesar registro de dirección (POST - guarda en BD) */
    @PostMapping("/registro/direccion")
    public String procesarDireccion(@RequestParam Long idCliente,
                                     @ModelAttribute DireccionCliente direccion,
                                     RedirectAttributes redirectAttributes) {
        try {
            Optional<Cliente> cliente = clienteService.obtenerClientePorId(idCliente);
            if (cliente.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensajeError", "Cliente no encontrado");
                return "redirect:/registro";
            }
            direccion.setCliente(cliente.get());
            direccion.setEsPrincipal(true); // Primera dirección es la principal
            direccionClienteService.guardarDireccion(direccion);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "¡Registro completo! Tu cuenta y dirección han sido creadas. Ya puedes iniciar sesión");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al registrar dirección: " + e.getMessage());
            return "redirect:/registro/direccion?idCliente=" + idCliente;
        }
    }

    // ==================== RECUPERACIÓN DE CONTRASEÑA ====================

    /**
     * Muestra formulario para ingresar el correo al que se enviará la nueva contraseña.
     */
    @GetMapping("/recuperar")
    public String mostrarRecuperar() {
        return "recuperar-contrasena";
    }

    /**
     * Procesa la solicitud de recuperación: genera clave nueva, guarda y envía un correo.
     */
    @PostMapping("/recuperar")
    public String procesarRecuperar(@RequestParam String correo,
                                    RedirectAttributes redirectAttributes) {
        Optional<Cliente> opt = clienteService.buscarPorCorreo(correo);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No existe ninguna cuenta asociada a ese correo");
            return "redirect:/recuperar";
        }
        // generar clave temporal aleatoria sencilla
        String nuevaClave = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 8);
        boolean ok = clienteService.actualizarContrasenaPorCorreo(correo, nuevaClave);
        if (ok) {
            emailService.enviarRecuperacion(correo, nuevaClave);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Se ha enviado un correo con instrucciones a " + correo);
        } else {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se pudo actualizar la contraseña. Intenta nuevamente.");
        }
        return "redirect:/recuperar";
    }

    // ==================== ADMINISTRACIÓN ====================

    /** Panel de administración principal - requiere sesión */
    @GetMapping("/admin")
    public String adminPanel(HttpSession session) {
        if (session.getAttribute("clienteLogueado") == null) {
            return "redirect:/login";
        }
        return "admin-panel";
    }
}
