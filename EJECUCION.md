# 🚀 GUÍA DE EJECUCIÓN - Comercial Robinson

## 📋 Requisitos Previos

- **Java 8+** instalado en tu sistema
- **Maven 3.6+** instalado (incluido con muchas IDEs)
- **Git** (opcional, para clonar el proyecto)

## 🎯 Opción 1: Ejecutar con Maven

### Paso 1: Compilar el Proyecto
Abre una terminal en la carpeta raíz del proyecto y ejecuta:

```bash
mvn clean install
```

Esto descargará todas las dependencias y compilará el proyecto.

### Paso 2: Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

La aplicación se iniciará y verás un mensaje como:
```
Application 'ComRobinson' is running!
Tomcat started on port(s): 8080 (http) with context path '/'
```

### Paso 3: Acceder a la Aplicación
Abre tu navegador web e ingresa:
```
http://localhost:8080
```

## 🎯 Opción 2: Ejecutar el Archivo Java Directamente

### Paso 1: Compilar con Maven
```bash
mvn clean package
```

Esto crea un archivo `.jar` en la carpeta `target/`.

### Paso 2: Ejecutar el JAR
```bash
java -jar target/ComRobinson-0.0.1-SNAPSHOT.jar
```

O desde la carpeta `target/`:
```bash
cd target
java -jar ComRobinson-0.0.1-SNAPSHOT.jar
```

### Paso 3: Acceder a la Aplicación
Abre tu navegador web en:
```
http://localhost:8080
```

## 🎯 Opción 3: Ejecutar desde tu IDE (Eclipse, IntelliJ, VS Code)

### En Eclipse:
1. Click derecho en el proyecto → Run As → Spring Boot App

### En IntelliJ:
1. Click en el botón Run (▶️) arriba a la derecha
2. Selecciona "ComRobinsonApplication"

### En VS Code + Extension Pack for Java:
1. Abre la paleta de comandos (Ctrl+Shift+P)
2. Busca "Spring Boot Dashboard"
3. Haz click en "Start"

## 🌐 Navegación Principal

Una vez ejecutado, accede a:

- **Inicio**: `http://localhost:8080/`
- **Electrodomésticos**: `http://localhost:8080/electrodomesticos`
- **Hogar**: `http://localhost:8080/hogar`
- **Contacto**: `http://localhost:8080/contacto`
- **Carrito**: `http://localhost:8080/carrito`
- **Panel de Operarios**: `http://localhost:8080/admin-panel`

## 🛒 Funcionalidades Principales

### 1. Ver Productos
- Navega a **Electrodomésticos** u **Hogar**
- Busca y filtra productos por categoría

### 2. Agregar al Carrito
- Haz click en **"Añadir al carrito"** en cualquier producto
- El contador del carrito se actualiza automáticamente
- Verás una notificación de confirmación

### 3. Gestionar Carrito
- Accede a **Carrito** desde el navbar
- Aumenta o disminuye cantidades
- Elimina productos individuales
- Vacía el carrito completo

### 4. Realizar un Pedido
- Desde el carrito, haz click en **"Procesar Pago"**
- Completa los datos de facturación:
  - Nombre y Apellido
  - Email
  - Teléfono
  - Dirección
  - Ciudad
- Haz click en **"Confirmar Pago"**
- Recibirás una confirmación con el número de orden

### 5. Panel de Operarios (Dormido)
- Accede a **Admin Panel** desde el navbar
- Actualmente sin funcionalidad activa
- Listo para ser activado cuando sea necesario

## 💾 Almacenamiento de Datos

Los datos se guardan en **localStorage del navegador**:
- El carrito persiste mientras no limpies el almacenamiento local
- Al cerrar el navegador, el carrito se mantiene
- Para limpiar, usa: Herramientas de Desarrollador → Application → Local Storage → Borrar

## 🐛 Solución de Problemas

### "Puerto 8080 ya en uso"
Si el puerto está ocupado, detén la aplicación anterior o cámbialo:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### "Maven no encontrado"
Asegúrate de haber instalado Maven correctamente o usa el `mvnw` incluido:
```bash
./mvnw clean install  # En Mac/Linux
mvnw.cmd clean install  # En Windows
```

### "No puedo acceder a http://localhost:8080"
- Verifica que la aplicación está ejecutándose correctamente
- Abre el navegador unos segundos después de ejecutar
- Intenta con `http://127.0.0.1:8080`

## 📊 Estructura del Proyecto

```
ComRobinson/
├── src/
│   ├── main/
│   │   ├── java/com/Robbinson/ComRobinson/
│   │   │   ├── controladores/        (Controllers)
│   │   │   ├── modelo/               (Models)
│   │   │   ├── servicios/            (Services)
│   │   │   └── ComRobinsonApplication.java
│   │   └── resources/
│   │       ├── templates/            (HTML Thymeleaf)
│   │       ├── static/               (CSS, JS, Imágenes)
│   │       └── application.properties
│   └── test/                         (Tests)
├── pom.xml                           (Dependencias Maven)
└── mvnw / mvnw.cmd                   (Maven Wrapper)
```

## 📱 Navegadores Soportados

- Chrome / Chromium (Recomendado)
- Firefox
- Safari
- Edge

## 🎨 Tecnologías Utilizadas

- **Backend**: Spring Boot 3.x + Java
- **Frontend**: HTML5 + CSS3 + JavaScript (Vanilla)
- **Framework CSS**: Bootstrap 5.3.3
- **Gráficos**: Chart.js
- **Almacenamiento**: LocalStorage
- **Servicio Plantillas**: Thymeleaf

## ⚡ Funciones del Carrito (En Tiempo Real)

✅ Agregar productos (con cantidad)
✅ Actualizar cantidades
✅ Eliminar productos
✅ Vaciar carrito completo
✅ Cálculo automático de totales
✅ Impuesto (IGV 18%) automático
✅ Guardar información del cliente
✅ Confirmar pedido con número de orden

## 📞 Soporte

Si encuentras problemas:
1. Verifica que Java está correctamente instalado: `java -version`
2. Verifica que Maven está correctamente instalado: `mvn -v`
3. Limpia la caché: `mvn clean`
4. Intenta instalar de nuevo: `mvn install`

---

**¡Tu aplicación está lista para usar!** 🎉

Navega a `http://localhost:8080` y comienza a explorar.
