package poo.model;

import org.json.JSONObject;

public class CanchaMultiproposito extends InstalacionDeportiva {

    private boolean graderia;

    public CanchaMultiproposito() {  }

    public CanchaMultiproposito(String id) { }

    public CanchaMultiproposito(String id, double ancho, double largo, String descripcion, boolean graderia, double valorHora) { }

    public CanchaMultiproposito(JSONObject json) { }

    public CanchaMultiproposito(CanchaMultiproposito multiproposito) { }

    public String getStrGraderia() {
        // devuelve "con gradería " o "sin gradería";
    }

    public boolean getGraderia() { }

    public void setGraderia(boolean graderia) { 
        // retorna una cadena indicando si tiene o no tiene gradería
    }

    @Override
    public String getTipoInstalacion() {
        // devuelve "Cancha multipropósito " + getStrGraderia();
    }

    @Override
    public String toString() { }

}
