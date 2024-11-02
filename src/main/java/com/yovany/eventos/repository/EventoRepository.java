package com.yovany.eventos.repository;

import com.yovany.eventos.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    // Puedes agregar métodos personalizados aquí si es necesario
}
