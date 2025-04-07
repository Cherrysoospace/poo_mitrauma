package poo.model;
import poo.helpers.Utils;

import org.json.JSONObject;

public class CanchaMultiproposito extends InstalacionDeportiva {

    private boolean graderia;


    //CONSTRUCTOR POR DEFECTO
    public CanchaMultiproposito() {
        super(Utils.getRandomKey(5), 1.5, 1.5, 1100, "Instalación deportiva sin descripción"); //Yo puedo poner super () y no darle los valores e igual no funciona?
        this.graderia = false;
    }

    //CONSTRUCTOR COPIA
    public CanchaMultiproposito(CanchaMultiproposito cm) {
        super(cm);
        this.graderia = cm.graderia;
    }

    //CONSTRUCTOR DE ID
    public CanchaMultiproposito(String id) { 
        super(id);
    }

    //CONSTRUCTOR PARAMETRIZADO
    public CanchaMultiproposito(String id, double ancho, double largo, String descripcion, boolean graderia, double valorHora) {
        super(id, ancho, largo, valorHora, descripcion);
        this.graderia = graderia;
    }


    //JSON
    public CanchaMultiproposito(JSONObject json) {
        super(json);
        this.graderia = json.getBoolean("graderia");
    }

    @Override
     public JSONObject toJSONObject() {
         return new JSONObject(this);
    }

    public boolean getGraderia() {
        return graderia;
    }


    //ESTE TRATAMIENTO QUE SE LE DA A GRADERIA SE LE DEBERIA DAR A OLIMPICA EN CLASE PISCINA?

    //DUDAAAA
    public String getStrGraderia() {
        return graderia ? "con graderia" : "sin graderia";
    }

    public void setGraderia(boolean graderia) { 
        this.graderia = graderia;
        System.out.println(getStrGraderia());
        // retorna una cadena indicando si tiene o no tiene gradería
    }

    @Override
    public String getTipoInstalacion() {
        // devuelve "Cancha multipropósito " + getStrGraderia();
        return String.format("graderia: %s", getStrGraderia());  
    }

    @Override
    public String toString() {
        return String.format("Graderia: %s\n", getStrGraderia(), super.toString());
     }

    
}
