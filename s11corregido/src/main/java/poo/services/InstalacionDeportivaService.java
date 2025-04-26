package poo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poo.model.InstalacionDeportiva;
import poo.model.Piscina;
import poo.model.CanchaMultiproposito;
import poo.model.CanchaTennis;

import poo.helpers.Utils;
import poo.listas.TipoCancha;

public class InstalacionDeportivaService implements Service<InstalacionDeportiva> {

    private List<InstalacionDeportiva> list;
    private final String fileName;
    private final Class<InstalacionDeportiva> c; // subclase utilizada

    @SuppressWarnings("unchecked")
    public InstalacionDeportivaService(Class<? extends InstalacionDeportiva> c)
            throws Exception {
        this.c = (Class<InstalacionDeportiva>) c; // referenciar la subclase

        if (Piscina.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig("archivos").getString("piscina");
        } else if (CanchaTennis.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig("archivos").getString("canchaTennis");
        } else if (CanchaMultiproposito.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig("archivos").getString("canchaMultiproposito");
        } else {
            throw new IllegalArgumentException("Se esperaba una InstalacionDeportiva");
        }

        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        InstalacionDeportiva instalacion = dataToAddOk(strJson);

        if (list.add(instalacion)) {
            Utils.writeJSON(list, fileName);
        }
        return new JSONObject().put("message", "ok").put("data", instalacion.toJSONObject());
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        InstalacionDeportiva instalacion = getItem(id);
        if (instalacion == null) {
            throw new NoSuchElementException(String.format("No se encontró una instalación deportiva con ID %s", id));
        }
        return instalacion.toJSONObject();
    }

    @Override
    public InstalacionDeportiva getItem(String id) throws Exception {
        int i = -1; // Por qu'e el valor inicia en -1?

        if (Piscina.class.isAssignableFrom(c)) {
            i = list.indexOf(new Piscina(id));
        } else if (CanchaTennis.class.isAssignableFrom(c)) {
            i = list.indexOf(new CanchaTennis(id));
        } else if (CanchaMultiproposito.class.isAssignableFrom(c)) {
            i = list.indexOf(new CanchaMultiproposito(id));
        } else {
            throw new IllegalArgumentException("Se esperaba una InstalacionDeportiva"); // Esto es necesario?
        }
        return i > -1 ? list.get(i) : null;
    }

    @Override
    public JSONObject getAll() {
        try {
            JSONArray data = new JSONArray();
            if (Utils.fileExists(fileName)) {
                data = new JSONArray(Utils.readText(fileName));
            }
            return new JSONObject()
                    .put("message", "ok")
                    .put("data", data)
                    .put("size", data.length())
                    .put("type", c.getSimpleName()); // Add the type of facility
        } catch (IOException | JSONException e) {
            Utils.printStackTrace(e);
            return Utils.keyValueToJson(
                    "message", "Sin acceso a datos de Instalaciones Deportivas",
                    "error", e.getMessage(),
                    "type", c.getSimpleName());
        }
    }

    @Override
    public List<InstalacionDeportiva> load() throws Exception {
        list = new ArrayList<>();

        JSONArray jsonArr = new JSONArray(Utils.readText(fileName));

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObject = jsonArr.getJSONObject(i);
            if (jsonObject.has("tipoCancha")) {
                list.add(new CanchaTennis(jsonObject));
            } else if (jsonObject.has("olimpica")) {
                list.add(new Piscina(jsonObject));
            } else if (jsonObject.has("graderia")) {
                list.add(new CanchaMultiproposito(jsonObject));
            } else {
                throw new IllegalArgumentException("Se esperaba una instalación Deportiva.");
            }
        }

        return list;
    }

    // Por que final y static al mismo tiempo?
    public final static List<InstalacionDeportiva> collectAll() throws Exception {
        List<InstalacionDeportiva> collect = new ArrayList<>();

        InstalacionDeportivaService piscinaService = new InstalacionDeportivaService(Piscina.class);
        InstalacionDeportivaService canchaTennisService = new InstalacionDeportivaService(CanchaTennis.class);
        InstalacionDeportivaService canchaMultipropositoService = new InstalacionDeportivaService(
                CanchaMultiproposito.class);

        collect.addAll(piscinaService.load());
        collect.addAll(canchaTennisService.load());
        collect.addAll(canchaMultipropositoService.load());

        return collect;
    }

    /** 7. Carga todas las instalaciones en formato JSON unificado */
    public static JSONObject loadAll() throws Exception {
        List<InstalacionDeportiva> all = collectAll();
        JSONArray data = new JSONArray();
        for (InstalacionDeportiva inst : all) {
            data.put(inst.toJSONObject());
        }
        return new JSONObject()
                .put("message", "ok")
                .put("data", data)
                .put("size", data.length());
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        // Create a JSONObject with the keys and values to update
        JSONObject newData = new JSONObject(strJson);

        // Find the sports facility to update and remember its position
        InstalacionDeportiva instalacion = getItem(id);

        if (instalacion == null) {
            throw new NullPointerException("No se encontró la instalación deportiva " + id);
        }
        int i = list.indexOf(instalacion);

        // Update the facility with new data
        instalacion = getUpdated(newData, instalacion);

        // Update the facility in the list
        list.set(i, instalacion);

        // Update the JSON file
        Utils.writeJSON(list, fileName);

        // Return the updated facility
        return new JSONObject().put("message", "ok").put("data", instalacion.toJSONObject());
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        JSONObject instalacion = get(id);
        if (instalacion == null) {
            throw new NoSuchElementException("No existe una instalación deportiva con la identificación " + id);
        }

        InstalacionDeportiva facility;
        // Create the appropriate instance based on the facility type
        if (Piscina.class.isAssignableFrom(c)) {
            facility = new Piscina(id);
        } else if (CanchaTennis.class.isAssignableFrom(c)) {
            facility = new CanchaTennis(id);
        } else if (CanchaMultiproposito.class.isAssignableFrom(c)) {
            facility = new CanchaMultiproposito(id);
        } else {
            throw new IllegalArgumentException("Se esperaba una InstalacionDeportiva");
        }

        String alquileres = Utils.getConfig("archivos").getString("alquileres");
        // Check if the facility has any rentals and don't allow deletion if it does
        if (Utils.exists(alquileres, "instalacion", instalacion)) {
            throw new Exception(
                    String.format("No eliminado. La instalación deportiva %s tiene alquileres registrados", id));
        }

        if (!list.remove(facility)) {
            throw new Exception(
                    String.format("No se pudo eliminar la instalación deportiva con identificación %s", id));
        }

        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", instalacion);
    }

    @Override
    public InstalacionDeportiva dataToAddOk(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);

        if (json.has("olimpica")) {
            return new Piscina(json);
        } else if (json.has("tipoCancha")) {
            return new CanchaTennis(json);
        } else if (json.has("graderia")) {
            return new CanchaMultiproposito(json);
        } else {
            throw new IllegalArgumentException("JSON no corresponde a una instalación deportiva conocida");
        }
    }

    @Override
    public InstalacionDeportiva getUpdated(JSONObject newData, InstalacionDeportiva current) throws Exception {
        // Create a copy of the current facility
        InstalacionDeportiva instalacion;

        if (current instanceof Piscina) {
            instalacion = new Piscina((Piscina) current);
        } else if (current instanceof CanchaTennis) {
            instalacion = new CanchaTennis((CanchaTennis) current);
        } else if (current instanceof CanchaMultiproposito) {
            instalacion = new CanchaMultiproposito((CanchaMultiproposito) current);
        } else {
            throw new IllegalArgumentException("Tipo de instalación deportiva no soportado");
        }

        // Update common properties
        if (newData.has("descripcion")) {
            instalacion.setDescripcion(newData.getString("descripcion"));
        }

        if (newData.has("ancho")) {
            instalacion.setAncho(newData.getDouble("ancho"));
        }

        if (newData.has("largo")) {
            instalacion.setLargo(newData.getDouble("largo"));
        }

        if (newData.has("valorHora")) {
            instalacion.setValorHora(newData.getInt("valorHora"));
        }

        // Update specific properties for each type of facility
        if (instalacion instanceof Piscina) {
            Piscina piscina = (Piscina) instalacion;
            if (newData.has("olimpica")) {
                piscina.setOlimpica(newData.getBoolean("olimpica"));
            }
        } else if (instalacion instanceof CanchaTennis) {
            CanchaTennis canchaTennis = (CanchaTennis) instalacion;
            if (newData.has("tipoCancha")) {
                // Convert string to TipoCancha enum
                canchaTennis.setTipoCancha(TipoCancha.valueOf(newData.getString("tipoCancha")));
            }
        } else if (instalacion instanceof CanchaMultiproposito) {
            CanchaMultiproposito canchaMulti = (CanchaMultiproposito) instalacion;
            if (newData.has("graderia")) {
                canchaMulti.setGraderia(newData.getBoolean("graderia"));
            }
        }

        return instalacion;
    }

    @Override
    public JSONObject size() {
        return new JSONObject()
                .put("size", list.size())
                .put("message", "ok")
                .put("type", c.getSimpleName()); 
    }

    @Override
    public Class<InstalacionDeportiva> getDataType() {
        return c;
    }

}
