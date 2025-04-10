package com.pontificia.horarioponti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /**
     * Redirige la página inicial al tablero de horarios
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/horarios";
    }
}
