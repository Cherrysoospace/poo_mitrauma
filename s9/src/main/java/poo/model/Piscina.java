package poo.model;

import org.json.JSONObject;

import poo.helpers.Utils;

public class Piscina extends InstalacionDeportiva{

    private boolean olimpica;

    public Piscina() { 
        super(Utils.getRandomKey(5), 2.0, 2.0, 1100, "Piscina sin descripcion");
        this.olimpica = false;
    }

    public Piscina(String id) {
        super(id);
     }


    //CONSTRUCTOR PARAMETRIZADO
    public Piscina (String id, double ancho, double largo, double valorHora, String descripcion, boolean olimpica){
        super(id, ancho, largo, valorHora,  descripcion);
        this.olimpica = olimpica;
    }

    public Piscina(JSONObject json) {
        super(json);
        setOlimpica(json.getBoolean("olimpica"));
    }

     @Override
     public JSONObject toJSONObject() {
         return new JSONObject(this);
    }

    //CONSTRUCTOR COPIA
    public Piscina (Piscina p) {
        super(p);
        this.olimpica = p.olimpica;
    }


    public boolean getOlimpica() {
        return olimpica;
    }

    public void setOlimpica(boolean olimpica) {
        this.olimpica = olimpica;
    }

    //public void setOlimpica(boolean olimpica) {
       //this.olimpica = olimpica;
    //}
    public String getStrGraderia() {
        return olimpica ? "Es olímpica" : "No es olímpica";
    }

    @Override
    public String getTipoInstalacion() {
        return olimpica ? "Es pisicina Olímpica" : "No es piscina Olímpica";
    }

    @Override
    public String toString() { 
        return super.toString() + String.format("%-10s", getOlimpica()) + String.format("%-10s", getTipoInstalacion()) + "\n";
     }
}
