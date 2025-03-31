package poo.model;

import org.json.JSONObject;

import poo.listas.TipoCancha;

public class CanchaTennis extends Instalacion {
    private TipoCancha tipoCancha;

    // CONSTRUCTOR PARAMETRIZADO
    public CanchaTennis(String numId, String descripcion, double precioHora, double dimensionAn, double dimensionLar, TipoCancha tipoCancha) {
        super(numId, descripcion, precioHora, dimensionAn, dimensionLar);
        setTipoCancha(tipoCancha); // Ahora sí pasamos un valor válido
    }

    // CONSTRUCTOR POR DEFECTO
    public CanchaTennis() {
        super("C0000", "Cancha de Tennis", 0, 1, 1);
        this.tipoCancha = TipoCancha.CESPED;
    }

    /*
     * public Estudiante(Estudiante otro) {
     * super(otro); // Llamamos al constructor de copia de Persona
     * this.carrera = otro.carrera;
     * }
     */

    // CONSTRUCTOR COPIA
    public CanchaTennis(CanchaTennis c) {
        super(c);
        this.tipoCancha = c.tipoCancha;

    }

    public TipoCancha getTipoCancha () {
        return tipoCancha;
    }

    public void setTipoCancha(TipoCancha tipoCancha) {
        if (tipoCancha == null) {
            throw new IllegalArgumentException("El tipo de cancha no puede ser nulo.");
        }
        this.tipoCancha = tipoCancha;
    }
    // JSON
    public CanchaTennis(JSONObject json) {
        super(json);
        this.tipoCancha = TipoCancha.valueOf(json.getString("tipoCancha"));
    }

     @Override
     public JSONObject toJSONObject() {
         JSONObject json = super.toJSONObject();
         json.put("tipoCancha", this.tipoCancha.toString());
         return json;
     }
    

    // TOSTRING
    @Override
    public String toString() {
        return String.format(
                "%sTipo de Cancha: %s\n",
                super.toString(),
                tipoCancha);
    }

    // EQUALS
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CanchaTennis c = (CanchaTennis) obj;

        return this.numId.equals(c.numId);
    }
}
