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

    public Alquiler() {
        // asigna un ID aleatorio, LocalDateTime.MAX.truncatedTo(ChronoUnit.SECONDS)al inicio, 
        // LocalDateTime.MIN.truncatedTo(ChronoUnit.SECONDS) al final
        // Un socio creado con el constructor por defecto y
        // Una CanchaMultiproposito
    }

    public Alquiler(String id, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Socio socio, InstalacionDeportiva instalacionDeportiva) { }

    public Alquiler(JSONObject json) {
        // tener en cuenta el uso de LocalDateTime.parse() y de
        // InstalacionDeportiva.getInstance(...)
    }

    public Alquiler(Alquiler alquiler) { }

    public String getId() { }

    public void setId(String id) {
        // el ID no puede ser nulo, estar en blanco o tener una longitud distinta de 5
    }

    public LocalDateTime getFechaHoraInicio() { }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        // lanzar NullPointerException si la fecha es nula
        // lanzar IllegalArgumentException si no se cumple que La fecha y hora de inicio debe ser menor que la fecha y hora de fin.
        // truncar a minutos
    }

    public LocalDateTime getFechaHoraFin() { }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        // lanzar un NullPointerException si fechaHoraInicio es null
        // lanzar un IllegalArgumentException si La fecha y hora de fin debe ser mayor que la fecha y hora de inicio.
        // truncar a minutos
    }

    public Socio getSocio() { }

    public void setSocio(Socio socio) {
        // el socio no puede ser null
    }

    public InstalacionDeportiva getInstalacionDeportiva() {  }

    public void setInstalacionDeportiva(InstalacionDeportiva instalacionDeportiva) {
        // la instalacionDeportiva no puede ser null
    }

    public long getHoras() { }

    public double getValorAlquiler() { }

    @Override
    public boolean equals(Object obj) {
        // dos alquileres son iguales si tienen el mismo ID o también
        // si tienen la misma fechaHoraInicio, fechaHoraFin, socio e instalacionDeportiva

    }

    public JSONObject toJSONObject() { }

    @Override
    public String toString() {  }

}
