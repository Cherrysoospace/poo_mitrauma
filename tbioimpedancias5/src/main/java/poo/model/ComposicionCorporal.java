package poo.model;

import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import org.json.JSONObject;

public class ComposicionCorporal {
    private LocalDate fechaRegistro;
    private double peso;
    private int estatura;
    private Persona persona;
    private double masaMuscular;
    private double imc;
    private int grasaVisceral;
    private double grasaCorporal;
    private double aguaCorporal;
    private double proteinas;
    private double masaOsea;

    public ComposicionCorporal(JSONObject json) {
        this.fechaRegistro = LocalDate.parse(json.optString("fechaRegistro", LocalDate.now().toString()));
        this.peso = json.getDouble("peso");
        this.estatura = json.getInt("estatura");
        this.masaMuscular = json.getDouble("masaMuscular");
        this.grasaVisceral = json.getInt("grasaVisceral");
        this.grasaCorporal = json.getDouble("grasaCorporal");
        this.aguaCorporal = json.getDouble("aguaCorporal");
        this.proteinas = json.getDouble("proteinas");
        this.masaOsea = json.getDouble("masaOsea");
        this.imc = json.getDouble("imc");
    
        // Inicializar la persona correctamente
        if (json.has("persona")) {
            this.persona = new Persona(json.getJSONObject("persona"));
        } else {
            this.persona = null;
        }
    
    }
    
    public Persona getPersona() {
        return persona;
    }

    public ComposicionCorporal() {
        this.fechaRegistro = LocalDate.now();
        this.persona = new Persona();
        this.masaMuscular = 0.0;
        this.imc = 0.0;
        this.grasaVisceral = 1;
        this.grasaCorporal = 1;
        this.aguaCorporal = 1;
        this.proteinas = 0.0;
        this.masaOsea = 1;
    }

  

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("fechaRegistro", fechaRegistro.toString());
        json.put("persona", persona.toJSONObject()); // ✅ Guarda la persona correctamente
        json.put("peso", peso);
        json.put("estatura", estatura);
        json.put("masaMuscular", masaMuscular);
        json.put("grasaVisceral", grasaVisceral);
        json.put("clasificacionGrasaVisceral", getClasificarGrasaVisceral()); // ✅ Agregado
        json.put("grasaCorporal", grasaCorporal);
        json.put("aguaCorporal", aguaCorporal);
        json.put("proteinas", proteinas);
        json.put("masaOsea", masaOsea);
        json.put("imc", imc);
        json.put("clasificacionIMC", getClasificarImc()); // ✅ Agregado
        return json;
    }

    public ComposicionCorporal(Persona persona, LocalDate fechaRegistro) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula.");
        }
        if (fechaRegistro.isAfter(LocalDate.now())) {
            throw new DateTimeException("La fecha de registro no puede ser futura.");
        }
        
        this.persona = persona;
        this.fechaRegistro = fechaRegistro;
    }

    public ComposicionCorporal(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula.");
        }
        this.persona = persona;
    }

    public void setFechaRegistro(LocalDate fechaRegistro, List<ComposicionCorporal> composicionesPrevias) {
        LocalDate fechaActual = LocalDate.now();
        
        // Validar que la fecha no sea futura
        if (fechaRegistro.isAfter(fechaActual)) {
            throw new DateTimeException("La fecha de registro no puede ser posterior a la fecha actual.");
        }
        
        // Si hay composiciones previas, verificar que la nueva tenga al menos 5 días de diferencia con la última
        if (!composicionesPrevias.isEmpty()) {
            LocalDate ultimaFecha = composicionesPrevias.get(composicionesPrevias.size() - 1).getFechaRegistro();
            if (Period.between(ultimaFecha, fechaRegistro).getDays() < 5) {
                throw new DateTimeException("Debe haber al menos 5 días de diferencia entre registros consecutivos.");
            }
        }
        
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

   

    public void setPersona (Persona persona) {
        this.persona = persona;
    }
    

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        if (peso < -1) {
            throw new IllegalArgumentException("El peso debe ser mayor o igual a 0.");
        }
        this.peso = peso;
    }

    public int getEstatura() {
        return estatura;
    }

    public void setEstatura(int estatura) {
        if (estatura < 0) {
            throw new IllegalArgumentException("La estatura debe ser mayor o igual a 0.");
        }
        this.estatura = estatura;
    }

    public double conversionEstatura(int estatura) {
        double eMetros = (double) estatura / 100;
        return eMetros;
    }

    // IMC = peso (kg) / estatura (m)2
    public void setImc(double peso, double eMetros) {
        if (peso <= 0 || eMetros <= 0) {
            throw new IllegalArgumentException("El peso y la estatura deben ser valores positivos.");
        }
        this.imc = peso / (eMetros * eMetros);
    }

    public double getImc() {
        return imc;
    }

    public String getClasificarImc() {
        if (imc < 18.5) {
            return ClasiPesos.PESODEMASIADOBAJO.getValue();
        } else if (imc < 25) {
            return ClasiPesos.PESOSALUDABLE.getValue();
        } else if (imc < 30) {
            return ClasiPesos.SOBREPESO.getValue();
        } else {
            return ClasiPesos.OBESIDAD.getValue();
        }
    }
    

    public double getMasaMuscular () {
        return masaMuscular;
    }

    public void setMasaMuscular (double masaMuscular) {
        if (masaMuscular<0) {
            throw new IllegalArgumentException("La masa muscular debe ser mayor a 0.");
        }
        this.masaMuscular= masaMuscular;
    }

    public String clasificarMasaMuscular() {
        if (persona.getSexo() == '\0') {
            throw new IllegalStateException("Debe establecer primero el sexo de la persona.");
        }
        int edad = persona.getEdadEstimada();
        char sexo = persona.getSexo();

        if (sexo == 'F') {
            if (edad >= 18 && edad <= 30) {
                if (masaMuscular < 35)
                    return "Bajo";
                if (masaMuscular <= 41)
                    return "Normal";
                return "Alto";
            } else if (edad >= 31 && edad <= 60) {
                if (masaMuscular < 33)
                    return "Bajo";
                if (masaMuscular <= 38)
                    return "Normal";
                return "Alto";
            } else if (edad > 60) {
                if (masaMuscular < 28)
                    return "Bajo";
                if (masaMuscular <= 33)
                    return "Normal";
                return "Alto";
            }
        } else if (sexo == 'M') {
            if (edad >= 18 && edad <= 30) {
                if (masaMuscular < 43)
                    return "Bajo";
                if (masaMuscular <= 56)
                    return "Normal";
                return "Alto";
            } else if (edad >= 31 && edad <= 60) {
                if (masaMuscular < 41)
                    return "Bajo";
                if (masaMuscular <= 50)
                    return "Normal";
                return "Alto";
            } else if (edad > 60) {
                if (masaMuscular < 38)
                    return "Bajo";
                if (masaMuscular <= 57)
                    return "Normal";
                return "Alto";
            }
        }
        return "Clasificación no disponible";
    }

    public void setGrasaVisceral(int grasaVisceral) {
        if (grasaVisceral < 0) {
            throw new IllegalArgumentException("El nivel de grasa visceral no puede ser negativo.");
        }
        this.grasaVisceral = grasaVisceral;
    }

    public double getGrasaVisceral() {
        return grasaVisceral;
    }

    public String getClasificarGrasaVisceral() {
        if (grasaVisceral <= 5) {
            return "Excelente";
        } else if (grasaVisceral <= 10) {
            return "Bueno";
        } else if (grasaVisceral <= 15) {
            return "Aceptable";
        } else {
            return "Peligroso";
        }
    }

    public void setGrasaCorporal(double grasaCorporal) {
        if (grasaCorporal < 0 || grasaCorporal> 100) {
            throw new IllegalArgumentException("El porcentaje de grasa corporal debe estar entre 0 y 100.");
        }
        this.grasaCorporal = grasaCorporal;
    }

    public double getGrasaCorporal() {
        return grasaCorporal;
    }

    public String clasificarGrasaCorporal() {
        String clasificacion = "Clasificación no disponible";
    
        int edad = persona.getEdadEstimada();
        char sexo = persona.getSexo();
    
        if (sexo == 'F') {
            if (edad >= 18 && edad <= 39) {
                if (grasaCorporal < 17) clasificacion = "Demasiado bajo";
                else if (grasaCorporal <= 30) clasificacion = "Saludable";
                else if (grasaCorporal <= 36) clasificacion = "Sobrepeso";
                else clasificacion = "Obesidad";
            } else if (edad >= 40 && edad <= 59) {
                if (grasaCorporal < 22) clasificacion = "Demasiado bajo";
                else if (grasaCorporal <= 33) clasificacion = "Saludable";
                else if (grasaCorporal <= 40) clasificacion = "Sobrepeso";
                else clasificacion = "Obesidad";
            } else if (edad >= 60) {
                if (grasaCorporal < 23) clasificacion = "Demasiado bajo";
                else if (grasaCorporal <= 35) clasificacion = "Saludable";
                else if (grasaCorporal <= 41) clasificacion = "Sobrepeso";
                else clasificacion = "Obesidad";
            }
        } else if (sexo == 'M') {
            if (edad >= 18 && edad <= 39) {
                if (grasaCorporal < 8) clasificacion = "Demasiado bajo";
                else if (grasaCorporal <= 19) clasificacion = "Saludable";
                else if (grasaCorporal <= 23) clasificacion = "Sobrepeso";
                else clasificacion = "Obesidad";
            } else if (edad >= 40 && edad <= 59) {
                if (grasaCorporal < 10) clasificacion = "Demasiado bajo";
                else if (grasaCorporal <= 21) clasificacion = "Saludable";
                else if (grasaCorporal <= 27) clasificacion = "Sobrepeso";
                else clasificacion = "Obesidad";
            } else if (edad >= 60) {
                if (grasaCorporal < 12) clasificacion = "Demasiado bajo";
                else if (grasaCorporal <= 23) clasificacion = "Saludable";
                else if (grasaCorporal <= 29) clasificacion = "Sobrepeso";
                else clasificacion = "Obesidad";
            }
        }
    
        return clasificacion;
    }

    public double getAguaCorporal () {
        return aguaCorporal;
    }

    public void setAguaCorporal(double aguaCorporal) {
        if (aguaCorporal <= 0) {
            throw new IllegalArgumentException("El porcentaje de agua corporal no puede ser 0 o negativo.");
        }
        char sexo = persona.getSexo();
        if ((sexo == 'F' && aguaCorporal > 60) || (sexo == 'M' && aguaCorporal > 65)) {
            throw new IllegalArgumentException("El porcentaje de agua corporal excede el límite permitido para el sexo indicado.");
        }
        this.aguaCorporal = aguaCorporal;
    }

    public String clasificarAguaCorporal() {
        char sexo = persona.getSexo();
        if (sexo == 'F') {
            return (aguaCorporal >= 45 && aguaCorporal <= 60) ? "Normal" : "Fuera de rango";
        } else if (sexo == 'M') {
            return (aguaCorporal >= 50 && aguaCorporal <= 65) ? "Normal" : "Fuera de rango";
        }
        return "Clasificación no disponible";
    }

    public void setProteinas(double proteinas) {
        if (proteinas <= 0) {
            throw new IllegalArgumentException("El porcentaje de proteínas no puede ser 0 o negativo.");
        }
        this.proteinas = proteinas;
    }

    public double getProteinas() {
        return proteinas;
    }

    public String clasificarProteinas() {
        if (proteinas < 16) {
            return "Bajo";
        } else if (proteinas <= 20) {
            return "Normal";
        } else {
            return "Excesivo";
        }
    }

    public double getMasaOsea() {
        return masaOsea;
    }

    public void setMasaOsea(double masaOsea) {
        if (masaOsea <= 0) {
            throw new IllegalArgumentException("La masa ósea no puede ser 0 o negativa.");
        }
        if (persona == null) {
            throw new IllegalStateException("La persona no está inicializada en la composición corporal.");
        }
        char sexo = persona.getSexo();
        if ((sexo == 'F' && (masaOsea < 2 || masaOsea > 3)) || 
            (sexo == 'M' && (masaOsea < 3 || masaOsea > 5))) {
            throw new IllegalArgumentException(
                String.format("La masa ósea %.1f está fuera del rango permitido para %s (%.1f - %.1f)",
                    masaOsea, (sexo == 'F' ? "mujeres" : "hombres"), (sexo == 'F' ? 2.0 : 3.0), (sexo == 'F' ? 3.0 : 5.0)));
        }
        this.masaOsea = masaOsea;
    }

    public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    
    ComposicionCorporal that = (ComposicionCorporal) obj;
    
    return persona.getIdentificacion().equals(that.persona.getIdentificacion()) &&
           fechaRegistro.equals(that.fechaRegistro);
}

@Override
public int hashCode() {
    return Objects.hash(persona.getIdentificacion(), fechaRegistro);
}
    

    @Override
    public String toString() {
        return "\nFecha de Registro: " + fechaRegistro +
               "\nMasa Muscular (%): " + masaMuscular + " - " + clasificarMasaMuscular() +
               "\nIMC: " + getImc() + " - " + getClasificarImc() +
               "\nGrasa Visceral: " + grasaVisceral + " - " + getClasificarGrasaVisceral() +
               "\nGrasa Corporal (%): " + grasaCorporal + " - " + clasificarGrasaCorporal() +
               "\nAgua Corporal (%): " + aguaCorporal +
               "\nProteínas (%): " + proteinas + " - " + clasificarProteinas() +
               "\nMasa Ósea (%): " + masaOsea;
    }
}

