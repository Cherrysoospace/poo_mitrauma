package poo.model;

import org.json.JSONObject;

public class CanchaMultiproposito extends Instalacion {
    public boolean graderia;

    // CONSTRUCTOR POR DEFECTO
    public CanchaMultiproposito() {
        super();
        this.graderia = false;
    }

    // CONSTRUCTOR PARAMETRIZADO
    public CanchaMultiproposito(String numId, String descripcion, double precioHora, double dimensionAn,
            double dimensionLar, boolean graderia) {
        super(numId, descripcion, precioHora, dimensionAn, dimensionLar);
        this.graderia = graderia;
    }

    // CONSTRUCTOR COPIA
    public CanchaMultiproposito(CanchaMultiproposito cm) {
        super(cm);
        this.graderia = cm.graderia;

    }

    public boolean getGraderia() {
        return graderia;
    }

    // TOSTRING
    @Override
    public String toString() {
        return super.toString() + String.format("%-10s", (this.graderia ? "Graderia" : "Sin Graderia"));
    }

    // JSON
    public CanchaMultiproposito(JSONObject json) {
        super(json);
        this.graderia = json.optBoolean("graderia", false); 
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
            json.put("graderia", this.graderia);
    return json;
}

    // EQUALS
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CanchaTennis cm = (CanchaTennis) obj;

        return this.numId.equals(cm.numId);
    }

}
