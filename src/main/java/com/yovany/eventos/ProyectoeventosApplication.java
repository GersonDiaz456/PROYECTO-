package com.yovany.eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.yovany.eventos") // Asegúrate de ajustar el paquete según sea necesario
public class ProyectoeventosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoeventosApplication.class, args);
    }
}
