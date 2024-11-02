package com.yovany.eventos.entity;




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "evento") // Nombre de la tabla en la base de datos
public class Evento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long id;                  // Identificador único del evento
    
    private String nombre;            // Nombre del evento
    
    private String lugar;
    
    private LocalDate fecha;          // Fecha del evento
    
    private LocalTime hora;           // Hora del evento

    // Constructor por defecto (requerido por JPA)
    public Evento() {}

    // Constructor con parámetros
    public Evento(Long id, String nombre, String lugar, LocalDate fecha, LocalTime hora) {
    this.id = id;
    this.nombre = nombre;
    this.lugar = lugar;  // Asignación correcta del campo lugar
    this.fecha = fecha;
    this.hora = hora;
}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    
    public String getLugar() {
        return lugar;
    }
    public void setLugar(String lugar){
        this.lugar = lugar;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    // Método toString para facilitar la depuración
    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", lugar='" + lugar + '\'' +
                '}';
    }
}
