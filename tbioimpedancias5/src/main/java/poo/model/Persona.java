package poo.model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;


import org.json.JSONObject;

public class Persona {
    private String identificacion;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private char sexo;
    private String correo;

     // Constructor con valores por defecto
     public Persona() {
        this.identificacion = "00000";
        this.nombreCompleto = "NN";
        this.fechaNacimiento = LocalDate.of(2000, 1, 1);
        this.sexo = 'M';
        this.correo = "default@email.com";
    }

    // Constructor principal con inicialización correcta
    public Persona(String identificacion, String nombreCompleto, LocalDate fechaNacimiento, char sexo, String correo) {
        this.identificacion = identificacion != null ? identificacion : "00000";
        this.nombreCompleto = nombreCompleto != null ? nombreCompleto : "Nombre Predeterminado";
        this.fechaNacimiento = fechaNacimiento != null ? fechaNacimiento : LocalDate.of(2000, 1, 1);
        this.sexo = (sexo == 'M' || sexo == 'F') ? sexo : 'M';
        this.correo = (correo != null && correo.contains("@")) ? correo : "default@email.com";
    }

    // Constructor desde JSON
    public Persona(JSONObject json) {
        this(
            json.optString("identificacion", "00000"),
            json.optString("nombreCompleto", "Nombre Predeterminado"),
            json.has("fechaNacimiento") ? LocalDate.parse(json.getString("fechaNacimiento")) : LocalDate.of(2000, 1, 1),
            json.has("sexo") ? json.getString("sexo").charAt(0) : 'M',
            json.optString("correo", "default@email.com")
        );
    }
    
    public Persona (String identificaion) {
       this();
       setIdentificacion(identificaion);
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.isBlank() || !identificacion.matches("[a-zA-Z0-9]{5}")) {
            throw new IllegalArgumentException("La identificación debe tener exactamente 5 caracteres alfanuméricos.");
        }
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El nombre debe tener al menos un caracter.");
        }
        this.nombreCompleto = nombreCompleto;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) throws DateTimeException {
        LocalDate fechaActual = LocalDate.now();
        int diferenciaAnios = Period.between(fechaNacimiento, fechaActual).getYears();

        if (diferenciaAnios > 120) {
            throw new DateTimeException("Error: La fecha de nacimiento no puede ser mayor a 120 años en el pasado.");
        }

        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public int getEdadEstimada() {
        LocalDate fechaActual = LocalDate.now();
        return Period.between(fechaNacimiento, fechaActual).getYears();
    }

    public char getSexo() {
        sexo = Character.toUpperCase(sexo);
        return sexo;
    }

    public void setSexo(char sexo) {
        if ((sexo != 'M' && sexo != 'F')) {
            throw new IllegalArgumentException("El dato solo puede ser 'M' o 'F'");
        }
        this.sexo = sexo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (correo == null || correo.isBlank() || !correo.contains("@")) {
            throw new IllegalArgumentException("El correo debe tener al menos un caracter, un @ y un .com.");
        }
        this.correo = correo;
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    @Override
    public String toString() {
        return String.format("%-10s %-40s %-15s %-2d %-10s %-30s",
                identificacion, nombreCompleto, fechaNacimiento, getEdadEstimada(),
                (sexo == 'M' ? "Masculino" : "Femenino"), correo);
    }
    @Override
    public boolean equals (Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        Persona p = (Persona) obj;
      
        return this.identificacion.equals(p.identificacion);
    }

    public String toStringDetallado() {
        return String.format("\nIdentificación: %s\nNombre: %s\nFecha de nacimiento: %s\nEdad: %d años\nSexo: %s\nCorreo: %s\n",
                identificacion, nombreCompleto, fechaNacimiento, getEdadEstimada(),
                (sexo == 'M' ? "Masculino" : "Femenino"), correo);
    }
}
