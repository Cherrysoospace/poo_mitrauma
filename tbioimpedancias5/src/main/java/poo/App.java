package poo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import poo.helpers.Utils;
import poo.model.ComposicionCorporal;
import poo.model.Persona;
import poo.helpers.Keyboard;

public class App {
    static ArrayList<Persona> personas = new ArrayList<>();
    static ArrayList<ComposicionCorporal> composicion = new ArrayList<>();
    

    public static void main(String[] args) {
        menu();
    }

    private static void menu() {

        try {
            inicializar();
        } catch (Exception e) {
            e.printStackTrace();
        }

        do {
            try {
                int opcion = leerOpcion();
                switch (opcion) {
                    case 1:
                        crearPersonas();
                        break;
                    case 2:
                        crearComposicion();
                        break;
                    case 3:
                        listarPersonas();
                        break;
                    case 4:
                        listarComposicionCorporal();
                        break;
                    case 5:
                        buscarPersonas();
                        break;
                    case 6:
                        buscarComposicion();
                        break;
                    case 7:
                        buscarComposicionDPersona();
                        break;
                    case 0:
                        salir();
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);

    }

    private static void inicializar() throws Exception {
        // throw new UnsupportedOperationException("Sin implementar App.inicializar()");
        System.out.print("\033[H\033[2J");

        // Separador de decimales: punto
        Locale.setDefault(Locale.of("es_CO"));
        // Para coma use: Locale.setDefault(Locale.US);

        inicializarPersonas();
        inicializarComposiciones();

        Persona p1 = personas.get(0);
        Persona p2 = p1;
        System.out.println("iguales = " + (p1 == p2));
    }

    // Crear la instancia del ArrayList <Persona>
    // Si existe personas.json entonces
    // Leer el archivo y crear un jsonArray con su contenido
    // Leer el archivo y crear un Json Array con su contenido
    // recorrer los elementos de JSON Array e ir creando instancias de persona
    // agregamos cada persona al ArrayList <persona>

    private static void inicializarPersonas() throws Exception {
        String fileName = "./data/personas.json";

        // Crear Instanica del ArrayList<Persona>
        personas = new ArrayList<>();

        // si existe personas.json entonces
        if (Utils.fileExists(fileName)) {
            Utils.readText(fileName);

            // Leer el archivo y crear un JSON.Array con su contenido
            JSONArray jsonArrary = new JSONArray(Utils.readText(fileName));

            // Recorrer los elementos de JSONArray e ir creando instancias de Pesona
            for (int i = 0; i < jsonArrary.length(); i++) {
                JSONObject json = jsonArrary.getJSONObject(i);
                // Persona p = new Persona(json);
                // Agregamos cada persona al ArrayList<Persona>
                personas.add(new Persona(json));
            }
        }
    }

    private static void inicializarComposiciones() throws Exception {
        String fileName = "./data/composiciones.json";
        composicion = new ArrayList<>();
    
        if (Utils.fileExists(fileName)) {
            JSONArray jsonArray = new JSONArray(Utils.readText(fileName));
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String idPersona = json.getString("persona"); // Ahora solo guardamos la ID
                
                Persona personaAsociada = buscarPersonaPorID(idPersona);
                if (personaAsociada == null) {
                    System.out.println("Advertencia: No se encontró la persona con ID " + idPersona);
                    continue; // O manejarlo de otra manera
                }
    
                ComposicionCorporal composicionCorporal = new ComposicionCorporal(json);
                composicionCorporal.setPersona(personaAsociada);
                composicion.add(composicionCorporal);
            }
        }
    }
    
    private static Persona buscarPersonaPorID(String id) {
        for (Persona p : personas) {
            if (p.getIdentificacion().equals(id)) {
                return p;
            }
        }
        return null;
    }

    private static List<ComposicionCorporal> obtenerComposicionesDePersona(String identificacion) {
        List<ComposicionCorporal> lista = new ArrayList<>();
    
        for (ComposicionCorporal c : composicion) { // "composiciones" debe ser una lista global
            if (c.getPersona().getIdentificacion().equals(identificacion)) {
                lista.add(c);
            }
        }
    
        return lista;
    }

    private static void salir() {
        // limpiar pantalla y salir
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.exit(0);
    }

    static int leerOpcion() {
        // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
        String opciones = String.format("\n%sMenú de opciones:%s\n", Utils.GREEN, Utils.RESET)
                + "  1 - Ingresar Persona\n" // check
                + "  2 - Ingresar Composiciones corporales\n" // check
                + "  3 - Listar Personas\n" // check
                + "  4 - Listar Composiciones Corporales\n" //check
                + "  5 - Buscar una persona\n" // 
                + "  6 - Buscar una composición corporal\n" //check
                + "  7 - Buscar las composiciones corporales de una persona\n"
                + String.format("  %s0 - Salir%s\n", Utils.RED, Utils.RESET) +
                String.format("\nElija una opción (%s0 para salir%s) > ", Utils.RED, Utils.RESET);

        int opcion = Keyboard.readInt(opciones);
        System.out.println();
        return opcion;
    }

    private static void crearPersonas() throws IOException {
        boolean agregar = false;
        Persona persona;
        System.out.println("Ingreso de personas");
    
        do {
            String identificacion = Keyboard.readString(5, 12, "Identificación (Intro para terminar): ");
            if (identificacion.isBlank()) {
                break; // Si el usuario presiona Intro, se sale del bucle
            }
    
            // Verificar si el ID ya existe en la lista
            boolean idExiste = personas.stream()
                .anyMatch(p -> p.getIdentificacion().equals(identificacion));
    
            if (idExiste) {
                System.out.println("El ID " + identificacion + " ya existe. Ingrese otro.");
                continue; // Vuelve a pedir una nueva identificación
            }
    
            String nombreCompleto = Keyboard.readString(1, 50, "Nombre Completo: ");
            LocalDate fechaNacimiento = Keyboard.readDate("Fecha de nacimiento (YYYY-MM-DD): ");
            char sexo = Keyboard.readChar("Ingrese el sexo (M/F): ");
            String correo = Keyboard.readString("Correo de contacto: ");
    
            persona = new Persona(identificacion, nombreCompleto, fechaNacimiento, sexo, correo);
            personas.add(persona);
    
            System.out.printf("Se agregó la persona con identificación %s%n", identificacion);
            agregar = true;
        } while (true);
    
        if (agregar) {
            Utils.writeJSON(personas, "./data/personas.json");
        }
    }

    /*
     * private static void crearPersona() throws Exception {
     * ArrayList<Persona> personas = new ArrayList<>();
     * 
     * do {
     * String identificacion = Keyboard.readString("Identificación: ");
     * if (identificacion.isBlank())
     * break;
     * String nombreCompleto = Keyboard.readString("Nombre completo: ");
     * // Para la fecha de nacimiento, lee los componentes individuales
     * LocalDate fechaNacimiento =
     * Keyboard.readDate("Fecha de nacimiento (YYYY-MM-DD): ");
     * int year = fechaNacimiento.getYear();
     * int month = fechaNacimiento.getMonthValue();
     * int day = fechaNacimiento.getDayOfMonth();
     * 
     * // Para el sexo, lee un String y extrae el primer carácter
     * String sexoStr = Keyboard.readString(1, 1,
     * "Ingrese su sexo (M/F): ").toUpperCase();
     * char sexo = sexoStr.charAt(0);
     * 
     * String correo = Keyboard.readString("Correo electrónico de contacto: ");
     * 
     * personas.add(new Persona(identificacion, nombreCompleto, year, month, day,
     * sexo, correo));
     * } while (true);
     * 
     * Utils.writeJSON(personas, "./data/personas.json");
     * }
     */

     private static void crearComposicion() throws Exception {
        ArrayList<ComposicionCorporal> composiciones = new ArrayList<>();
        String archivoComposiciones = "./data/composiciones.json";
    
        Persona persona = elegirPersona();
        if (persona == null) {
            System.out.println("Operación cancelada.");
            return;
        }
    
        LocalDate fechaRegistro = Keyboard.readDate("Ingrese la fecha de registro (YYYY-MM-DD): ");
        List<ComposicionCorporal> composicionesPrevias = obtenerComposicionesDePersona(persona.getIdentificacion());
    
        ComposicionCorporal composicion = new ComposicionCorporal(persona);
        composicion.setFechaRegistro(fechaRegistro, composicionesPrevias);
    
        double peso = Keyboard.readDouble(0.1, 500, "Peso (kg): ");
        composicion.setPeso(peso);
    
        int estatura = Keyboard.readInt(1, 300, "Estatura (cm): ");
        composicion.setEstatura(estatura);
    
        double masaMuscular = Keyboard.readDouble(0.1, 100, "Masa muscular (%): ");
        composicion.setMasaMuscular(masaMuscular);
    
        double estaturaEnMetros = composicion.conversionEstatura(estatura);
        composicion.setImc(peso, estaturaEnMetros);
    
        int grasaVisceral = Keyboard.readInt(1, 30, "Grasa visceral (nivel 1-30): ");
        composicion.setGrasaVisceral(grasaVisceral);
    
        double grasaCorporal = Keyboard.readDouble(0.1, 100, "Grasa corporal (%): ");
        composicion.setGrasaCorporal(grasaCorporal);
    
        char sexo = persona.getSexo();
        double limiteAgua = (sexo == 'F') ? 60 : 65;
        double aguaCorporal = Keyboard.readDouble(0.1, limiteAgua, "Agua corporal (%): ");
        composicion.setAguaCorporal(aguaCorporal);
    
        double proteinas = Keyboard.readDouble(0.1, 30, "Proteínas (%): ");
        composicion.setProteinas(proteinas);
    
        double minMasaOsea = (sexo == 'F') ? 2 : 3;
        double maxMasaOsea = (sexo == 'F') ? 3 : 5;
        double masaOsea = Keyboard.readDouble(minMasaOsea, maxMasaOsea,
                String.format("Masa ósea (kg) [%s: %.1f-%.1f]: ",
                        (sexo == 'F' ? "Mujeres" : "Hombres"), minMasaOsea, maxMasaOsea));
        composicion.setMasaOsea(masaOsea);
    
        System.out.println("\n--- Resumen de Composición Corporal ---");
        System.out.println("Persona: " + persona.getNombreCompleto());
        System.out.println(composicion);
    
        boolean guardar = Keyboard.readBoolean("\n¿Guardar esta composición corporal? (S/N): ");
        if (guardar) {
            if (Utils.fileExists(archivoComposiciones)) {
                JSONArray jsonArray = new JSONArray(Utils.readText(archivoComposiciones));
                for (int i = 0; i < jsonArray.length(); i++) {
                    composiciones.add(new ComposicionCorporal(jsonArray.getJSONObject(i)));
                }
            }
            composiciones.add(composicion);
            Utils.writeJSON(composiciones, archivoComposiciones);
            System.out.println("Composición corporal guardada exitosamente.");
        }
    }

    private static void listarComposicionCorporal() {
        String fileName = "./data/composiciones.json";
    
        if (!Utils.fileExists(fileName)) {
            System.out.println("No hay composiciones corporales registradas.");
            return;
        }
    
        try {
            JSONArray jsonArray = new JSONArray(Utils.readText(fileName));
            if (jsonArray.length() == 0) {
                System.out.println("No hay composiciones corporales registradas en el archivo.");
                return;
            }
    
            // Mapa para agrupar composiciones por persona
            Map<String, List<ComposicionCorporal>> composicionesPorPersona = new LinkedHashMap<>();
    
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ComposicionCorporal composicion = new ComposicionCorporal(json);
                Persona persona = composicion.getPersona();
                
                // Verificar si la persona es válida
                String nombrePersona = (persona != null) ? persona.getNombreCompleto() : "Desconocido";
    
                composicionesPorPersona.putIfAbsent(nombrePersona, new ArrayList<>());
                composicionesPorPersona.get(nombrePersona).add(composicion);
            }
    
            // Imprimir las composiciones organizadas por persona
            for (Map.Entry<String, List<ComposicionCorporal>> entry : composicionesPorPersona.entrySet()) {
                System.out.println("Persona: " + entry.getKey());
                List<ComposicionCorporal> listaComposiciones = entry.getValue();
                for (int j = 0; j < listaComposiciones.size(); j++) {
                    System.out.println("   Composición " + (j + 1) + ". " + listaComposiciones.get(j));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar composiciones corporales: " + e.getMessage());
        }
    }


    private static void buscarPersonas() {
        String identificacion = Keyboard.readString(5, 12, "Ingrese la identificación a buscar: ");
        Persona p = new Persona(identificacion);
        int i = personas.indexOf(p);
    
        if (i > -1) {
            System.out.println("\nDatos de la persona encontrada:");
            System.out.println(personas.get(i).toStringDetallado());
        } else {
            System.out.println("No se encontró una persona con ID " + identificacion);
        }
    }

    private static void buscarComposicion() throws IOException {
        String archivoComposiciones = "./data/composiciones.json";
        
        if (!Utils.fileExists(archivoComposiciones)) {
            System.out.println("No hay composiciones corporales registradas.");
            return;
        }
        
        String identificacion = Keyboard.readString(5, 12, "Ingrese la identificación de la persona: ");
        LocalDate fechaRegistro = Keyboard.readDate("Ingrese la fecha de registro (YYYY-MM-DD): ");
        
        JSONArray jsonArray = new JSONArray(Utils.readText(archivoComposiciones));
        boolean encontrada = false;
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            JSONObject personaJson = json.getJSONObject("persona");
            String idPersona = personaJson.getString("identificacion");
            String fechaJson = json.getString("fechaRegistro");
            
            if (idPersona.equals(identificacion) && fechaJson.equals(fechaRegistro.toString())) {
                System.out.println("\n--- Composición Corporal Encontrada ---");
                System.out.println(new ComposicionCorporal(json));
                encontrada = true;
                break;
            }
        }
        
        if (!encontrada) {
            System.out.println("No se encontró una composición corporal para la persona con ID " + identificacion + " en la fecha " + fechaRegistro);
        }
    }

    private static void listarPersonas() {
        String fileName = "./data/personas.json";
        if (!Utils.fileExists(fileName)) {
            System.out.println("No hay personas registradas.");
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(Utils.readText(fileName));
            if (jsonArray.length() == 0) {
                System.out.println("No hay personas registradas en el archivo.");
                return;
            }
            System.out.println("\n--- Lista de Personas ---");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Persona persona = new Persona(json);
                System.out.println(persona);
            }
        } catch (Exception e) {
            System.out.println("Error al listar personas: " + e.getMessage());
        }

    }

    private static void buscarComposicionDPersona() throws IOException {
        String archivoComposiciones = "./data/composiciones.json";
        
        if (!Utils.fileExists(archivoComposiciones)) {
            System.out.println("No hay composiciones corporales registradas.");
            return;
        }
        
        String identificacion = Keyboard.readString(5, 12, "Ingrese la identificación de la persona: ");
        
        JSONArray jsonArray = new JSONArray(Utils.readText(archivoComposiciones));
        boolean encontrada = false;
        
        System.out.println("\n--- Composiciones Corporales de la Persona con ID " + identificacion + " ---");
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            JSONObject personaJson = json.getJSONObject("persona");
            String idPersona = personaJson.getString("identificacion");
            
            if (idPersona.equals(identificacion)) {
                System.out.println(new ComposicionCorporal(json));
                encontrada = true;
            }
        }
        
        if (!encontrada) {
            System.out.println("No se encontraron composiciones corporales para la persona con ID " + identificacion);
        }
    }
    
    private static Persona elegirPersona() {
        while (true) {
            System.out.println("\n--- Personas disponibles ---");
            System.out.println("0 - Salir sin elegir");
            
            for (int i = 0; i < personas.size(); i++) {
                System.out.printf("%d - %s (%s)\n", i + 1, personas.get(i).getNombreCompleto(), personas.get(i).getIdentificacion());
            }
    
            int opcion = Keyboard.readInt(0, personas.size(), "Seleccione una persona (número): ");
            
            if (opcion == 0) {
                return null; // Opción para salir sin elegir
            }
            
            return personas.get(opcion - 1); // Retorna la persona seleccionada
        }
    }
}
