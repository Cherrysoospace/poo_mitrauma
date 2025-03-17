package poo.model;

public enum ClasiPesos {
    /*
     * Elija una de las siguientes opciones:
    Peso demasiado bajo
    Peso saludable
    Sobrepeso
    Obesidad

     */
    PESODEMASIADOBAJO("Peso demasiado bajo"), 
    PESOSALUDABLE("Peso Normal"), 
    SOBREPESO("Sobrepeso"),
    OBESIDAD("Obesidad");

    
    private final String value;

    private ClasiPesos(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }

    public static ClasiPesos getEnum(String value) throws IllegalArgumentException {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El valor no puede ser nulo o vacio");
        }

        value = value.trim();

        for (ClasiPesos c : values()) { //esto recorre cada uno de los enum
            if (c.getValue().equalsIgnoreCase(value)) { //el IgnoreCase implica que ignora si la entrada
                return c;                               //es minuscula o mayuscula.
            }
        }

        throw new IllegalArgumentException("No se encontro una categoria con el valor: " + value);
    }

}
