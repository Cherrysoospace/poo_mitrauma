package poo.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import poo.listas.TipoCancha;
import poo.helpers.Utils;
import poo.model.CanchaTennis;
import poo.model.InstalacionDeportiva;

public class CanchaTennisService extends InstalacionDeportivaService {
    public CanchaTennisService() throws Exception {
        super("canchaTennis");
    }

    @Override
    public InstalacionDeportiva getUpdated(JSONObject patch, InstalacionDeportiva current) {
        CanchaTennis c = new CanchaTennis((CanchaTennis) current);
        if (patch.has("ancho"))
            c.setAncho(patch.getDouble("ancho"));
        if (patch.has("largo"))
            c.setLargo(patch.getDouble("largo"));
        if (patch.has("valorHora"))
            c.setValorHora(patch.getDouble("valorHora"));
        if (patch.has("descripcion"))
            c.setDescripcion(patch.getString("descripcion"));
        if (patch.has("tipoCancha"))
            c.setTipoCancha(TipoCancha.valueOf(patch.getString("tipoCancha").toUpperCase()));
        return c;
    }

    @Override
    public List<InstalacionDeportiva> load() throws Exception {
        list = new ArrayList<>();
        JSONArray arr = new JSONArray(Utils.readText(fileName));
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            list.add(new CanchaTennis(o)); // Usar directamente el constructor
        }
        return list;
    }

    @Override
    public InstalacionDeportiva dataToAddOk(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);
        if (!json.has("id") || json.getString("id").isBlank()) {
            json.put("id", Utils.getRandomKey(5));
        }
        return new CanchaTennis(json);
    }

}
