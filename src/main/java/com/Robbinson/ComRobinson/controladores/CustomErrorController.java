package com.Robbinson.ComRobinson.controladores;

import java.time.LocalDateTime;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador simple para manejar /error y presentar una vista amigable
 */
@Controller
public class CustomErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", status != null ? status.toString() : "N/A");
        model.addAttribute("message", message != null ? message.toString() : "Ha ocurrido un error inesperado");
        model.addAttribute("exception", exception != null ? exception.toString() : "");

        return "error";
    }
}
