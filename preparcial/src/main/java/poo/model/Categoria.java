package poo.model;

public enum Categoria {
    ELECTRONICA("Electrónica"), 
    ALIMENTO ("Alimentos"),
    ROPA("Ropa");

    private final String value;

    private Categoria (String value) {
        this.value = value;

    }

    public String getValue () {
        return value;
    }

    public static Categoria getEnum (String value) throws IllegalArgumentException {
        if (value ==  null || value.isBlank()) {
            throw new IllegalArgumentException("El valor no puede ser nulo o vacio");
        }

        value = value.trim();

        for (Categoria c : values()) { //esto recorre cada uno de los enum
            if (c.getValue().equalsIgnoreCase(value)) { //el IgnoreCase implica que ignora si la entrada
                return c;                               //es minuscula o mayuscula.
            }
        }

        throw new IllegalArgumentException("No se encontro una categoria con el valor: " + value);


    }
}
