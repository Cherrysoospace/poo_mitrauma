package poo.services;

import org.json.JSONObject;

import poo.helpers.Utils;
import poo.model.InstalacionDeportiva;
import poo.model.Piscina;

public class PiscinaService extends InstalacionDeportivaService {
    public PiscinaService() throws Exception {
        super("piscina");  // clave en config.json
    }

    @Override
    public Piscina getUpdated(JSONObject patch, InstalacionDeportiva current) {
        Piscina p = new Piscina((Piscina) current);
        if (patch.has("ancho"))    p.setAncho(patch.getDouble("ancho"));
        if (patch.has("largo"))    p.setLargo(patch.getDouble("largo"));
        if (patch.has("valorHora"))p.setValorHora(patch.getDouble("valorHora"));
        if (patch.has("descripcion")) p.setDescripcion(patch.getString("descripcion"));
        if (patch.has("olimpica")) p.setOlimpica(patch.getBoolean("olimpica"));
        return p;
    }

    @Override
    public InstalacionDeportiva dataToAddOk(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);
        if (!json.has("id") || json.getString("id").isBlank()) {
            json.put("id", Utils.getRandomKey(5));
        }

        Piscina p = new Piscina(json);
        return p;
    }
}
