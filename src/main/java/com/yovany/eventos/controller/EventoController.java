package com.yovany.eventos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yovany.eventos.entity.Evento; 
import com.yovany.eventos.service.EventoService;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Método para crear un nuevo evento
   @PostMapping
public ResponseEntity<Evento> crearEvento(@RequestBody Evento nuevoEvento) {
    try {
        Evento eventoGuardado = eventoService.save(nuevoEvento);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoGuardado); // Retorna 201 Created
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Retorna 500 si hay error
    }
}
    // Método para obtener la lista de todos los eventos
    @GetMapping
    public List<Evento> obtenerEventos() {
        return eventoService.findAll(); // Recupera la lista de eventos
    }

    // Método para obtener un evento específico por su ID
@GetMapping("/{id}")
public ResponseEntity<Evento> obtenerEventoPorId(@PathVariable("id") Long id) {
    Evento evento = eventoService.findById(id);
    
    // Verificar si el evento existe
    if (evento != null) {
        return ResponseEntity.ok(evento); // Retorna el evento si se encontró
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 si no se encontró
    }
}
}