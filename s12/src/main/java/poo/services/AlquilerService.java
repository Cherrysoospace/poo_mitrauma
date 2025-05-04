package poo.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poo.helpers.Utils;
import poo.model.Alquiler;
import poo.model.InstalacionDeportiva;
import poo.model.Socio;
import poo.services.*;


public class AlquilerService implements Service<Alquiler> {

    private List<Alquiler> list;
    private final String fileName;
    private List<Socio> socios;
    private List<InstalacionDeportiva> instalaciones;

    public AlquilerService(
            List<Socio> socios,
            List<InstalacionDeportiva> instalaciones) throws Exception {
        this.socios = socios;
        this.instalaciones = instalaciones;
        fileName = Utils.getConfig("archivos").getString("alquileres");


        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }
    
    public AlquilerService(
        Service<Socio> socioService,
        List<InstalacionDeportiva> instalaciones) throws Exception {
    this.instalaciones = instalaciones;
    this.socios = socioService.load();
    fileName = Utils.getConfig("archivos").getString("alquileres");

    if (Utils.fileExists(fileName)) {
        load();
    } else {
        list = new ArrayList<>();
    }
}

    @Override
    public JSONObject add(String strJson) throws Exception {
        Alquiler a = dataToAddOk(strJson);
        if (list.add(a)) {
            Utils.writeJSON(list, fileName);
        }
        return new JSONObject()
                .put("message", "ok")
                .put("data", a.toJSONObject());
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        Alquiler a = getItem(id);
        if (a == null) {
            throw new NoSuchElementException("No existe un alquiler con ID " + id);
        }
        return a.toJSONObject();
    }

    @Override
    public Alquiler getItem(String id) {
        return list.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public JSONObject getAll() {
        try {
            JSONArray arr = new JSONArray(Utils.readText(fileName));
            return new JSONObject()
                    .put("message", "ok")
                    .put("data", arr)
                    .put("size", arr.length());
        } catch (IOException | JSONException e) {
            Utils.printStackTrace(e);
            return Utils.keyValueToJson(
                    "message", "error al leer alquileres",
                    "error", e.getMessage());
        }
    }

    @Override
    public List<Alquiler> load() throws Exception {
        list = new ArrayList<>();
        JSONArray arr = new JSONArray(Utils.readText(fileName));
        for (int i = 0; i < arr.length(); i++) {
            list.add(new Alquiler(arr.getJSONObject(i)));
        }
        return list;
    }

    private void verificarCruceHorarios(Alquiler nuevoAlquiler) throws Exception {
        // Obtener la instalación deportiva y las fechas del nuevo alquiler
        InstalacionDeportiva instalacion = nuevoAlquiler.getInstalacionDeportiva();
        LocalDateTime inicio = nuevoAlquiler.getFechaHoraInicio();
        LocalDateTime fin = nuevoAlquiler.getFechaHoraFin();
        String idNuevo = nuevoAlquiler.getId();
        
        // Verificar cada alquiler existente
        for (Alquiler alquilerExistente : list) {
            // Si es el mismo alquiler (para casos de actualización) o no es la misma instalación, no hay problema
            if (alquilerExistente.getId().equals(idNuevo) || 
                !alquilerExistente.getInstalacionDeportiva().equals(instalacion)) {
                continue;
            }
            
            // Obtener fechas del alquiler existente
            LocalDateTime inicioExistente = alquilerExistente.getFechaHoraInicio();
            LocalDateTime finExistente = alquilerExistente.getFechaHoraFin();
            
            // Verificar si hay cruces:
            // 1. El inicio del nuevo está entre el inicio y fin del existente
            // 2. El fin del nuevo está entre el inicio y fin del existente
            // 3. El nuevo engloba completamente al existente
            if ((inicio.isEqual(inicioExistente) || inicio.isAfter(inicioExistente)) && inicio.isBefore(finExistente) ||
                (fin.isAfter(inicioExistente) && (fin.isBefore(finExistente) || fin.isEqual(finExistente))) ||
                (inicio.isBefore(inicioExistente) && fin.isAfter(finExistente))) {
                
                throw new IllegalArgumentException(
                    "La instalación " + instalacion.getId() + 
                    " ya está alquilada entre " + inicioExistente + 
                    " y " + finExistente);
            }
        }
    }

    // Para aplicar los cambios en el método update
    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        Alquiler old = getItem(id);
            if (old == null) {
                throw new NoSuchElementException("No existe un alquiler con ID " + id);
            }
        JSONObject patch = new JSONObject(strJson);
        Alquiler updated = getUpdated(patch, old);
    // La verificación ya se hizo en getUpdated, así que procedemos con la actualización
        int idx = list.indexOf(old);
        list.set(idx, updated);
        Utils.writeJSON(list, fileName);
            return new JSONObject()
                    .put("message", "ok")
                    .put("data", updated.toJSONObject());
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        Alquiler a = getItem(id);
        if (a == null) {
            throw new NoSuchElementException("No existe un alquiler con ID " + id);
        }
        list.remove(a);
        Utils.writeJSON(list, fileName);
        return new JSONObject()
                .put("message", "ok")
                .put("data", a.toJSONObject());
    }

    // Modificar el método dataToAddOk para incluir la verificación
@Override
public Alquiler dataToAddOk(String strJson) throws Exception {
    JSONObject json = new JSONObject(strJson);

    // Generar ID si no viene
    if (!json.has("id") || json.getString("id").isBlank()) {
        json.put("id", Utils.getRandomKey(5));
    }

    // Construye el alquiler (parsea fechas, socio e instalación)
    Alquiler a = new Alquiler(json);

    // Evita duplicados por ID
    if (list.contains(a)) {
        throw new ArrayStoreException("Ya existe un alquiler con ID " + a.getId());
    }
    
    // Verificar cruce de horarios
    verificarCruceHorarios(a);
    
    return a;
}

    // Modificar también el método getUpdated para incluir la verificación en actualizaciones
@Override
public Alquiler getUpdated(JSONObject patch, Alquiler current) {
    // Copia los valores actuales
    Alquiler a = new Alquiler(current);

    if (patch.has("fechaHoraInicio")) {
        LocalDateTime inicio = LocalDateTime.parse(patch.getString("fechaHoraInicio"));
        a.setFechaHoraInicio(inicio);
    }
    if (patch.has("fechaHoraFin")) {
        LocalDateTime fin = LocalDateTime.parse(patch.getString("fechaHoraFin"));
        a.setFechaHoraFin(fin);
    }
    if (patch.has("socio")) {
        Socio s = new Socio(patch.getJSONObject("socio"));
        a.setSocio(s);
    }
    if (patch.has("instalacionDeportiva")) {
        JSONObject instJson = patch.getJSONObject("instalacionDeportiva");
        InstalacionDeportiva inst = InstalacionDeportiva.getInstance(instJson);
        a.setInstalacionDeportiva(inst);
    }
    
    try {
        // Verificar cruce de horarios (esta verificación se debe hacer antes de guardar el alquiler)
        verificarCruceHorarios(a);
    } catch (Exception e) {
        throw new IllegalArgumentException(e.getMessage());
    }
    
    return a;
}

    @Override
    public Class<Alquiler> getDataType() {
        return Alquiler.class;
    }

    @Override
    public JSONObject size() {
        return new JSONObject()
                .put("message", "ok")
                .put("size", list.size());
    }
}


