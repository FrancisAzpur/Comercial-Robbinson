package com.Robbinson.ComRobinson.controladores;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.modelo.Pedido;
import com.Robbinson.ComRobinson.modelo.Venta;
import com.Robbinson.ComRobinson.servicios.ServicioCliente;
import com.Robbinson.ComRobinson.servicios.ServicioPedido;
import com.Robbinson.ComRobinson.servicios.ServicioVenta;

/**
 * Controlador de Gestión para administrar Clientes, Pedidos y Ventas
 * Proporciona operaciones CRUD completas y vistas para el sistema
 */
@Controller
@RequestMapping("/gestion")
public class ControladorGestion {

    // Inyectamos los servicios
    @Autowired
    private ServicioCliente servicioCliente;

    @Autowired
    private ServicioPedido servicioPedido;

    @Autowired
    private ServicioVenta servicioVenta;

    // ==================== RUTAS DE CLIENTES ====================

    /**
     * Mostrar página de listado de clientes
     */
    @GetMapping("/clientes")
    public String listarClientes(Model modelo) {
        List<Cliente> clientes = servicioCliente.obtenerTodosLosClientes();
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
        servicioCliente.agregarCliente(cliente);
        return "redirect:/gestion/clientes";
    }

    /**
     * Mostrar detalle de un cliente específico
     */
    @GetMapping("/clientes/{id}")
    public String detalleCliente(@PathVariable Long id, Model modelo) {
        Optional<Cliente> cliente = servicioCliente.obtenerClientePorId(id);
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
        Optional<Cliente> cliente = servicioCliente.obtenerClientePorId(id);
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
        servicioCliente.actualizarCliente(id, clienteActualizado);
        return "redirect:/gestion/clientes/" + id;
    }

    /**
     * Eliminar un cliente
     */
    @GetMapping("/clientes/{id}/eliminar")
    public String eliminarCliente(@PathVariable Long id) {
        servicioCliente.eliminarCliente(id);
        return "redirect:/gestion/clientes";
    }

    /**
     * Buscar clientes por nombre
     */
    @GetMapping("/clientes/buscar")
    public String buscarClientes(@RequestParam String nombre, Model modelo) {
        List<Cliente> clientes = servicioCliente.buscarPorNombre(nombre);
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
        List<Pedido> pedidos = servicioPedido.obtenerTodosLosPedidos();
        int[] conteos = servicioPedido.contarPedidosPorEstado();
        
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
        modelo.addAttribute("titulo", "Crear Nuevo Pedido");
        return "gestion/pedidos-formulario";
    }

    /**
     * Guardar nuevo pedido
     */
    @PostMapping("/pedidos/guardar")
    public String guardarPedido(Pedido pedido) {
        // Generar número de orden automático
        if (pedido.getNumeroOrden() == null || pedido.getNumeroOrden().isEmpty()) {
            pedido.setNumeroOrden("ORD-" + System.currentTimeMillis());
        }
        servicioPedido.agregarPedido(pedido);
        return "redirect:/gestion/pedidos";
    }

    /**
     * Ver detalle de un pedido
     */
    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model modelo) {
        Optional<Pedido> pedido = servicioPedido.obtenerPedidoPorId(id);
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
        Optional<Pedido> pedido = servicioPedido.obtenerPedidoPorId(id);
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
        servicioPedido.actualizarPedido(id, pedidoActualizado);
        return "redirect:/gestion/pedidos/" + id;
    }

    /**
     * Cambiar estado de un pedido
     */
    @PostMapping("/pedidos/{id}/cambiar-estado")
    public String cambiarEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        servicioPedido.cambiarEstadoPedido(id, estado);
        return "redirect:/gestion/pedidos/" + id;
    }

    /**
     * Eliminar un pedido
     */
    @GetMapping("/pedidos/{id}/eliminar")
    public String eliminarPedido(@PathVariable Long id) {
        servicioPedido.eliminarPedido(id);
        return "redirect:/gestion/pedidos";
    }

    // ==================== RUTAS DE VENTAS ====================

    /**
     * Mostrar página de listado de ventas
     */
    @GetMapping("/ventas")
    public String listarVentas(Model modelo) {
        List<Venta> ventas = servicioVenta.obtenerTodasLasVentas();
        BigDecimal totalVentas = servicioVenta.calcularTotalVentas();
        int totalUnidades = servicioVenta.obtenerTotalUnidadesVendidas();
        
        modelo.addAttribute("ventas", ventas);
        modelo.addAttribute("totalVentas", totalVentas);
        modelo.addAttribute("totalUnidades", totalUnidades);
        modelo.addAttribute("promedio", ventas.isEmpty() ? BigDecimal.ZERO : 
            totalVentas.divide(new BigDecimal(ventas.size()), 2, java.math.RoundingMode.HALF_UP));
        modelo.addAttribute("titulo", "Gestión de Ventas");
        
        return "gestion/ventas-listado";
    }

    /**
     * Mostrar formulario para registrar nueva venta
     */
    @GetMapping("/ventas/nuevo")
    public String formularioNuevaVenta(Model modelo) {
        modelo.addAttribute("venta", new Venta());
        modelo.addAttribute("titulo", "Registrar Nueva Venta");
        return "gestion/ventas-formulario";
    }

    /**
     * Guardar nueva venta
     */
    @PostMapping("/ventas/guardar")
    public String guardarVenta(Venta venta) {
        servicioVenta.agregarVenta(venta);
        return "redirect:/gestion/ventas";
    }

    /**
     * Ver detalle de una venta
     */
    @GetMapping("/ventas/{id}")
    public String detalleVenta(@PathVariable Long id, Model modelo) {
        Optional<Venta> venta = servicioVenta.obtenerVentaPorId(id);
        if (venta.isPresent()) {
            modelo.addAttribute("venta", venta.get());
            modelo.addAttribute("titulo", "Detalle de la Venta");
            return "gestion/ventas-detalle";
        }
        return "redirect:/gestion/ventas";
    }

    /**
     * Mostrar formulario para editar venta
     */
    @GetMapping("/ventas/{id}/editar")
    public String formularioEditarVenta(@PathVariable Long id, Model modelo) {
        Optional<Venta> venta = servicioVenta.obtenerVentaPorId(id);
        if (venta.isPresent()) {
            modelo.addAttribute("venta", venta.get());
            modelo.addAttribute("titulo", "Editar Venta");
            return "gestion/ventas-formulario";
        }
        return "redirect:/gestion/ventas";
    }

    /**
     * Actualizar venta
     */
    @PostMapping("/ventas/{id}/actualizar")
    public String actualizarVenta(@PathVariable Long id, Venta ventaActualizada) {
        servicioVenta.actualizarVenta(id, ventaActualizada);
        return "redirect:/gestion/ventas/" + id;
    }

    /**
     * Eliminar una venta
     */
    @GetMapping("/ventas/{id}/eliminar")
    public String eliminarVenta(@PathVariable Long id) {
        servicioVenta.eliminarVenta(id);
        return "redirect:/gestion/ventas";
    }

    /**
     * Buscar ventas por nombre de producto
     */
    @GetMapping("/ventas/buscar")
    public String buscarVentas(@RequestParam String producto, Model modelo) {
        List<Venta> ventas = servicioVenta.buscarPorNombreProducto(producto);
        BigDecimal totalBusqueda = ventas.stream()
            .map(Venta::getMontoTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        modelo.addAttribute("ventas", ventas);
        modelo.addAttribute("totalVentas", totalBusqueda);
        modelo.addAttribute("titulo", "Búsqueda de Ventas: " + producto);
        
        return "gestion/ventas-listado";
    }

    // ==================== RUTA PRINCIPAL DE GESTIÓN ====================

    /**
     * Dashboard principal de gestión
     */
    @GetMapping("/dashboard")
    public String dashboard(Model modelo) {
        int totalClientes = servicioCliente.contarClientes();
        int totalPedidos = servicioPedido.contarPedidos();
        int totalVentas = servicioVenta.contarVentas();
        BigDecimal totalVentasMonto = servicioVenta.calcularTotalVentas();
        
        modelo.addAttribute("totalClientes", totalClientes);
        modelo.addAttribute("totalPedidos", totalPedidos);
        modelo.addAttribute("totalVentas", totalVentas);
        modelo.addAttribute("totalVentasMonto", totalVentasMonto);
        modelo.addAttribute("titulo", "Dashboard de Gestión");
        
        return "gestion/dashboard";
    }

    // ==================== RUTAS DE GRÁFICOS ====================

    /**
     * Mostrar gráficos de ventas (barras, líneas, circulares)
     */
    @GetMapping("/graficos/ventas")
    public String graficosVentas(Model modelo) {
        List<Venta> ventas = servicioVenta.obtenerTodasLasVentas();
        
        // Calcular datos para gráficos
        java.util.Map<String, Integer> ventasPorProducto = new java.util.HashMap<>();
        java.util.Map<String, BigDecimal> ventasPorFecha = new java.util.HashMap<>();
        java.util.Map<String, BigDecimal> ventasPorVendedor = new java.util.HashMap<>();
        
        // Contar ventas por producto
        for (Venta venta : ventas) {
            ventasPorProducto.put(venta.getNombreProducto(), 
                ventasPorProducto.getOrDefault(venta.getNombreProducto(), 0) + venta.getCantidadVendida());
            
            // Sumar montos por fecha
            String fecha = venta.getFechaVenta().toString();
            ventasPorFecha.put(fecha, 
                ventasPorFecha.getOrDefault(fecha, BigDecimal.ZERO).add(venta.getMontoTotal()));
            
            // Sumar montos por vendedor
            ventasPorVendedor.put(venta.getVendedor(), 
                ventasPorVendedor.getOrDefault(venta.getVendedor(), BigDecimal.ZERO).add(venta.getMontoTotal()));
        }
        
        BigDecimal totalMonto = servicioVenta.calcularTotalVentas();
        int totalUnidades = servicioVenta.obtenerTotalUnidadesVendidas();
        int totalVentasCount = ventas.size();
        BigDecimal promedio = totalVentasCount > 0 ? totalMonto.divide(BigDecimal.valueOf(totalVentasCount), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
        
        modelo.addAttribute("ventasPorProducto", ventasPorProducto);
        modelo.addAttribute("ventasPorFecha", ventasPorFecha);
        modelo.addAttribute("ventasPorVendedor", ventasPorVendedor);
        modelo.addAttribute("totalVentas", ventas.size());
        modelo.addAttribute("totalMonto", totalMonto);
        modelo.addAttribute("totalUnidades", totalUnidades);
        modelo.addAttribute("promedio", promedio);
        
        return "gestion/graficos-ventas";
    }

    /**
     * Mostrar gráficos de pedidos (barras, líneas, circulares)
     */
    @GetMapping("/graficos/pedidos")
    public String graficosPedidos(Model modelo) {
        List<Pedido> pedidos = servicioPedido.obtenerTodosLosPedidos();
        
        // Obtener conteo de estados: [Pendiente, Procesando, Enviado, Entregado]
        int[] conteoEstados = servicioPedido.contarPedidosPorEstado();
        
        // Calcular datos para gráficos
        java.util.Map<String, Integer> pedidosPorFecha = new java.util.HashMap<>();
        java.util.Map<String, Integer> pedidosPorMetodo = new java.util.HashMap<>();
        
        // Contar pedidos por fecha
        for (Pedido pedido : pedidos) {
            String fecha = pedido.getFechaPedido().toLocalDate().toString();
            pedidosPorFecha.put(fecha, 
                pedidosPorFecha.getOrDefault(fecha, 0) + 1);
            
            // Contar por método de pago
            pedidosPorMetodo.put(pedido.getMetodoPago(), 
                pedidosPorMetodo.getOrDefault(pedido.getMetodoPago(), 0) + 1);
        }
        
        modelo.addAttribute("conteoEstados", conteoEstados);
        modelo.addAttribute("pedidosPorFecha", pedidosPorFecha);
        modelo.addAttribute("pedidosPorMetodo", pedidosPorMetodo);
        modelo.addAttribute("titulo", "Análisis de Pedidos");
        
        return "gestion/graficos-pedidos";
    }
}
