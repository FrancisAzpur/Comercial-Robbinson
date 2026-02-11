package com.Robbinson.ComRobinson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * =========================================================================
 * CLASE PRINCIPAL - Punto de entrada de la aplicación Spring Boot
 * =========================================================================
 * 
 * PROYECTO: Comercial Robinson - Sistema de Gestión Comercial
 * DESCRIPCIÓN: Aplicación web para la gestión integral de una tienda de
 *              electrodomésticos y artículos del hogar, que incluye:
 *              - Catálogo de productos con carrito de compras
 *              - Gestión CRUD de Clientes, Productos, Pedidos y Direcciones
 *              - Dashboard con estadísticas y gráficos de ventas
 *              - Autenticación de usuarios con sesiones HTTP
 *              - API REST para el carrito de compras
 * 
 * TECNOLOGÍAS UTILIZADAS:
 *   - Spring Boot 4.0.2 (Framework principal)
 *   - Spring Data JPA + Hibernate (Persistencia y ORM)
 *   - Thymeleaf (Motor de plantillas HTML)
 *   - MySQL (Base de datos relacional)
 *   - Bootstrap 5.3.3 (Framework CSS)
 *   - Chart.js (Gráficos estadísticos)
 * 
 * ARQUITECTURA: Modelo-Vista-Controlador (MVC)
 *   - modelo/        → Entidades JPA mapeadas a tablas de la BD
 *   - repositorio/   → Interfaces JPA Repository para acceso a datos
 *   - servicios/     → Lógica de negocio (@Service)
 *   - controladores/ → Controladores web (@Controller / @RestController)
 *   - templates/     → Vistas HTML con Thymeleaf
 * 
 * @SpringBootApplication activa:
 *   - @Configuration: marca esta clase como fuente de configuración
 *   - @EnableAutoConfiguration: auto-configura JPA, Thymeleaf, Web, etc.
 *   - @ComponentScan: escanea el paquete actual para detectar @Controller, @Service, @Repository
 * =========================================================================
 */
@SpringBootApplication
public class ComRobinsonApplication {

	/**
	 * Método main: arranca el servidor embebido Tomcat en el puerto 8083
	 * (configurado en application.properties)
	 */
	public static void main(String[] args) {
		SpringApplication.run(ComRobinsonApplication.class, args);
	}

}