package poo.model;

import poo.helpers.Utils;
import org.json.JSONObject;

import poo.listas.TipoCancha;


public class CanchaTennis extends InstalacionDeportiva {

    private TipoCancha tipo;
    

    //CONSTRUCTOR PARAMETRIZADO
    public CanchaTennis(String id, double ancho, double largo, double valorHora, String descripcion, TipoCancha tipo) { 
        super(id, ancho, largo, valorHora, descripcion);
        this.tipo = tipo;
    }

    //CONSTRUCTOR POR DEFECTO
    public CanchaTennis() {
        super(Utils.getRandomKey(5), 1.5, 1.5, 1100, "Instalación deportiva sin descripción");
        this.tipo = TipoCancha.CESPED;
    }


    //CONSTRUCTOR ID
    public CanchaTennis(String id) {
        super(id);
    }

    //CONSTRUCTOR COPIA
     // CONSTRUCTOR COPIA
     public CanchaTennis(CanchaTennis ct) {
        super(ct);
        this.tipo = ct.tipo;

    }

    public CanchaTennis(JSONObject json) {
        super(json);
        this.tipo = TipoCancha.valueOf(json.getString("tipoCancha"));
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }
    

    public void setTipoCancha(TipoCancha tipo) { 
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de cancha no puede ser nulo.");
        }
        this.tipo = tipo;
    }

    public TipoCancha getTipoCancha() { 
        return tipo;
    }

    //DUDA ACA
    @Override
    public String getTipoInstalacion() {
        return String.format("Tipo: %s", tipo.getValue());
    }


    @Override
    public String toString() { 
        return String.format("%-28s %s", getTipoInstalacion(), super.toString());
    }

    @Override
    public double getCostoFuncionamiento(JSONObject config) {
        JSONObject datos = config.getJSONObject("costoFuncionamiento").getJSONObject("canchaTennis");
        double mantenimiento = 0;
    
        switch (tipo) {
            case CESPED -> mantenimiento = datos.getDouble("mantenimientoCesped");
            case LADRILLO -> mantenimiento = datos.getDouble("mantenimientoLadrillo");
            default -> mantenimiento = datos.getDouble("mantenimientoOtros");
        }
    
        return datos.getDouble("electricidad") + mantenimiento;
    }  

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
    
        CanchaTennis ct = (CanchaTennis) obj;
    
        // Validar null antes de comparar
        if (this.id == null || ct.id == null) return false;
        
        return this.id.equals(ct.id);
    }
}
