package com.Robbinson.ComRobinson.controladores;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.modelo.Pedido;
import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.servicios.ClienteService;
import com.Robbinson.ComRobinson.servicios.PedidoService;
import com.Robbinson.ComRobinson.servicios.ProductoService;

/**
 * Controlador de Gestión para administrar Clientes, Pedidos y Productos
 * Proporciona operaciones CRUD completas y vistas para el sistema
 */
@Controller
@RequestMapping("/gestion")
public class GestionController {

    // Servicios inyectados por constructor (buena práctica)
    private final ClienteService clienteService;
    private final PedidoService pedidoService;
    private final ProductoService productoService;

    // Inyección de dependencias por constructor
    public GestionController(ClienteService clienteService, 
                            PedidoService pedidoService,
                            ProductoService productoService) {
        this.clienteService = clienteService;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    // ==================== RUTAS DE CLIENTES ====================

    /**
     * Mostrar página de listado de clientes
     * Ruta: GET /gestion/clientes
     */
    @GetMapping("/clientes")
    public String listarClientes(Model modelo) {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("titulo", "Gestión de Clientes");
        return "gestion/clientes-listado";
    }

    /**
     * Mostrar formulario para agregar nuevo cliente
     */
    @GetMapping("/clientes/nuevo")
    public String formularioNuevoCliente(Model modelo) {
        modelo.addAttribute("cliente", new Cliente());
        modelo.addAttribute("titulo", "Registrar Nuevo Cliente");
        return "gestion/clientes-formulario";
    }

    /**
     * Procesar el formulario y agregar cliente
     */
    @PostMapping("/clientes/guardar")
    public String guardarCliente(Cliente cliente) {
        clienteService.guardarCliente(cliente);
        return "redirect:/gestion/clientes";
    }

    /**
     * Mostrar detalle de un cliente específico
     */
    @GetMapping("/clientes/{id}")
    public String detalleCliente(@PathVariable Long id, Model modelo) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            modelo.addAttribute("cliente", cliente.get());
            modelo.addAttribute("titulo", "Detalle del Cliente");
            return "gestion/clientes-detalle";
        }
        return "redirect:/gestion/clientes";
    }

    /**
     * Mostrar formulario para editar cliente
     */
    @GetMapping("/clientes/{id}/editar")
    public String formularioEditarCliente(@PathVariable Long id, Model modelo) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            modelo.addAttribute("cliente", cliente.get());
            modelo.addAttribute("titulo", "Editar Cliente");
            return "gestion/clientes-formulario";
        }
        return "redirect:/gestion/clientes";
    }

    /**
     * Procesar actualización de cliente
     */
    @PostMapping("/clientes/{id}/actualizar")
    public String actualizarCliente(@PathVariable Long id, Cliente clienteActualizado) {
        clienteService.actualizarCliente(id, clienteActualizado);
        return "redirect:/gestion/clientes/" + id;
    }

    /**
     * Eliminar un cliente
     */
    @GetMapping("/clientes/{id}/eliminar")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return "redirect:/gestion/clientes";
    }

    /**
     * Buscar clientes por nombre
     */
    @GetMapping("/clientes/buscar")
    public String buscarClientes(@RequestParam String nombre, Model modelo) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("titulo", "Resultados de búsqueda: " + nombre);
        return "gestion/clientes-listado";
    }

    // ==================== RUTAS DE PEDIDOS ====================

    /**
     * Mostrar página de listado de pedidos
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
        modelo.addAttribute("titulo", "Gestión de Pedidos");
        
        return "gestion/pedidos-listado";
    }

    /**
     * Mostrar formulario para crear nuevo pedido
     */
    @GetMapping("/pedidos/nuevo")
    public String formularioNuevoPedido(Model modelo) {
        modelo.addAttribute("pedido", new Pedido());
        modelo.addAttribute("clientes", clienteService.obtenerClientesActivos());
        modelo.addAttribute("titulo", "Crear Nuevo Pedido");
        return "gestion/pedidos-formulario";
    }

    /**
     * Guardar nuevo pedido
     */
    @PostMapping("/pedidos/guardar")
    public String guardarPedido(Pedido pedido) {
        pedidoService.guardarPedido(pedido);
        return "redirect:/gestion/pedidos";
    }

    /**
     * Ver detalle de un pedido
     */
    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model modelo) {
        Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
        if (pedido.isPresent()) {
            modelo.addAttribute("pedido", pedido.get());
            modelo.addAttribute("titulo", "Detalle del Pedido");
            return "gestion/pedidos-detalle";
        }
        return "redirect:/gestion/pedidos";
    }

    /**
     * Mostrar formulario para editar pedido
     */
    @GetMapping("/pedidos/{id}/editar")
    public String formularioEditarPedido(@PathVariable Long id, Model modelo) {
        Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
        if (pedido.isPresent()) {
            modelo.addAttribute("pedido", pedido.get());
            modelo.addAttribute("titulo", "Editar Pedido");
            return "gestion/pedidos-formulario";
        }
        return "redirect:/gestion/pedidos";
    }

    /**
     * Actualizar pedido
     */
    @PostMapping("/pedidos/{id}/actualizar")
    public String actualizarPedido(@PathVariable Long id, Pedido pedidoActualizado) {
        pedidoService.actualizarPedido(id, pedidoActualizado);
        return "redirect:/gestion/pedidos/" + id;
    }

    /**
     * Cambiar estado de un pedido
     */
    @PostMapping("/pedidos/{id}/cambiar-estado")
    public String cambiarEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        pedidoService.cambiarEstadoPedido(id, estado);
        return "redirect:/gestion/pedidos/" + id;
    }

    /**
     * Eliminar un pedido
     */
    @GetMapping("/pedidos/{id}/eliminar")
    public String eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return "redirect:/gestion/pedidos";
    }

    // ==================== RUTAS DE PRODUCTOS ====================

    /**
     * Mostrar página de listado de productos
     */
    @GetMapping("/productos")
    public String listarProductos(Model modelo) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("titulo", "Gestión de Productos");
        return "gestion/productos-listado";
    }

    /**
     * Mostrar formulario para agregar nuevo producto
     */
    @GetMapping("/productos/nuevo")
    public String formularioNuevoProducto(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        modelo.addAttribute("titulo", "Registrar Nuevo Producto");
        return "gestion/productos-formulario";
    }

    /**
     * Guardar nuevo producto
     */
    @PostMapping("/productos/guardar")
    public String guardarProducto(Producto producto) {
        productoService.guardarProducto(producto);
        return "redirect:/gestion/productos";
    }

    /**
     * Ver detalle de un producto
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
     * Mostrar formulario para editar producto
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
     */
    @PostMapping("/productos/{id}/actualizar")
    public String actualizarProducto(@PathVariable Long id, Producto productoActualizado) {
        Optional<Producto> productoOpt = productoService.obtenerProductoPorId(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setNombreProducto(productoActualizado.getNombreProducto());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecioVenta(productoActualizado.getPrecioVenta());
            producto.setPrecioCompra(productoActualizado.getPrecioCompra());
            producto.setStockActual(productoActualizado.getStockActual());
            producto.setActivo(productoActualizado.getActivo());
            productoService.guardarProducto(producto);
        }
        return "redirect:/gestion/productos/" + id;
    }

    /**
     * Eliminar un producto
     */
    @GetMapping("/productos/{id}/eliminar")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/gestion/productos";
    }

    /**
     * Buscar productos por nombre
     */
    @GetMapping("/productos/buscar")
    public String buscarProductos(@RequestParam String nombre, Model modelo) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("titulo", "Resultados de búsqueda: " + nombre);
        return "gestion/productos-listado";
    }

    // ==================== DASHBOARD ====================

    /**
     * Dashboard principal de gestión
     */
    @GetMapping("/dashboard")
    public String dashboard(Model modelo) {
        long totalClientes = clienteService.contarClientes();
        long totalPedidos = pedidoService.contarPedidos();
        long totalProductos = productoService.contarProductosActivos();
        
        modelo.addAttribute("totalClientes", totalClientes);
        modelo.addAttribute("totalPedidos", totalPedidos);
        modelo.addAttribute("totalProductos", totalProductos);
        modelo.addAttribute("titulo", "Dashboard de Gestión");
        
        return "gestion/dashboard";
    }

    /**
     * Ruta principal de gestión (redirige al dashboard)
     */
    @GetMapping("")
    public String gestionIndex() {
        return "redirect:/gestion/dashboard";
    }
}
