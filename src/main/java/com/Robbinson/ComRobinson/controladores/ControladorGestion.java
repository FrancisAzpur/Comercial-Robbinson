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
@Controller  // Le dice a Spring que esta clase maneja las páginas web
@RequestMapping("/gestion")  // Todas las rutas de este controlador empiezan con /gestion
public class ControladorGestion {

    // ========== SERVICIOS - Son como ayudantes que hacen el trabajo pesado ==========
    // Los servicios contienen la lógica de negocio y se comunican con la base de datos
    
    @Autowired  // Spring inyecta automáticamente el servicio (Inyección de Dependencias)
    private ServicioCliente servicioCliente;  // Maneja todo lo relacionado con clientes

    @Autowired
    private ServicioPedido servicioPedido;  // Maneja todo lo relacionado con pedidos

    @Autowired
    private ServicioVenta servicioVenta;  // Maneja todo lo relacionado con ventas

    // ==================== RUTAS DE CLIENTES ====================

    /**
     * Mostrar página de listado de clientes
     * Ruta: GET /gestion/clientes
     */
    @GetMapping("/clientes")  // Responde a peticiones GET (para mostrar páginas)
    public String listarClientes(Model modelo) {
        // 1. Obtener todos los clientes desde la base de datos usando el servicio
        List<Cliente> clientes = servicioCliente.obtenerTodosLosClientes();
        
        // 2. Pasar los datos a la vista (página HTML) usando el modelo
        modelo.addAttribute("clientes", clientes);  // Los clientes estarán disponibles en la vista
        modelo.addAttribute("titulo", "Gestión de Clientes");  // El título de la página
        
        // 3. Retornar el nombre de la vista (archivo HTML en templates/gestion/)
        return "gestion/clientes-listado";  // Spring busca: templates/gestion/clientes-listado.html
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
     * Ruta: POST /gestion/clientes/guardar
     */
    @PostMapping("/clientes/guardar")  // POST se usa para enviar datos (formularios)
    public String guardarCliente(Cliente cliente) {
        // Spring automáticamente crea el objeto Cliente con los datos del formulario
        // Esto se llama "Data Binding" - los campos del formulario se mapean a las propiedades del objeto
        
        servicioCliente.agregarCliente(cliente);  // Guardar el cliente en la base de datos
        
        // redirect: redirige a otra página (en lugar de mostrar una vista)
        return "redirect:/gestion/clientes";  // Volver a la lista de clientes
    }

    /**
     * Mostrar detalle de un cliente específico
     * Ruta: GET /gestion/clientes/{id}  Ejemplo: /gestion/clientes/5
     */
    @GetMapping("/clientes/{id}")  // {id} es una variable en la URL
    public String detalleCliente(@PathVariable Long id, Model modelo) {
        // @PathVariable extrae el valor de {id} de la URL y lo convierte a Long
        // Ejemplo: si la URL es /clientes/5, entonces id = 5
        
        // Optional es un contenedor que puede tener un valor o estar vacío (evita null)
        Optional<Cliente> cliente = servicioCliente.obtenerClientePorId(id);
        
        // Verificar si encontramos el cliente
        if (cliente.isPresent()) {  // Si existe el cliente
            modelo.addAttribute("cliente", cliente.get());  // Enviar cliente a la vista
            modelo.addAttribute("titulo", "Detalle del Cliente");
            return "gestion/clientes-detalle";  // Mostrar página de detalle
        }
        // Si no existe, redirigir a la lista de clientes
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
     * Ruta: GET /gestion/clientes/buscar?nombre=Juan
     */
    @GetMapping("/clientes/buscar")
    public String buscarClientes(@RequestParam String nombre, Model modelo) {
        // @RequestParam obtiene parámetros de la URL después del ?
        // Ejemplo: /clientes/buscar?nombre=Juan → nombre = "Juan"
        
        List<Cliente> clientes = servicioCliente.buscarPorNombre(nombre);
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("titulo", "Resultados de búsqueda: " + nombre);
        
        // Reutilizamos la misma vista de listado para mostrar los resultados
        return "gestion/clientes-listado";
    }

    // ==================== RUTAS DE PEDIDOS ====================

    /**
     * Mostrar página de listado de pedidos
     * Ruta: GET /gestion/pedidos
     */
    @GetMapping("/pedidos")
    public String listarPedidos(Model modelo) {
        // Obtener todos los pedidos
        List<Pedido> pedidos = servicioPedido.obtenerTodosLosPedidos();
        
        // Obtener estadísticas: cuántos pedidos hay en cada estado
        // Array con 4 posiciones: [Pendiente, Procesando, Enviado, Entregado]
        int[] conteos = servicioPedido.contarPedidosPorEstado();
        
        // Pasar todos los datos a la vista
        modelo.addAttribute("pedidos", pedidos);
        modelo.addAttribute("totalPendiente", conteos[0]);    // Pedidos pendientes
        modelo.addAttribute("totalProcesando", conteos[1]);   // Pedidos en proceso
        modelo.addAttribute("totalEnviado", conteos[2]);      // Pedidos enviados
        modelo.addAttribute("totalEntregado", conteos[3]);    // Pedidos entregados
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
     * Ruta: POST /gestion/pedidos/guardar
     */
    @PostMapping("/pedidos/guardar")
    public String guardarPedido(Pedido pedido) {
        // Generar número de orden automático si no existe
        // System.currentTimeMillis() retorna el tiempo actual en milisegundos (es único)
        if (pedido.getNumeroOrden() == null || pedido.getNumeroOrden().isEmpty()) {
            pedido.setNumeroOrden("ORD-" + System.currentTimeMillis());
            // Ejemplo: ORD-1706472345678
        }
        
        servicioPedido.agregarPedido(pedido);  // Guardar en la base de datos
        return "redirect:/gestion/pedidos";     // Volver al listado
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
        if (pedido.isPresent()) { // isPresent verifica si el pedido existe
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
     * Ruta: POST /gestion/pedidos/{id}/cambiar-estado
     * Combina @PathVariable (id del pedido) y @RequestParam (nuevo estado)
     */
    @PostMapping("/pedidos/{id}/cambiar-estado")
    public String cambiarEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        // Ejemplo de URL: /pedidos/5/cambiar-estado?estado=Enviado
        // id = 5 (de la URL)
        // estado = "Enviado" (parámetro)
        
        servicioPedido.cambiarEstadoPedido(id, estado);  // Actualizar estado
        return "redirect:/gestion/pedidos/" + id;        // Volver al detalle del pedido
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
     * Ruta: GET /gestion/ventas
     */
    @GetMapping("/ventas")
    public String listarVentas(Model modelo) {
        // Obtener todas las ventas
        List<Venta> ventas = servicioVenta.obtenerTodasLasVentas();
        
        // Calcular estadísticas importantes
        BigDecimal totalVentas = servicioVenta.calcularTotalVentas();  // Suma de todos los montos
        int totalUnidades = servicioVenta.obtenerTotalUnidadesVendidas();  // Total de productos vendidos
        
        // Pasar datos a la vista
        modelo.addAttribute("ventas", ventas);
        modelo.addAttribute("totalVentas", totalVentas);
        modelo.addAttribute("totalUnidades", totalUnidades);
        
        // Calcular promedio de venta (evitar división por cero)
        // BigDecimal se usa para cálculos monetarios precisos (no usa double por imprecisión)
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

    // ==================== DASHBOARD - PANEL PRINCIPAL ====================
    
    /**
     * Dashboard principal de gestión
     * Muestra un resumen general con estadísticas clave del negocio
     * Ruta: GET /gestion/dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model modelo) {
        // Recopilar métricas principales de cada módulo
        int totalClientes = servicioCliente.contarClientes();        // Total de clientes registrados
        int totalPedidos = servicioPedido.contarPedidos();          // Total de pedidos realizados
        int totalVentas = servicioVenta.contarVentas();             // Total de ventas completadas
        BigDecimal totalVentasMonto = servicioVenta.calcularTotalVentas();  // Dinero total generado
        
        // Enviar todas las métricas a la vista del dashboard
        modelo.addAttribute("totalClientes", totalClientes);
        modelo.addAttribute("totalPedidos", totalPedidos);
        modelo.addAttribute("totalVentas", totalVentas);
        modelo.addAttribute("totalVentasMonto", totalVentasMonto);
        modelo.addAttribute("titulo", "Dashboard de Gestión");
        
        // Mostrar página del dashboard con todas las estadísticas
        return "gestion/dashboard";
    }

    // ==================== RUTAS DE GRÁFICOS ====================

    // ==================== GRÁFICOS Y ANÁLISIS DE DATOS ====================
    
    /**
     * Mostrar gráficos de ventas (barras, líneas, circulares)
     * Procesa los datos para visualización en gráficos
     * Ruta: GET /gestion/graficos/ventas
     */
    @GetMapping("/graficos/ventas")
    public String graficosVentas(Model modelo) {
        List<Venta> ventas = servicioVenta.obtenerTodasLasVentas();
        
        // Preparar estructuras de datos para los gráficos
        // Map (diccionario) almacena pares clave-valor para agrupar datos
        java.util.Map<String, Integer> ventasPorProducto = new java.util.HashMap<>();
        java.util.Map<String, BigDecimal> ventasPorFecha = new java.util.HashMap<>();
        java.util.Map<String, BigDecimal> ventasPorVendedor = new java.util.HashMap<>();
        
        // Procesar cada venta y agrupar los datos para diferentes análisis
        for (Venta venta : ventas) {
            // 1. Agrupar por producto: ¿Cuántas unidades se vendieron de cada producto?
            // getOrDefault: si no existe la clave, usa el valor por defecto (0)
            ventasPorProducto.put(venta.getNombreProducto(), 
                ventasPorProducto.getOrDefault(venta.getNombreProducto(), 0) + venta.getCantidadVendida());
            
            // 2. Agrupar por fecha: ¿Cuánto se vendió cada día?
            String fecha = venta.getFechaVenta().toString();
            ventasPorFecha.put(fecha, 
                ventasPorFecha.getOrDefault(fecha, BigDecimal.ZERO).add(venta.getMontoTotal()));
            
            // 3. Agrupar por vendedor: ¿Cuánto vendió cada vendedor?
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
