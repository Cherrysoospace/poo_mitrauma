package poo.services;

import org.json.JSONObject;

import poo.helpers.Utils;
import poo.model.CanchaMultiproposito;
import poo.model.InstalacionDeportiva;

public class CanchaMultipropositoService extends InstalacionDeportivaService {
    public CanchaMultipropositoService() throws Exception {
        super("canchaMultiproposito");  
    }

    @Override
    public InstalacionDeportiva getUpdated(JSONObject patch, InstalacionDeportiva current) {
        CanchaMultiproposito m = new CanchaMultiproposito((CanchaMultiproposito) current);
        if (patch.has("ancho"))       m.setAncho(patch.getDouble("ancho"));
        if (patch.has("largo"))       m.setLargo(patch.getDouble("largo"));
        if (patch.has("valorHora"))   m.setValorHora(patch.getDouble("valorHora"));
        if (patch.has("descripcion")) m.setDescripcion(patch.getString("descripcion"));
        if (patch.has("graderia"))    m.setGraderia(patch.getBoolean("graderia"));
        return m;
    }
    
    @Override
    public InstalacionDeportiva dataToAddOk(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);
        if (!json.has("id") || json.getString("id").isBlank()) {
            json.put("id", Utils.getRandomKey(5));
        }
        return new CanchaMultiproposito(json);
    }
}
