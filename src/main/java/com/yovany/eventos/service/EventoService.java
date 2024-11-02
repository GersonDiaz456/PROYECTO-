package com.yovany.eventos.service;

import com.yovany.eventos.entity.Evento;
import com.yovany.eventos.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    // Método para guardar un nuevo evento
    public Evento save(Evento evento) {
        return eventoRepository.save(evento);
    }

    // Método para obtener todos los eventos
    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    // Método para obtener un evento por ID
    public Evento findById(Long id) {
        return eventoRepository.findById(id).orElse(null); // Devuelve el evento o null si no existe
    }
}
