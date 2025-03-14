package poo.model;

import org.json.JSONArray;
import org.json.JSONObject;

import poo.model.Categoria;

public class Producto {
    private String id;
    private String nombre;
    private double precioBase;
    private Categoria categoria;

    //Constructor por defecto
    public Producto () {
        this("P001", "Default", 0, Categoria.ALIMENTO);
    }

    public Producto (String id) {
        this();
        setId(id);
    }
        //Constructor Parametrizado
    public Producto (String id, String nombre, double precioBase, Categoria categoria) {
        setId(id);
        setNombre(nombre);
        setPrecioBase(precioBase); //se usa el set para que herede las validaciones, 
        setCategoria(categoria);    //en lugar del this.
    }

    //Constructor Copia
    public Producto (Producto e) {
        this(e.id, e.nombre, e.precioBase, e.categoria);
    }

    //Constructor JSON
    public Producto (JSONObject json) {
        this(json.getString("id"), json.getString("nombre"), json.getDouble("precioBase"), json.getEnum(Categoria.class, "categoria"));
    }

    //¿Cuándo se utiliza el .isBlank y cuando es el .isEmpty()?
    public void setId (String id) {
        if (id == null || id.isBlank() || id.length()!=4) {
            throw new IllegalArgumentException("El id no puede ser nulo ni vacio");
        }
        this.id = id;
    }

    public String getId () {
        return id;
    }

    public void setNombre (String nombre) {
        if (nombre == null || nombre.isBlank() || nombre.length()<3) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio, ni longitud menor a 3.");
        }
        this.nombre = nombre;
    }

    public String getNombre () {
        return nombre;
    }

    public void setPrecioBase (double precioBase) {

        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo. Intenta de nuevo.");
        }
        this.precioBase = precioBase;
    }

    public double getPrecioBase () {
        return precioBase;
    }

    public void setCategoria (Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula. Intenta de nuevo.");
        }
        this.categoria = categoria;
    }

    public Categoria getCategoria () {
        return categoria;
    }

    public double getTasaImpuesto () {
        if (categoria == categoria.ELECTRONICA) {
            return 0.19;
        } else if (categoria == categoria.ROPA){
            return 0.10;
        } else {
            return 0.05;
        }
    }
    //NOTA: SIEMPRE AGREGAR EL GET O NO APARECE EN EL JSON
    public double getPrecioFinal () {
        double impuesto = precioBase *getTasaImpuesto();
        return precioBase + impuesto;
    }


    /*Para hacer saltos de linea tambien se puede hacer uso del %n
    //pero solo se puede cuando es un String.format o soutf*/

    @Override
    public String toString() {
        // TODO Auto-generated method stub

        return String.format("Producto (ID): %s\n Nombre: %s - Precio Base: $%.2f\n - Categoria: %s \n - Tasa Impuesto: %.2f\n- Precio final: $%.2f\n", id,
            nombre, precioBase, categoria.getValue(), getTasaImpuesto(), getPrecioFinal());
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Producto producto = (Producto) obj;

        return this.id.equals(producto.getId());
    }
}
