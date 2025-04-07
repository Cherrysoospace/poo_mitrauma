package poo.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.json.JSONObject;
import poo.helpers.Utils;

public class Alquiler {

    private String id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Socio socio;
    private InstalacionDeportiva instalacionDeportiva;

    // CONSTRUCTOR POR DEFECTO
    public Alquiler() {
        setId(Utils.getRandomKey(5));
        setFechaHoraInicio(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        setFechaHoraFin(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MINUTES));
        // asigna un ID aleatorio, LocalDateTime.MAX.truncatedTo(ChronoUnit.SECONDS)al
        // inicio,
        // LocalDateTime.MIN.truncatedTo(ChronoUnit.SECONDS) al final

        // Un socio creado con el constructor por defecto y
        setSocio(new Socio());
        // Una CanchaMultiproposito
        setInstalacionDeportiva(new CanchaMultiproposito());

    }

    // CONSTRUCTOR PARAMETRIZADO
    public Alquiler(String id, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Socio socio,
            InstalacionDeportiva instalacionDeportiva) {
        setId(id);
        setFechaHoraInicio(fechaHoraInicio);
        setFechaHoraFin(fechaHoraFin);
        setSocio(socio);
        setInstalacionDeportiva(instalacionDeportiva);
    }

    // JSON
    public Alquiler(JSONObject json) {
        this(
                json.getString("id"),
                LocalDateTime.parse(json.getString("fechaHoraInicio")),
                LocalDateTime.parse(json.getString("fechaHoraFin")),
                new Socio(json.getJSONObject("socio")),
                InstalacionDeportiva.getInstance(json.getJSONObject("instalacionDeportiva")));
        // tener en cuenta el uso de LocalDateTime.parse() y de
        // InstalacionDeportiva.getInstance(...)
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    // CONSTRUCTOR COPIA
    public Alquiler(Alquiler a) {
        this(
                a.id,
                a.fechaHoraInicio,
                a.fechaHoraFin,
                a.socio,
                a.instalacionDeportiva);
    }

    // ACCESORES
    public String getId() {
        return id;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public Socio getSocio() {
        return socio;
    }

    public InstalacionDeportiva getInstalacionDeportiva() {
        return instalacionDeportiva;
    }

    /// MUTADORES
    public void setId(String id) {
        // el ID no puede ser nulo, estar en blanco o tener una longitud distinta de 5
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede ser nulo o estar en blanco.");
        }

        if (id.length() != 5) {
            throw new IllegalArgumentException("El ID debe tener exactamente 5 caracteres.");
        }
        this.id = id;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        // lanzar NullPointerException si la fecha es nula
        if (fechaHoraInicio == null) {
            throw new NullPointerException("La fecha y hora de inicio no puede ser nula.");
        }
        // lanzar IllegalArgumentException si fechaHoraFin ya está establecido y no se
        // cumple la condición
        if (fechaHoraFin != null && fechaHoraInicio.isAfter(fechaHoraFin)) {
            throw new IllegalArgumentException("La fecha y hora de inicio debe ser menor que la fecha y hora de fin.");
        }
        // truncar a minutos
        this.fechaHoraInicio = fechaHoraInicio.truncatedTo(ChronoUnit.MINUTES);
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        // lanzar un NullPointerException si fechaHoraFin es null
        if (fechaHoraFin == null) {
            throw new NullPointerException("La fecha y hora de fin no puede ser nula.");
        }
        // lanzar un IllegalArgumentException si fechaHoraInicio ya está establecido y
        // no se cumple la condición
        if (fechaHoraInicio != null && fechaHoraFin.isBefore(fechaHoraInicio)) {
            throw new IllegalArgumentException("La fecha y hora de fin debe ser mayor que la fecha y hora de inicio.");
        }
        // truncar a minutos
        this.fechaHoraFin = fechaHoraFin.truncatedTo(ChronoUnit.MINUTES);
    }

    public void setSocio(Socio socio) {
        // el socio no puede ser null
        if (socio == null) {
            throw new NullPointerException("El socio no puede ser nulo.");
        }
        this.socio = socio; // Add this line
    }

    public void setInstalacionDeportiva(InstalacionDeportiva instalacionDeportiva) {
        // la instalacionDeportiva no puede ser null
        if (instalacionDeportiva == null) {
            throw new NullPointerException("La instalación deportiva no puede ser nula.");
        }
        this.instalacionDeportiva = instalacionDeportiva; // Add this line
    }

    // COMPORTAMIENTOS
    public long getHoras() {
        long horas = fechaHoraFin.getHour() - fechaHoraInicio.getHour();
        return horas;
    }

    public double getValorAlquiler() {
        double valorAlquiler;
        valorAlquiler = instalacionDeportiva.getValorHora() * getHoras();
        return valorAlquiler;
    }

    @Override
    public boolean equals(Object obj) {
        // dos alquileres son iguales si tienen el mismo ID o también
        // si tienen la misma fechaHoraInicio, fechaHoraFin, socio e
        // instalacionDeportiva

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

        return this.id.equals(a.id) && this.fechaHoraInicio.equals(a.fechaHoraInicio)
                && this.fechaHoraFin.equals(a.fechaHoraFin) && this.socio.equals(a.socio)
                && this.instalacionDeportiva.equals(a.instalacionDeportiva);
    }

    @Override
    public String toString() {
        return String.format("Id Alquiler: %s\n", getId()) +
                String.format("Fecha y Hora Inicio: %s\n", getFechaHoraInicio()) +
                String.format("Fecha y Hora Fin: %s\n", getFechaHoraFin()) +
                String.format("Socio: %s\n", getSocio()) +
                String.format("Instalacion Deportiva: %s\n", getInstalacionDeportiva()) +
                String.format("Valor Alquiler: %.2f\n", getValorAlquiler());
    }

}