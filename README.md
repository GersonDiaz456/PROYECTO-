# PROYECTO III



## Aplicación para Gestionar Eventos

La aplicación que se presenta a continuación esta diseñada para permitir a los usuarios crear, modificar y eliminar eventos de manera sencilla. Utilizando Java, Swing para la interfaz gráfica, y Spring Boot, la aplicación se conecta a una base de datos gestionada con HeidiSQL. Esta combinación de tecnologías permite un manejo eficiente de los datos de eventos.

![enter image description here](https://download.logo.wine/logo/Java_%28programming_language%29/Java_%28programming_language%29-Logo.wine.png)

## Tecnologías utilizadas

-   **Java:** Lenguaje de programación utilizado para el desarrollo de la aplicación.
-   **Swing:** Biblioteca de Java para la creación de interfaces gráficas de usuario (GUI).
-   **Spring Boot:** Framework para el desarrollo de aplicaciones web en Java, facilitando la configuración y el despliegue.
-   **HeidiSQL:** Herramienta para la gestión de bases de datos que permite interactuar con bases de datos MySQL y MariaDB.


![enter image description here](https://i.postimg.cc/VvzhYZyD/Tecnologias.jpg)


## Instalación
 1. Requisitos:
 -   Java Development Kit (JDK) 8 o superior.
 -   Un servidor de base de datos MySQL o MariaDB.
 -   HeidiSQL para la gestión de la base de datos.
 -   Maven (opcional, si se utiliza para gestionar dependencias de Spring Boot).
2. Configura tu entorno:
 - Puedes clonar el proyecto desde el repositorio de GitHub para acceder
   al código fuente.
 - Configura la base de datos, abre HeidiSQL e inicia sesión en tu servidor y crea una nueva base de datos.
  - Configura las credenciales de la base de datos en Spring Boot: En el archivo `src/main/resources/application.properties`, configura las credenciales de la base de datos:
   `spring.datasource.url=jdbc:mysql://localhost:3306/gestor_eventos
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
`
-   Reemplaza `gestor_eventos` por el nombre de tu base de datos si es diferente.
-   Asegúrate de que `TU_USUARIO` y `TU_CONTRASEÑA` coincidan con las credenciales configuradas en tu servidor MySQL.
3. Ejecución de proyecto:
- Descargar e instalar todas las dependencias necesarias con Maven.
- Inicia el servidor de Spring Boot. 
- Verifica la conexión: Accede a la interfaz gráfica de usuario para gestionar eventos.
- La aplicación debe estar conectada a la base de datos configurada y mostrar los datos existentes.
- Compila y ejecuta.   

## Uso

-   Inicia la aplicación.
-   Accede a la interfaz gráfica de usuario.
-   Utiliza las opciones disponibles para crear,  modificar o eliminar eventos.
-   Los datos ingresados se gestionan en la base de datos configurada.

##  Descripción del código

[![1.jpg](https://i.postimg.cc/B6BspS8M/1.jpg)](https://postimg.cc/fty6Thf9)

## Base de datos

![enter image description here](https://i.postimg.cc/Kjcbs6BF/Base-de-datos.jpg)

***Clases principales***

## ProyectoeventosApplication.java

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

**Esta clase permite que Spring Boot inicialice y gestione los componentes de la aplicación y configura automáticamente los componentes encontrados en el paquete especificado.**

## TestDBConnection.java

    package com.yovany.eventos;
    
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;
    
    
    public class TestDBConnection {
         public static void main(String[] args) {
            String url = "jdbc:mariadb://localhost:3306/eventosempresa";
            String user = "Rodeumg";
            String password = "1234";
    
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                if (connection != null) {
                    System.out.println("Conexión exitosa a la base de datos.");
                }
            } catch (SQLException e) {
                System.out.println("Error de conexión: " + e.getMessage());
            }
        }
    } 

**Esta clase funciona para verificar manualmente la conexión a la base de datos. Su propósito principal es probar si las credenciales y la configuración de la conexión son correctas.**   

## EventoController.java

        package com.yovany.eventos.controller;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.http.HttpStatus;
    import org.springframework.web.bind.annotation.*;
    import com.yovany.eventos.entity.Evento; 
    import com.yovany.eventos.service.EventoService;
    
    import java.util.List;
    import java.util.Optional;
    
    @RestController
    @RequestMapping("/eventos")
    public class EventoController {
    
        @Autowired
        private EventoService eventoService;
    
        // Crear un nuevo evento
        @PostMapping
        public Evento crearEvento(@RequestBody Evento nuevoEvento) {
            return eventoService.save(nuevoEvento);
        }
    
        // Obtener todos los eventos
        @GetMapping
        public List<Evento> obtenerEventos() {
            return eventoService.findAll();
        }
    
        // Obtener un evento por ID
        @GetMapping("/{id}")
        public ResponseEntity<Evento> obtenerEventoPorId(@PathVariable Long id) {
            Optional<Evento> evento = eventoService.findById(id);
            if (evento.isPresent()) {
                return ResponseEntity.ok(evento.get()); // Devuelve el evento con estado 200
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Devuelve 404 si no se encuentra
            }
        }
    
        // Obtener un evento por nombre
        @GetMapping("/nombre/{nombre}")
        public List<Evento> obtenerEventoPorNombre(@PathVariable String nombre) {
            return eventoService.findByNombre(nombre);
        }
    
        // Actualizar un evento existente
        @PutMapping("/{id}")
        public Evento actualizarEvento(@PathVariable Long id, @RequestBody Evento eventoActualizado) {
            return eventoService.update(id, eventoActualizado);
        }
    
        // Eliminar un evento por ID
        @DeleteMapping("/{id}")
        public void eliminarEvento(@PathVariable Long id) {
            eventoService.deleteById(id);
        }
        @GetMapping("/ids")
    public List<Long> obtenerIdsEventos() {
        return eventoService.findAll().stream()
                            .map(Evento::getId)
                            .toList();
    }
    }

**Esta clase es un controlador REST en Spring Boot que define varias rutas para gestionar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los eventos. Esta clase utiliza el servicio EventoService para interactuar con la base de datos.**

## InvitadoController.java

    package com.yovany.eventos.controller;
    
    import com.yovany.eventos.entity.Invitado;
    import com.yovany.eventos.service.InvitadoService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.List;
    import java.util.Optional;
    
    @RestController
    @RequestMapping("/invitados")
    public class InvitadoController {
        @Autowired
        private InvitadoService invitadoService;
    
        @PostMapping
        public ResponseEntity<Invitado> crearInvitado(@RequestBody Invitado invitado) {
            Invitado nuevoInvitado = invitadoService.save(invitado);
            return new ResponseEntity<>(nuevoInvitado, HttpStatus.CREATED);
        }
    
        @GetMapping
        public List<Invitado> listarInvitados() {
            return invitadoService.findAll();
        }
    
        @GetMapping("/{id}")
        public ResponseEntity<Invitado> obtenerInvitadoPorId(@PathVariable Integer id) {
            Optional<Invitado> invitadoOpt = invitadoService.findById(id);
            return invitadoOpt.map(ResponseEntity::ok)
                              .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
        @PutMapping("/{id}")
        public ResponseEntity<Invitado> actualizarInvitado(@PathVariable Integer id, @RequestBody Invitado invitadoActualizado) {
            return invitadoService.update(id, invitadoActualizado)
                    .map(invitado -> new ResponseEntity<>(invitado, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarInvitado(@PathVariable Integer id) {
            if (invitadoService.deleteById(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

**Este controlador expone una API REST para manejar los invitados de un evento, permitiendo a los clientes realizar operaciones básicas sobre los datos de invitados.**


## Evento.java

    
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
        
        private String lugar;             // Lugar del evento
        
        private LocalDate fecha;          // Fecha del evento
        
        private LocalTime hora;           // Hora del evento
    
        private Integer costo;            // Costo del evento
    
        // Constructor por defecto (requerido por JPA)
        public Evento() {}
    
        // Constructor con parámetros
        public Evento(Long id, String nombre, String lugar, LocalDate fecha, LocalTime hora, Integer costo) {
            this.id = id;
            this.nombre = nombre;
            this.lugar = lugar;
            this.fecha = fecha;
            this.hora = hora;
            this.costo = costo;
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
    
        public String getLugar() {
            return lugar;
        }
    
        public void setLugar(String lugar) {
            this.lugar = lugar;
        }
    
        public LocalDate getFecha() {
            return fecha;
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
    
        public Integer getCosto() {
            return costo;
        }
    
        public void setCosto(Integer costo) {
            this.costo = costo;
        }
    
        // Método toString para facilitar la depuración
        @Override
        public String toString() {
            return "Evento{" +
                    "id=" + id +
                    ", nombre='" + nombre + '\'' +
                    ", lugar='" + lugar + '\'' +
                    ", fecha=" + fecha +
                    ", hora=" + hora +
                    ", costo=" + costo +
                    '}';
        }
    }

**Esta clase define claramente la estructura de un evento en términos de atributos y su mapeo a una base de datos. Es la base para realizar operaciones CRUD a través de EventoRepository y EventoService.**

## Invitado.java

    package com.yovany.eventos.entity;
    
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.Table;
    
    @Entity
    @Table(name = "invitado")
    public class Invitado {
    
        @Id // Esta anotación indica que 'identificacion' es la clave primaria
        private Integer identificacion; // Clave primaria (Integer)
    
        private String nombreinvitado; // Nombre del invitado
        private Integer telefonoinvitado; // Teléfono del invitado (Integer)
    
        // Relación ManyToOne con la entidad Evento
        @ManyToOne
        private Evento evento;
    
        // Constructor por defecto
        public Invitado() {}
    
        // Constructor con parámetros
        public Invitado(Integer identificacion, String nombreinvitado, Integer telefonoinvitado, Evento evento) {
            this.identificacion = identificacion;
            this.nombreinvitado = nombreinvitado;
            this.telefonoinvitado = telefonoinvitado;
            this.evento = evento; // Asocia el evento
        }
    
        // Getters y Setters
        public Integer getIdentificacion() {
            return identificacion;
        }
    
        public void setIdentificacion(Integer identificacion) {
            this.identificacion = identificacion;
        }
    
        public String getNombreinvitado() {
            return nombreinvitado;
        }
    
        public void setNombreinvitado(String nombreinvitado) {
            this.nombreinvitado = nombreinvitado;
        }
    
        public Integer getTelefonoinvitado() {
            return telefonoinvitado;
        }
    
        public void setTelefonoinvitado(Integer telefonoinvitado) {
            this.telefonoinvitado = telefonoinvitado;
        }
    
        public Evento getEvento() {
            return evento;
        }
    
        public void setEvento(Evento evento) {
            this.evento = evento;
        }
    
        @Override
        public String toString() {
            return "Invitado{" +
                    "identificacion=" + identificacion +
                    ", nombre='" + nombreinvitado + '\'' +
                    ", telefonoinvitado=" + telefonoinvitado +
                    ", evento=" + (evento != null ? evento.getNombre() : "No asignado") +
                    '}';
        }
    }

**Esta case representa la entidad Invitado en una aplicación de eventos. Está configurada para ser persistida en una base de datos utilizando JPA (Jakarta Persistence API).**

## EventoRepository.java

            package com.yovany.eventos.repository;
    
    
    import com.yovany.eventos.entity.Evento;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    
    import java.util.List;
    
    public interface EventoRepository extends JpaRepository<Evento, Long> {
        List<Evento> findByNombre(String nombre);
    }

**Esta clase es un repositorio de datos en Spring Data JPA que facilita la interacción con la base de datos para la entidad Evento.**

## InvitadoRepository.java
    
        package com.yovany.eventos.repository;
        
        import com.yovany.eventos.entity.Invitado;
        import org.springframework.data.jpa.repository.JpaRepository;
        import java.util.List;
        
        public interface InvitadoRepository extends JpaRepository<Invitado, Integer> {
        
            List<Invitado> findByEvento(com.yovany.eventos.entity.Evento evento); // Ahora la consulta funcionará
        }

**Es una interfaz que facilita la interacción con la base de datos para gestionar datos de la entidad Invitado, incluyendo la capacidad de realizar consultas personalizadas como buscar invitados por un evento específico.**

## EventoService.java

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
    
        // Guardar un nuevo evento
        public Evento save(Evento evento) {
            if (evento.getNombre() == null || evento.getNombre().isEmpty()) {
                throw new IllegalArgumentException("El nombre del evento no puede estar vacío");
            }
            // Agregar más validaciones según sea necesario
            return eventoRepository.save(evento);
        }
    
        // Encontrar todos los eventos
        public List<Evento> findAll() {
            return eventoRepository.findAll();
        }
    
        // Encontrar un evento por ID
        public Optional<Evento> findById(Long id) {
            return eventoRepository.findById(id);
        }
    
        // Encontrar eventos por nombre
        public List<Evento> findByNombre(String nombre) {
            return eventoRepository.findByNombre(nombre);
        }
    
        // Actualizar un evento existente
        public Evento update(Long id, Evento eventoActualizado) {
            return eventoRepository.findById(id).map(evento -> {
                evento.setNombre(eventoActualizado.getNombre());
                evento.setFecha(eventoActualizado.getFecha());
                evento.setHora(eventoActualizado.getHora()); // Asegúrate de tener el campo hora
                evento.setLugar(eventoActualizado.getLugar()); // Asegúrate de tener el campo lugar
                evento.setCosto(eventoActualizado.getCosto());
                return eventoRepository.save(evento);
            }).orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
        }
    
        // Eliminar un evento por ID
        public void deleteById(Long id) {
            eventoRepository.deleteById(id);
        }
    }

**Es una clase de servicio en la arquitectura Spring, la cual encapsula la lógica de negocio para manejar la entidad Evento. Esta clase se comunica con la base de datos a través de EventoRepository. EventoService actúa como intermediario entre el controlador (EventoController) y el repositorio (EventoRepository).**

## InvitadoService.java

    package com.yovany.eventos.service;
    
    import com.yovany.eventos.entity.Invitado;
    import com.yovany.eventos.entity.Evento;
    import com.yovany.eventos.repository.InvitadoRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    
    import java.util.List;
    import java.util.Optional;
    
    @Service
    public class InvitadoService {
         @Autowired
        private InvitadoRepository invitadoRepository;
    
        // Guardar un nuevo invitado
        public Invitado save(Invitado invitado) {
            return invitadoRepository.save(invitado);
        }
    
        // Encontrar todos los invitados
        public List<Invitado> findAll() {
            return invitadoRepository.findAll();
        }
    
        // Encontrar un invitado por ID (ahora usando Integer)
        public Optional<Invitado> findById(Integer id) {
            return invitadoRepository.findById(id);
        }
    
        // Encontrar todos los invitados de un evento específico
        public List<Invitado> findByEvento(Evento evento) {
            return invitadoRepository.findByEvento(evento);
        }
    
        // Actualizar un invitado existente (ahora usando Integer para id)
        public Optional<Invitado> update(Integer id, Invitado invitadoActualizado) {
            return invitadoRepository.findById(id).map(invitado -> {
                invitado.setIdentificacion(invitadoActualizado.getIdentificacion());
                invitado.setNombreinvitado(invitadoActualizado.getNombreinvitado());
                invitado.setTelefonoinvitado(invitadoActualizado.getTelefonoinvitado());
                
                return invitadoRepository.save(invitado);
            });
        }
    
        // Eliminar un invitado por ID (ahora usando Integer)
        public boolean deleteById(Integer id) {
            if (invitadoRepository.existsById(id)) {
                invitadoRepository.deleteById(id);
                return true;
            }
            return false;
        }
        
    }

**Esta clase actúa como una capa intermedia que gestiona la lógica de negocio y facilita la interacción entre el controlador y el repositorio de Invitado.**

## ControleventosApplicationTests.java

    package com.yovany.eventos.controleventos;
    
    import org.junit.jupiter.api.Test;
    import org.springframework.boot.test.context.SpringBootTest;
    
    @SpringBootTest
    class ControleventosApplicationTests {
    
    	@Test
    	void contextLoads() {
    	}
    
    }

**Es una clase de prueba en un proyecto Spring Boot, y su propósito es asegurar que el contexto de la aplicación se cargue correctamente durante las pruebas.**

- ***Recurso para conexión a la base de datos*** 

## application.properties
        spring.datasource.url=jdbc:mariadb://localhost:3306/eventosEmpresa
    spring.datasource.username=Rodeumg
    spring.datasource.password=1234
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.devtools.restart.enabled=false
    spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
    
    server.port=8080

**Esta clase permite que la aplicación Spring Boot se conecte a una base de datos MariaDB.**

***Códigos relacionados a la ejecución de la interfaz***

## Login.java

    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
     */
    package com.yovany.eventos.ui;
    import javax.swing.JOptionPane;
    /**
     *
     * @author DELL
     */
    public class Login extends javax.swing.JFrame {
    
        /**
         * Creates new form Login
         */
        public Login() {
            initComponents();
          Btningresar.addActionListener(evt -> verificarCredenciales());
        }
          // Método para verificar las credenciales
        private void verificarCredenciales() {
            // Obtener los valores de los campos de texto
            String usuario = Tusuario.getText();
            String contraseña = new String(Pcontraseña.getPassword()); // Convertir char[] a String
    
            // Verificar si el usuario y la contraseña son correctos
            if (usuario.equals("Beta1") && contraseña.equals("http8080")) {
                // Credenciales correctas, abrir la ventana de Menu
                new Menu().setVisible(true);  // Suponiendo que tienes una clase llamada Menu
                this.dispose(); // Cerrar la ventana actual
            } else {
                // Credenciales incorrectas, mostrar mensaje de error
                JOptionPane.showMessageDialog(this, "Usuario y/o contraseña inválidos", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
            }
        }
    
    
        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
    
            Jtitulo = new javax.swing.JLabel();
            Jcontraseña = new javax.swing.JLabel();
            Jusuario = new javax.swing.JLabel();
            Tusuario = new javax.swing.JTextField();
            Pcontraseña = new javax.swing.JPasswordField();
            jPanel1 = new javax.swing.JPanel();
            Btningresar = new javax.swing.JToggleButton();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
    
            Jtitulo.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            Jtitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            Jtitulo.setText("LOGIN");
    
            Jcontraseña.setFont(new java.awt.Font("Sylfaen", 0, 18)); // NOI18N
            Jcontraseña.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            Jcontraseña.setText("CONTRASEÑA");
    
            Jusuario.setFont(new java.awt.Font("Sylfaen", 0, 18)); // NOI18N
            Jusuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            Jusuario.setText("USUARIO");
    
            Tusuario.setToolTipText("");
    
            Pcontraseña.setForeground(new java.awt.Color(255, 0, 0));
    
            jPanel1.setBackground(new java.awt.Color(0, 153, 153));
    
            Btningresar.setText("INGRESAR");
            Btningresar.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(271, Short.MAX_VALUE)
                    .addComponent(Btningresar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(261, 261, 261))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(Btningresar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(42, Short.MAX_VALUE))
            );
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Pcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Jcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(Jusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(134, 134, 134)
                            .addComponent(Jtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(Tusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(Jtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(56, 56, 56)
                            .addComponent(Jusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Tusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(51, 51, 51)
                    .addComponent(Jcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(Pcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
    
            pack();
        }// </editor-fold>                        
    
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
    
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Login().setVisible(true);
                }
            });
        }
    
        // Variables declaration - do not modify                     
        private javax.swing.JToggleButton Btningresar;
        private javax.swing.JLabel Jcontraseña;
        private javax.swing.JLabel Jtitulo;
        private javax.swing.JLabel Jusuario;
        private javax.swing.JPasswordField Pcontraseña;
        private javax.swing.JTextField Tusuario;
        private javax.swing.JPanel jPanel1;
        // End of variables declaration                   
    }

![enter image description here](https://i.postimg.cc/WzYHhKCy/Login.jpg)

## Menu.java

     /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
     */
    package com.yovany.eventos.ui;
    import com.yovany.eventos.ui.RegistroEventos;
    /**
     *
     * @author Yovany
     */
    public class Menu extends javax.swing.JFrame {
    
        /**
         * Creates new form Menu
         */
        public Menu() {
            initComponents();
        }
    
        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
    
            jToggleButton1 = new javax.swing.JToggleButton();
            jPanel1 = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            jPanel2 = new javax.swing.JPanel();
            Eventos = new javax.swing.JButton();
            Btnbuscareventos = new javax.swing.JToggleButton();
            Btneditareventos = new javax.swing.JToggleButton();
            Btneliminar = new javax.swing.JToggleButton();
            Btninvitados = new javax.swing.JToggleButton();
            jScrollPane1 = new javax.swing.JScrollPane();
            jTextArea1 = new javax.swing.JTextArea();
    
            jToggleButton1.setText("jToggleButton1");
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
    
            jLabel1.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("MENU");
    
            jPanel2.setBackground(new java.awt.Color(0, 153, 153));
    
            Eventos.setText("Registrar Eventos");
            Eventos.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Eventos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    EventosActionPerformed(evt);
                }
            });
    
            Btnbuscareventos.setText("Buscar Eventos");
            Btnbuscareventos.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btnbuscareventos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnbuscareventosActionPerformed(evt);
                }
            });
    
            Btneditareventos.setText("Editar Eventos");
            Btneditareventos.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btneditareventos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtneditareventosActionPerformed(evt);
                }
            });
    
            Btneliminar.setText("Eliminar Eventos ");
            Btneliminar.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btneliminar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtneliminarActionPerformed(evt);
                }
            });
    
            Btninvitados.setText("Agregar Invitados");
            Btninvitados.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btninvitados.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtninvitadosActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Eventos, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(Btninvitados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Btneliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Btneditareventos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Btnbuscareventos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(33, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(Eventos, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(Btnbuscareventos, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Btneditareventos, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(Btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(Btninvitados, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
    
            jTextArea1.setColumns(20);
            jTextArea1.setFont(new java.awt.Font("Sylfaen", 0, 18)); // NOI18N
            jTextArea1.setRows(5);
            jTextArea1.setText("\n¡Bienvenido a FilmmakerGT! 🎉\n\nNos alegra mucho tenerte aquí. En FilmmakerGT, \nsomos expertos en ayudarte a planear y llevar a cabo \neventos inolvidables. Nuestra plataforma está diseñada\n para que la organización de eventos sea fácil, eficiente \ny, sobre todo, adaptada a tus necesidades.");
            jScrollPane1.setViewportView(jTextArea1);
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(200, 200, 200)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(14, 14, 14))))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(42, 42, 42))
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
    
            pack();
        }// </editor-fold>                        
    
        private void EventosActionPerformed(java.awt.event.ActionEvent evt) {                                        
          // Crear una instancia de RegistroEventos y hacerla visible
         RegistroEventos registroeventos = new RegistroEventos();
        registroeventos.setVisible(true);
      
        this.dispose();
        }                                       
    
        private void BtneliminarActionPerformed(java.awt.event.ActionEvent evt) {                                            
           Eliminar eliminar = new Eliminar();
        eliminar.setVisible(true);
        this.dispose();
            
            
        }                                           
    
        private void BtninvitadosActionPerformed(java.awt.event.ActionEvent evt) {                                             
           Invitados invitados = new Invitados();
        invitados.setVisible(true);
        this.dispose();
           
        }                                            
    
        private void BtnbuscareventosActionPerformed(java.awt.event.ActionEvent evt) {                                                 
          
            busquedaEventos busquedaeventos = new busquedaEventos();
        busquedaeventos.setVisible(true);
        this.dispose();
            
            
        }                                                
    
        private void BtneditareventosActionPerformed(java.awt.event.ActionEvent evt) {                                                 
            editarEventos editareventos = new editarEventos();
        editareventos.setVisible(true);
        this.dispose();
            
            
        }                                                
    
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
    
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Menu().setVisible(true);
                }
            });
        }
    
        // Variables declaration - do not modify                     
        private javax.swing.JToggleButton Btnbuscareventos;
        private javax.swing.JToggleButton Btneditareventos;
        private javax.swing.JToggleButton Btneliminar;
        private javax.swing.JToggleButton Btninvitados;
        private javax.swing.JButton Eventos;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTextArea jTextArea1;
        private javax.swing.JToggleButton jToggleButton1;
        // End of variables declaration                   
    }

![enter image description here](https://i.postimg.cc/PfSc3pJM/Menu.jpg)

## RegistroEventos.java

    package com.yovany.eventos.ui;
    
    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.time.format.DateTimeFormatter;
    import java.time.format.DateTimeParseException;
    
    import com.yovany.eventos.entity.Evento; // Asegúrate de importar la clase Evento
    import java.io.IOException;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.nio.charset.StandardCharsets;
    import javax.swing.JOptionPane;
    
    /**
     *
     * @author Yovany
     */
    public class RegistroEventos extends javax.swing.JFrame {
    
        /**
         * Creates new form RegistroEventos
         */
        public RegistroEventos() {
            initComponents();
        }
        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
    
            jPanel1 = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            NombreEvento = new javax.swing.JLabel();
            LugarEvento = new javax.swing.JLabel();
            FechaEvento = new javax.swing.JLabel();
            HoraEvento = new javax.swing.JLabel();
            NombreEbento = new javax.swing.JTextField();
            LugarEbento = new javax.swing.JTextField();
            FechaEbento = new javax.swing.JTextField();
            HoraEbento = new javax.swing.JTextField();
            CostoEvento = new javax.swing.JLabel();
            CostoEbento = new javax.swing.JTextField();
            jPanel2 = new javax.swing.JPanel();
            GuardarEvento = new javax.swing.JButton();
            BtnMenu = new javax.swing.JToggleButton();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
    
            jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
            jLabel1.setText("Registro Eventos");
    
            NombreEvento.setText("Nombre Evento");
    
            LugarEvento.setText("Lugar del Evento");
    
            FechaEvento.setText("Fecha del evento");
    
            HoraEvento.setText("Hora del Evento");
    
            NombreEbento.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    NombreEbentoActionPerformed(evt);
                }
            });
    
            CostoEvento.setText("Costo");
    
            jPanel2.setBackground(new java.awt.Color(0, 153, 153));
    
            GuardarEvento.setText("Guardar Evento");
            GuardarEvento.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            GuardarEvento.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    GuardarEventoActionPerformed(evt);
                }
            });
    
            BtnMenu.setText("Menu");
            BtnMenu.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            BtnMenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnMenuActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(62, 62, 62)
                    .addComponent(GuardarEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                    .addComponent(BtnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(70, 70, 70))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(BtnMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(GuardarEvento, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                    .addContainerGap(24, Short.MAX_VALUE))
            );
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(42, 42, 42)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LugarEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(NombreEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FechaEvento, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(HoraEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(CostoEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(35, 35, 35)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(FechaEbento)
                        .addComponent(NombreEbento, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(HoraEbento, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(LugarEbento)
                        .addComponent(CostoEbento))
                    .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(86, 86, 86))
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel1)
                    .addGap(44, 44, 44)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(NombreEvento)
                        .addComponent(NombreEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(LugarEvento))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addComponent(LugarEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(23, 23, 23)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(FechaEvento)
                        .addComponent(FechaEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(HoraEvento)
                        .addComponent(HoraEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CostoEvento)
                        .addComponent(CostoEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
    
            pack();
        }// </editor-fold>                        
    
        private void NombreEbentoActionPerformed(java.awt.event.ActionEvent evt) {                                             
            // TODO add your handling code here:
        }                                            
    
        private void GuardarEventoActionPerformed(java.awt.event.ActionEvent evt) {                                              
        GuardarEvento.setEnabled(false);   
        Evento nuevoEvento = new Evento();
        
        
        // Verifica que los campos no estén vacíos
        if (NombreEbento.getText().isEmpty() || FechaEbento.getText().isEmpty() || HoraEbento.getText().isEmpty() || LugarEbento.getText().isEmpty() || CostoEbento.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos");
            GuardarEvento.setEnabled(true); // Rehabilitar el botón si hay error
            return; // Sale si hay un error
        }
        
        
        nuevoEvento.setNombre(NombreEbento.getText());
        nuevoEvento.setLugar(LugarEbento.getText());
        
        // Validar el campo de costo como entero
        int costo;
        try {
            costo = Integer.parseInt(CostoEbento.getText());
            if (costo < 0) {
                JOptionPane.showMessageDialog(this, "El costo debe ser un número entero positivo");
                GuardarEvento.setEnabled(true);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El costo debe ser un número entero");
            GuardarEvento.setEnabled(true);
            return;
        }
        
        // Convertir la fecha y la hora desde String a LocalDate y LocalTime
        try {
            String fechaTexto = FechaEbento.getText();
            String horaTexto = HoraEbento.getText();
    
            // Asegúrate de que el formato coincida con lo que esperas
            DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Ajusta el formato según tu necesidad
            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm"); // Ajusta el formato según tu necesidad
    
            LocalDate fecha = LocalDate.parse(fechaTexto, formatterFecha);
            LocalTime hora = LocalTime.parse(horaTexto, formatterHora);
    
            nuevoEvento.setFecha(fecha);
            nuevoEvento.setHora(hora);
            nuevoEvento.setCosto(costo); // Establece el costo en el evento
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha/hora incorrecto: " + e.getMessage());
            return; // Salir si hay un error de formato
        }
        
        // Envía los datos a la API
        String url = "http://localhost:8080/eventos"; // Cambia la URL según tu configuración
    
        try {
            // Aquí se realiza la llamada HTTP POST
            URL obj = new URL(url); // Esta línea crea un objeto URL con la dirección de tu API
            HttpURLConnection con = (HttpURLConnection) obj.openConnection(); // Abre la conexión
            con.setRequestMethod("POST"); // Especifica que es una solicitud POST
            con.setRequestProperty("Content-Type", "application/json"); // Establece el tipo de contenido a JSON
            con.setDoOutput(true); // Permite que se envíen datos a la conexión
            
    
            // Convertir el objeto Evento a JSON
            String jsonInputString = String.format(
             "{\"nombre\":\"%s\", \"fecha\":\"%s\", \"hora\":\"%s\", \"lugar\":\"%s\", \"costo\":%d}",
                nuevoEvento.getNombre(), nuevoEvento.getFecha(), nuevoEvento.getHora(), nuevoEvento.getLugar(), nuevoEvento.getCosto()
            );
    
            // Enviar la solicitud
            try (java.io.OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8); // Convierte el JSON a bytes
                os.write(input, 0, input.length); // Escribe los bytes en el flujo de salida
            }
    
            int responseCode = con.getResponseCode(); // Obtiene el código de respuesta de la API
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Evento guardado con éxito");
                // Limpiar campos después de guardar
                NombreEbento.setText("");
                FechaEbento.setText("");
                HoraEbento.setText("");
                LugarEbento.setText("");
                CostoEbento.setText(""); // Limpiar el campo de costo también
                
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el evento");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        } finally {
            GuardarEvento.setEnabled(true); // Rehabilitar el botón después de intentar guardar
        }
        
    
        }                                             
    
        private void BtnMenuActionPerformed(java.awt.event.ActionEvent evt) {                                        
            Menu menu = new Menu();
        menu.setVisible(true);
      
        this.dispose();
            
            
        }                                       
    
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(RegistroEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(RegistroEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(RegistroEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(RegistroEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
    
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new RegistroEventos().setVisible(true);
                }
            });
        }
    
        // Variables declaration - do not modify                     
        private javax.swing.JToggleButton BtnMenu;
        private javax.swing.JTextField CostoEbento;
        private javax.swing.JLabel CostoEvento;
        private javax.swing.JTextField FechaEbento;
        private javax.swing.JLabel FechaEvento;
        private javax.swing.JButton GuardarEvento;
        private javax.swing.JTextField HoraEbento;
        private javax.swing.JLabel HoraEvento;
        private javax.swing.JTextField LugarEbento;
        private javax.swing.JLabel LugarEvento;
        private javax.swing.JTextField NombreEbento;
        private javax.swing.JLabel NombreEvento;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        // End of variables declaration                   
    }

![enter image description here](https://i.postimg.cc/Hkyhk62S/Registro.jpg)

## busquedaEventos.java

    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
     */
    package com.yovany.eventos.ui;
    
    import javax.swing.JOptionPane;
    import javax.swing.JOptionPane;
    import java.net.HttpURLConnection;
    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.net.URLEncoder;
    
    
    /**
     *
     * @author Yovany
     */
    public class busquedaEventos extends javax.swing.JFrame {
    
        /**
         * Creates new form busquedaEventos
         */
        public busquedaEventos() {
         initComponents();
            // Agrega un ActionListener al botón
          
             
        }
    
        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
    
            jPanel1 = new javax.swing.JPanel();
            Jcodigo = new javax.swing.JLabel();
            Jnombre = new javax.swing.JLabel();
            Tnombre = new javax.swing.JTextField();
            Tid = new javax.swing.JTextField();
            Jlugar = new javax.swing.JLabel();
            Jfecha = new javax.swing.JLabel();
            Jhora = new javax.swing.JLabel();
            Tlugar = new javax.swing.JTextField();
            Tfecha = new javax.swing.JTextField();
            Thora = new javax.swing.JTextField();
            Jcosto = new javax.swing.JLabel();
            Tcosto = new javax.swing.JTextField();
            jPanel3 = new javax.swing.JPanel();
            Btnmenu = new javax.swing.JToggleButton();
            Btnbusqueda = new javax.swing.JButton();
            jLabel1 = new javax.swing.JLabel();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
    
            Jcodigo.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jcodigo.setText("Codigo");
    
            Jnombre.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jnombre.setText("Nombre Evento");
    
            Tnombre.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TnombreActionPerformed(evt);
                }
            });
    
            Tid.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TidActionPerformed(evt);
                }
            });
    
            Jlugar.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jlugar.setText("lugar del evento");
    
            Jfecha.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jfecha.setText("Fecha del evento");
    
            Jhora.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jhora.setText("Hora del evento");
    
            Tlugar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TlugarActionPerformed(evt);
                }
            });
    
            Tfecha.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TfechaActionPerformed(evt);
                }
            });
    
            Thora.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ThoraActionPerformed(evt);
                }
            });
    
            Jcosto.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
            Jcosto.setText("Costo");
    
            Tcosto.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TcostoActionPerformed(evt);
                }
            });
    
            jPanel3.setBackground(new java.awt.Color(0, 153, 153));
    
            Btnmenu.setText("MENU");
            Btnmenu.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btnmenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnmenuActionPerformed(evt);
                }
            });
    
            Btnbusqueda.setText("BUSQUEDA EVENTO");
            Btnbusqueda.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btnbusqueda.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnbusquedaActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(51, 51, 51)
                    .addComponent(Btnbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(192, 192, 192)
                    .addComponent(Btnmenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(55, 55, 55))
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Btnbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Btnmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(39, Short.MAX_VALUE))
            );
    
            jLabel1.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Busqueda");
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(68, 68, 68)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Jnombre)
                                .addComponent(Jcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Jlugar)
                                .addComponent(Jfecha)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(Jcosto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Jhora, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGap(79, 79, 79)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Tnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tid, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tlugar, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Thora, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tcosto, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(213, 213, 213)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(107, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Tid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(31, 31, 31)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Tnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jlugar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Tlugar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(26, 26, 26)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Jfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Tfecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jhora, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Thora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Jcosto)
                        .addComponent(Tcosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(79, 79, 79)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
    
            pack();
        }// </editor-fold>                        
    
        private void BtnmenuActionPerformed(java.awt.event.ActionEvent evt) {                                        
            Menu menu = new Menu();
            menu.setVisible(true);
    
            this.dispose();
        }                                       
    
        private void TcostoActionPerformed(java.awt.event.ActionEvent evt) {                                       
            // TODO add your handling code here:
        }                                      
    
        private void ThoraActionPerformed(java.awt.event.ActionEvent evt) {                                      
            // TODO add your handling code here:
        }                                     
    
        private void TfechaActionPerformed(java.awt.event.ActionEvent evt) {                                       
            // TODO add your handling code here:
        }                                      
    
        private void TlugarActionPerformed(java.awt.event.ActionEvent evt) {                                       
            // TODO add your handling code here:
        }                                      
    
        private void TidActionPerformed(java.awt.event.ActionEvent evt) {                                    
            // TODO add your handling code here:
        }                                   
    
        private void BtnbusquedaActionPerformed(java.awt.event.ActionEvent evt) {                                            
            String id = Tid.getText();
            String nombre = Tnombre.getText();
    
            // Construir la URL de la API
            String url = "http://localhost:8080/eventos/";
            if (!id.isEmpty()) {
                url += id; // Si se proporciona un ID, buscar por ID
            } else if (!nombre.isEmpty()) {
                url += "nombre/" + nombre; // Si se proporciona un nombre, buscar por nombre
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID o un nombre de evento.");
                return; // Salir si no se ingresa nada
            }
    
            try {
                // Crear un objeto URL y abrir una conexión
                java.net.URL apiUrl = new java.net.URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
    
                // Obtener la respuesta de la API
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leer la respuesta
                    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
    
                    // Convertir la respuesta en String y extraer datos manualmente
                    String jsonResponse = response.toString();
    
                    Tnombre.setText(extractValue(jsonResponse, "nombre"));
                    Thora.setText(extractValue(jsonResponse, "hora"));
                    Tfecha.setText(extractValue(jsonResponse, "fecha"));
                    Tlugar.setText(extractValue(jsonResponse, "lugar"));
                    Tcosto.setText(extractValue(jsonResponse, "costo"));
    
                    // Bloquear los campos para que no se puedan modificar
                    Tnombre.setEditable(false);
                    Thora.setEditable(false);
                    Tfecha.setEditable(false);
                    Tlugar.setEditable(false);
                    Tcosto.setEditable(false); // Bloquea el campo de costo
    
                    // Mantener el campo de ID como editable para nuevas búsquedas
                    Tid.setEditable(true);
    
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    // Si el evento no se encuentra, mostrar el mensaje
                    JOptionPane.showMessageDialog(this, "ID no encontrado. Por favor, intente con otro.");
                    Tnombre.setText("");
                    Thora.setText("");
                    Tfecha.setText("");
                    Tlugar.setText("");
                    Tcosto.setText(""); // Limpia el campo de costo si no se encuentra el evento
                } else {
                    // Limpiar los campos si no se encuentra el evento
    
                    JOptionPane.showMessageDialog(this, "Error al buscar el evento. Código de respuesta: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al buscar el evento: " + e.getMessage());
            }
    
        }                                           
    
        private void TnombreActionPerformed(java.awt.event.ActionEvent evt) {                                        
            // TODO add your handling code here:
        }                                       
            private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey) + searchKey.length();
        int endIndex;
        
        // Verificar si el valor es un número (sin comillas) o una cadena (con comillas)
        if (json.charAt(startIndex) == '\"') {  // Es una cadena
            startIndex += 1;  // Saltar la comilla inicial
            endIndex = json.indexOf("\"", startIndex);
        } else {  // Es un número
            endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) { // Puede ser el último elemento sin coma
                endIndex = json.indexOf("}", startIndex);
            }
        }
    
        if (startIndex != -1 && endIndex != -1) {
            return json.substring(startIndex, endIndex);
        }
        return ""; // Retorna cadena vacía si no se encuentra el valor
    }
         
        // Método para manejar la búsqueda del evento
        
       
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(busquedaEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(busquedaEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(busquedaEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(busquedaEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
    
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new busquedaEventos().setVisible(true);
                }
            });
        }
    
        // Variables declaration - do not modify                     
        private javax.swing.JButton Btnbusqueda;
        private javax.swing.JToggleButton Btnmenu;
        private javax.swing.JLabel Jcodigo;
        private javax.swing.JLabel Jcosto;
        private javax.swing.JLabel Jfecha;
        private javax.swing.JLabel Jhora;
        private javax.swing.JLabel Jlugar;
        private javax.swing.JLabel Jnombre;
        private javax.swing.JTextField Tcosto;
        private javax.swing.JTextField Tfecha;
        private javax.swing.JTextField Thora;
        private javax.swing.JTextField Tid;
        private javax.swing.JTextField Tlugar;
        private javax.swing.JTextField Tnombre;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel3;
        // End of variables declaration                   
    
    }

![enter image description here](https://i.postimg.cc/XJ8h0X84/Busqueda.jpg)

## editarEventos.java

    package com.yovany.eventos.ui;
    
    
    import javax.swing.JOptionPane;
    import java.net.HttpURLConnection;
    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.net.URLEncoder;
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.io.OutputStream;
    
    
    /**
     *
     * @author Yovany
     */
    public class editarEventos extends javax.swing.JFrame {
    
        /**
         * Creates new form editarEventos
         */
        public editarEventos() {
            initComponents();
        }
    
        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
    
            jPanel1 = new javax.swing.JPanel();
            Jid = new javax.swing.JLabel();
            Jnombre = new javax.swing.JLabel();
            Jfecha = new javax.swing.JLabel();
            Jlugar = new javax.swing.JLabel();
            Jhora = new javax.swing.JLabel();
            Tlugar = new javax.swing.JTextField();
            Tnombre = new javax.swing.JTextField();
            Tfecha = new javax.swing.JTextField();
            Thora = new javax.swing.JTextField();
            Tid = new javax.swing.JTextField();
            Jcosto = new javax.swing.JLabel();
            Tcosto = new javax.swing.JTextField();
            jPanel2 = new javax.swing.JPanel();
            Btnbuscar = new javax.swing.JButton();
            Btnguardar = new javax.swing.JButton();
            Btnmenu = new javax.swing.JToggleButton();
            jLabel1 = new javax.swing.JLabel();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
    
            Jid.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jid.setText("ID");
    
            Jnombre.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jnombre.setText("Nombre Evento");
    
            Jfecha.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jfecha.setText("Fecha del Evento");
    
            Jlugar.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jlugar.setText("Lugar del Evento");
    
            Jhora.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jhora.setText("Hora del Evento");
    
            Tlugar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TlugarActionPerformed(evt);
                }
            });
    
            Tnombre.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TnombreActionPerformed(evt);
                }
            });
    
            Tfecha.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TfechaActionPerformed(evt);
                }
            });
    
            Thora.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ThoraActionPerformed(evt);
                }
            });
    
            Tid.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TidActionPerformed(evt);
                }
            });
    
            Jcosto.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jcosto.setText("Costo");
    
            jPanel2.setBackground(new java.awt.Color(0, 153, 153));
    
            Btnbuscar.setText("BUSCAR");
            Btnbuscar.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btnbuscar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnbuscarActionPerformed(evt);
                }
            });
    
            Btnguardar.setText("ACTUALIZAR");
            Btnguardar.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btnguardar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnguardarActionPerformed(evt);
                }
            });
    
            Btnmenu.setText("MENU");
            Btnmenu.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btnmenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnmenuActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(98, 98, 98)
                    .addComponent(Btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(Btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(Btnmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(110, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(38, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Btnmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(23, 23, 23))
            );
    
            jLabel1.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            jLabel1.setText("Modificar");
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(Jid, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Tid, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(Jnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Tnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(Jhora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Jlugar, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Jfecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(Jcosto, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Tlugar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tfecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Thora, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tcosto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(33, 33, 33))
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(255, 255, 255))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jid, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Tid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(25, 25, 25)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Tnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(24, 24, 24)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jlugar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Tlugar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(26, 26, 26)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Tfecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(26, 26, 26)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jhora, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Thora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jcosto)
                        .addComponent(Tcosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
    
            pack();
        }// </editor-fold>                        
    
        private void TlugarActionPerformed(java.awt.event.ActionEvent evt) {                                       
            // TODO add your handling code here:
        }                                      
    
        private void TnombreActionPerformed(java.awt.event.ActionEvent evt) {                                        
            // TODO add your handling code here:
        }                                       
    
        private void TfechaActionPerformed(java.awt.event.ActionEvent evt) {                                       
            // TODO add your handling code here:
        }                                      
    
        private void ThoraActionPerformed(java.awt.event.ActionEvent evt) {                                      
            // TODO add your handling code here:
        }                                     
    
        private void TidActionPerformed(java.awt.event.ActionEvent evt) {                                    
            // TODO add your handling code here:
        }                                   
    
        private void BtnbuscarActionPerformed(java.awt.event.ActionEvent evt) {                                          
           String id = Tid.getText();
            String nombre = Tnombre.getText();
            
            if (id.isEmpty() && nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID o un nombre de evento.");
                return;
            }
    
            // Construcción dinámica de la URL de búsqueda
            String url = "http://localhost:8080/eventos/" + (!id.isEmpty() ? id : "nombre/" + nombre);
        
         try {
                HttpURLConnection connection = (HttpURLConnection) new java.net.URL(url).openConnection();
                connection.setRequestMethod("GET");
            
              int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leer y procesar la respuesta
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                
                String jsonResponse = response.toString();
                    Tnombre.setText(extractValue(jsonResponse, "nombre"));
                    Thora.setText(extractValue(jsonResponse, "hora"));
                    Tfecha.setText(extractValue(jsonResponse, "fecha"));
                    Tlugar.setText(extractValue(jsonResponse, "lugar"));
                    Tcosto.setText(extractValue(jsonResponse, "costo"));
                
               
    
              } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // Si el evento no se encuentra, mostrar el mensaje
                JOptionPane.showMessageDialog(this, "ID no encontrado. Por favor, intente con otro.");
                Tnombre.setText("");
                Thora.setText("");
                Tfecha.setText("");
                Tlugar.setText("");
                Tcosto.setText(""); // Limpia el campo de costo si no se encuentra el evento
            } else {
                // Limpiar los campos si no se encuentra el evento
                
                JOptionPane.showMessageDialog(this, "Error al buscar el evento. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar el evento: " + e.getMessage());
        }
        
         
        }                                         
            
        
        
        
        private void BtnguardarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        
            // Obtener datos desde los campos de texto
            String idEvento = Tid.getText(); 
            String nombreEvento = Tnombre.getText(); 
            String fechaEvento = Tfecha.getText(); 
            String horaEvento = Thora.getText(); 
            String lugarEvento = Tlugar.getText(); 
            String costoEvento = Tcosto.getText(); // Asegúrate de capturar el costo también
            
            // Validar que los campos no estén vacíos
        if (idEvento.isEmpty() || nombreEvento.isEmpty() || fechaEvento.isEmpty() || horaEvento.isEmpty() || lugarEvento.isEmpty() || costoEvento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }
        
        // Manejo de la conversión de costoEvento a Integer
        Integer costo;
        try {
            costo = Integer.valueOf(costoEvento); // Convertir la cadena a Integer
            if (costo <= 0) { // Validar que el costo sea positivo
                JOptionPane.showMessageDialog(this, "El costo debe ser un número positivo.");
                return;
            }
        } catch (NumberFormatException e) { // Capturar excepciones de conversión
            JOptionPane.showMessageDialog(this, "El costo debe ser un número válido.");
            return;
        }
            
       // Construir la URL para la actualización
        // Construir la URL para la actualización
        String url = "http://localhost:8080/eventos/" + idEvento;
        String jsonInputString = String.format("{\"nombre\":\"%s\", \"fecha\":\"%s\", \"hora\":\"%s\", \"lugar\":\"%s\", \"costo\":%d}", 
                                                 nombreEvento, fechaEvento, horaEvento, lugarEvento, costo);
        
            try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
         
            
            // Enviar la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);           
            }
            
            
             // Leer la respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Evento actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el evento. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el evento: " + e.getMessage());
        }   
       
    
    
    
    // TODO add your handling code here:
        }                                          
    
        private void BtnmenuActionPerformed(java.awt.event.ActionEvent evt) {                                        
              Menu menu = new Menu();
        menu.setVisible(true);
      
        this.dispose();
        }                                       
           private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey) + searchKey.length();
        int endIndex;
        
        // Verificar si el valor es un número (sin comillas) o una cadena (con comillas)
        if (json.charAt(startIndex) == '\"') {  // Es una cadena
            startIndex += 1;  // Saltar la comilla inicial
            endIndex = json.indexOf("\"", startIndex);
        } else {  // Es un número
            endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) { // Puede ser el último elemento sin coma
                endIndex = json.indexOf("}", startIndex);
            }
        }
    
        if (startIndex != -1 && endIndex != -1) {
            return json.substring(startIndex, endIndex);
        }
        return ""; // Retorna cadena vacía si no se encuentra el valor
    }
    
    
    
    
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(editarEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(editarEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(editarEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(editarEventos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
    
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new editarEventos().setVisible(true);
                }
            });
        }
    
        // Variables declaration - do not modify                     
        private javax.swing.JButton Btnbuscar;
        private javax.swing.JButton Btnguardar;
        private javax.swing.JToggleButton Btnmenu;
        private javax.swing.JLabel Jcosto;
        private javax.swing.JLabel Jfecha;
        private javax.swing.JLabel Jhora;
        private javax.swing.JLabel Jid;
        private javax.swing.JLabel Jlugar;
        private javax.swing.JLabel Jnombre;
        private javax.swing.JTextField Tcosto;
        private javax.swing.JTextField Tfecha;
        private javax.swing.JTextField Thora;
        private javax.swing.JTextField Tid;
        private javax.swing.JTextField Tlugar;
        private javax.swing.JTextField Tnombre;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        // End of variables declaration                   
    }

![enter image description here](https://i.postimg.cc/m2qn4vQz/Modificar.jpg)

## Eliminar.java

     package com.yovany.eventos.ui;
        
        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
    import javax.swing.JOptionPane;
    
    /**
     *
     * @author DELL
     */
    public class Eliminar extends javax.swing.JFrame {
    
        /**
         * Creates new form Eliminar
         */
        public Eliminar() {
            initComponents();
              // Añadir acción al botón buscar evento
            botonbuscarevento.addActionListener(evt -> cargarEvento());
            
            // Añadir acción al botón eliminar
            botoneliminar.addActionListener(evt -> eliminarEvento());
            
        }
         private void cargarEvento() {
             String id = Tid.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingresa un ID.");
                return;
            }
    
            String url = "http://localhost:8080/eventos/" + id;
    
            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
    
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
    
                    String jsonResponse = response.toString();
    
                    // Llenar los campos de texto con los datos del evento
                    Tnombre.setText(extractValue(jsonResponse, "nombre"));
                    Tlugar.setText(extractValue(jsonResponse, "lugar"));
                    Tfecha.setText(extractValue(jsonResponse, "fecha"));
                    Thora.setText(extractValue(jsonResponse, "hora"));
    
                    // Bloquear los campos para que no se puedan modificar
                    Tnombre.setEditable(false);
                    Tlugar.setEditable(false);
                    Tfecha.setEditable(false);
                    Thora.setEditable(false);
    
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    JOptionPane.showMessageDialog(this, "ID inválido. Evento no encontrado.");
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al buscar el evento.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
            }
        }
    
        private void eliminarEvento() {
           String id = Tid.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingresa un ID.");
                return;
            }
    
            String url = "http://localhost:8080/eventos/" + id;
    
            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("DELETE");
    
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    JOptionPane.showMessageDialog(this, "Evento eliminado con éxito.");
                    limpiarCampos();
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    JOptionPane.showMessageDialog(this, "Evento no encontrado.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el evento.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
            }
        }
          private void limpiarCampos() {
            Tid.setText("");
            Tnombre.setText("");
            Tlugar.setText("");
            Tfecha.setText("");
            Thora.setText("");
        }
            private String extractValue(String json, String key) {
         String searchKey = "\"" + key + "\":\"";
            int startIndex = json.indexOf(searchKey) + searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            if (startIndex != -1 && endIndex != -1) {
                return json.substring(startIndex, endIndex);
            }
            return ""; // Retorna cadena vacía si no se encuentra el valor
        }
        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
    
            bg = new javax.swing.JPanel();
            tituloeliminar = new javax.swing.JLabel();
            id = new javax.swing.JLabel();
            nombreevento = new javax.swing.JLabel();
            lugarevento = new javax.swing.JLabel();
            fechaevento = new javax.swing.JLabel();
            horaevento = new javax.swing.JLabel();
            Thora = new javax.swing.JTextField();
            Tid = new javax.swing.JTextField();
            Tnombre = new javax.swing.JTextField();
            Tlugar = new javax.swing.JTextField();
            Tfecha = new javax.swing.JTextField();
            jPanel1 = new javax.swing.JPanel();
            botoneliminar = new javax.swing.JToggleButton();
            botonbuscarevento = new javax.swing.JToggleButton();
            Btnmenu = new javax.swing.JToggleButton();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
    
            bg.setLayout(null);
    
            tituloeliminar.setFont(new java.awt.Font("Sylfaen", 0, 18)); // NOI18N
            tituloeliminar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            tituloeliminar.setText("ELIMINAR");
            bg.add(tituloeliminar);
            tituloeliminar.setBounds(268, 26, 104, 47);
    
            id.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            id.setText("ID");
            bg.add(id);
            id.setBounds(39, 88, 94, 36);
    
            nombreevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            nombreevento.setText("Nombre Evento");
            bg.add(nombreevento);
            nombreevento.setBounds(39, 130, 158, 32);
    
            lugarevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            lugarevento.setText("Lugar del Evento");
            bg.add(lugarevento);
            lugarevento.setBounds(40, 170, 169, 32);
    
            fechaevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            fechaevento.setText("Fecha del Evento");
            bg.add(fechaevento);
            fechaevento.setBounds(40, 210, 190, 30);
    
            horaevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            horaevento.setText("Hora del Evento");
            bg.add(horaevento);
            horaevento.setBounds(40, 250, 170, 30);
            bg.add(Thora);
            Thora.setBounds(260, 250, 260, 22);
            bg.add(Tid);
            Tid.setBounds(260, 90, 260, 22);
            bg.add(Tnombre);
            Tnombre.setBounds(260, 130, 260, 22);
            bg.add(Tlugar);
            Tlugar.setBounds(260, 170, 260, 22);
            bg.add(Tfecha);
            Tfecha.setBounds(260, 210, 260, 22);
    
            jPanel1.setBackground(new java.awt.Color(0, 153, 153));
    
            botoneliminar.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            botoneliminar.setText("Eliminar");
            botoneliminar.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
    
            botonbuscarevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            botonbuscarevento.setText("Buscar Evento");
            botonbuscarevento.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            botonbuscarevento.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    botonbuscareventoActionPerformed(evt);
                }
            });
    
            Btnmenu.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            Btnmenu.setText("Menu");
            Btnmenu.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btnmenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnmenuActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(40, 40, 40)
                    .addComponent(botoneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                    .addComponent(botonbuscarevento, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(119, 119, 119)
                    .addComponent(Btnmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(52, 52, 52))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(botoneliminar)
                        .addComponent(botonbuscarevento)
                        .addComponent(Btnmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(33, Short.MAX_VALUE))
            );
    
            bg.add(jPanel1);
            jPanel1.setBounds(0, 390, 770, 100);
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
            );
    
            pack();
        }// </editor-fold>                        
    
        private void botonbuscareventoActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        cargarEvento();
        }                                                 
    
        private void BtnmenuActionPerformed(java.awt.event.ActionEvent evt) {                                        
        Menu menu = new Menu();
        menu.setVisible(true);
      
        this.dispose();
            
        }                                       
    
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Eliminar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(Eliminar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(Eliminar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(Eliminar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
    
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Eliminar().setVisible(true);
                }
            });
        }
    
        // Variables declaration - do not modify                     
        private javax.swing.JToggleButton Btnmenu;
        private javax.swing.JTextField Tfecha;
        private javax.swing.JTextField Thora;
        private javax.swing.JTextField Tid;
        private javax.swing.JTextField Tlugar;
        private javax.swing.JTextField Tnombre;
        private javax.swing.JPanel bg;
        private javax.swing.JToggleButton botonbuscarevento;
        private javax.swing.JToggleButton botoneliminar;
        private javax.swing.JLabel fechaevento;
        private javax.swing.JLabel horaevento;
        private javax.swing.JLabel id;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JLabel lugarevento;
        private javax.swing.JLabel nombreevento;
        private javax.swing.JLabel tituloeliminar;
        // End of variables declaration                   
    }
        
![enter image description here](https://i.postimg.cc/hvtY4gyx/Eliminar.jpg)

## Invitados.java

    package com.yovany.eventos.ui;
    
    import com.yovany.eventos.entity.Invitado;
    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.nio.charset.StandardCharsets;
    import javax.swing.JOptionPane;
    
    /**
     *
     * @author DELL
     */
    public class Invitados extends javax.swing.JFrame {
    
        /**
         * Creates new form Invitados
         */
        public Invitados() {
            initComponents();
            cargarEventos(); 
            // Crear el filtro numérico
            
        }
        private void cargarEventos() {
        try {
            // Conexión al endpoint que devuelve solo los IDs de los eventos
            URL url = new URL("http://localhost:8080/eventos/ids");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
    
            // Leer la respuesta como una lista de IDs
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
    
            // Procesar la respuesta sin dependencias de JSON
            String[] ids = response.toString()
                                   .replace("[", "")  // Eliminar corchetes
                                   .replace("]", "")  // Eliminar corchetes
                                   .split(",");       // Dividir por comas
    
            // Limpiar el JComboBox antes de añadir los nuevos valores
            Jeventos.removeAllItems();
    
            // Añadir cada ID de evento al JComboBox
            for (String id : ids) {
                Jeventos.addItem(id.trim());
            }
    
            conn.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los eventos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
    
            jPanel1 = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            Jnombreinv = new javax.swing.JLabel();
            jtelefonoinv = new javax.swing.JLabel();
            Tnombreinv = new javax.swing.JTextField();
            Ttelefonoinv = new javax.swing.JTextField();
            Jidentificacion = new javax.swing.JLabel();
            Tidentifiacioninv = new javax.swing.JTextField();
            jidevento = new javax.swing.JLabel();
            Jeventos = new javax.swing.JComboBox<>();
            jPanel2 = new javax.swing.JPanel();
            Btninvitado = new javax.swing.JButton();
            Btnmenu = new javax.swing.JToggleButton();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
    
            jLabel1.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Invitados");
    
            Jnombreinv.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            Jnombreinv.setText("Nombre");
    
            jtelefonoinv.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            jtelefonoinv.setText("Telefono");
    
            Jidentificacion.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            Jidentificacion.setText("Identificacion");
    
            jidevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            jidevento.setText("ID del Evento");
    
            Jeventos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
            Jeventos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    JeventosActionPerformed(evt);
                }
            });
    
            jPanel2.setBackground(new java.awt.Color(0, 153, 153));
    
            Btninvitado.setText("Guardar Invitado");
            Btninvitado.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btninvitado.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtninvitadoActionPerformed(evt);
                }
            });
    
            Btnmenu.setText("Menu");
            Btnmenu.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            Btnmenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnmenuActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(91, 91, 91)
                    .addComponent(Btninvitado, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                    .addComponent(Btnmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(54, 54, 54))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Btninvitado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Btnmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(37, Short.MAX_VALUE))
            );
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Jnombreinv, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jtelefonoinv)
                                .addComponent(Jidentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jidevento, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(123, 123, 123)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(Tnombreinv)
                                .addComponent(Ttelefonoinv)
                                .addComponent(Tidentifiacioninv)
                                .addComponent(Jeventos, 0, 240, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(307, 307, 307)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1, 1, 1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Jidentificacion)
                                .addComponent(Tidentifiacioninv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(27, 27, 27)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Jnombreinv)
                                .addComponent(Tnombreinv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jtelefonoinv)
                                .addComponent(Ttelefonoinv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(67, 67, 67))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jidevento, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Jeventos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
    
            pack();
        }// </editor-fold>                        
    
        private void BtninvitadoActionPerformed(java.awt.event.ActionEvent evt) {                                            
     String identificacion = Tidentifiacioninv.getText().replace(",", "");
        String nombre = Tnombreinv.getText();
        String telefono = Ttelefonoinv.getText().replace(",", "");
    
        if (identificacion.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int identificacionInt = 0;
        int telefonoInt = 0;
    
        try {
            identificacionInt = Integer.parseInt(identificacion);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Identificación debe ser un número entero sin punto decimal.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            telefonoInt = Integer.parseInt(telefono);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Teléfono debe ser un número entero sin punto decimal.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Obtener el evento_id seleccionado del JComboBox
        String eventoIdSeleccionado = (String) Jeventos.getSelectedItem();
        int eventoIdInt = Integer.parseInt(eventoIdSeleccionado); // Convertir a int para el JSON
    
        Invitado invitado = new Invitado();
        invitado.setIdentificacion(identificacionInt);
        invitado.setNombreinvitado(nombre);
        invitado.setTelefonoinvitado(telefonoInt);
    
        try {
            URL url = new URL("http://localhost:8080/invitados");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
    
            // Incluir evento_id en el JSON
            String jsonInputString = String.format("{\"identificacion\":%d,\"nombreinvitado\":\"%s\",\"telefonoinvitado\":%d,\"evento\":{\"id\":%d}}",
                    invitado.getIdentificacion(), invitado.getNombreinvitado(), invitado.getTelefonoinvitado(), eventoIdInt);
    
            try (var os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
    
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK || conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                JOptionPane.showMessageDialog(this, "Invitado guardado exitosamente");
                Tidentifiacioninv.setText("");
                Tnombreinv.setText("");
                Ttelefonoinv.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el invitado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        }                                           
    
        private void JeventosActionPerformed(java.awt.event.ActionEvent evt) {                                         
            // TODO add your handling code here:
        }                                        
    
        private void BtnmenuActionPerformed(java.awt.event.ActionEvent evt) {                                        
            Menu menu = new Menu();
        menu.setVisible(true);
      
        this.dispose();
        }                                       
    
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Invitados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(Invitados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(Invitados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(Invitados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
    
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Invitados().setVisible(true);
                }
            });
        }
    
        // Variables declaration - do not modify                     
        private javax.swing.JButton Btninvitado;
        private javax.swing.JToggleButton Btnmenu;
        private javax.swing.JComboBox<String> Jeventos;
        private javax.swing.JLabel Jidentificacion;
        private javax.swing.JLabel Jnombreinv;
        private javax.swing.JTextField Tidentifiacioninv;
        private javax.swing.JTextField Tnombreinv;
        private javax.swing.JTextField Ttelefonoinv;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JLabel jidevento;
        private javax.swing.JLabel jtelefonoinv;
        // End of variables declaration                   
    }

![enter image description here](https://i.postimg.cc/gkr7fz6b/Invitador.jpg)

```

