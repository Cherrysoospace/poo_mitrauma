package poo.model;


import poo.helpers.Utils;
import org.json.JSONObject;

public abstract class InstalacionDeportiva {

    protected String id;
    protected double ancho;
    protected double largo;
    protected double valorHora;
    protected String descripcion;
    protected double costoFuncionamiento;


    // CONSTRUCTOR POR DEFECTO
    public InstalacionDeportiva() {
        // sólo un ID de 5 caracteres aleatorios y la descripción "Instalación deportiva sin descripción"
        setId(Utils.getRandomKey(5));
        setAncho(2.5);
        setLargo(2.5);
        setValorHora(2000);
        setDescripcion("Instalación deportiva sin descripción");
    }

    //CONSTRUCTOR COPIA
    public InstalacionDeportiva (InstalacionDeportiva i) {
        this(
            i.id,
            i.ancho,
            i.largo,
            i.valorHora,
            i.descripcion
               );

    }

    // CONSTRUCTOR POR ID
    public InstalacionDeportiva(String id) { 
        this.id = id;
        this.descripcion = descripcion;
    }


     // CONSTRUCTOR CON PARAMETROS
    public InstalacionDeportiva(String id, double ancho, double largo, double valorHora, String descripcion) {
       setId(id);
       setAncho(ancho);
       setLargo(largo);
       setValorHora(valorHora);
       setDescripcion(descripcion);
     }

     //JSON
    public InstalacionDeportiva(JSONObject json) { 
        this(
                json.getString("id"),
                json.getDouble("ancho"),
                json.getDouble("largo"),
                json.getDouble("valorHora"),
                json.getString("descripcion"));
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
     }

    //public InstalacionDeportiva(InstalacionDeportiva instalacion) { }


     //ACCESORES
    public String getId() { 
        return id;
    }
    public double getAncho() { 
        return ancho;
    }

    public double getLargo() { 
        return largo;
    }

    public double getValorHora() { 
        return valorHora;
    }

    public String getDescripcion() { 
        return descripcion;
    }



     //MODIFICADORES
    public void setId(String id) {  
        if ((id == null) || (id.isBlank())) {
            throw new IllegalArgumentException("El ID de la instalación no puede ser vacío o nulo.");
        }

        if (id.length() != 5) {
            throw new IllegalArgumentException("El ID de la instalación debe tener 5 caracteres.");
        }
        this.id = id;
    }

    public void setAncho(double ancho) {
        if (ancho <= 0) {
            throw new IllegalArgumentException("El ancho no puede ser menor o igual a 0.");
        }
       
        this.ancho = ancho; 
    }
    
    public void setLargo(double largo) {
       if (largo <= 0) {
            throw new IllegalArgumentException("El largo no puede ser menor o igual a 0.");
        }
         
        this.largo = largo; 
    }

    public void setValorHora(double valorHora) {
        // El valor de la hora no puede ser menor que 1000
        if (valorHora < 1000) {
            throw new IllegalArgumentException("El valor de la hora no puede ser menor que 1000.");
        }
        this.valorHora = valorHora;
    }


    public void setDescripcion(String descripcion) {
        // no aceptar null, blancos o longitudes menores que 10 o mayores que 200
        if ((descripcion == null) || (descripcion.isBlank())) {
            throw new IllegalArgumentException("La descripción no puede ser vacía ni nula.");
        }

        if (descripcion.length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 15 caracteres.");
        }

        if (descripcion.length() > 200) {
            throw new IllegalArgumentException("La descripción no puede tener maás de 200 caracteres.");
        }
        this.descripcion = descripcion;
    }

    public static InstalacionDeportiva getInstance(JSONObject json) {
        // utilizar json.has("keyName") para condicionar la creación y retorno de la instancia correcta: Piscina, CanchaTenis o CanchaMultiproposito
        // en otro caso generar IllegalArgumentException("Se esperaba una subclase de InstalacionDeportiva")
        if (json.has("olimpica")) {
            return new Piscina(json);
        } 
        else if(json.has("tipoCancha")) {
            return new CanchaTennis(json);
        } 
        else if (json.has("graderia")) {
            return new CanchaMultiproposito(json);
        } else {
            throw new IllegalArgumentException("Se esperaba una subclase de InstalacionDeportiva");
        }
    }

    public abstract String getTipoInstalacion();

    //COMPORTAMIENTOS

    public double getArea() {
        double area = ancho * largo;
        return area;
    }

    public abstract double getCostoFuncionamiento(JSONObject tarifas);

    //EQUALS
    @Override
    public String toString() {
        return String.format(
                "Tipo Instalacion: %s\nId Instalación: %s\nAncho: %.2f\n Largo: %.2f\n Area: %.2f m²\nPrecio por hora: %.2f\nDescripción: %s\n",
                getTipoInstalacion(), id, ancho, largo, getArea(), valorHora, descripcion);
    }

    //EQUALS
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        InstalacionDeportiva i = (InstalacionDeportiva) obj;

        return this.id.equals(i.id) && this.descripcion.equals(i.descripcion);
    }
}
