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
  

    public void setGraderia(boolean graderia) { 
        this.graderia = graderia;
    }

    @Override
    public String getTipoInstalacion() {
        return "Cancha multipróposito";  
    }

    @Override
    public String toString() {
        String strGraderia = graderia ? "Con graderia": "Sin graderia";
        return String.format("%s\n \n%s", strGraderia, super.toString());
     }

    /*@Override
    public double getCostoFuncionamiento(JSONObject config) {
        JSONObject porcentaje = config.getJSONObject("costoFuncionamiento").getJSONObject("canchaMultiproposito").getJSONObject("porcentaje")
        
    }*/

    @Override
    public double getCostoFuncionamiento(JSONObject config) {
        JSONObject datos = config.getJSONObject("costoFuncionamiento")
                                 .getJSONObject("canchaMultiproposito")
                                 .getJSONObject("porcentaje");
    
        double porcentaje = graderia ? datos.getDouble("conGraderia") : datos.getDouble("sinGraderia");
        return valorHora * porcentaje;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
    
        CanchaMultiproposito cm = (CanchaMultiproposito) obj;
    
        // Validar null antes de comparar
        if (this.id == null || cm.id == null) return false;
        
        return this.id.equals(cm.id);
    }
    
}
