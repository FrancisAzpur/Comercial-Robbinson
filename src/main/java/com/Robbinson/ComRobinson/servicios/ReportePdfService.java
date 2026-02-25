package com.Robbinson.ComRobinson.servicios;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Robbinson.ComRobinson.modelo.Pedido;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;

/**
 * =========================================================================
 * SERVICIO DE REPORTES PDF - Exportación con OpenPDF (LibrePDF)
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Exportación de datos a PDF
 *
 * Usa la librería OpenPDF (com.github.librepdf:openpdf) para generar
 * reportes en formato PDF descargables desde el navegador.
 * 
 * DEPENDENCIA en pom.xml:
 *   <groupId>com.github.librepdf</groupId>
 *   <artifactId>openpdf</artifactId>
 *   <version>2.0.3</version>
 * 
 * MÉTODO PRINCIPAL:
 *   generarReporteVentas() → Genera un PDF con todas las ventas (pedidos
 *   con estado ENTREGADO) incluyendo:
 *     - Título con fecha de generación
 *     - Tabla con: N° Pedido, Cliente, Fecha, Método de Pago, Total
 *     - Totales al final del reporte
 * 
 * UBICACIÓN: com.Robbinson.ComRobinson.servicios.ReportePdfService
 * ENDPOINT:  GET /gestion/reportes/ventas/pdf (en GestionController)
 * =========================================================================
 */
@Service
public class ReportePdfService {

    @Autowired
    private PedidoService pedidoService;

    // Colores corporativos de Comercial Robinson
    private static final Color COLOR_HEADER = new Color(13, 27, 42);      // #0d1b2a
    private static final Color COLOR_ACCENT = new Color(224, 122, 95);    // #e07a5f
    private static final Color COLOR_FILA_PAR = new Color(248, 249, 250); // #f8f9fa
    private static final Color COLOR_BLANCO = Color.WHITE;

    /**
     * Genera un PDF con el reporte de todas las ventas (pedidos ENTREGADOS).
     * Sin filtros — exporta todo lo que hay en la base de datos.
     *
     * @return byte[] con el contenido del PDF listo para descargar
     */
    public byte[] generarReporteVentas() throws Exception {
        List<Pedido> ventas = pedidoService.obtenerPedidosPorEstado("ENTREGADO");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 50, 40);
        PdfWriter.getInstance(document, baos);
        document.open();

        // ── FUENTES ──
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, COLOR_HEADER);
        Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);
        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_BLANCO);
        Font fuenteCelda = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        Font fuenteTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, COLOR_HEADER);
        Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);

        // ── TÍTULO PRINCIPAL ──
        Paragraph titulo = new Paragraph("COMERCIAL ROBINSON", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // ── SUBTÍTULO: REPORTE DE VENTAS ──
        Paragraph subtitulo = new Paragraph("Reporte de Ventas Completadas", 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COLOR_ACCENT));
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingBefore(6);
        document.add(subtitulo);

        // ── FECHA DE GENERACIÓN ──
        DateTimeFormatter fmtFechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Paragraph fechaGen = new Paragraph(
                "Fecha de generación: " + LocalDateTime.now().format(fmtFechaHora),
                fuenteSubtitulo);
        fechaGen.setAlignment(Element.ALIGN_CENTER);
        fechaGen.setSpacingBefore(4);
        fechaGen.setSpacingAfter(20);
        document.add(fechaGen);

        // ── LÍNEA SEPARADORA ──
        PdfPTable lineaSep = new PdfPTable(1);
        lineaSep.setWidthPercentage(100);
        PdfPCell celdaLinea = new PdfPCell();
        celdaLinea.setBorderWidthTop(2);
        celdaLinea.setBorderWidthBottom(0);
        celdaLinea.setBorderWidthLeft(0);
        celdaLinea.setBorderWidthRight(0);
        celdaLinea.setBorderColorTop(COLOR_ACCENT);
        celdaLinea.setFixedHeight(4);
        lineaSep.addCell(celdaLinea);
        document.add(lineaSep);

        // ── RESUMEN RÁPIDO ──
        int totalRegistros = ventas.size();
        BigDecimal montoTotal = ventas.stream()
                .map(Pedido::getTotal)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Paragraph resumen = new Paragraph(
                "Total de ventas: " + totalRegistros + "   |   " +
                "Monto total: S/ " + montoTotal.toString(),
                fuenteTotal);
        resumen.setAlignment(Element.ALIGN_LEFT);
        resumen.setSpacingBefore(14);
        resumen.setSpacingAfter(14);
        document.add(resumen);

        // ── TABLA DE VENTAS ──
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{15f, 28f, 17f, 18f, 12f});
        tabla.setSpacingBefore(6);

        // Encabezados de la tabla
        String[] encabezados = {"N° Pedido", "Cliente", "Fecha", "Método Pago", "Total (S/)"};
        for (String encabezado : encabezados) {
            PdfPCell celda = new PdfPCell(new Phrase(encabezado, fuenteEncabezado));
            celda.setBackgroundColor(COLOR_HEADER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setPadding(8);
            celda.setBorderWidth(0);
            tabla.addCell(celda);
        }

        // Filas de datos
        DateTimeFormatter fmtFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int fila = 0;
        for (Pedido venta : ventas) {
            Color colorFondo = (fila % 2 == 0) ? COLOR_BLANCO : COLOR_FILA_PAR;

            // N° Pedido
            agregarCelda(tabla, 
                    venta.getNumeroPedido() != null ? venta.getNumeroPedido() : "-",
                    fuenteCelda, colorFondo, Element.ALIGN_CENTER);

            // Cliente
            String nombreCliente = "-";
            if (venta.getCliente() != null && venta.getCliente().getNombreCompleto() != null) {
                nombreCliente = venta.getCliente().getNombreCompleto();
            }
            agregarCelda(tabla, nombreCliente, fuenteCelda, colorFondo, Element.ALIGN_LEFT);

            // Fecha del pedido
            String fecha = "-";
            if (venta.getFechaPedido() != null) {
                fecha = venta.getFechaPedido().format(fmtFecha);
            }
            agregarCelda(tabla, fecha, fuenteCelda, colorFondo, Element.ALIGN_CENTER);

            // Método de pago
            String metodo = venta.getMetodoPago() != null ? venta.getMetodoPago().name() : "-";
            agregarCelda(tabla, metodo, fuenteCelda, colorFondo, Element.ALIGN_CENTER);

            // Total
            String total = venta.getTotal() != null ? venta.getTotal().toString() : "0.00";
            agregarCelda(tabla, total, fuenteCelda, colorFondo, Element.ALIGN_RIGHT);

            fila++;
        }

        // Fila de TOTAL
        PdfPCell celdaVacia = new PdfPCell(new Phrase(""));
        celdaVacia.setColspan(4);
        celdaVacia.setBorderWidth(0);
        celdaVacia.setBackgroundColor(COLOR_HEADER);
        celdaVacia.setPadding(8);
        // Texto "TOTAL" a la derecha de las 4 columnas
        Paragraph textoTotal = new Paragraph("TOTAL GENERAL:", fuenteEncabezado);
        textoTotal.setAlignment(Element.ALIGN_RIGHT);
        celdaVacia.addElement(textoTotal);
        tabla.addCell(celdaVacia);

        PdfPCell celdaTotal = new PdfPCell(new Phrase("S/ " + montoTotal.toString(), fuenteEncabezado));
        celdaTotal.setBackgroundColor(COLOR_ACCENT);
        celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaTotal.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaTotal.setPadding(8);
        celdaTotal.setBorderWidth(0);
        tabla.addCell(celdaTotal);

        document.add(tabla);

        // ── PIE DE PÁGINA ──
        Paragraph pie = new Paragraph(
                "Comercial Robinson — Reporte generado automáticamente. " +
                "Los datos reflejan las ventas con estado ENTREGADO en la base de datos.",
                fuentePie);
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(25);
        document.add(pie);

        document.close();
        return baos.toByteArray();
    }

    /**
     * Método auxiliar para agregar una celda con formato uniforme a la tabla.
     */
    private void agregarCelda(PdfPTable tabla, String texto, Font fuente, 
                               Color fondo, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(fondo);
        celda.setHorizontalAlignment(alineacion);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(6);
        celda.setBorderWidth(0.5f);
        celda.setBorderColor(new Color(229, 231, 235)); // #e5e7eb
        tabla.addCell(celda);
    }
}
