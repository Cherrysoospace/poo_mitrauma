package poo.model;

import org.json.JSONObject;

public class CanchaTenis extends InstalacionDeportiva {

    private TipoCancha tipoCancha;

    public CanchaTenis() { }

    public CanchaTenis(String id) { }

    public CanchaTenis(String id, double ancho, double largo, String descripcion, TipoCancha tipo, double valorHora) { }

    public CanchaTenis(JSONObject json) {
        // use TipoCancha.valueOf(...) para asignar el atributo tipoCancha
    }

    public CanchaTenis(CanchaTenis canchaTenis) { }

    public void setTipoCancha(TipoCancha tipo) { }

    public TipoCancha getTipoCancha() { }

    @Override
    public String getTipoInstalacion() {
        // devuelve el dato para humano de la constante enumerada
    }

    @Override
    public String toString() { }

}
