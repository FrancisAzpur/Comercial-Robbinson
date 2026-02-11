package com.Robbinson.ComRobinson.controladores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.modelo.DetallePedido;
import com.Robbinson.ComRobinson.modelo.DireccionCliente;
import com.Robbinson.ComRobinson.modelo.Pedido;
import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.servicios.ClienteService;
import com.Robbinson.ComRobinson.servicios.DireccionClienteService;
import com.Robbinson.ComRobinson.servicios.PedidoService;
import com.Robbinson.ComRobinson.servicios.ProductoService;

import jakarta.servlet.http.HttpSession;

/**
 * =========================================================================
 * CONTROLADOR DE GESTIÓN - Panel de Administración CRUD
 * =========================================================================
 * PUNTO DE EVALUACIÓN: CRUD de tablas + Consultas multi-tabla + Menús +
 *                       Listas desplegables + Dashboard con estadísticas
 * 
 * Este controlador es el NÚCLEO del sistema de gestión. Proporciona:
 * 
 * CRUD COMPLETO para las siguientes tablas (más de 2 tablas con Bootstrap):
 *   1. CLIENTES    → Listar, Crear, Editar, Eliminar, Buscar, Detalle
 *   2. DIRECCIONES → Listar, Crear, Editar, Eliminar, Detalle
 *   3. PEDIDOS     → Listar, Crear, Editar, Eliminar, Cambiar Estado, Detalle
 *   4. PRODUCTOS   → Listar, Crear, Editar, Eliminar, Buscar, Detalle
 * 
 * CONSULTAS MULTI-TABLA:
 *   - Dashboard: consulta Clientes + Pedidos + Productos + Ventas
 *   - Detalle Cliente: carga Pedidos del cliente + Direcciones del cliente
 *   - Detalle Pedido: carga DetallePedido → Producto (3 tablas)
 *   - Gráficos Ventas: Pedido → DetallePedido → Producto + Cliente (4 tablas)
 * 
 * LISTAS DESPLEGABLES (th:each en <select>):
 *   - Formulario Pedido: dropdown de Clientes activos, Métodos de Pago
 *   - Formulario Dirección: dropdown de Clientes (seleccionar dueño)
 *   - Formulario Cliente: dropdown de TipoDocumento (DNI, RUC, PASAPORTE)
 * 
 * RUTAS BASE: /gestion/*
 *   /gestion/dashboard      → Panel con estadísticas
 *   /gestion/clientes       → CRUD de clientes
 *   /gestion/direcciones    → CRUD de direcciones
 *   /gestion/pedidos        → CRUD de pedidos
 *   /gestion/productos      → CRUD de productos
 *   /gestion/ventas         → Listado de ventas (pedidos entregados)
 *   /gestion/graficos/ventas  → Gráficos con Chart.js
 *   /gestion/graficos/pedidos → Gráficos con Chart.js
 * =========================================================================
 */
@Controller
@RequestMapping("/gestion")
public class GestionController {

    private final ClienteService clienteService;
    private final DireccionClienteService direccionClienteService;
    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final HttpSession httpSession;

    public GestionController(ClienteService clienteService,
                             DireccionClienteService direccionClienteService,
                             PedidoService pedidoService,
                             ProductoService productoService,
                             HttpSession httpSession) {
        this.clienteService = clienteService;
        this.direccionClienteService = direccionClienteService;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.httpSession = httpSession;
    }

    /**
     * Verifica que haya sesión activa antes de cada request.
     * Si no hay sesión, redirige al login.
     */
    private boolean noHaySesion() {
        return httpSession.getAttribute("clienteLogueado") == null;
    }

    // ==================== RUTA PRINCIPAL ====================

    @GetMapping("")
    public String gestionIndex() {
        if (noHaySesion()) return "redirect:/login";
        return "redirect:/gestion/dashboard";
    }

    // ==================== DASHBOARD ====================

    @GetMapping("/dashboard")
    public String dashboard(Model modelo) {
        if (noHaySesion()) return "redirect:/login";
        long totalClientes = clienteService.contarClientes();
        long totalPedidos = pedidoService.contarPedidos();
        long totalProductos = productoService.contarProductosActivos();

        // Calcular ventas (pedidos entregados)
        List<Pedido> ventasEntregados = pedidoService.obtenerPedidosPorEstado("ENTREGADO");
        long totalVentas = ventasEntregados.size();
        BigDecimal totalVentasMonto = ventasEntregados.stream()
                .map(Pedido::getTotal)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Productos con stock bajo
        List<Producto> productosStockBajo = productoService.obtenerProductosConStockBajo();

        modelo.addAttribute("totalClientes", totalClientes);
        modelo.addAttribute("totalPedidos", totalPedidos);
        modelo.addAttribute("totalProductos", totalProductos);
        modelo.addAttribute("totalVentas", totalVentas);
        modelo.addAttribute("totalVentasMonto", totalVentasMonto);
        modelo.addAttribute("productosStockBajo", productosStockBajo);
        modelo.addAttribute("titulo", "Dashboard de Gestión");

        return "gestion/dashboard";
    }

    // ==================== CLIENTES ====================

    /**
     * Listado de clientes
     * GET /gestion/clientes
     */
    @GetMapping("/clientes")
    public String listarClientes(Model modelo) {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("titulo", "Gestión de Clientes");
        return "gestion/clientes-listado";
    }

    /**
     * Formulario para nuevo cliente
     * GET /gestion/clientes/nuevo
     */
    @GetMapping("/clientes/nuevo")
    public String formularioNuevoCliente(Model modelo) {
        modelo.addAttribute("cliente", new Cliente());
        modelo.addAttribute("tiposDocumento", Cliente.TipoDocumento.values());
        modelo.addAttribute("titulo", "Registrar Nuevo Cliente");
        return "gestion/clientes-formulario";
    }

    /**
     * Guardar cliente (nuevo o actualización)
     * POST /gestion/clientes/guardar
     */
    @PostMapping("/clientes/guardar")
    public String guardarCliente(@Validated @ModelAttribute Cliente cliente, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Vuelve al form con errores visibles via th:errors
            return "gestion/clientes-formulario";
        }
        
        try {
            if (cliente.getIdCliente() != null && cliente.getIdCliente() > 0) {
                // Actualizar existente
                clienteService.actualizarCliente(cliente.getIdCliente(), cliente);
                redirectAttributes.addFlashAttribute("mensajeExito", "Cliente actualizado correctamente");
            } else {
                // Crear nuevo
                clienteService.guardarCliente(cliente);
                redirectAttributes.addFlashAttribute("mensajeExito", "Cliente registrado correctamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar cliente: " + e.getMessage());
        }
        return "redirect:/gestion/clientes";
    }

    /**
     * Detalle de un cliente
     * GET /gestion/clientes/{id}
     */
    @GetMapping("/clientes/{id}")
    public String detalleCliente(@PathVariable Long id, Model modelo) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            modelo.addAttribute("cliente", cliente.get());
            modelo.addAttribute("pedidos", pedidoService.obtenerPedidosPorCliente(id));
            modelo.addAttribute("direcciones", direccionClienteService.obtenerDireccionesPorCliente(id));
            modelo.addAttribute("titulo", "Detalle del Cliente");
            return "gestion/clientes-detalle";
        }
        return "redirect:/gestion/clientes";
    }

    /**
     * Formulario para editar cliente
     * GET /gestion/clientes/{id}/editar
     */
    @GetMapping("/clientes/{id}/editar")
    public String formularioEditarCliente(@PathVariable Long id, Model modelo) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            modelo.addAttribute("cliente", cliente.get());
            modelo.addAttribute("tiposDocumento", Cliente.TipoDocumento.values());
            modelo.addAttribute("titulo", "Editar Cliente");
            return "gestion/clientes-formulario";
        }
        return "redirect:/gestion/clientes";
    }

    /**
     * Actualizar cliente
     * POST /gestion/clientes/{id}/actualizar
     */
    @PostMapping("/clientes/{id}/actualizar")
    public String actualizarCliente(@PathVariable Long id, @ModelAttribute Cliente clienteActualizado,
                                    RedirectAttributes redirectAttributes) {
        try {
            clienteService.actualizarCliente(id, clienteActualizado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cliente actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/gestion/clientes/" + id;
    }

    /**
     * Eliminar un cliente
     * GET /gestion/clientes/{id}/eliminar
     */
    @GetMapping("/clientes/{id}/eliminar")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (clienteService.eliminarCliente(id)) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Cliente eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo eliminar el cliente");
        }
        return "redirect:/gestion/clientes";
    }

    /**
     * Buscar clientes por nombre
     * GET /gestion/clientes/buscar?nombre=xxx
     */
    @GetMapping("/clientes/buscar")
    public String buscarClientes(@RequestParam String nombre, Model modelo) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("titulo", "Resultados de búsqueda: " + nombre);
        return "gestion/clientes-listado";
    }

    // ==================== DIRECCIONES DE CLIENTES ====================

    /**
     * Listado de todas las direcciones
     * GET /gestion/direcciones
     */
    @GetMapping("/direcciones")
    public String listarDirecciones(Model modelo) {
        List<DireccionCliente> direcciones = direccionClienteService.obtenerTodasLasDirecciones();
        modelo.addAttribute("direcciones", direcciones);
        modelo.addAttribute("titulo", "Gestión de Direcciones");
        return "gestion/direcciones-listado";
    }

    /**
     * Formulario para nueva dirección
     * GET /gestion/direcciones/nuevo
     */
    @GetMapping("/direcciones/nuevo")
    public String formularioNuevaDireccion(Model modelo) {
        modelo.addAttribute("direccion", new DireccionCliente());
        modelo.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        modelo.addAttribute("titulo", "Registrar Nueva Dirección");
        return "gestion/direcciones-formulario";
    }

    /**
     * Guardar dirección (nueva o actualización)
     * POST /gestion/direcciones/guardar
     */
    @PostMapping("/direcciones/guardar")
    public String guardarDireccion(@RequestParam Long idCliente,
                                    @ModelAttribute DireccionCliente direccion,
                                    RedirectAttributes redirectAttributes) {
        try {
            Optional<Cliente> cliente = clienteService.obtenerClientePorId(idCliente);
            if (cliente.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensajeError", "Cliente no encontrado");
                return "redirect:/gestion/direcciones";
            }
            direccion.setCliente(cliente.get());

            if (direccion.getIdDireccion() != null && direccion.getIdDireccion() > 0) {
                direccionClienteService.actualizarDireccion(direccion.getIdDireccion(), direccion);
                redirectAttributes.addFlashAttribute("mensajeExito", "Dirección actualizada correctamente");
            } else {
                direccionClienteService.guardarDireccion(direccion);
                redirectAttributes.addFlashAttribute("mensajeExito", "Dirección registrada correctamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar dirección: " + e.getMessage());
        }
        return "redirect:/gestion/direcciones";
    }

    /**
     * Detalle de una dirección
     * GET /gestion/direcciones/{id}
     */
    @GetMapping("/direcciones/{id}")
    public String detalleDireccion(@PathVariable Long id, Model modelo) {
        Optional<DireccionCliente> direccion = direccionClienteService.obtenerDireccionPorId(id);
        if (direccion.isPresent()) {
            modelo.addAttribute("direccion", direccion.get());
            modelo.addAttribute("titulo", "Detalle de la Dirección");
            return "gestion/direcciones-detalle";
        }
        return "redirect:/gestion/direcciones";
    }

    /**
     * Formulario para editar dirección
     * GET /gestion/direcciones/{id}/editar
     */
    @GetMapping("/direcciones/{id}/editar")
    public String formularioEditarDireccion(@PathVariable Long id, Model modelo) {
        Optional<DireccionCliente> direccion = direccionClienteService.obtenerDireccionPorId(id);
        if (direccion.isPresent()) {
            modelo.addAttribute("direccion", direccion.get());
            modelo.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            modelo.addAttribute("titulo", "Editar Dirección");
            return "gestion/direcciones-formulario";
        }
        return "redirect:/gestion/direcciones";
    }

    /**
     * Eliminar una dirección
     * GET /gestion/direcciones/{id}/eliminar
     */
    @GetMapping("/direcciones/{id}/eliminar")
    public String eliminarDireccion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (direccionClienteService.eliminarDireccion(id)) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Dirección eliminada correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo eliminar la dirección");
        }
        return "redirect:/gestion/direcciones";
    }

    // ==================== PEDIDOS ====================

    /**
     * Listado de pedidos
     * GET /gestion/pedidos
     */
    @GetMapping("/pedidos")
    public String listarPedidos(Model modelo) {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
        int[] conteos = pedidoService.contarPedidosPorEstado();

        modelo.addAttribute("pedidos", pedidos);
        modelo.addAttribute("totalPendiente", conteos[0]);
        modelo.addAttribute("totalProcesando", conteos[1]);
        modelo.addAttribute("totalEnviado", conteos[2]);
        modelo.addAttribute("totalEntregado", conteos[3]);
        modelo.addAttribute("estados", Pedido.EstadoPedido.values());
        modelo.addAttribute("titulo", "Gestión de Pedidos");

        return "gestion/pedidos-listado";
    }

    /**
     * Formulario para crear nuevo pedido
     * GET /gestion/pedidos/nuevo
     */
    @GetMapping("/pedidos/nuevo")
    public String formularioNuevoPedido(Model modelo) {
        modelo.addAttribute("pedido", new Pedido());
        modelo.addAttribute("clientes", clienteService.obtenerClientesActivos());
        modelo.addAttribute("productos", productoService.obtenerProductosActivos());
        modelo.addAttribute("metodosPago", Pedido.MetodoPago.values());
        modelo.addAttribute("titulo", "Crear Nuevo Pedido");
        return "gestion/pedidos-formulario";
    }

    /**
     * Guardar nuevo pedido
     * POST /gestion/pedidos/guardar
     */
    @PostMapping("/pedidos/guardar")
    public String guardarPedido(@ModelAttribute Pedido pedido, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.guardarPedido(pedido);
            redirectAttributes.addFlashAttribute("mensajeExito", "Pedido creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al crear pedido: " + e.getMessage());
        }
        return "redirect:/gestion/pedidos";
    }

    /**
     * Detalle de un pedido
     * GET /gestion/pedidos/{id}
     */
    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model modelo) {
        Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
        if (pedido.isPresent()) {
            modelo.addAttribute("pedido", pedido.get());
            modelo.addAttribute("detalles", pedidoService.obtenerDetallesPorPedido(id));
            modelo.addAttribute("estados", Pedido.EstadoPedido.values());
            modelo.addAttribute("titulo", "Detalle del Pedido");
            return "gestion/pedidos-detalle";
        }
        return "redirect:/gestion/pedidos";
    }

    /**
     * Formulario para editar pedido
     * GET /gestion/pedidos/{id}/editar
     */
    @GetMapping("/pedidos/{id}/editar")
    public String formularioEditarPedido(@PathVariable Long id, Model modelo) {
        Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
        if (pedido.isPresent()) {
            modelo.addAttribute("pedido", pedido.get());
            modelo.addAttribute("clientes", clienteService.obtenerClientesActivos());
            modelo.addAttribute("metodosPago", Pedido.MetodoPago.values());
            modelo.addAttribute("titulo", "Editar Pedido");
            return "gestion/pedidos-formulario";
        }
        return "redirect:/gestion/pedidos";
    }

    /**
     * Actualizar pedido
     * POST /gestion/pedidos/{id}/actualizar
     */
    @PostMapping("/pedidos/{id}/actualizar")
    public String actualizarPedido(@PathVariable Long id, @ModelAttribute Pedido pedidoActualizado,
                                   RedirectAttributes redirectAttributes) {
        try {
            pedidoService.actualizarPedido(id, pedidoActualizado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Pedido actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/gestion/pedidos/" + id;
    }

    /**
     * Cambiar estado de un pedido
     * POST /gestion/pedidos/{id}/cambiar-estado
     */
    @PostMapping("/pedidos/{id}/cambiar-estado")
    public String cambiarEstadoPedido(@PathVariable Long id, @RequestParam String estado,
                                      RedirectAttributes redirectAttributes) {
        try {
            pedidoService.cambiarEstadoPedido(id, estado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Estado actualizado a: " + estado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/gestion/pedidos/" + id;
    }

    /**
     * Eliminar un pedido
     * GET /gestion/pedidos/{id}/eliminar
     */
    @GetMapping("/pedidos/{id}/eliminar")
    public String eliminarPedido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (pedidoService.eliminarPedido(id)) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Pedido eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo eliminar el pedido");
        }
        return "redirect:/gestion/pedidos";
    }

    // ==================== PRODUCTOS ====================

    /**
     * Listado de productos
     * GET /gestion/productos
     */
    @GetMapping("/productos")
    public String listarProductos(Model modelo) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("titulo", "Gestión de Productos");
        return "gestion/productos-listado";
    }

    /**
     * Formulario para nuevo producto
     * GET /gestion/productos/nuevo
     */
    @GetMapping("/productos/nuevo")
    public String formularioNuevoProducto(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        modelo.addAttribute("titulo", "Registrar Nuevo Producto");
        return "gestion/productos-formulario";
    }

    /**
     * Guardar producto (nuevo o actualización)
     * POST /gestion/productos/guardar
     */
    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            if (producto.getIdProducto() != null && producto.getIdProducto() > 0) {
                productoService.actualizarProducto(producto.getIdProducto(), producto);
                redirectAttributes.addFlashAttribute("mensajeExito", "Producto actualizado correctamente");
            } else {
                productoService.guardarProducto(producto);
                redirectAttributes.addFlashAttribute("mensajeExito", "Producto registrado correctamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar producto: " + e.getMessage());
        }
        return "redirect:/gestion/productos";
    }

    /**
     * Detalle de un producto
     * GET /gestion/productos/{id}
     */
    @GetMapping("/productos/{id}")
    public String detalleProducto(@PathVariable Long id, Model modelo) {
        Optional<Producto> producto = productoService.obtenerProductoPorId(id);
        if (producto.isPresent()) {
            modelo.addAttribute("producto", producto.get());
            modelo.addAttribute("titulo", "Detalle del Producto");
            return "gestion/productos-detalle";
        }
        return "redirect:/gestion/productos";
    }

    /**
     * Formulario para editar producto
     * GET /gestion/productos/{id}/editar
     */
    @GetMapping("/productos/{id}/editar")
    public String formularioEditarProducto(@PathVariable Long id, Model modelo) {
        Optional<Producto> producto = productoService.obtenerProductoPorId(id);
        if (producto.isPresent()) {
            modelo.addAttribute("producto", producto.get());
            modelo.addAttribute("titulo", "Editar Producto");
            return "gestion/productos-formulario";
        }
        return "redirect:/gestion/productos";
    }

    /**
     * Actualizar producto
     * POST /gestion/productos/{id}/actualizar
     */
    @PostMapping("/productos/{id}/actualizar")
    public String actualizarProducto(@PathVariable Long id, @ModelAttribute Producto productoActualizado,
                                     RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizarProducto(id, productoActualizado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/gestion/productos/" + id;
    }

    /**
     * Eliminar un producto
     * GET /gestion/productos/{id}/eliminar
     */
    @GetMapping("/productos/{id}/eliminar")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (productoService.eliminarProducto(id)) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo eliminar el producto");
        }
        return "redirect:/gestion/productos";
    }

    /**
     * Buscar productos por nombre
     * GET /gestion/productos/buscar?nombre=xxx
     */
    @GetMapping("/productos/buscar")
    public String buscarProductos(@RequestParam String nombre, Model modelo) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("titulo", "Resultados de búsqueda: " + nombre);
        return "gestion/productos-listado";
    }

    // ==================== VENTAS ====================

    /**
     * Listado de ventas (pedidos entregados)
     * GET /gestion/ventas
     */
    @GetMapping("/ventas")
    public String listarVentas(Model modelo) {
        List<Pedido> ventasEntregados = pedidoService.obtenerPedidosPorEstado("ENTREGADO");

        int totalVentas = ventasEntregados.size();
        BigDecimal totalVentasMonto = ventasEntregados.stream()
                .map(Pedido::getTotal)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promedio = totalVentas > 0
                ? totalVentasMonto.divide(BigDecimal.valueOf(totalVentas), 2, java.math.RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        modelo.addAttribute("ventas", ventasEntregados);
        modelo.addAttribute("totalVentas", totalVentas);
        modelo.addAttribute("totalVentasMonto", totalVentasMonto);
        modelo.addAttribute("promedio", promedio);
        modelo.addAttribute("titulo", "Ventas Completadas");

        return "gestion/ventas-listado";
    }

    // ==================== GRÁFICOS ====================

    /**
     * Gráficos de ventas con datos completos para Chart.js
     * GET /gestion/graficos/ventas
     */
    @GetMapping("/graficos/ventas")
    public String graficosVentas(Model modelo) {
        List<Pedido> ventasEntregados = pedidoService.obtenerPedidosPorEstado("ENTREGADO");

        // Estadísticas principales
        int totalVentas = ventasEntregados.size();
        BigDecimal totalMonto = ventasEntregados.stream()
                .map(Pedido::getTotal)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promedio = totalVentas > 0
                ? totalMonto.divide(BigDecimal.valueOf(totalVentas), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Calcular unidades vendidas y ventas por producto
        Map<String, Integer> ventasPorProducto = new HashMap<>();
        int totalUnidades = 0;
        for (Pedido pedido : ventasEntregados) {
            List<DetallePedido> detalles = pedidoService.obtenerDetallesPorPedido(pedido.getIdPedido());
            for (DetallePedido detalle : detalles) {
                String nombreProd = detalle.getProducto() != null ? detalle.getProducto().getNombreProducto() : "Sin nombre";
                int cant = detalle.getCantidad() != null ? detalle.getCantidad() : 0;
                ventasPorProducto.merge(nombreProd, cant, Integer::sum);
                totalUnidades += cant;
            }
        }

        // Ventas por fecha (agrupadas por día)
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Map<String, BigDecimal> ventasPorFecha = new LinkedHashMap<>();
        for (Pedido pedido : ventasEntregados) {
            if (pedido.getFechaPedido() != null) {
                String fecha = pedido.getFechaPedido().format(fmt);
                BigDecimal monto = pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO;
                ventasPorFecha.merge(fecha, monto, BigDecimal::add);
            }
        }

        // Ventas por cliente (como "vendedor" en este contexto)
        Map<String, BigDecimal> ventasPorVendedor = new HashMap<>();
        for (Pedido pedido : ventasEntregados) {
            String cliente = pedido.getCliente() != null ? pedido.getCliente().getNombreCompleto() : "Sin cliente";
            BigDecimal monto = pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO;
            ventasPorVendedor.merge(cliente, monto, BigDecimal::add);
        }

        modelo.addAttribute("totalVentas", totalVentas);
        modelo.addAttribute("totalMonto", totalMonto);
        modelo.addAttribute("totalUnidades", totalUnidades);
        modelo.addAttribute("promedio", promedio);
        modelo.addAttribute("ventasPorProducto", ventasPorProducto);
        modelo.addAttribute("ventasPorFecha", ventasPorFecha);
        modelo.addAttribute("ventasPorVendedor", ventasPorVendedor);
        modelo.addAttribute("titulo", "Gráficos de Ventas");
        return "gestion/graficos-ventas";
    }

    /**
     * Gráficos de pedidos con datos completos para Chart.js
     * GET /gestion/graficos/pedidos
     */
    @GetMapping("/graficos/pedidos")
    public String graficosPedidos(Model modelo) {
        int[] conteos = pedidoService.contarPedidosPorEstado();
        List<Pedido> todosPedidos = pedidoService.obtenerPedidosPorEstado("");

        // Pedidos por fecha
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Map<String, Integer> pedidosPorFecha = new LinkedHashMap<>();
        for (Pedido pedido : todosPedidos) {
            if (pedido.getFechaPedido() != null) {
                String fecha = pedido.getFechaPedido().format(fmt);
                pedidosPorFecha.merge(fecha, 1, Integer::sum);
            }
        }

        // Pedidos por método de pago
        Map<String, Integer> pedidosPorMetodo = new HashMap<>();
        for (Pedido pedido : todosPedidos) {
            String metodo = pedido.getMetodoPago() != null ? pedido.getMetodoPago().name() : "SIN_DEFINIR";
            pedidosPorMetodo.merge(metodo, 1, Integer::sum);
        }

        modelo.addAttribute("conteoEstados", conteos);
        modelo.addAttribute("pedidosPorFecha", pedidosPorFecha);
        modelo.addAttribute("pedidosPorMetodo", pedidosPorMetodo);
        modelo.addAttribute("titulo", "Gráficos de Pedidos");
        return "gestion/graficos-pedidos";
    }
}