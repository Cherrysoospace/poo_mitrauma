package poo.model;

import org.json.JSONObject;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;

//¿Cuándo es viable usar ZonedDateTime?

public class Alquiler {
    protected String id;
    protected Instalacion instalacion;
    protected Socio socio;
    protected LocalDateTime fechaHoraInicio;
    protected LocalDateTime fechaHoraFin;

    //CONSTRUCTOR POR DEFECTO
    public Alquiler () {
        this.id=("00000");
        //setInstalacion(null); ??
        this.socio = new Socio();
        this.fechaHoraInicio = LocalDateTime.now();
        this.fechaHoraFin = LocalDateTime.now().plusHours(1);
    }



    //CONSTRUCTOR COPIA
    public Alquiler (Alquiler a) {
        this.id = a.id;
        //this.instalacion = a.instalacion; //REVISAR EN MONITORIA
        this.socio = new Socio(a.socio);
        this.fechaHoraInicio = a.fechaHoraInicio;
        this.fechaHoraFin = a.fechaHoraFin;
    }
    
    //CONSTRUCTOR PARAMETRIZADO
    public Alquiler (String id, Instalacion instalacion, Socio socio, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        setId(id);
        setInstalacion(instalacion);
        setSocio(socio);
        setFechaHoraInicio(fechaHoraInicio);
        setFechaHoraFin(fechaHoraFin);
    }

    //JSON
    public Alquiler(JSONObject json) {
        this.id = json.getString("id");
        this.socio = new Socio(json.getJSONObject("socio"));
        this.fechaHoraInicio = LocalDateTime.parse(json.getString("fechaHoraInicio"));
        this.fechaHoraFin = LocalDateTime.parse(json.getString("fechaHoraFin"));
        // Note: The instalacion will be set after construction
    }

    //ACCESORES
    public String getId () {
        return id;
    }

    public Instalacion getInstalacion () {
        return instalacion;
    }

    public Socio getSocio () {
        return socio;
    }

    public LocalDateTime getFechaHoraInicio () {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin () {
        return fechaHoraFin;
    }
    
    //MUTADORES

    public void setId (String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id no puede estar vacío.");
        }

        if (id.length() !=5) {
            throw new IllegalArgumentException("El id de Alquiler debe tener exactamente 5 caracteres");
        }
        this.id = id;
    }

    public void setInstalacion (Instalacion instalacion) {
        if (instalacion == null) {
            throw new IllegalArgumentException("La instalación no puede ser nula.");
        }
        this.instalacion = instalacion;
    }

    public void setSocio (Socio socio) {
        if (socio == null) {
            throw new IllegalArgumentException("El socio no puede ser nulo.");
        }
        this.socio = socio;
    }

    public void setFechaHoraInicio (LocalDateTime fechaHoraInicio) {
        if (fechaHoraInicio == null) {
            throw new IllegalArgumentException("La fecha y hora de inicio no pueden ser nulas.");
        }
        long diferencia = fechaHoraInicio.getHour() - LocalDateTime.now().getHour();

        if (diferencia > 24) {
            throw new DateTimeException("La fecha y hora de inicio del alquiler no pueden ser realizadas más de 24 horas en el futuro.");
        }

        if (fechaHoraInicio.isBefore(LocalDateTime.now())) {
            throw new DateTimeException("La fecha y hora de inicio no pueden ser previas a la fecha y hora actual.");
        }

        LocalTime inicio = LocalTime.of(6, 0); // 6:00 AM
        
        if (fechaHoraInicio.toLocalTime().isBefore(inicio)) {
            throw new DateTimeException("La fecha y hora de inicio no pueden ser antes de la 6AM.");
        }

        LocalTime fin = LocalTime.of(22, 0);  // 10:00 PM
        if (fechaHoraInicio.toLocalTime().isAfter(fin)) {
            throw new DateTimeException("La fecha y hora de inicio no pueden ser después de las 10PM.");
        }
        //DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); Puedo y debo implementar esto?
       ;
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public void setFechaHoraFin (LocalDateTime fechaHoraFin) {
        if (fechaHoraFin == null) {
            throw new IllegalArgumentException("La fecha y hora de fin no pueden ser nulas.");
        }

        if (fechaHoraFin.isBefore(fechaHoraInicio)) {
            throw new DateTimeException("La fecha y hora de fin no pueden ser previas a la fecha y hora de inicio.");
        }

        LocalTime inicio = LocalTime.of(6, 0); // 6:00 AM
        if (fechaHoraFin.toLocalTime().isBefore(inicio)) {
            throw new DateTimeException("La fecha y hora de fin no pueden ser antes de las 6AM.");
        }

        LocalTime fin = LocalTime.of(22, 0);  // 10:00 PM
        if (fechaHoraFin.toLocalTime().isAfter(fin)) {
            throw new DateTimeException("La fecha y hora de fin no pueden ser después de las 10PM.");
        }
        this.fechaHoraFin = fechaHoraFin;
    }

    //COMPORTAMIENTO
    public int getHorasCalculadas () {
        int hora = fechaHoraFin.getHour() - fechaHoraInicio.getHour();
        if (hora < 1) {
            throw new DateTimeException("El alquiler de una instalación debe ser de al menos 1 hora.");
        }

        return hora;
    }

    //Añadir un format para las horas del documento para el momento que se exporten al JSON
    //Crear un comportamiento que lleve las h

    //TOSTRING MODIFICADO
    @Override
    public String toString() {
        return String.format("ID de Alquiler: %s\nInstalación: %s\nSocio: %s\nFecha y hora de inicio: %s\nFecha y hora de fin: %s\n", 
                id, instalacion, socio, fechaHoraInicio, fechaHoraFin);
    }

    //EQUALS MODIFICADO
    @Override
    public boolean equals(Object obj) {
        // si obj es null, retorna falso
        // si el tipo de datos != Persona, retorna falso
        // si

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        Alquiler a = (Alquiler) obj;

        return this.id.equals(a.id);
    }
}
