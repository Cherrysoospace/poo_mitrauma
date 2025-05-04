package poo.model;

import org.json.JSONObject;
import poo.helpers.Utils;

public class Socio {

    private String id;
    private String nombre;
    private String direccion;
    private String telefono;

    // CONSTRUCTOR POR DEFECTO
    public Socio() {
        // asignar ID de 5 caracteres, NN, "Dirección no registrada" y "Teléfono no
        // registrado"
        setId(Utils.getRandomKey(5));
        setNombre("NN");
        setDireccion("Dirección no registrada");
        setTelefono("Teléfono no registrado");
    }

    public Socio(String id) {
        this.id = id;
    }

    // CONSTRUCTOR PARAMETRIZADO
    public Socio(String id, String nombre, String direccion, String telefono) {
        setId(id);
        setNombre(nombre);
        setDireccion(direccion);
        setTelefono(telefono);
    }

    // CONSTRUCTOR COPIA
    public Socio(Socio s) {
        this(
                s.id,
                s.nombre,
                s.direccion,
                s.telefono);

    }

    // JSON
    public Socio(JSONObject json) {
        this(
                json.getString("id"),
                json.getString("nombre"),
                json.getString("direccion"),
                json.getString("telefono"));
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    // ACCESORES
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    // MUTADORES
    public void setId(String id) {
        // no aceptar null, blancos y longitudes distintas a 12 caracteres
        // PREGRUNTA: EN CLASE HABIA HABLADO DE 5 CARACTERES PARA AL ID, Y AHORA SON 12
        // EN LAS GUIAS, QUE TIENE M'AS PESO?
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        }
        if (id.length() != 5) {
            throw new IllegalArgumentException("La identificación debe tener exactamente 5 carácteres.");
        }
        this.id = id;
    }

    public void setNombre(String nombre) {
        // el nombre no puede ser null o estar en blanco
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        if (nombre.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }

        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        // no puede ser null, blancos, menor que 10 caracteres ni mayor a 200
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }
        if (direccion.length() < 10) {
            throw new IllegalArgumentException("La dirección debe tener al menos 10 caracteres.");
        }
        // eliminar los posibles blancos al principio y/o al final
        this.direccion = direccion.trim();
    }

    // SI SE ACEPTAN VARIOS ESO NO IMPLICA QUE ES UN ARREGLO
    public void setTelefono(String telefono) {
        // no puede ser null, blancos, menor que 10 caracteres ni mayor a 100
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }

        if (telefono.length() < 10) {
            throw new IllegalArgumentException("El teléfono no puede ser inferior a 10 dígitos.");
        }

        if (telefono.length() > 100) {
            throw new IllegalArgumentException("El teléfono no puede ser mayor a 100 dígitos.");
        }
        // eliminar los posibles blancos al principio y/o al final. Se aceptan varios?
        this.telefono = telefono.trim();
    }

    @Override
    public boolean equals(Object obj) {
        // true si los ID son iguales
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        Socio s = (Socio) obj;

        return this.id.equals(s.id);
    }

    @Override
    public String toString() {
        return String.format("Identificación: %s\nNombre: %s\nDirección: %s\nTeléfono: +57 %s\n",
                id, nombre, direccion, telefono);
    }

}