package poo.model;

import java.time.LocalTime;

import org.json.JSONObject;

public abstract class Instalacion {
    protected String numId;
    protected String descripcion;
    protected double precioHora;
    protected double dimensionAn;
    protected double dimensionLar;

    // Control K + U, para descomentar el codigo

    // CONSTRUCTOR CON PARAMETROS
    public Instalacion(String numId, String descripcion, double precioHora, double dimensionAn, double dimensionLar) {
        this.numId = numId;
        this.descripcion = descripcion;
        this.precioHora = precioHora;
        this.dimensionAn = dimensionAn;
        this.dimensionLar = dimensionLar;
    }

    // CONSTRUCTOR POR DEFECTO
    public Instalacion() {
        this.numId = "I0000";
        this.descripcion = "";
        this.precioHora = 0;
        this.dimensionAn = 0;
        this.dimensionLar = 0;
    }

    // CONSTRUCTOR COPIA
    public Instalacion(Instalacion i) {
        this(i.numId,
                i.descripcion,
                i.precioHora,
                i.dimensionAn,
                i.dimensionLar);

    }

    // JSON
    public Instalacion(JSONObject json) {
        this(
                json.getString("numId"),
                json.getString("descripcion"),
                json.getInt("precioHora"),
                json.getDouble("dimensionAn"),
                json.getDouble("dimensionLar"));
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    // JSONObject json = new JSONObject(); FALTAAAA

    /*
     * public JSONObject toJSONObject() {
     * JSONObject json = new JSONObject();
     * json.put("numId", this.numId);
     * json.put("descripcion", this.descripcion);
     * json.put("precioHora", this.precioHora);
     * json.put("dimensionAn", this.dimensionAn);
     * json.put("dimensionLar", this.dimensionLar);
     * return json;
     * }
     */

    // ACCESORES
    public String getNumId() {
        return numId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecioHora() {
        return precioHora;
    }

    public double getDimensionAn() {
        return dimensionAn;
    }

    public double getDimensionLar() {
        return dimensionLar;
    }

    // MUTADORES
    public void setNumId(String numId) throws IllegalArgumentException {
        if ((numId == null) || (numId.isBlank())) {
            throw new IllegalArgumentException("El ID de la instalación no puede ser vacío o nulo.");
        }

        if (numId.length() != 5) {
            throw new IllegalArgumentException("El ID de la instalación debe tener 5 caracteres.");
        }
        this.numId = numId;
    }

    // Se hará la descripcion obligatoria para el usuario? En este caso, se hara
    // obligatoria.
    public void setDescripcion(String descripcion) throws IllegalArgumentException {
        if ((descripcion == null) || (descripcion.isBlank())) {
            throw new IllegalArgumentException("La descripción no puede ser vacía o nula.");
        }

        if (descripcion.length() < 15) {
            throw new IllegalArgumentException("La descripción debe tener al menos 15 caracteres.");
        }
        this.descripcion = descripcion;
    }

    public void setPrecioHora(int precioHora) throws IllegalArgumentException {
        if (precioHora < 0) {
            throw new IllegalArgumentException("El precio por hora no puede ser negativo.");
        }
        this.precioHora = precioHora;
    }

    public void setDimensionAn(double dimensionAn) {
        if (dimensionAn <= 0) {
            throw new IllegalArgumentException("La dimensión no puede ser negativa.");
        }
        this.dimensionAn = dimensionAn;
    }

    public void setDimensionLar(double dimensionLar) {
        if (dimensionLar <= 0) {
            throw new IllegalArgumentException("La dimensión no puede ser negativa.");
        }
        this.dimensionLar = dimensionLar;
    }

    // COMPORTAMIENTO
    public double getArea() {
        double area = dimensionAn * dimensionLar;
        return area;
    }

    // TOSTRING MODIFICAAAAAAR
    @Override
    public String toString() {
        return String.format(
                "Id Instalación: %s\nDescripción: %s\nPrecio por hora: %.2f\nAncho: %.2f\n Largo: %.2f\n Area: %.2f\n",
                numId, descripcion, precioHora, dimensionAn, dimensionLar, getArea());
    }

    //EQUALS
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        Instalacion i = (Instalacion) obj;

        return this.numId.equals(i.numId);
    }

}
