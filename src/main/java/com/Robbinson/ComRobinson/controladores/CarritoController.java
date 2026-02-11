package com.Robbinson.ComRobinson.controladores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.modelo.DetallePedido;
import com.Robbinson.ComRobinson.modelo.DireccionCliente;
import com.Robbinson.ComRobinson.modelo.Pedido;
import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.servicios.DireccionClienteService;
import com.Robbinson.ComRobinson.servicios.PedidoService;
import com.Robbinson.ComRobinson.servicios.ProductoService;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador REST para el carrito de compras.
 * Maneja el carrito en la sesión del servidor (HttpSession) en lugar de localStorage.
 * Si el usuario está logueado, usa sus datos de la BD para el checkout.
 */
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DireccionClienteService direccionClienteService;

    // ==================== CLAVE DE SESIÓN ====================
    private static final String CARRITO_SESSION_KEY = "carritoItems";

    // ==================== DTO INTERNO PARA ITEMS DEL CARRITO ====================

    /**
     * Representa un item en el carrito (almacenado en la sesión HTTP).
     */
    public static class CarritoItem implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private Long idProducto;
        private String nombre;
        private BigDecimal precio;
        private String imagen;
        private int cantidad;

        public CarritoItem() {}

        public CarritoItem(Long idProducto, String nombre, BigDecimal precio, String imagen, int cantidad) {
            this.idProducto = idProducto;
            this.nombre = nombre;
            this.precio = precio;
            this.imagen = imagen;
            this.cantidad = cantidad;
        }

        public Long getIdProducto() { return idProducto; }
        public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public BigDecimal getPrecio() { return precio; }
        public void setPrecio(BigDecimal precio) { this.precio = precio; }

        public String getImagen() { return imagen; }
        public void setImagen(String imagen) { this.imagen = imagen; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public BigDecimal getSubtotal() {
            return precio.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    // ==================== OBTENER CARRITO ====================

    /**
     * GET /api/carrito
     * Retorna el carrito actual de la sesión.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerCarrito(HttpSession session) {
        List<CarritoItem> carrito = obtenerCarritoDeSesion(session);

        BigDecimal subtotal = calcularSubtotal(carrito);
        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = subtotal.add(igv);

        Map<String, Object> response = new HashMap<>();
        response.put("items", carrito);
        response.put("totalItems", carrito.stream().mapToInt(CarritoItem::getCantidad).sum());
        response.put("subtotal", subtotal);
        response.put("igv", igv);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    // ==================== CONTADOR DEL CARRITO ====================

    /**
     * GET /api/carrito/contador
     * Retorna solo la cantidad total de items en el carrito (para el badge del navbar).
     */
    @GetMapping("/contador")
    public ResponseEntity<Map<String, Integer>> contadorCarrito(HttpSession session) {
        List<CarritoItem> carrito = obtenerCarritoDeSesion(session);
        int total = carrito.stream().mapToInt(CarritoItem::getCantidad).sum();

        Map<String, Integer> response = new HashMap<>();
        response.put("totalItems", total);

        return ResponseEntity.ok(response);
    }

    // ==================== AGREGAR AL CARRITO ====================

    /**
     * POST /api/carrito/agregar
     * Agrega un producto al carrito. Recibe { idProducto, nombre, precio, imagen }.
     * Si el producto ya existe, incrementa la cantidad.
     */
    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregarAlCarrito(@RequestBody Map<String, Object> body,
                                                                 HttpSession session) {
        Long idProducto;
        String nombre;
        BigDecimal precio;
        String imagen;

        try {
            // Extraer datos del request
            idProducto = Long.valueOf(body.get("idProducto").toString());
            nombre = (String) body.get("nombre");
            precio = new BigDecimal(body.get("precio").toString());
            imagen = (String) body.get("imagen");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", "Datos del producto inválidos"
            ));
        }

        // Validar que el producto existe en la BD
        Optional<Producto> productoBD = productoService.obtenerProductoPorId(idProducto);
        if (productoBD.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", "Producto no encontrado en la base de datos"
            ));
        }

        // Usar el precio real de la BD (no confiar en el frontend)
        Producto prod = productoBD.get();
        precio = prod.getPrecioVenta();
        nombre = prod.getNombreProducto();

        List<CarritoItem> carrito = obtenerCarritoDeSesion(session);

        // Buscar si ya existe en el carrito
        boolean encontrado = false;
        for (CarritoItem item : carrito) {
            if (item.getIdProducto().equals(idProducto)) {
                item.setCantidad(item.getCantidad() + 1);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            carrito.add(new CarritoItem(idProducto, nombre, precio, imagen, 1));
        }

        session.setAttribute(CARRITO_SESSION_KEY, carrito);

        int totalItems = carrito.stream().mapToInt(CarritoItem::getCantidad).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("exito", true);
        response.put("mensaje", "Producto agregado al carrito");
        response.put("totalItems", totalItems);

        return ResponseEntity.ok(response);
    }

    // ==================== ACTUALIZAR CANTIDAD ====================

    /**
     * PUT /api/carrito/actualizar/{idProducto}
     * Actualiza la cantidad de un producto. Recibe { cantidad }.
     * Si la cantidad llega a 0, elimina el item.
     */
    @PutMapping("/actualizar/{idProducto}")
    public ResponseEntity<Map<String, Object>> actualizarCantidad(@PathVariable Long idProducto,
                                                                   @RequestBody Map<String, Object> body,
                                                                   HttpSession session) {
        int nuevaCantidad;
        try {
            nuevaCantidad = Integer.parseInt(body.get("cantidad").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", "Cantidad inválida"
            ));
        }

        List<CarritoItem> carrito = obtenerCarritoDeSesion(session);

        if (nuevaCantidad <= 0) {
            carrito.removeIf(item -> item.getIdProducto().equals(idProducto));
        } else {
            for (CarritoItem item : carrito) {
                if (item.getIdProducto().equals(idProducto)) {
                    item.setCantidad(nuevaCantidad);
                    break;
                }
            }
        }

        session.setAttribute(CARRITO_SESSION_KEY, carrito);

        Map<String, Object> response = new HashMap<>();
        response.put("exito", true);
        response.put("totalItems", carrito.stream().mapToInt(CarritoItem::getCantidad).sum());

        return ResponseEntity.ok(response);
    }

    // ==================== ELIMINAR DEL CARRITO ====================

    /**
     * DELETE /api/carrito/eliminar/{idProducto}
     * Elimina un producto del carrito.
     */
    @DeleteMapping("/eliminar/{idProducto}")
    public ResponseEntity<Map<String, Object>> eliminarDelCarrito(@PathVariable Long idProducto,
                                                                   HttpSession session) {
        List<CarritoItem> carrito = obtenerCarritoDeSesion(session);
        carrito.removeIf(item -> item.getIdProducto().equals(idProducto));
        session.setAttribute(CARRITO_SESSION_KEY, carrito);

        Map<String, Object> response = new HashMap<>();
        response.put("exito", true);
        response.put("mensaje", "Producto eliminado del carrito");
        response.put("totalItems", carrito.stream().mapToInt(CarritoItem::getCantidad).sum());

        return ResponseEntity.ok(response);
    }

    // ==================== VACIAR CARRITO ====================

    /**
     * DELETE /api/carrito/vaciar
     * Vacía todo el carrito.
     */
    @DeleteMapping("/vaciar")
    public ResponseEntity<Map<String, Object>> vaciarCarrito(HttpSession session) {
        session.removeAttribute(CARRITO_SESSION_KEY);

        Map<String, Object> response = new HashMap<>();
        response.put("exito", true);
        response.put("mensaje", "Carrito vaciado");
        response.put("totalItems", 0);

        return ResponseEntity.ok(response);
    }

    // ==================== CHECKOUT / PROCESAR PEDIDO ====================

    /**
     * POST /api/carrito/checkout
     * Procesa el pedido del carrito.
     * - Si el usuario está logueado: usa sus datos de la BD directamente.
     * - Si no está logueado: requiere datos del formulario en el body.
     * Recibe opcionalmente { metodoPago, observaciones }.
     */
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> procesarCheckout(@RequestBody(required = false) Map<String, Object> body,
                                                                 HttpSession session) {
        List<CarritoItem> carrito = obtenerCarritoDeSesion(session);

        if (carrito.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", "El carrito está vacío"
            ));
        }

        // Verificar si hay un cliente logueado
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        if (clienteLogueado == null) {
            return ResponseEntity.status(401).body(Map.of(
                "exito", false,
                "mensaje", "Debes iniciar sesión para realizar una compra",
                "requiereLogin", true
            ));
        }

        // El cliente está logueado → crear pedido con sus datos de la BD
        try {
            // Obtener dirección principal del cliente
            Optional<DireccionCliente> direccionPrincipal =
                    direccionClienteService.obtenerDireccionPrincipal(clienteLogueado.getIdCliente());

            // Determinar método de pago
            Pedido.MetodoPago metodoPago = Pedido.MetodoPago.EFECTIVO;
            if (body != null && body.get("metodoPago") != null) {
                try {
                    metodoPago = Pedido.MetodoPago.valueOf(body.get("metodoPago").toString().toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Mantener EFECTIVO por defecto
                }
            }

            String observaciones = (body != null && body.get("observaciones") != null)
                    ? body.get("observaciones").toString()
                    : null;

            // Crear el pedido
            Pedido pedido = new Pedido();
            pedido.setCliente(clienteLogueado);
            pedido.setMetodoPago(metodoPago);
            pedido.setObservaciones(observaciones);
            pedido.setEstado(Pedido.EstadoPedido.PAGADO);
            pedido.setCostoEnvio(BigDecimal.ZERO);

            if (direccionPrincipal.isPresent()) {
                pedido.setDireccion(direccionPrincipal.get());
            }

            // Construir los detalles del pedido desde los items del carrito
            List<DetallePedido> detalles = new ArrayList<>();
            for (CarritoItem item : carrito) {
                DetallePedido detalle = new DetallePedido();
                Producto producto = new Producto();
                producto.setIdProducto(item.getIdProducto());
                detalle.setProducto(producto);
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(item.getPrecio());
                detalles.add(detalle);
            }
            pedido.setDetalles(detalles);

            // Usar el servicio existente que valida stock, calcula IGV, etc.
            Pedido pedidoCreado = pedidoService.crearPedido(pedido);

            // Limpiar el carrito de la sesión
            session.removeAttribute(CARRITO_SESSION_KEY);

            // Construir respuesta con información del pedido
            Map<String, Object> response = new HashMap<>();
            response.put("exito", true);
            response.put("mensaje", "¡Pedido realizado exitosamente!");
            response.put("numeroPedido", pedidoCreado.getNumeroPedido());
            response.put("fechaPedido", pedidoCreado.getFechaPedido().toString());
            response.put("subtotal", pedidoCreado.getSubtotal());
            response.put("igv", pedidoCreado.getImpuesto());
            response.put("total", pedidoCreado.getTotal());
            response.put("estado", pedidoCreado.getEstado().name());
            response.put("metodoPago", pedidoCreado.getMetodoPago().name());
            response.put("cliente", clienteLogueado.getNombreCompleto());

            if (direccionPrincipal.isPresent()) {
                DireccionCliente dir = direccionPrincipal.get();
                response.put("direccionEnvio", dir.getDireccion()
                        + (dir.getDistrito() != null ? ", " + dir.getDistrito() : "")
                        + (dir.getProvincia() != null ? ", " + dir.getProvincia() : ""));
            }

            // Items del pedido para mostrar en la confirmación
            List<Map<String, Object>> itemsConfirmacion = new ArrayList<>();
            for (CarritoItem item : carrito) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("nombre", item.getNombre());
                itemMap.put("cantidad", item.getCantidad());
                itemMap.put("precio", item.getPrecio());
                itemMap.put("subtotal", item.getSubtotal());
                itemsConfirmacion.add(itemMap);
            }
            response.put("items", itemsConfirmacion);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", "Error al procesar el pedido: " + e.getMessage()
            ));
        }
    }

    // ==================== VERIFICAR SESIÓN ====================

    /**
     * GET /api/carrito/sesion
     * Verifica si el usuario tiene sesión activa y retorna datos básicos.
     */
    @GetMapping("/sesion")
    public ResponseEntity<Map<String, Object>> verificarSesion(HttpSession session) {
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        Map<String, Object> response = new HashMap<>();
        if (clienteLogueado != null) {
            response.put("logueado", true);
            response.put("nombre", clienteLogueado.getNombreCompleto());
            response.put("correo", clienteLogueado.getCorreoElectronico());
            response.put("telefono", clienteLogueado.getTelefono());

            // Obtener dirección principal
            Optional<DireccionCliente> dir =
                    direccionClienteService.obtenerDireccionPrincipal(clienteLogueado.getIdCliente());
            if (dir.isPresent()) {
                response.put("direccion", dir.get().getDireccion());
                response.put("provincia", dir.get().getProvincia());
                response.put("distrito", dir.get().getDistrito());
            }
        } else {
            response.put("logueado", false);
        }

        return ResponseEntity.ok(response);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    @SuppressWarnings("unchecked")
    private List<CarritoItem> obtenerCarritoDeSesion(HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute(CARRITO_SESSION_KEY);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(CARRITO_SESSION_KEY, carrito);
        }
        return carrito;
    }

    private BigDecimal calcularSubtotal(List<CarritoItem> carrito) {
        return carrito.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
