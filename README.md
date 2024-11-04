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
    }    

**Esta clase es un controlador REST en Spring Boot que define varias rutas para gestionar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los eventos. Esta clase utiliza el servicio EventoService para interactuar con la base de datos.**

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
    
        public void setLugar(String lugar) {
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

**Esta clase define claramente la estructura de un evento en términos de atributos y su mapeo a una base de datos. Es la base para realizar operaciones CRUD a través de EventoRepository y EventoService.**

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
                // Setear otros campos necesarios
                return eventoRepository.save(evento);
            }).orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
        }
    
        // Eliminar un evento por ID
        public void deleteById(Long id) {
            eventoRepository.deleteById(id);
        }
    }    

**Es una clase de servicio en la arquitectura Spring, la cual encapsula la lógica de negocio para manejar la entidad Evento. Esta clase se comunica con la base de datos a través de EventoRepository. EventoService actúa como intermediario entre el controlador (EventoController) y el repositorio (EventoRepository).**

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

## Menu.java

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
            Eventos = new javax.swing.JButton();
    
            jToggleButton1.setText("jToggleButton1");
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
            Eventos.setText("Eventos");
            Eventos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    EventosActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(140, 140, 140)
                    .addComponent(Eventos)
                    .addContainerGap(188, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(61, 61, 61)
                    .addComponent(Eventos)
                    .addContainerGap(216, Short.MAX_VALUE))
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
        RegistroEventos registroEventos = new RegistroEventos();
        registroEventos.setVisible(true);
        
        // Opcional: cerrar o esconder el formulario actual (Menu) si lo deseas
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
        private javax.swing.JButton Eventos;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JToggleButton jToggleButton1;
        // End of variables declaration                   
    }

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
            GuardarEvento = new javax.swing.JButton();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
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
    
            GuardarEvento.setText("Guardar Evento");
            GuardarEvento.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    GuardarEventoActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(226, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(86, 86, 86))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(67, 67, 67)
                    .addComponent(GuardarEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(42, 42, 42)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(LugarEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(NombreEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FechaEvento, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addComponent(HoraEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(35, 35, 35)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(FechaEbento)
                        .addComponent(NombreEbento, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(HoraEbento, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(LugarEbento))
                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel1)
                    .addGap(83, 83, 83)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(NombreEvento)
                        .addComponent(NombreEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(23, 23, 23)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(LugarEvento)
                        .addComponent(LugarEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(FechaEvento)
                        .addComponent(FechaEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(HoraEvento)
                        .addComponent(HoraEbento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(30, 30, 30)
                    .addComponent(GuardarEvento)
                    .addGap(21, 21, 21))
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
        if (NombreEbento.getText().isEmpty() || FechaEbento.getText().isEmpty() || HoraEbento.getText().isEmpty() || LugarEbento.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos");
            GuardarEvento.setEnabled(true); // Rehabilitar el botón si hay error
            return; // Sale si hay un error
        }
        nuevoEvento.setNombre(NombreEbento.getText());
        nuevoEvento.setLugar(LugarEbento.getText());
        
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
                 "{\"nombre\":\"%s\", \"fecha\":\"%s\", \"hora\":\"%s\", \"lugar\":\"%s\"}",
                    nuevoEvento.getNombre(), nuevoEvento.getFecha(), nuevoEvento.getHora(), nuevoEvento.getLugar()
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
                
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el evento");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        } finally {
            GuardarEvento.setEnabled(true); // Rehabilitar el botón después de intentar guardar
        }
        
    
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
        // End of variables declaration                   
    }

![enter image description here](https://i.postimg.cc/g0gs01Jt/Registro.jpg)

## busquedaEventos.java

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
            Btnbusqueda = new javax.swing.JButton();
            Tid = new javax.swing.JTextField();
            Jlugar = new javax.swing.JLabel();
            Jfecha = new javax.swing.JLabel();
            Jhora = new javax.swing.JLabel();
            Tlugar = new javax.swing.JTextField();
            Tfecha = new javax.swing.JTextField();
            Thora = new javax.swing.JTextField();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
            Jcodigo.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jcodigo.setText("Codigo");
    
            Jnombre.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            Jnombre.setText("Nombre Evento");
    
            Tnombre.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TnombreActionPerformed(evt);
                }
            });
    
            Btnbusqueda.setText("BUSQUEDA EVENTO");
            Btnbusqueda.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnbusquedaActionPerformed(evt);
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
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(68, 68, 68)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Jnombre)
                                .addComponent(Jcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Jlugar)
                                .addComponent(Jfecha)
                                .addComponent(Jhora))
                            .addGap(79, 79, 79)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Tnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tid, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tlugar, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Thora, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(58, 58, 58)
                            .addComponent(Btnbusqueda)))
                    .addContainerGap(29, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(41, 41, 41)
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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                    .addComponent(Btnbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(63, 63, 63))
            );
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
    
            pack();
        }// </editor-fold>                        
    
        private void TnombreActionPerformed(java.awt.event.ActionEvent evt) {                                        
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
    
                // Bloquear los campos para que no se puedan modificar
                Tnombre.setEditable(false);
                Thora.setEditable(false);
                Tfecha.setEditable(false);
                Tlugar.setEditable(false);
    
                // Mantener el campo de ID como editable para nuevas búsquedas
                Tid.setEditable(true);  
    
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // Si el evento no se encuentra, mostrar el mensaje
                JOptionPane.showMessageDialog(this, "ID no encontrado. Por favor, intente con otro.");
                Tnombre.setText("");
                Thora.setText("");
                Tfecha.setText("");
                Tlugar.setText("");
            } else {
                // Limpiar los campos si no se encuentra el evento
                
                JOptionPane.showMessageDialog(this, "Error al buscar el evento. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar el evento: " + e.getMessage());
        }
     
         
             
        
             
             
        }                                           
            // Método auxiliar para extraer valores del JSON
            private String extractValue(String json, String key) {
            String searchKey = "\"" + key + "\":\"";
            int startIndex = json.indexOf(searchKey) + searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            if (startIndex != -1 && endIndex != -1) {
            return json.substring(startIndex, endIndex);
        }
            return ""; // Retorna cadena vacía si no se encuentra el valor
    }
        
        
        
        private void TidActionPerformed(java.awt.event.ActionEvent evt) {                                    
            // TODO add your handling code here:
        }                                   
    
        private void TlugarActionPerformed(java.awt.event.ActionEvent evt) {                                       
            // TODO add your handling code here:
        }                                      
    
        private void TfechaActionPerformed(java.awt.event.ActionEvent evt) {                                       
            // TODO add your handling code here:
        }                                      
    
        private void ThoraActionPerformed(java.awt.event.ActionEvent evt) {                                      
            // TODO add your handling code here:
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
        private javax.swing.JLabel Jcodigo;
        private javax.swing.JLabel Jfecha;
        private javax.swing.JLabel Jhora;
        private javax.swing.JLabel Jlugar;
        private javax.swing.JLabel Jnombre;
        private javax.swing.JTextField Tfecha;
        private javax.swing.JTextField Thora;
        private javax.swing.JTextField Tid;
        private javax.swing.JTextField Tlugar;
        private javax.swing.JTextField Tnombre;
        private javax.swing.JPanel jPanel1;
        // End of variables declaration                   
    
    }

![enter image description here](https://i.postimg.cc/GpLzdbcs/Busqueda.jpg)

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
            Btnbuscar = new javax.swing.JButton();
            Btnguardar = new javax.swing.JButton();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
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
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
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
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(Jhora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Jlugar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Jfecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Tlugar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tfecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Thora, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(33, 33, 33))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(53, 53, 53)
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
                    .addContainerGap(55, Short.MAX_VALUE))
            );
    
            Btnbuscar.setText("BUSCAR");
            Btnbuscar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnbuscarActionPerformed(evt);
                }
            });
    
            Btnguardar.setText("ACTUALIZAR");
            Btnguardar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BtnguardarActionPerformed(evt);
                }
            });
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(63, 63, 63)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addComponent(Btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(Btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 27, Short.MAX_VALUE))
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
                
               
    
              } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // Si el evento no se encuentra, mostrar el mensaje
                JOptionPane.showMessageDialog(this, "ID no encontrado. Por favor, intente con otro.");
                Tnombre.setText("");
                Thora.setText("");
                Tfecha.setText("");
                Tlugar.setText("");
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
            
            if (idEvento.isEmpty() || nombreEvento.isEmpty() || fechaEvento.isEmpty() || horaEvento.isEmpty() || lugarEvento.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
                return;
            }
            
         // Crear objeto JSON con los datos actualizados del evento
            String jsonInputString = String.format(
                "{\"id\": \"%s\", \"nombre\": \"%s\", \"fecha\": \"%s\", \"hora\": \"%s\", \"lugar\": \"%s\"}",
                idEvento, nombreEvento, fechaEvento, horaEvento, lugarEvento
            );
            
            try {
            URL url = new URL("http://localhost:8080/eventos/" + idEvento);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
            
            // Enviar los datos en formato JSON
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
    
            // Verificar respuesta de la API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Evento actualizado correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el evento. Código de respuesta: " + responseCode);
            }
    
            } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar actualizar el evento.");
        }
    
      
    
    
    
    // TODO add your handling code here:
        }                                          
             // Método auxiliar para extraer valores del JSON
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
        private javax.swing.JLabel Jfecha;
        private javax.swing.JLabel Jhora;
        private javax.swing.JLabel Jid;
        private javax.swing.JLabel Jlugar;
        private javax.swing.JLabel Jnombre;
        private javax.swing.JTextField Tfecha;
        private javax.swing.JTextField Thora;
        private javax.swing.JTextField Tid;
        private javax.swing.JTextField Tlugar;
        private javax.swing.JTextField Tnombre;
        private javax.swing.JPanel jPanel1;
        // End of variables declaration                   
    }

![enter image description here](https://i.postimg.cc/G2NjFYvk/Modificar.jpg)

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
            botoneliminar = new javax.swing.JToggleButton();
            botonbuscarevento = new javax.swing.JToggleButton();
    
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
            nombreevento.setBounds(39, 130, 159, 32);
    
            lugarevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            lugarevento.setText("Lugar del Evento");
            bg.add(lugarevento);
            lugarevento.setBounds(40, 170, 173, 32);
    
            fechaevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            fechaevento.setText("Fecha del Evento");
            bg.add(fechaevento);
            fechaevento.setBounds(40, 210, 190, 30);
    
            horaevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            horaevento.setText("Hora del Evento");
            bg.add(horaevento);
            horaevento.setBounds(40, 250, 170, 30);
            bg.add(Thora);
            Thora.setBounds(260, 250, 260, 24);
            bg.add(Tid);
            Tid.setBounds(260, 90, 260, 24);
            bg.add(Tnombre);
            Tnombre.setBounds(260, 130, 260, 24);
            bg.add(Tlugar);
            Tlugar.setBounds(260, 170, 260, 24);
            bg.add(Tfecha);
            Tfecha.setBounds(260, 210, 260, 24);
    
            botoneliminar.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            botoneliminar.setText("Eliminar");
            bg.add(botoneliminar);
            botoneliminar.setBounds(50, 410, 120, 40);
    
            botonbuscarevento.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
            botonbuscarevento.setText("Buscar Evento");
            botonbuscarevento.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    botonbuscareventoActionPerformed(evt);
                }
            });
            bg.add(botonbuscarevento);
            botonbuscarevento.setBounds(230, 410, 200, 40);
    
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
        private javax.swing.JLabel lugarevento;
        private javax.swing.JLabel nombreevento;
        private javax.swing.JLabel tituloeliminar;
        // End of variables declaration                   
    }

![enter image description here](https://i.postimg.cc/V65R040f/Eliminar.jpg)








```
