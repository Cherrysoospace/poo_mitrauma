package poo.model;

import org.json.JSONObject;
public class Socio {
    private String id;
    private String nombre;
    private String direccion;
    private String telefono;


    //CONSTRUCTOR COPIA

    public Socio (Socio s) {
        this(
            s.id, 
            s.nombre, 
            s.direccion, 
            s.telefono);
    }
    
    //CONSTRUCTOR CON PARAMETROS
    public Socio(String id, String nombre, String direccion, String telefono) throws IllegalArgumentException {
        setId(id);
        setNombre(nombre);
        setDireccion(direccion);
        setTelefono(telefono);
    }
    //CONSTRUCTOR POR DEFECTO
    public Socio() throws IllegalArgumentException {
        setId("00000");
        setNombre("NN");
        setDireccion("NN");
        setTelefono("0000000000");
    }
     /*Buena pregunta. 
     La palabra "opt" en los métodos optString(), optInt(), optBoolean(), etc., 
     viene de "opcional". Estos métodos pertenecen a la clase JSONObject 
     y sirven para extraer valores de un JSON sin generar excepciones ç
     si la clave no existe o tiene un tipo de dato inesperado.
    */

     //JSON
    public Socio (JSONObject json) {
        this (
            json.getString("id"),
            json.getString("nombre"),
            json.getString("direccion"),
            json.getString("telefono")
        );
    }

     /*public Socio (JSONObject json) {
        this.id = json.getString("id");
        this.nombre = json.getString("nombre");
        this.direccion = json.getString("direccion");
        this.telefono = json.getString("telefono");
     }*/

     public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    //ACCESORES
    public String getId() {
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getDireccion (){
        return direccion;
    }

    public String getTelefono (){
        return telefono;
    }

    //MUTADORES

    public void setId(String id) throws IllegalArgumentException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        }
        if (id.length() != 5) {
            throw new IllegalArgumentException("La identificación debe tener exactamente 5 carácteres.");
        }
        this.id = id;
    }

    public void setNombre(String nombre) throws IllegalArgumentException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        if (nombre.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }

        this.nombre = nombre;
    }
    
    // Se tendra en cuenta si la direccion es urbana o rural? No, sólo como un string simple.
    public void setDireccion (String direccion) throws IllegalArgumentException {

        //Por que se pone values en vez de value?
        //if (TipoDireccion.values(direccion) == null) {

        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }

        /*if ((!direccion.contains("#"))){
            throw new IllegalArgumentException("Para indicar el numero de residencia debe contener el símbolo '#'.");
        }?*/

        this.direccion = direccion;
    }

    public void setTelefono (String telefono) throws IllegalArgumentException {
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }

        if (telefono.length() != 10) {
            throw new IllegalArgumentException("El teléfono debe tener exactamente 10 dígitos.");
        }
        this.telefono = telefono;
    }
    

    //TOSTRING
    @Override
    public String toString() {
        return String.format("Identificación: %s\nNombre: %s\nDirección: %s\nTeléfono: +57 %s\n", 
        id, nombre, direccion, telefono);
    }

    //METODO .equals()
    @Override
    public boolean equals(Object obj) {
        // si obj es null, retorna falso
        // si el tipo de datos != Persona, retorna falso
        // si

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
}
