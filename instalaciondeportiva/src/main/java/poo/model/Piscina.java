package poo.model;

import org.json.JSONObject;

public class Piscina extends Instalacion {
    public boolean piscinaOlimpica;

    //CONSTRUCTOR POR DEFECTO
    public Piscina (){
        super("P0000", " ", 50, 1, 1);
        this.piscinaOlimpica = true;
    }

    //CONSTRUCTOR COPIA
    public Piscina (Piscina p) {
        super(p);
        this.piscinaOlimpica = p.piscinaOlimpica;
    }

    //CONSTRUCTOR PARAMETRIZADO
    public Piscina (String numId, String descripcion, double precioHora, double dimensionAn, double dimensionLar, boolean piscinaOlimpica){
        super(numId, descripcion, precioHora, dimensionAn, dimensionLar);
        this.piscinaOlimpica = piscinaOlimpica;
    }

    public boolean getPiscinaOlimpica() {
        return piscinaOlimpica;
    }

    
    //TOSTRING
    @Override
    public String toString() {
        return super.toString() + String.format("%-10s", (this.piscinaOlimpica ? "Es pisicina Olímpica" : "No es piscina Olímpica"));
    }

    // JSON
    public Piscina(JSONObject json) {
        super(json);
        this.piscinaOlimpica = json.getBoolean("piscinaOlimpica");

    }

    @Override
    public JSONObject toJSONObject() {
            JSONObject json = super.toJSONObject();
            json.put("piscinaOlimpica", this.piscinaOlimpica);
            return json;
    }

    //EQUALS
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
        Piscina p = (Piscina) obj;

        return this.numId.equals(p.numId);
    }
    
}
