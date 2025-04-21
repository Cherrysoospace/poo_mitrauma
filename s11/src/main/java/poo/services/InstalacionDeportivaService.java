package poo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poo.helpers.Utils;
import poo.model.InstalacionDeportiva;

public abstract class InstalacionDeportivaService implements Service<InstalacionDeportiva> {

    protected List<InstalacionDeportiva> list;
    protected final String fileName;

    public InstalacionDeportivaService(String configKey) throws Exception {
        this.fileName = Utils.getConfig("archivos").getString(configKey);
        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    
    @Override
    public JSONObject add(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);
        InstalacionDeportiva inst = InstalacionDeportiva.getInstance(json);
        if (list.contains(inst)) {
            throw new ArrayStoreException("Ya existe una instalación con ID " + inst.getId());
        }
        list.add(inst);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", inst.toJSONObject());
    }

    @Override
    public InstalacionDeportiva getItem(String id) {
        return list.stream()
                   .filter(i -> i.getId().equals(id))
                   .findFirst()
                   .orElse(null);
    }

    @Override
    public JSONObject get(String id) throws Exception {
        InstalacionDeportiva inst = getItem(id);
        if (inst == null) throw new NoSuchElementException("No existe instalación con ID " + id);
        return inst.toJSONObject();
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject getAll() {
        try {
            JSONArray arr = new JSONArray(Utils.readText(fileName));
            return new JSONObject()
                   .put("message","ok")
                   .put("data",arr)
                   .put("size",arr.length());
        } catch(IOException|JSONException e) {
            Utils.printStackTrace(e);
            return Utils.keyValueToJson("message","error","error",e.getMessage());
        }
    }

    @Override
    public List<InstalacionDeportiva> load() throws Exception {
        list = new ArrayList<>();
        JSONArray arr = new JSONArray(Utils.readText(fileName));
        for(int i=0; i<arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            list.add(InstalacionDeportiva.getInstance(o));
        }
        return list;
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        InstalacionDeportiva old = getItem(id);
        if (old==null) throw new NoSuchElementException("No existe instalación con ID " + id);
        JSONObject patch = new JSONObject(strJson);
        InstalacionDeportiva updated = getUpdated(patch, old);
        int idx = list.indexOf(old);
        list.set(idx, updated);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message","ok").put("data", updated.toJSONObject());
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        InstalacionDeportiva inst = getItem(id);
        if (inst==null) throw new NoSuchElementException("No existe instalación con ID " + id);
        list.remove(inst);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message","ok").put("data", inst.toJSONObject());
    }

    /** Implementar cómo aplicar cambios de patch sobre cada subclase */
    public abstract InstalacionDeportiva getUpdated(JSONObject patch, InstalacionDeportiva current);

    @Override
    public Class<InstalacionDeportiva> getDataType() {
        return InstalacionDeportiva.class;
    }

    @Override
    public JSONObject size() {
        return new JSONObject().put("size", list.size()).put("message", "ok");
    }
}
