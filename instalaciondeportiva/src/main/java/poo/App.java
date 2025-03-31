package poo;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import poo.helpers.Keyboard;
import poo.helpers.Utils;
import poo.listas.TipoCancha;
import poo.model.Alquiler;
import poo.model.CanchaMultiproposito;
import poo.model.CanchaTennis;
import poo.model.Instalacion;
import poo.model.Piscina;
import poo.helpers.Color;
import java.time.format.DateTimeFormatter;
import poo.model.Socio;
import org.json.JSONArray;
import org.json.JSONObject;

public class App {
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
                       crearInstalacion();
                        break;
                    case 2:
                        crearSocio();
                        break;
                    case 3:
                        buscarInstalacionDeportiva();
                        break;
                    case 4:
                        crearAlquiler();
                        break;
                    case 5:
                        buscarAlquileresDeSocio();
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

    private static List<Socio> socios;
    private static List<Alquiler> alquileres;

    private static void inicializar() throws Exception {
        // limpiar la terminal
        System.out.print("\033[H\033[2J");

        // Separador de decimales: punto
        Locale.setDefault(Locale.of("es_CO"));
        inicializarInstalaciones();
        inicializarSocios();       // Make sure this is called!
        inicializarAlquileres();

    }

    private static void salir() {
        // limpiar pantalla y salir
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.exit(0);
    }

    static int leerOpcion() {
        // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
        String opciones = String.format("\n%sMenú de opciones:%s\n", Color.GREEN, Color.RESET) +
                "  1 - Crear Nueva Instalación Deportiva\n" + //check
                "  2 - Crear Nuevo Socio\n" + // cHeck
                "  3 - Buscar Instalación Deportiva\n" + //check
                "  4 - Alquilar Instalación\n" + //check
                "  5 - Buscar Alquileres de un Socio\n" +
                String.format("\nElija una opción (%s0 para salir%s) > ", Color.RED, Color.RESET);

        int opcion = Keyboard.readInt(opciones);
        System.out.println();
        return opcion;
    }

    // ZONA INSTALACION----------------------------------------------------------
    // INICIALIZADOR DE INSTALACIONES
    
    // Declara la lista de instalaciones
private static List<Instalacion> instalaciones;

// INICIALIZADOR DE INSTALACIONES
private static void inicializarInstalaciones() throws Exception {
    String fileName = "./data/instalaciones.json";

    // Crear Instancia del ArrayList<Instalacion>
    instalaciones = new ArrayList<>();

    if (Utils.fileExists(fileName)) {
        // Leer el archivo y crear un JSONArray con su contenido
        JSONArray jsonArray = new JSONArray(Utils.readText(fileName));

        // Recorrer los elementos de JSONArray e ir creando instancias según el tipo
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String tipo = json.getString("tipo");
            
            switch (tipo) {
                case "Piscina":
                    instalaciones.add(new Piscina(json));
                    break;
                case "CanchaTennis":
                    instalaciones.add(new CanchaTennis(json));
                    break;
                case "CanchaMultiproposito":
                    instalaciones.add(new CanchaMultiproposito(json));
                    break;
                default:
                    System.out.println("Tipo de instalación desconocido: " + tipo);
            }
        }
    }
}

// CREAR INSTALACION
private static void crearInstalacion() throws Exception {
    boolean agregar = false;
    Instalacion instalacion = null;
    Map<String, Double> preciosHora = cargarPreciosHora();
    
    System.out.println(Color.YELLOW + "---Ingreso de datos de instalación---" + Color.RESET);

    do {
        // Menú de tipo de instalación
        System.out.println("Seleccione el tipo de instalación:");
        System.out.println("1 - Piscina");
        System.out.println("2 - Cancha de Tennis");
        System.out.println("3 - Cancha Multipropósito");
        System.out.println("0 - Cancelar");
        
        int tipoInstalacion = Keyboard.readInt("Opción: ");
        
        if (tipoInstalacion == 0) {
            break;
        }
        
        if (tipoInstalacion < 1 || tipoInstalacion > 3) {
            System.out.println(Color.RED + "Opción inválida" + Color.RESET);
            continue;
        }
        
        // Pedir el ID
        String numId = Keyboard.readString("Ingrese el ID (5 caracteres, ENTER para cancelar): ");
        if (numId.isBlank()) {
            break;
        }
        
        // Validar longitud del ID
        if (numId.length() != 5) {
            System.out.println(Color.RED + "El ID debe tener exactamente 5 caracteres." + Color.RESET);
            continue;
        }
        
        // Verificar si el ID ya existe
        boolean idExiste = instalaciones.stream()
                .anyMatch(i -> i.getNumId().equals(numId));
                
        if (idExiste) {
            System.out.println(Color.RED + "El ID " + numId + " ya existe. Ingrese otro." + Color.RESET);
            continue;
        }
        
        // Pedir descripción
        String descripcion = Keyboard.readString("Ingrese la descripción (mínimo 15 caracteres): ");
        if (descripcion.length() < 15) {
            System.out.println(Color.RED + "La descripción debe tener al menos 15 caracteres." + Color.RESET);
            continue;
        }
        
        // Pedir dimensiones
        double dimensionAn = Keyboard.readDouble("Ingrese el ancho (metros): ");
        if (dimensionAn <= 0) {
            System.out.println(Color.RED + "El ancho debe ser mayor que cero." + Color.RESET);
            continue;
        }
        
        double dimensionLar = Keyboard.readDouble("Ingrese el largo (metros): ");
        if (dimensionLar <= 0) {
            System.out.println(Color.RED + "El largo debe ser mayor que cero." + Color.RESET);
            continue;
        }
        
        // Crear la instalación según el tipo seleccionado
        switch (tipoInstalacion) {
            case 1: // Piscina
                boolean esOlimpica = Keyboard.readBoolean("¿Es piscina olímpica? (s/n): ");
                double precioPiscina = preciosHora.getOrDefault("piscina", 0.0);
                instalacion = new Piscina(numId, descripcion, precioPiscina, dimensionAn, dimensionLar, esOlimpica);
                break;
                
            case 2: // Cancha de Tennis
                System.out.println("Seleccione el tipo de cancha:");
                System.out.println("1 - CESPED");
                System.out.println("2 - LADRILLO");
                System.out.println("3 - OTRO");
                int tipoCancha = Keyboard.readInt("Opción: ");
                
                TipoCancha tipo;
                switch (tipoCancha) {
                    case 1:
                        tipo = TipoCancha.CESPED;
                        break;
                    case 2:
                        tipo = TipoCancha.LADRILLO;
                        break;
                    case 3:
                        tipo = TipoCancha.OTRO;
                        break;
                    default:
                        System.out.println(Color.RED + "Opción inválida, se usará CESPED por defecto." + Color.RESET);
                        tipo = TipoCancha.CESPED;
                        break;
                }
                
                double precioCanchaTennis = preciosHora.getOrDefault("canchaTennis", 0.0);
                instalacion = new CanchaTennis(numId, descripcion, precioCanchaTennis, dimensionAn, dimensionLar, tipo);
                break;
                
            case 3: // Cancha Multipropósito
                boolean tieneGraderia = Keyboard.readBoolean("¿Tiene gradería? (s/n): ");
                double precioCanchaMulti = preciosHora.getOrDefault("canchaMultiproposito", 0.0);
                instalacion = new CanchaMultiproposito(numId, descripcion, precioCanchaMulti, dimensionAn, dimensionLar, tieneGraderia);
                break;
        }
        
        instalaciones.add(instalacion);
        
        System.out.println(Color.GREEN + "¡Se ha agregado la instalación con ID: " + numId + "!" + Color.RESET);
        System.out.println(instalacion);
        
        agregar = true;
        
        if (!Keyboard.readBoolean("¿Desea agregar otra instalación? (s/n): ")) {
            break;
        }
        
    } while (true);
    
    // Guardar las instalaciones en el archivo JSON si se agregó alguna
    if (agregar) {
        guardarInstalaciones();
    }
}

// Método auxiliar para guardar instalaciones en el archivo
private static void guardarInstalaciones() throws Exception {
    // Crear un JSONArray con todas las instalaciones
    JSONArray jsonArray = new JSONArray();
    
    for (Instalacion instalacion : instalaciones) {
        JSONObject json = instalacion.toJSONObject();
        
        // Agregar el tipo de instalación al JSON
        if (instalacion instanceof Piscina) {
            json.put("tipo", "Piscina");
        } else if (instalacion instanceof CanchaTennis) {
            json.put("tipo", "CanchaTennis");
        } else if (instalacion instanceof CanchaMultiproposito) {
            json.put("tipo", "CanchaMultiproposito");
        }
        
        jsonArray.put(json);
    }
    
    // Guardar el JSONArray en el archivo
    Utils.writeText(jsonArray.toString(2), "./data/instalaciones.json");
}

// Método para cargar los precios por hora desde el archivo JSON
private static Map<String, Double> cargarPreciosHora() throws Exception {
    Map<String, Double> precios = new HashMap<>();
    String fileName = "./data/precioHoraInstalacion.json";
    
    if (Utils.fileExists(fileName)) {
        JSONObject json = new JSONObject(Utils.readText(fileName));
        
        // Extraer los precios para cada tipo de instalación
        if (json.has("piscina")) {
            precios.put("piscina", json.getDouble("piscina"));
        }
        
        if (json.has("canchaTennis")) {
            precios.put("canchaTennis", json.getDouble("canchaTennis"));
        }
        
        if (json.has("canchaMultiproposito")) {
            precios.put("canchaMultiproposito", json.getDouble("canchaMultiproposito"));
        }
    } else {
        System.out.println(Color.RED + "No se encontró el archivo de precios. Se usarán valores por defecto." + Color.RESET);
        // Valores por defecto en caso de que no exista el archivo
        precios.put("piscina", 15000.0);
        precios.put("canchaTennis", 7000.0);
        precios.put("canchaMultiproposito", 5000.0);
    }
    
    return precios;
}
  
 

    // ZONA SOCIO----------------------------------------------------------
    // INICIALIZADOR DE SOCIO
    private static void inicializarSocios() throws Exception {
        String fileName = "./data/socios.json";
    
        // Crear instancia del ArrayList<Socio>
        socios = new ArrayList<>();
    
        if (Utils.fileExists(fileName)) {
            // Leer el archivo y crear un JSON.Array con su contenido
            JSONArray jsonArray = new JSONArray(Utils.readText(fileName));
    
            // Recorrer los elementos de JSONArray e ir creando instancias de Socio
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                
                // Crear el socio con el constructor JSON o con los datos individuales
                Socio socio = new Socio(json);
                socios.add(socio);
            }
        }
    }

    // CREAR NUEVO SOCIO
    private static void crearSocio() throws Exception {
        // Verificar si la lista es nula e inicializarla si es necesario
        if (socios == null) {
            socios = new ArrayList<>();
        }
        
        boolean agregar = false;
        Socio socio;
        System.out.println(Color.YELLOW + "---Ingreso de datos de socio.---" + Color.RESET);
    

        do {
            String id = Keyboard.readString("Ingrese el ID (ENTER Termina): ");
            if (id.isBlank()) {
                break;
            }

            // Verificar si el ID ya existe en la lista
            boolean idExiste = socios.stream()
                    .anyMatch(p -> p.getId().equals(id));

            if (idExiste) {
                System.out.println(Color.RED + "El ID " + id + " ya existe. Ingrese otro." + Color.RESET);
                continue; // Vuelve a pedir una nueva identificación
            }

            String nombre = Keyboard.readString("Ingrese el Nombre Completo: ");
            String direccion = Keyboard.readString("Ingrese la dirección: ");
            String telefono = Keyboard.readString("Ingrese el teléfono: ");

            socio = new Socio(id, nombre, direccion, telefono);
            socios.add(socio);

            System.out.println(Color.GREEN + "Se agregó el socio con id: " + id + "!" + Color.RESET);

            agregar = true;
        } while (true);

        if (agregar) {
            Utils.writeJSON(socios, "./data/socios.json");
        }

    }

    /// ZONA ALQUILER----------------------------------------------------------
    // INICIALIZADOR DE ALQUILERES

    private static void inicializarAlquileres() throws Exception {
        String fileName = "./data/alquileres.json";
    
        // Crear Instancia del ArrayList<Alquiler>
        alquileres = new ArrayList<>();
    
        if (Utils.fileExists(fileName)) {
            // Leer el archivo y crear un JSON.Array con su contenido
            JSONArray jsonArray = new JSONArray(Utils.readText(fileName));
    
            // Recorrer los elementos de JSONArray e ir creando instancias de Alquiler
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                
                // Crear el alquiler con el constructor JSON
                Alquiler alquiler = new Alquiler(json);
                
                // Buscar la instalación correspondiente
                String instalacionId = json.getJSONObject("instalacion").getString("numId");
                Instalacion instalacion = buscarInstalacionPorId(instalacionId);
                alquiler.setInstalacion(instalacion);
                
                alquileres.add(alquiler);
            }
        }
    }
    
    // Método auxiliar para buscar una instalación por su ID
    private static Instalacion buscarInstalacionPorId(String id) {
        for (Instalacion instalacion : instalaciones) {
            if (instalacion.getNumId().equals(id)) {
                return instalacion;
            }
        }
        return null; // O lanzar una excepción si no se encuentra
    }



// Método auxiliar para buscar un socio por ID
private static Socio buscarSocioPorId(String id) {
    for (Socio socio : socios) {
        if (socio.getId().equals(id)) {
            return socio;
        }
    }
    return null;
}

private static void crearAlquiler() throws Exception {
    boolean agregar = false;
    
    System.out.println(Color.YELLOW + "---Creación de nuevo alquiler---" + Color.RESET);
    
    // Verificar si hay instalaciones y socios disponibles
    if (instalaciones.isEmpty()) {
        System.out.println(Color.RED + "No hay instalaciones registradas. Debe crear al menos una instalación primero." + Color.RESET);
        return;
    }
    
    if (socios.isEmpty()) {
        System.out.println(Color.RED + "No hay socios registrados. Debe crear al menos un socio primero." + Color.RESET);
        return;
    }
    
    do {
        // Mostrar lista de instalaciones disponibles
        System.out.println("\nInstalaciones disponibles:");
        for (int i = 0; i < instalaciones.size(); i++) {
            Instalacion inst = instalaciones.get(i);
            System.out.printf("%d - %s: %s (%.2f por hora)\n", 
                i + 1, 
                inst.getNumId(), 
                inst.getDescripcion(),
                inst.getPrecioHora());
        }
        
        int opcionInstalacion = Keyboard.readInt("\nSeleccione una instalación (0 para cancelar): ");
        if (opcionInstalacion == 0) {
            break;
        }
        
        if (opcionInstalacion < 1 || opcionInstalacion > instalaciones.size()) {
            System.out.println(Color.RED + "Opción inválida" + Color.RESET);
            continue;
        }
        
        Instalacion instalacionSeleccionada = instalaciones.get(opcionInstalacion - 1);
        
        // Mostrar lista de socios
        System.out.println("\nSocios registrados:");
        for (int i = 0; i < socios.size(); i++) {
            Socio s = socios.get(i);
            System.out.printf("%d - %s: %s\n", i + 1, s.getId(), s.getNombre());
        }
        
        int opcionSocio = Keyboard.readInt("\nSeleccione un socio (0 para cancelar): ");
        if (opcionSocio == 0) {
            break;
        }
        
        if (opcionSocio < 1 || opcionSocio > socios.size()) {
            System.out.println(Color.RED + "Opción inválida" + Color.RESET);
            continue;
        }
        
        Socio socioSeleccionado = socios.get(opcionSocio - 1);
        
        // Leer fecha
        String fechaStr = Keyboard.readString("Ingrese la fecha (formato YYYY-MM-DD): ");
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr);
            
            // Verificar que la fecha no sea anterior a hoy
            if (fecha.isBefore(LocalDate.now())) {
                System.out.println(Color.RED + "La fecha no puede ser anterior a hoy." + Color.RESET);
                continue;
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Formato de fecha inválido. Use YYYY-MM-DD." + Color.RESET);
            continue;
        }
        
        // Leer hora de inicio
        String horaInicioStr = Keyboard.readString("Ingrese hora de inicio (formato HH:MM): ");
        LocalTime horaInicio;
        try {
            horaInicio = LocalTime.parse(horaInicioStr);
        } catch (Exception e) {
            System.out.println(Color.RED + "Formato de hora inválido. Use HH:MM." + Color.RESET);
            continue;
        }
        
        // Leer cantidad de horas
        int cantidadHoras = Keyboard.readInt("Ingrese la cantidad de horas: ");
        if (cantidadHoras <= 0) {
            System.out.println(Color.RED + "La cantidad de horas debe ser positiva." + Color.RESET);
            continue;
        }
        
        // Convertir a LocalDateTime para compatibilidad con la clase Alquiler
        LocalDateTime fechaHoraInicio = LocalDateTime.of(fecha, horaInicio);
        LocalDateTime fechaHoraFin = fechaHoraInicio.plusHours(cantidadHoras);
        
        // Verificar disponibilidad (no hay otro alquiler para la misma instalación, fecha y rango de horas)
        boolean disponible = true;
        
        for (Alquiler alq : alquileres) {
            if (alq.getInstalacion().equals(instalacionSeleccionada)) {
                LocalDateTime alqInicio = alq.getFechaHoraInicio();
                LocalDateTime alqFin = alq.getFechaHoraFin();
                
                // Verificar si hay solapamiento
                if ((fechaHoraInicio.isBefore(alqFin) || fechaHoraInicio.isEqual(alqFin)) && 
                    (fechaHoraFin.isAfter(alqInicio) || fechaHoraFin.isEqual(alqInicio))) {
                    disponible = false;
                    break;
                }
            }
        }
        
        if (!disponible) {
            System.out.println(Color.RED + "La instalación no está disponible en ese horario." + Color.RESET);
            continue;
        }
        
        // Generar ID para el alquiler (podría ser algo más sofisticado en una aplicación real)
        String alquilerId = generateAlquilerId();
        
        // Calcular precio total (aunque no aparece en la clase Alquiler actual)
        double precioTotal = instalacionSeleccionada.getPrecioHora() * cantidadHoras;
        System.out.println("Precio total: $" + precioTotal);
        
        // Crear el alquiler usando el constructor que tiene la clase
        Alquiler nuevoAlquiler = new Alquiler(alquilerId, instalacionSeleccionada, socioSeleccionado, fechaHoraInicio, fechaHoraFin);
        alquileres.add(nuevoAlquiler);
        
        System.out.println(Color.GREEN + "¡Alquiler creado con éxito!" + Color.RESET);
        System.out.println(nuevoAlquiler);
        
        agregar = true;
        
        if (!Keyboard.readBoolean("¿Desea crear otro alquiler? (s/n): ")) {
            break;
        }
        
    } while (true);
    
    // Guardar los alquileres si se agregó alguno
    if (agregar) {
        guardarAlquileres();
    }
}

// Método para generar un ID único para el alquiler
private static String generateAlquilerId() {
    // Generar un ID de 5 caracteres
    if (alquileres.isEmpty()) {
        return "A0001";
    } else {
        // Obtener el último ID y aumentarlo en 1
        int lastId = Integer.parseInt(alquileres.get(alquileres.size() - 1).getId().substring(1));
        return String.format("A%04d", lastId + 1);
    }
}

// Método para guardar alquileres en el archivo JSON
private static void guardarAlquileres() throws Exception {
    JSONArray jsonArray = new JSONArray();
    
    for (Alquiler alquiler : alquileres) {
        JSONObject json = new JSONObject();
        json.put("id", alquiler.getId());
        
        // Guardar solo el ID de la instalación
        JSONObject instJson = new JSONObject();
        instJson.put("numId", alquiler.getInstalacion().getNumId());
        json.put("instalacion", instJson);
        
        // Guardar el socio completo o su ID
        json.put("socio", new JSONObject()
            .put("id", alquiler.getSocio().getId())
            .put("nombre", alquiler.getSocio().getNombre())
            .put("direccion", alquiler.getSocio().getDireccion())
            .put("telefono", alquiler.getSocio().getTelefono()));
        
        // Guardar fechas y horas
        json.put("fechaHoraInicio", alquiler.getFechaHoraInicio().toString());
        json.put("fechaHoraFin", alquiler.getFechaHoraFin().toString());
        
        jsonArray.put(json);
    }
    
    // Guardar el JSONArray en el archivo
    Utils.writeText(jsonArray.toString(2), "./data/alquileres.json");
}
   // Implementar la opción 3 del menú: Buscar Instalación Deportiva
private static void buscarInstalacionDeportiva() {
    System.out.println(Color.YELLOW + "---Búsqueda de Instalación Deportiva---" + Color.RESET);
    
    // Verificar si hay instalaciones registradas
    if (instalaciones == null || instalaciones.isEmpty()) {
        System.out.println(Color.RED + "No hay instalaciones deportivas registradas." + Color.RESET);
        return;
    }
    
    String idBusqueda = Keyboard.readString("Ingrese el ID de la instalación a buscar (5 caracteres): ");
    
    // Validar el formato del ID
    if (idBusqueda.length() != 5) {
        System.out.println(Color.RED + "El ID debe tener exactamente 5 caracteres." + Color.RESET);
        return;
    }
    
    // Buscar la instalación por su ID
    Instalacion instalacionEncontrada = null;
    for (Instalacion inst : instalaciones) {
        if (inst.getNumId().equals(idBusqueda)) {
            instalacionEncontrada = inst;
            break;
        }
    }
    
    // Mostrar resultados
    if (instalacionEncontrada != null) {
        System.out.println(Color.GREEN + "Instalación encontrada:" + Color.RESET);
        System.out.println(instalacionEncontrada);
        
        // Mostrar información adicional según el tipo de instalación
        if (instalacionEncontrada instanceof Piscina) {
            Piscina piscina = (Piscina) instalacionEncontrada;
            System.out.println("Tipo: Piscina");
            System.out.println("Es olímpica: " + (piscina.getPiscinaOlimpica() ? "Sí" : "No"));
        } else if (instalacionEncontrada instanceof CanchaTennis) {
            CanchaTennis canchaTennis = (CanchaTennis) instalacionEncontrada;
            System.out.println("Tipo: Cancha de Tennis");
            System.out.println("Tipo de cancha: " + canchaTennis.getTipoCancha());
        } else if (instalacionEncontrada instanceof CanchaMultiproposito) {
            CanchaMultiproposito canchaMulti = (CanchaMultiproposito) instalacionEncontrada;
            System.out.println("Tipo: Cancha Multipropósito");
            System.out.println("Tiene gradería: " + (canchaMulti.getGraderia() ? "Sí" : "No"));
        }
        
        // Mostrar alquileres asociados a esta instalación
        mostrarAlquileresDeInstalacion(instalacionEncontrada);
    } else {
        System.out.println(Color.RED + "No se encontró ninguna instalación con el ID: " + idBusqueda + Color.RESET);
    }
}

// Método auxiliar para mostrar los alquileres asociados a una instalación
private static void mostrarAlquileresDeInstalacion(Instalacion instalacion) {
    if (alquileres == null || alquileres.isEmpty()) {
        System.out.println("No hay alquileres registrados para esta instalación.");
        return;
    }
    
    boolean tieneAlquileres = false;
    System.out.println("\nAlquileres asociados a esta instalación:");
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    for (Alquiler alq : alquileres) {
        if (alq.getInstalacion().getNumId().equals(instalacion.getNumId())) {
            System.out.printf("- ID: %s, Socio: %s, Fecha: %s a %s\n",
                alq.getId(),
                alq.getSocio().getNombre(),
                alq.getFechaHoraInicio().format(formatter),
                alq.getFechaHoraFin().format(formatter));
            tieneAlquileres = true;
        }
    }
    
        if (!tieneAlquileres) {
            System.out.println("No hay alquileres registrados para esta instalación.");
         }
    }
    // Implementar la opción 5 del menú: Buscar Alquileres de un Socio
private static void buscarAlquileresDeSocio() {
    System.out.println(Color.YELLOW + "---Búsqueda de Alquileres por Socio---" + Color.RESET);
    
    // Verificar si hay socios y alquileres registrados
    if (socios == null || socios.isEmpty()) {
        System.out.println(Color.RED + "No hay socios registrados en el sistema." + Color.RESET);
        return;
    }
    
    if (alquileres == null || alquileres.isEmpty()) {
        System.out.println(Color.RED + "No hay alquileres registrados en el sistema." + Color.RESET);
        return;
    }
    
    // Mostrar lista de socios para seleccionar
    System.out.println("\nSocios registrados:");
    for (int i = 0; i < socios.size(); i++) {
        Socio s = socios.get(i);
        System.out.printf("%d - %s: %s\n", i + 1, s.getId(), s.getNombre());
    }
    
    // Opción de buscar por ID o seleccionar de la lista
    boolean buscarPorId = Keyboard.readBoolean("¿Desea buscar por ID? (s/n): ");
    
    Socio socioSeleccionado = null;
    
    if (buscarPorId) {
        String idBusqueda = Keyboard.readString("Ingrese el ID del socio (5 caracteres): ");
        
        // Validar formato del ID
        if (idBusqueda.length() != 5) {
            System.out.println(Color.RED + "El ID debe tener exactamente 5 caracteres." + Color.RESET);
            return;
        }
        
        // Buscar el socio por su ID
        socioSeleccionado = buscarSocioPorId(idBusqueda);
        
        if (socioSeleccionado == null) {
            System.out.println(Color.RED + "No se encontró ningún socio con el ID: " + idBusqueda + Color.RESET);
            return;
        }
    } else {
        int opcionSocio = Keyboard.readInt("\nSeleccione un socio (0 para cancelar): ");
        
        if (opcionSocio == 0) {
            return;
        }
        
        if (opcionSocio < 1 || opcionSocio > socios.size()) {
            System.out.println(Color.RED + "Opción inválida" + Color.RESET);
            return;
        }
        
        socioSeleccionado = socios.get(opcionSocio - 1);
    }
    
    // Mostrar información del socio seleccionado
    System.out.println(Color.GREEN + "\nDatos del socio seleccionado:" + Color.RESET);
    System.out.println(socioSeleccionado);
    
    // Buscar los alquileres asociados al socio
    List<Alquiler> alquileresSocio = new ArrayList<>();
    
    for (Alquiler alq : alquileres) {
        if (alq.getSocio().getId().equals(socioSeleccionado.getId())) {
            alquileresSocio.add(alq);
        }
    }
    
    // Mostrar resultados
    if (alquileresSocio.isEmpty()) {
        System.out.println(Color.RED + "El socio no tiene alquileres registrados." + Color.RESET);
    } else {
        System.out.println(Color.MAGENTA_BOLD + "\nAlquileres encontrados para " + socioSeleccionado.getNombre() + ":" + Color.RESET);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        double totalGastado = 0.0;
        
        // Tabla de encabezado
        System.out.println("\n" + "+---------+---------------+--------------------+----------------------+----------------------+-------------+");
        System.out.println("| ID Alq. | ID Instalación | Descripción        | Fecha/Hora Inicio    | Fecha/Hora Fin       | Precio      |");
        System.out.println("+---------+---------------+--------------------+----------------------+----------------------+-------------+");
        
        for (Alquiler alq : alquileresSocio) {
            Instalacion inst = alq.getInstalacion();
            // Calcular precio del alquiler
            int horas = alq.getHorasCalculadas();
            double precio = inst.getPrecioHora() * horas;
            totalGastado += precio;
            
            // Limitar descripción a 18 caracteres para mantener formato de tabla
            String descripcion = inst.getDescripcion();
            if (descripcion.length() > 18) {
                descripcion = descripcion.substring(0, 15) + "...";
            }
            
            System.out.printf("| %-7s | %-13s | %-18s | %-20s | %-20s | $%-10.2f |\n",
                alq.getId(),
                inst.getNumId(),
                descripcion,
                alq.getFechaHoraInicio().format(formatter),
                alq.getFechaHoraFin().format(formatter),
                precio);
        }
        
        System.out.println("+---------+---------------+--------------------+----------------------+----------------------+-------------+");
        System.out.printf("Total de alquileres: %d | Valor total: $%.2f\n", alquileresSocio.size(), totalGastado);
    }
    
    // Preguntar si desea ver el detalle de algún alquiler específico
    if (!alquileresSocio.isEmpty() && Keyboard.readBoolean("\n¿Desea ver el detalle de algún alquiler específico? (s/n): ")) {
        String idAlquiler = Keyboard.readString("Ingrese el ID del alquiler a consultar: ");
        
        boolean encontrado = false;
        for (Alquiler alq : alquileresSocio) {
            if (alq.getId().equals(idAlquiler)) {
                System.out.println(Color.GREEN + "\nDetalle del alquiler:" + Color.RESET);
                System.out.println(alq);
                
                // Mostrar información adicional de la instalación
                Instalacion inst = alq.getInstalacion();
                System.out.println("Detalles de la instalación:");
                System.out.println("- Dimensiones: " + inst.getDimensionAn() + "m × " + inst.getDimensionLar() + "m");
                System.out.println("- Precio por hora: $" + inst.getPrecioHora());
                
                if (inst instanceof Piscina) {
                    Piscina piscina = (Piscina) inst;
                    System.out.println("- Tipo: Piscina");
                    System.out.println("- Es olímpica: " + (piscina.getPiscinaOlimpica() ? "Sí" : "No"));
                } else if (inst instanceof CanchaTennis) {
                    CanchaTennis canchaTennis = (CanchaTennis) inst;
                    System.out.println("- Tipo: Cancha de Tennis");
                    System.out.println("- Tipo de cancha: " + canchaTennis.getTipoCancha());
                } else if (inst instanceof CanchaMultiproposito) {
                    CanchaMultiproposito canchaMulti = (CanchaMultiproposito) inst;
                    System.out.println("- Tipo: Cancha Multipropósito");
                    System.out.println("- Tiene gradería: " + (canchaMulti.getGraderia() ? "Sí" : "No"));
                }
                
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            System.out.println(Color.RED + "No se encontró ningún alquiler con el ID: " + idAlquiler + Color.RESET);
        }
    }
}

}