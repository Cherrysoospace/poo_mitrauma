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
    private final double tarifa;  // Tarifa por hora leída del archivo de configuración
    private final Class<InstalacionDeportiva> c; // subclase utilizada

    @SuppressWarnings("unchecked")
    public InstalacionDeportivaService(Class<? extends InstalacionDeportiva> c)
            throws Exception {
        this.c = (Class<InstalacionDeportiva>) c; // referenciar la subclase
        
        if (Piscina.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig("archivos").getString("piscina");
            tarifa = Utils.getConfig("tarifas").getDouble("piscina");
        } else if (CanchaTennis.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig("archivos").getString("canchaTennis");
            tarifa = Utils.getConfig("tarifas").getDouble("canchaTennis");
        } else if (CanchaMultiproposito.class.isAssignableFrom(c)) {
            fileName = Utils.getConfig("archivos").getString("canchaMultiproposito");
            tarifa = Utils.getConfig("tarifas").getDouble("canchaMultiproposito");
        } else {
            throw new IllegalArgumentException("Se esperaba una InstalacionDeportiva");
        }

        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    /**
     * Obtiene la tarifa por hora para esta instalación deportiva
     * @return valor de la tarifa por hora
     */
    public double getTarifa() {
        return tarifa;
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        InstalacionDeportiva instalacion = dataToAddOk(strJson);
        
        // Asignar la tarifa leída del archivo de configuración
        instalacion.setValorHora(tarifa);

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
        int i = -1; // El valor inicia en -1 para indicar que no se ha encontrado la instalación

        if (Piscina.class.isAssignableFrom(c)) {
            i = list.indexOf(new Piscina(id));
        } else if (CanchaTennis.class.isAssignableFrom(c)) {
            i = list.indexOf(new CanchaTennis(id));
        } else if (CanchaMultiproposito.class.isAssignableFrom(c)) {
            i = list.indexOf(new CanchaMultiproposito(id));
        } else {
            throw new IllegalArgumentException("Se esperaba una InstalacionDeportiva");
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
            
            // Añadir valorHora al objeto JSON si no existe
            if (!jsonObject.has("valorHora")) {
                jsonObject.put("valorHora", tarifa);
            }
            
            InstalacionDeportiva instalacion = null;
            
            if (jsonObject.has("tipoCancha")) {
                instalacion = new CanchaTennis(jsonObject);
            } else if (jsonObject.has("olimpica")) {
                instalacion = new Piscina(jsonObject);
            } else if (jsonObject.has("graderia")) {
                instalacion = new CanchaMultiproposito(jsonObject);
            } else {
                throw new IllegalArgumentException("Se esperaba una instalación Deportiva.");
            }
            
            // Si el valor de la hora es 0, asignar el valor de la configuración
            if (instalacion.getValorHora() == 0) {
                instalacion.setValorHora(tarifa);
            }
            
            list.add(instalacion);
        }

        return list;
    }

    // Es final y static al mismo tiempo para que sea un método que no puede ser sobreescrito (final)
    // y que puede ser llamado sin instanciar la clase (static)
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

    /** Carga todas las instalaciones en formato JSON unificado */
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
    
        // Si no se especificó un valor de hora en la actualización, asegurar que se mantiene la tarifa
        if (!newData.has("valorHora")) {
            instalacion.setValorHora(tarifa);
        }
    
        // Update the facility in the list
        list.set(i, instalacion);
        
        // Actualizar la instalación deportiva en el archivo de alquileres utilizando el método especializado
        try {
            boolean updated = Utils.updateInstalacionReferences(id, instalacion.toJSONObject());
            if (Utils.trace) {
                System.out.println("Actualización de referencias en alquileres: " + (updated ? "Exitosa" : "No requerida"));
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar referencias: " + e.getMessage());
            Utils.printStackTrace(e);
        }
        
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
        // Usar método exists buscando por "instalacionDeportiva" y comparando el id
        JSONObject jsonId = new JSONObject().put("id", id);
        if (Utils.exists(alquileres, "instalacionDeportiva", jsonId, "id")) {
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

        if (!json.has("id") || json.getString("id").isBlank()) {
            json.put("id", Utils.getRandomKey(5));
        }
        
        // Añadir el valorHora desde la configuración si no existe
        if (!json.has("valorHora")) {
            json.put("valorHora", tarifa);
        }

        InstalacionDeportiva instalacion = null;
        
        if (json.has("olimpica")) {
            instalacion = new Piscina(json);
        } else if (json.has("tipoCancha")) {
            instalacion = new CanchaTennis(json);
        } else if (json.has("graderia")) {
            instalacion = new CanchaMultiproposito(json);
        } else {
            throw new IllegalArgumentException("JSON no corresponde a una instalación deportiva conocida");
        }
        
        // Verificar que se haya asignado la tarifa correctamente
        if (instalacion.getValorHora() == 0) {
            instalacion.setValorHora(tarifa);
        }
        
        return instalacion;
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
        
        // Actualizar el valor hora si viene en los datos
        if (newData.has("valorHora")) {
            instalacion.setValorHora(newData.getDouble("valorHora"));
        } else {
            // Mantener la tarifa si no se especificó un nuevo valor
            instalacion.setValorHora(tarifa);
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