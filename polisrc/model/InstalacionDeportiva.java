package poo.model;

import org.json.JSONObject;
import poo.helpers.Utils;

public abstract class InstalacionDeportiva {

    protected String id;
    protected double ancho;
    protected double largo;
    protected double valorHora;
    protected String descripcion;

    public InstalacionDeportiva() {
        // sólo un ID de 5 caracteres aleatorios y la descripción "Instalación deportiva sin descripción"
    }

    public InstalacionDeportiva(String id) { }

    public InstalacionDeportiva(String id, double ancho, double largo, double valorHora, String descripcion) { }

    public InstalacionDeportiva(JSONObject json) { }

    public InstalacionDeportiva(InstalacionDeportiva instalacion) { }

    public String getId() { }

    public void setId(String id) {  }

    public double getAncho() { }

    public void setAncho(double ancho) {
        // no aceptar anchos inferiores a 1.2 metros
        // si el ancho es mayor que el largo y largo es mayor a cero, generar un IllegalArgumentException
    }

    public double getLargo() { }

    public void setLargo(double largo) {
        // no aceptar largos inferiores a 1.2 metros
        // si el largo es menor que el ancho y el ancho es mayor a cero, generar un IllegalArgumentException
    }

    public double getArea() { }

    public double getValorHora() { }

    public void setValorHora(double valorHora) {
        // El valor de la hora no puede ser menor que 1000
    }

    public String getDescripcion() {  }

    public void setDescripcion(String descripcion) {
        // no aceptar null, blancos o longitudes menores que 10 o mayores que 200
    }

    public static InstalacionDeportiva getInstance(JSONObject json) {
        // utilizar json.has("keyName") para condicionar la creación y retorno de la instancia correcta: Piscina, CanchaTenis o CanchaMultiproposito
        // en otro caso generar IllegalArgumentException("Se esperaba una subclase de InstalacionDeportiva")
    }

    public abstract String getTipoInstalacion();

    @Override
    public boolean equals(Object obj) { }

    public JSONObject toJSONObject() { }

    @Override
    public String toString() { }

}
