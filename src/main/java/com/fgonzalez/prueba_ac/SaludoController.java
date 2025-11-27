package com.fgonzalez.prueba_ac;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saludo")
public class SaludoController {

    @GetMapping("/hola-mundo")
    public String holaMundo(){
        return "Hola mundo";
    }

}
