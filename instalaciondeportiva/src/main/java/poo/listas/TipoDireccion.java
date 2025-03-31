package poo.listas;

public enum TipoDireccion {
    AVENIDA ("AV."),
    CARRERA ("CRA."),
    CALLE("CLL."),
    MANZANA("MZ."),
    CALLEJON("CLJ."),
    CONJUNTO("CONJ."),
    TORRE("TOR."),
    EDIFICIO("EDIF.");

    public String value;
    
    private TipoDireccion(String value) {
        this.value = value;
    }
}
