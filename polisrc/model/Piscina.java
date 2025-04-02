package poo.model;

import org.json.JSONObject;

public class Piscina extends InstalacionDeportiva {

    private boolean olimpica;

    public Piscina() { }

    public Piscina(String id) { }

    public Piscina(String id, double ancho, double largo, String descripcion, boolean olimpica, double valorHora) { }

    public Piscina(JSONObject json) { }

    public Piscina(Piscina piscina) { }

    public boolean getOlimpica() { }

    public void setOlimpica(boolean olimpica) { }

    @Override
    public String getTipoInstalacion() {
        // devolver "Piscina olímpica" : "Piscina normal"
    }

    @Override
    public String toString() {  }

}
