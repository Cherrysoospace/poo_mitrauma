package poo.listas;

public enum TipoCancha {
    CESPED("Césped"),
    LADRILLO("Ladrillo"),
    OTRO("Otro");

    public String value;

    private TipoCancha(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
