package poo.model;

import org.json.JSONObject;
import poo.helpers.Utils;

public class Socio {

    private String id;
    private String nombre;
    private String direccion;
    private String telefono;

    public Socio() {
        // asignar ID de 12 caracteres, NN, "Dirección no registrada" y "Teléfono no registrado"
    }

    public Socio(String id) { }

    public Socio(String id, String nombre, String direccion, String telefono) { }

    public Socio(JSONObject json) { }

    public Socio(Socio s) { }

    public String getId() { }

    public void setId(String id) {
        // no aceptar null, blancos y longitudes distintas a 12 caracteres
    }

    public String getNombre() { }

    public void setNombre(String nombre) {
        // el nombre no puede ser null o estar en blanco
    }

    public String getDireccion() { }

    public void setDireccion(String direccion) {
        //  no puede ser null, blancos, menor que 10 caracteres ni mayor a 200
        //  eliminar los posibles blancos al principio y/o al final 
    }

    public String getTelefono() { }

    public void setTelefono(String telefono) {
        //  no puede ser null, blancos, menor que 10 caracteres ni mayor a 100
        //  eliminar los posibles blancos al principio y/o al final. Se aceptan varios?
    }

    @Override
    public boolean equals(Object obj) {
        // true si los ID son iguales
    }

    public JSONObject toJSONObject() { }

    @Override
    public String toString() {  }

}
