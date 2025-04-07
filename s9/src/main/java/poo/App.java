package poo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import poo.helpers.Keyboard;
import poo.helpers.Utils;
import poo.listas.TipoCancha;
import poo.model.*;
import poo.helpers.Color;

public class App {

    private static JSONObject config; // configuración con tarifas y rutas de archivos
    private static JSONObject tarifas; // las tarifas de las diferentes instalaciones
    private static JSONObject archivos; // los nombres de los archivos que se utilizan

    private static List<Socio> socios;
    private static List<InstalacionDeportiva> piscinas;
    private static List<InstalacionDeportiva> canchasTennis;
    private static List<InstalacionDeportiva> canchasMultiproposito;
    private static List<Alquiler> alquileres;

    public static void main(String[] args) throws Exception {
        menu();
    }

    private static void menu() throws Exception {

        try {
            inicializar();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * Consultar una instalación deportiva por su ID,
         * Consultar los alquileres de un socio.
         */

        do {
            try {
                int opcion = leerOpcion();
                switch (opcion) {
                    case 1:
                        crearSocios();
                        break;
                    case 2:
                        listarSocios();
                        break;
                    case 3:
                        crearInstalaciones();
                        break;
                    case 4:
                        listarInstalaciones(piscinas);
                        break;
                    case 5:
                        listarInstalaciones(canchasTennis);
                        break;
                    case 6:
                        listarInstalaciones(canchasMultiproposito);
                        break;
                    case 7:
                        alquilarInstalaciones();
                        break;
                    case 8:
                        listarAlquileres();
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
        // limpiar la terminal
        System.out.print("\033[H\033[2J");

        // Separador de decimales: punto
        Locale.setDefault(Locale.of("es_CO"));

        config = new JSONObject(Utils.readText("./data/config.json"));
        tarifas = config.getJSONObject("tarifas");
        archivos = config.getJSONObject("archivos");

        inicializarSocios();
        inicializarInstalaciones(CanchaTennis.class);
        inicializarInstalaciones(Piscina.class);
        inicializarInstalaciones(CanchaMultiproposito.class);
        inicializarAlquileres();
    }

    private static void inicializarSocios() throws Exception {
        String nombreArchivo = archivos.getString("socios");
        socios = new ArrayList<>();
        if (Utils.fileExists(nombreArchivo)) {
            JSONArray jsonArr = new JSONArray(Utils.readText(nombreArchivo));
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                socios.add(new Socio(jsonObj));
            }
        }
    }
    
    private static void inicializarInstalaciones(Class<? extends InstalacionDeportiva> c) throws Exception {
        String nombreArchivo;
        
        try {
            if (Piscina.class.isAssignableFrom(c)) {
                nombreArchivo = archivos.getString("piscina");
                System.out.println(Color.CYAN_UNDERLINED+"Intentando cargar piscinas desde: " + nombreArchivo+Color.RESET);
                piscinas = new ArrayList<>();
                if (Utils.fileExists(nombreArchivo)) {
                    System.out.println(Color.YELLOW_BOLD_BRIGHT+ "Archivo de piscinas encontrado."+Color.RESET);
                    String contenido = Utils.readText(nombreArchivo);
                    System.out.println("Contenido leído: " + (contenido == null ? Color.RED + "null" + Color.RESET : Color.GREEN_BOLD_BRIGHT + "OK"+ Color.RESET));
                    JSONArray jsonArr = new JSONArray(contenido);
                    System.out.println("JSONArray creado con " + jsonArr.length() + " elementos.");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        piscinas.add(new Piscina(jsonObj));
                    }
                    System.out.println(Color.GREEN_BOLD_BRIGHT+ "Se cargaron " + piscinas.size() + " piscinas."+Color.RESET);
                } else {
                    System.out.println(Color.RED+ "Archivo de piscinas no encontrado."+ Color.RESET);
                }
            } else if (CanchaTennis.class.isAssignableFrom(c)) {
                nombreArchivo = archivos.getString("canchaTennis");
                System.out.println(Color.GREEN_UNDERLINED+"Intentando cargar canchas de tenis desde: " + nombreArchivo+Color.RESET);
                canchasTennis = new ArrayList<>();
                if (Utils.fileExists(nombreArchivo)) {
                    System.out.println(Color.YELLOW_BOLD_BRIGHT+"Archivo de canchas de tenis encontrado."+Color.RESET);
                    String contenido = Utils.readText(nombreArchivo);
                    System.out.println("Contenido leído: " + (contenido == null ? "null" : "OK"));
                    JSONArray jsonArr = new JSONArray(contenido);
                    System.out.println("JSONArray creado con " + jsonArr.length() + " elementos.");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        canchasTennis.add(new CanchaTennis(jsonObj));
                    }
                    System.out.println(Color.GREEN_BOLD_BRIGHT+"Se cargaron " + canchasTennis.size() + " canchas de tenis."+Color.RESET);
                } else {
                    System.out.println(Color.RED+"Archivo de canchas de tenis no encontrado."+Color.RESET);
                }
            } else if (CanchaMultiproposito.class.isAssignableFrom(c)) {
                nombreArchivo = archivos.getString("canchaMultiproposito");
                System.out.println(Color.MAGENTA_UNDERLINED+"Intentando cargar canchas multipropósito desde: " + nombreArchivo+Color.RESET);
                canchasMultiproposito = new ArrayList<>();
                if (Utils.fileExists(nombreArchivo)) {
                    System.out.println(Color.YELLOW_BOLD_BRIGHT+"Archivo de canchas multipropósito encontrado."+Color.RESET);
                    String contenido = Utils.readText(nombreArchivo);
                    System.out.println("Contenido leído: " + (contenido == null ? "null" : "OK"));
                    JSONArray jsonArr = new JSONArray(contenido);
                    System.out.println("JSONArray creado con " + jsonArr.length() + " elementos.");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        canchasMultiproposito.add(new CanchaMultiproposito(jsonObj));
                    }
                    System.out.println(Color.GREEN_BOLD_BRIGHT+"Se cargaron " + canchasMultiproposito.size() + " canchas multipropósito."+Color.RESET);
                } else {
                    System.out.println(Color.RED+"Archivo de canchas multipropósito no encontrado."+Color.RESET);
                }
            } else {
                throw new IllegalArgumentException("Se esperaba una subclase de InstalacionDeportiva");
            }
        } catch (Exception e) {
            System.out.println("Error al inicializar instalaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void inicializarAlquileres() throws Exception {
        String nombreArchivo = archivos.getString("alquileres");
        alquileres = new ArrayList<>();
        if (Utils.fileExists(nombreArchivo)) {
            JSONArray jsonArr = new JSONArray(Utils.readText(nombreArchivo));
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                alquileres.add(new Alquiler(jsonObj));
            }
        }
    }

    private static void crearInstalaciones() throws IOException {
        Utils.readText("./data/config.json");
        String nombreArchivo = "";
        boolean agregar = false;
        InstalacionDeportiva instalacion = null;
        List<InstalacionDeportiva> instalaciones = null;
        System.out.println("Ingreso de instalaciones deportivas\n");
        
        do {
            int tipo = Keyboard.readInt(0, 3, "Seleccione el tipo de instalación [1-Piscina 2-Tenis 3-Mutipropósito -0-Salir]: ");
            
            if (tipo == 0) {
                break;
            } else if (tipo == 1) {
                nombreArchivo = archivos.getString("piscina");
                // Inicializar piscinas si es null
                if (piscinas == null) {
                    piscinas = new ArrayList<>();
                }
                instalaciones = piscinas;
                instalacion = new Piscina();
                Piscina p = (Piscina) instalacion;
                p.setOlimpica(Keyboard.readBoolean("¿Es olímpica? [S/N]: "));
                p.setValorHora(tarifas.getDouble("piscina"));
            } else if (tipo == 2) {
                nombreArchivo = archivos.getString("canchaTennis");
                // Inicializar canchasTennis si es null
                if (canchasTennis == null) {
                    canchasTennis = new ArrayList<>();
                }
                instalaciones = canchasTennis;
                instalacion = new CanchaTennis();
                CanchaTennis ct = (CanchaTennis) instalacion;
                ct.setTipoCancha(Keyboard.readEnum(TipoCancha.class, "Tipo de cancha [1.Césped - 2. Ladrillo - 3. Otro]: "));
                ct.setValorHora(tarifas.getDouble("canchaTennis"));
            } else if (tipo == 3) {
                nombreArchivo = archivos.getString("canchaMultiproposito");
                // Inicializar canchasMultiproposito si es null
                if (canchasMultiproposito == null) {
                    canchasMultiproposito = new ArrayList<>();
                }
                instalaciones = canchasMultiproposito;
                instalacion = new CanchaMultiproposito();
                CanchaMultiproposito cmp = (CanchaMultiproposito) instalacion;
                cmp.setGraderia(Keyboard.readBoolean("¿Tiene gradería? [S/N]: "));
                cmp.setValorHora(tarifas.getDouble("canchaMultiproposito"));
            } else {
                throw new IllegalArgumentException("Tipo de instalación no válido");
            }
            
            instalacion.setAncho(Keyboard.readDouble("Ancho: "));
            instalacion.setLargo(Keyboard.readDouble("Largo: "));
            instalacion.setDescripcion(Keyboard.readString("Descripcion: "));
            
            instalaciones.add(instalacion);
            System.out.printf("Se agregó una instalación con ID: %s%n%n", instalacion.getId());
            agregar = true;
        } while (true);
        
        if (agregar) {
            Utils.writeJSON(instalaciones, nombreArchivo);
        }
    }

    private static void crearSocios() throws IOException {
        Utils.readText("./data/config.json");
        System.out.println("Ingreso de nuevo socio\n");
        
        // Obtener la ruta del archivo desde config.json
        String nombreArchivo = archivos.getString("socios");
        
        // Inicializar la lista de socios si es null
        if (socios == null) {
            socios = new ArrayList<>();
        } else {
            // Si la lista ya existe, verificar si el archivo JSON existe y cargar datos
            if (Utils.fileExists(nombreArchivo) && socios.isEmpty()) {
                JSONArray jsonArr = new JSONArray(Utils.readText(nombreArchivo));
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    socios.add(new Socio(jsonObj));
                }
            }
        }
        
        boolean agregar = false;
        
        do {
            String id = Keyboard.readString("Ingrese ID (5 caracteres o Enter para volver al menú): ").trim();
            
            if (id.isEmpty()) {
                System.out.println("Operación cancelada.\n");
                break;
            }
            
            // Verificar que el ID no exista ya
            boolean existe = false;
            for (Socio s : socios) {
                if (s.getId().equalsIgnoreCase(id)) {
                    System.out.printf("Ya existe un socio con el ID: %s%n%n", id);
                    existe = true;
                    break;
                }
            }
            
            if (existe) {
                continue; // pedir nuevo ID
            }
            
            String nombre = Keyboard.readString("Nombre: ");
            String direccion = Keyboard.readString("Dirección: ");
            String telefono = Keyboard.readString("Teléfono: ");
            
            Socio nuevoSocio = new Socio(id, nombre, direccion, telefono);
            socios.add(nuevoSocio);
            
            System.out.println(Color.GREEN_BOLD_BRIGHT + "Socio creado con ID: \n" + id + Color.RESET);
            
            agregar = true;
            
        } while (Keyboard.readBoolean("¿Desea agregar otro socio? [S/N]: "));
        
        // Si se agregó al menos un socio, guardar la lista completa en el archivo JSON
        if (agregar) {
            // Asegurar que la carpeta exista
            String pathArchivo = Utils.getPath(nombreArchivo);
            if (pathArchivo != null) {
                Utils.createFolderIfNotExist(pathArchivo);
            }
            
            // Guardar la lista completa en el archivo JSON (sin sobreescribir los datos)
            Utils.writeJSON(socios, nombreArchivo);
        }
    }

    public static void alquilarInstalaciones() throws Exception {
        // elegirSocio() y salir si null
        Socio socio = elegirSocio();
        if (socio == null) {
            return;
        }
    
        // elegir elegirInstalacionDeportiva() y salir si null
        InstalacionDeportiva instalacion = elegirInstalacionDeportiva();
        if (instalacion == null) {
            return;
        }
    
        // leer la hora de inicio y de finalización
        System.out.println(Color.MAGENTA_UNDERLINED + "\n---Datos del alquiler---\n" + Color.RESET);
        
        // Obtener la fecha y hora de inicio
        System.out.println("Fecha y hora de inicio:");
        LocalDateTime horaInicio = Keyboard.readDateTime("Ingrese fecha y hora de inicio (yyyy-MM-dd HH:mm): ");
        
        // Obtener la fecha y hora de finalización
        System.out.println("\nFecha y hora de finalización:");
        LocalDateTime horaFin = Keyboard.readDateTime("Ingrese fecha y hora de finalización (yyyy-MM-dd HH:mm): ");
        
        // Verificar si hay cruce con otro alquiler
        if (hayCruce(instalacion, horaInicio, horaFin)) {
            System.out.printf("%nNo se pudo registrar el alquiler:%n");
            System.out.printf(
                    "La instalación \"%s - %s\" está ocupada en ese horario%n%n",
                    instalacion.getId(), instalacion.getTipoInstalacion());
        } else {
            // crear la instancia de Alquiler con el constructor parametrizado
            String idAlquiler = Utils.getRandomKey(5);
            Alquiler nuevoAlquiler = new Alquiler(idAlquiler, horaInicio, horaFin, socio, instalacion);
            
            // Inicializar la lista de alquileres si es null
            if (alquileres == null) {
                alquileres = new ArrayList<>();
            }
            
            // agregar la nueva instancia a alquileres
            alquileres.add(nuevoAlquiler);
            
            // actualizar el archivo json
            String nombreArchivo = archivos.getString("alquileres");
            Utils.writeJSON(alquileres, nombreArchivo);
            
            // informar al usuario del éxito de la operación
            System.out.println(Color.GREEN_BOLD_BRIGHT + "\n¡Alquiler registrado exitosamente!" + Color.RESET);
            System.out.printf("ID del alquiler: %s%n", idAlquiler);
            System.out.printf("Socio: %s - %s%n", socio.getId(), socio.getNombre());
            System.out.printf("Instalación: %s - %s%n", instalacion.getId(), instalacion.getDescripcion());
            System.out.printf("Fecha y hora de inicio: %s%n", horaInicio);
            System.out.printf("Fecha y hora de fin: %s%n", horaFin);
            System.out.printf("Duración en horas: %d%n", nuevoAlquiler.getHoras());
            System.out.printf("Valor del alquiler: $%.2f%n%n", nuevoAlquiler.getValorAlquiler());
        }
    }

    private static Socio elegirSocio() {
        // Verificar si hay socios registrados
        if (socios == null || socios.isEmpty()) {
            System.out.println(Color.RED + "No hay socios registrados en el sistema." + Color.RESET);
            System.out.println("Primero debe crear al menos un socio (opción 1).\n");
            return null;
        }
        
        System.out.println(Color.MAGENTA_UNDERLINED + "\n---Selección de socio---\n" + Color.RESET);
        
        // Imprimir encabezado de la tabla
        System.out.printf("%-10s %-30s %-30s %-15s%n", "ID", "NOMBRE", "DIRECCIÓN", "TELÉFONO");
        System.out.println("─".repeat(85));
        
        // Imprimir cada socio con un número de opción
        int i = 1;
        for (Socio socio : socios) {
            System.out.printf("%3d. %-6s %-30s %-30s %-15s%n", 
                i++, 
                socio.getId(), 
                socio.getNombre(), 
                socio.getDireccion(), 
                socio.getTelefono());
        }
        
        System.out.println("\nTotal de socios: " + socios.size() + "\n");
        
        // Solicitar al usuario que elija un socio o cancelar (0)
        int opcion = Keyboard.readInt(0, socios.size(), "Seleccione un socio (0 para cancelar): ");
        
        // Si el usuario elige 0, retornar null (no se seleccionó ningún socio)
        if (opcion == 0) {
            System.out.println("Operación cancelada.\n");
            return null;
        }
        
        // Retornar el socio seleccionado
        Socio socioSeleccionado = socios.get(opcion - 1);
        System.out.printf("%nSe ha seleccionado al socio: %s - %s%n%n", 
            socioSeleccionado.getId(), socioSeleccionado.getNombre());
        
        return socioSeleccionado;
    }

    private static InstalacionDeportiva elegirInstalacionDeportiva() {
        // Verificar si hay instalaciones deportivas registradas
        boolean hayPiscinas = piscinas != null && !piscinas.isEmpty();
        boolean hayCanchasTennis = canchasTennis != null && !canchasTennis.isEmpty();
        boolean hayCanchasMultiproposito = canchasMultiproposito != null && !canchasMultiproposito.isEmpty();
        
        if (!hayPiscinas && !hayCanchasTennis && !hayCanchasMultiproposito) {
            System.out.println(Color.RED + "No hay instalaciones deportivas registradas en el sistema." + Color.RESET);
            System.out.println("Primero debe crear al menos una instalación (opción 3).\n");
            return null;
        }
        
        System.out.println(Color.MAGENTA_UNDERLINED + "\n---Selección de instalación deportiva---\n" + Color.RESET);
        
        // Solicitar al usuario que elija el tipo de instalación
        System.out.println("Tipos de instalaciones disponibles:");
        int opcionTipo = 0;
        int contador = 1;
        
        if (hayPiscinas) {
            System.out.printf("%d. Piscinas (%d disponibles)\n", contador++, piscinas.size());
        }
        
        if (hayCanchasTennis) {
            System.out.printf("%d. Canchas de tenis (%d disponibles)\n", contador++, canchasTennis.size());
        }
        
        if (hayCanchasMultiproposito) {
            System.out.printf("%d. Canchas multipropósito (%d disponibles)\n", contador++, canchasMultiproposito.size());
        }
        
        // Agregar opción para cancelar
        System.out.printf("%d. Cancelar\n\n", contador);
        
        // Leer la opción seleccionada
        opcionTipo = Keyboard.readInt(1, contador, "Seleccione el tipo de instalación: ");
        
        // Si el usuario selecciona la última opción (cancelar)
        if (opcionTipo == contador) {
            System.out.println("Operación cancelada.\n");
            return null;
        }
        
        // Determinar qué lista de instalaciones usar
        List<InstalacionDeportiva> listaSeleccionada = null;
        String tipoInstalacion = "";
        contador = 1;
        
        if (hayPiscinas) {
            if (contador == opcionTipo) {
                listaSeleccionada = piscinas;
                tipoInstalacion = "Piscina";
            }
            contador++;
        }
        
        if (hayCanchasTennis && listaSeleccionada == null) {
            if (contador == opcionTipo) {
                listaSeleccionada = canchasTennis;
                tipoInstalacion = "Cancha de tenis";
            }
            contador++;
        }
        
        if (hayCanchasMultiproposito && listaSeleccionada == null) {
            if (contador == opcionTipo) {
                listaSeleccionada = canchasMultiproposito;
                tipoInstalacion = "Cancha multipropósito";
            }
        }
        
        // Mostrar la lista de instalaciones del tipo seleccionado
        System.out.printf("\nInstalaciones de tipo: %s\n\n", tipoInstalacion);
        System.out.printf("%-5s %-10s %-15s %-15s %-15s %s\n", 
                        "N°", "ID", "ANCHO", "LARGO", "VALOR/HORA", "DESCRIPCIÓN");
        System.out.println("─".repeat(100));
        
        for (int i = 0; i < listaSeleccionada.size(); i++) {
            InstalacionDeportiva instalacion = listaSeleccionada.get(i);
            System.out.printf("%3d. %-8s %-15.2f %-15.2f %-15.2f %s\n", 
                            i + 1, 
                            instalacion.getId(), 
                            instalacion.getAncho(), 
                            instalacion.getLargo(), 
                            instalacion.getValorHora(),
                            instalacion.getDescripcion().substring(0, Math.min(40, instalacion.getDescripcion().length())) + 
                            (instalacion.getDescripcion().length() > 40 ? "..." : ""));
        }
        
        // Solicitar al usuario que elija una instalación o cancelar (0)
        int opcionInstalacion = Keyboard.readInt(0, listaSeleccionada.size(), 
                                              "\nSeleccione una instalación (0 para cancelar): ");
        
        // Si el usuario elige 0, retornar null (no se seleccionó ninguna instalación)
        if (opcionInstalacion == 0) {
            System.out.println("Operación cancelada.\n");
            return null;
        }
        
        // Retornar la instalación seleccionada
        InstalacionDeportiva instalacionSeleccionada = listaSeleccionada.get(opcionInstalacion - 1);
        System.out.printf("%nSe ha seleccionado la instalación: %s - %s%n%n", 
                        instalacionSeleccionada.getId(), instalacionSeleccionada.getDescripcion());
        
        return instalacionSeleccionada;
    }

    public static boolean hayCruce(InstalacionDeportiva instalacion, LocalDateTime horaInicio, LocalDateTime horaFin) {
        // Verificar que los alquileres estén inicializados
        if (alquileres == null || alquileres.isEmpty()) {
            return false; // No hay cruces si no hay alquileres previos
        }
        
        // Recorrer todos los alquileres existentes
        for (Alquiler alquiler : alquileres) {
            // Verificar si el alquiler es para la misma instalación
            if (alquiler.getInstalacionDeportiva().getId().equals(instalacion.getId())) {
                // Verificar si hay solapamiento de horarios usando Utils.OverlapSchedules
                if (Utils.OverlapSchedules(
                        horaInicio, horaFin,
                        alquiler.getFechaHoraInicio(), alquiler.getFechaHoraFin())) {
                    return true; // Hay cruce de horarios
                }
            }
        }
        
        // Si no se encontró ningún cruce después de recorrer todos los alquileres
        return false;
    }

    // -------- listados -----------

    private static void listarSocios() {
        System.out.println(Color.MAGENTA_UNDERLINED + "\n---Listado de socios.---\n"+ Color.RESET);
        
        // Verificar si hay socios registrados
        if (socios == null || socios.isEmpty()) {
            System.out.println("No hay socios registrados en el sistema.\n");
            return;
        }
        
        // Imprimir encabezado de la tabla
        System.out.printf("%-10s %-30s %-45s %-40s%n", "ID", "NOMBRE", "DIRECCIÓN", "TELÉFONO");
        System.out.println("─".repeat(100));
        
        // Imprimir cada socio
        for (Socio socio : socios) {
            System.out.printf("%-10s %-30s %-40s %-40s%n", 
                socio.getId(), 
                socio.getNombre(), 
                socio.getDireccion(), 
                socio.getTelefono());
        }
        
        System.out.println("\nTotal de socios: " + socios.size() + "\n");
    }

    public static void listarInstalaciones(List<InstalacionDeportiva> instalaciones) {
        // Determinar qué tipo de instalación estamos listando
        String tipoInstalacion = "";
        if (instalaciones != null && !instalaciones.isEmpty()) {
            tipoInstalacion = instalaciones.get(0).getTipoInstalacion();
        }
        
        System.out.println(Color.MAGENTA_UNDERLINED + "\n---Listado de " + tipoInstalacion + "s---\n" + Color.RESET);
        
        // Verificar si hay instalaciones registradas
        if (instalaciones == null || instalaciones.isEmpty()) {
            System.out.println("No hay instalaciones de este tipo registradas en el sistema.\n");
            return;
        }
        
        // Imprimir encabezado de la tabla
        System.out.printf("%-10s %-15s %-15s %-15s %-40s %s\n", 
                         "ID", "ANCHO", "LARGO", "VALOR/HORA", "DESCRIPCIÓN", "CARACTERÍSTICAS");
        System.out.println("─".repeat(110));
        String nombreIns = "";
        
        // Imprimir cada instalación
        for (InstalacionDeportiva instalacion : instalaciones) {
            String caracteristicas = "";
            
            // Detectar el tipo de instalación y mostrar sus características específicas
            if (instalacion instanceof Piscina) {
                Piscina piscina = (Piscina) instalacion;
                caracteristicas = "Olímpica: " + (piscina.getOlimpica() ? "Sí" : "No");
                nombreIns = "Piscinas";
            } else if (instalacion instanceof CanchaTennis) {
                CanchaTennis tenis = (CanchaTennis) instalacion;
                caracteristicas = "Tipo: " + tenis.getTipoCancha();
                nombreIns= "Canchas de tennis";
            } else if (instalacion instanceof CanchaMultiproposito) {
                CanchaMultiproposito multi = (CanchaMultiproposito) instalacion;
                caracteristicas = "Gradería: " + (multi.getGraderia() ? "Sí" : "No");
                nombreIns = "Canchas multipropósito";
            }
            
            // Recortar descripción si es muy larga
            String descripcionAcortada = instalacion.getDescripcion().length() > 40 ? 
                                       instalacion.getDescripcion().substring(0, 37) + "..." : 
                                       instalacion.getDescripcion();
            
            System.out.printf("%-10s %-15.2f %-15.2f $%-14.2f %-40s %s\n", 
                             instalacion.getId(), 
                             instalacion.getAncho(), 
                             instalacion.getLargo(),
                             instalacion.getValorHora(), 
                             descripcionAcortada,
                             caracteristicas);
        }
        
        System.out.println("\nTotal de " + nombreIns + ": " + instalaciones.size() + "\n");
    }
    
    public static void listarAlquileres() {
        System.out.println(Color.BLUE_BOLD_BRIGHT + "\n---Listado de alquileres---\n" + Color.RESET);
        
        // Verificar si hay alquileres registrados
        if (alquileres == null || alquileres.isEmpty()) {
            System.out.println("No hay alquileres registrados en el sistema.\n");
            return;
        }
        
        // Imprimir encabezado de la tabla
        System.out.printf("%-8s %-20s %-20s %-15s %-15s %-10s $%-10s\n", 
                         "ID", "FECHA INICIO", "FECHA FIN", "ID SOCIO", "ID INSTALACIÓN", "HORAS", "VALOR");
        System.out.println("─".repeat(100));
        
        // Imprimir cada alquiler
        for (Alquiler alquiler : alquileres) {
            System.out.printf("%-8s %-20s %-20s %-15s %-15s %-10d $%-10.2f\n", 
                             alquiler.getId(), 
                             alquiler.getFechaHoraInicio().toString().replace("T", " "), 
                             alquiler.getFechaHoraFin().toString().replace("T", " "), 
                             alquiler.getSocio().getId(),
                             alquiler.getInstalacionDeportiva().getId(),
                             alquiler.getHoras(),
                             alquiler.getValorAlquiler());
        }
        
        // Calcular el valor total de todos los alquileres
        double valorTotal = 0;
        for (Alquiler alquiler : alquileres) {
            valorTotal += alquiler.getValorAlquiler();
        }
        
        System.out.println("\nTotal de alquileres: " + alquileres.size());
        System.out.printf("Valor total: $%.2f\n\n", valorTotal);
    }

    private static void salir() {
        // limpiar pantalla y salir
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.exit(0);
    }

    static int leerOpcion() {
        // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
        String opciones = String.format("\n%sMenú de opciones:%s\n", Color.YELLOW,
                Color.RESET) +
                "  1 - Ingresar socios\n" + //Check
                "  2 - Listar socios\n" + //Check
                "  3 - Crear instalaciones\n" + //Check
                "  4 - Listar piscinas\n" + //check
                "  5 - Listar canchas de tenis\n" + //check
                "  6 - Listar canchas multipropósito\n" + //check
                "  7 - Alquilar instalaciones\n" + //Check
                "  8 - Listar alquileres\n" + //check
                String.format("\nElija una opción (%s0 para salir%s) > ", Color.RED, Color.RESET);

        int opcion = Keyboard.readInt(opciones);
        System.out.println();
        return opcion;
    }
}