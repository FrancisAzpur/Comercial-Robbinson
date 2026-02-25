package com.Robbinson.ComRobinson.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String REMITENTE = "drakoxgmer@gmail.com";

    /**
     * Envía un correo de texto simple
     */
    public void EnviarEmail(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destinatario);
        email.setSubject(asunto);
        email.setText(mensaje);
        email.setFrom(REMITENTE);
        mailSender.send(email);
    }

    /**
     * Envía un correo HTML (para newsletter, bienvenidas, etc.)
     */
    public void enviarEmailHtml(String destinatario, String asunto, String contenidoHtml) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(contenidoHtml, true);
        helper.setFrom(REMITENTE);
        mailSender.send(mensaje);
    }

    /**
     * Envía el correo de bienvenida al newsletter de ofertas
     * con diseño HTML atractivo sin consumir recursos externos
     */
    public void enviarNewsletterBienvenida(String destinatario) throws MessagingException {
        String asunto = "🎉 ¡Bienvenido/a al Newsletter de Comercial Robinson!";
        String html = construirHtmlNewsletter(destinatario);
        enviarEmailHtml(destinatario, asunto, html);
    }

    /**
     * Construye el HTML del correo de bienvenida al newsletter.
     * Diseño inline (sin CSS externo) para máxima compatibilidad con clientes de correo.
     */
    private String construirHtmlNewsletter(String correoDestino) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head><meta charset="UTF-8"></head>
            <body style="margin:0; padding:0; background-color:#f4f1ee; font-family:'Segoe UI',Arial,sans-serif;">
              <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f1ee; padding:30px 0;">
                <tr><td align="center">
                  <table role="presentation" width="600" cellpadding="0" cellspacing="0" style="background:#ffffff; border-radius:16px; overflow:hidden; box-shadow:0 4px 24px rgba(0,0,0,0.08);">

                    <!-- HEADER -->
                    <tr>
                      <td style="background: linear-gradient(135deg, #0d1b2a 0%%, #1b263b 100%%); padding:35px 40px; text-align:center;">
                        <h1 style="color:#ffffff; margin:0; font-size:26px; letter-spacing:1px;">
                          🏬 COMERCIAL ROBINSON
                        </h1>
                        <p style="color:#e07a5f; margin:8px 0 0; font-size:14px; font-weight:600; letter-spacing:2px;">
                          OFERTAS &amp; NOVEDADES
                        </p>
                      </td>
                    </tr>

                    <!-- BANNER DE BIENVENIDA -->
                    <tr>
                      <td style="background:#e07a5f; padding:25px 40px; text-align:center;">
                        <h2 style="color:#ffffff; margin:0; font-size:22px;">
                          🎉 ¡Te has suscrito exitosamente!
                        </h2>
                      </td>
                    </tr>

                    <!-- CONTENIDO PRINCIPAL -->
                    <tr>
                      <td style="padding:35px 40px;">
                        <p style="color:#333; font-size:16px; line-height:1.6; margin:0 0 20px;">
                          ¡Hola! Gracias por suscribirte al newsletter de
                          <strong style="color:#0d1b2a;">Comercial Robinson</strong>.
                          A partir de ahora recibirás en <strong>%s</strong> las mejores ofertas
                          y novedades antes que nadie.
                        </p>

                        <!-- BENEFICIOS -->
                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin:20px 0;">
                          <tr>
                            <td style="padding:12px 16px; background:#fdf8f4; border-radius:10px; border-left:4px solid #e07a5f; margin-bottom:10px;">
                              <p style="margin:0; color:#333; font-size:15px;">
                                🏠 <strong>Productos del Hogar</strong> — Toallas, almohadas, decoración y más al mejor precio
                              </p>
                            </td>
                          </tr>
                          <tr><td style="height:10px;"></td></tr>
                          <tr>
                            <td style="padding:12px 16px; background:#f0f7ff; border-radius:10px; border-left:4px solid #3b82f6;">
                              <p style="margin:0; color:#333; font-size:15px;">
                                ⚡ <strong>Electrodomésticos</strong> — Refrigeradoras, lavadoras, TV y tecnología de las mejores marcas
                              </p>
                            </td>
                          </tr>
                          <tr><td style="height:10px;"></td></tr>
                          <tr>
                            <td style="padding:12px 16px; background:#f0fdf4; border-radius:10px; border-left:4px solid #22c55e;">
                              <p style="margin:0; color:#333; font-size:15px;">
                                🏷️ <strong>Ofertas Exclusivas</strong> — Descuentos de hasta 25%% solo para suscriptores
                              </p>
                            </td>
                          </tr>
                          <tr><td style="height:10px;"></td></tr>
                          <tr>
                            <td style="padding:12px 16px; background:#fefce8; border-radius:10px; border-left:4px solid #eab308;">
                              <p style="margin:0; color:#333; font-size:15px;">
                                🎁 <strong>Combos Especiales</strong> — Paquetes a precios irresistibles para equipar tu hogar
                              </p>
                            </td>
                          </tr>
                        </table>

                        <!-- BOTÓN CTA -->
                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                          <tr><td align="center" style="padding:20px 0;">
                            <a href="http://localhost:8083/ofertas"
                               style="display:inline-block; background:#e07a5f; color:#ffffff; text-decoration:none;
                                      padding:14px 40px; border-radius:50px; font-size:16px; font-weight:700;
                                      letter-spacing:0.5px;">
                              🔥 VER OFERTAS AHORA
                            </a>
                          </td></tr>
                        </table>
                      </td>
                    </tr>

                    <!-- FOOTER -->
                    <tr>
                      <td style="background:#f8f9fa; padding:25px 40px; text-align:center; border-top:1px solid #e5e7eb;">
                        <p style="color:#6b7280; font-size:13px; margin:0 0 8px;">
                          Comercial Robinson — Tu hogar ideal comienza aquí
                        </p>
                        <p style="color:#9ca3af; font-size:11px; margin:0;">
                          Recibiste este correo porque te suscribiste en nuestra página de ofertas.
                        </p>
                      </td>
                    </tr>

                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(correoDestino);
    }

    /**
     * Construye y envía un mensaje de recuperación de contraseña.
     */
    public void enviarRecuperacion(String destinatario, String nuevaClave) {
        String asunto = "Recuperación de contraseña — Comercial Robinson";
        String cuerpo = "Tu nueva contraseña es: " + nuevaClave
                + "\nPor favor ingresa al sitio y cambia tu clave inmediatamente.";
        EnviarEmail(destinatario, asunto, cuerpo);
    }
}