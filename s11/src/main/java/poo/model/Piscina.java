package poo.model;

import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import poo.helpers.Utils;
import poo.helpers.Utils;

public class Piscina extends InstalacionDeportiva {

    private boolean olimpica;
    private JSONObject config;
    private double costoFuncionamiento;

    public Piscina() {
        super(Utils.getRandomKey(5), 2.0, 2.0, 1100, "Piscina sin descripcion");
        this.olimpica = false;
    }

    public Piscina(String id) {
        super(id);
    }

    // CONSTRUCTOR PARAMETRIZADO
    public Piscina(String id, double ancho, double largo, double valorHora, String descripcion, boolean olimpica) {
        super(id, ancho, largo, valorHora, descripcion);
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

    // CONSTRUCTOR COPIA
    public Piscina(Piscina p) {
        super(p);
        this.olimpica = p.olimpica;
    }

    public boolean getOlimpica() {
        return olimpica;
    }

    public void setOlimpica(boolean olimpica) {
        this.olimpica = olimpica;
    }

    // public void setOlimpica(boolean olimpica) {
    // this.olimpica = olimpica;
    // }
    public String getStrGraderia() {
        return olimpica ? "Es olímpica" : "No es olímpica";
    }

    @Override
    public String getTipoInstalacion() {
        return olimpica ? "Pisicina Olímpica" : "Piscina no Olímpica";
    }

    @Override
    public String toString() {
        return super.toString() + String.format("%-10s", getOlimpica()) + String.format("%-10s", getTipoInstalacion())
                + "\n";
    }

    @Override
    public double getCostoFuncionamiento(JSONObject config) {
        JSONObject datos = config.getJSONObject("costoFuncionamiento").getJSONObject("piscina");
        double costo = datos.getDouble("quimico") +
                datos.getDouble("electricidad") +
                datos.getDouble("agua") +
                datos.getDouble("limpiezaMantenimiento");

        if (olimpica) {
            costo += costo * datos.getDouble("Olimpica");
        }
        return costo;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
    
        Piscina p = (Piscina) obj;
    
        // Validar null antes de comparar
        if (this.id == null || p.id == null) return false;
        
        return this.id.equals(p.id);
    }
}
