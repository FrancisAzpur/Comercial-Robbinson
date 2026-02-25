package com.Robbinson.ComRobinson.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void EnviarEmail(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destinatario);
        email.setSubject(asunto);
        email.setText(mensaje);
        email.setFrom("drakoxgmer@gmail.com");
        mailSender.send(email);
        
    }

    /**
     * Construye y envía un mensaje de recuperación de contraseña.
     */
    public void enviarRecuperacion(String destinatario, String nuevaClave) {
        String asunto = "Recuperación de contraseña Robbinson";
        String cuerpo = "Tu nueva contraseña es: " + nuevaClave
                + "\nPor favor ingresa al sitio y cambia tu clave inmediatamente.";
        EnviarEmail(destinatario, asunto, cuerpo);
    }
}