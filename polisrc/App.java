package poo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import poo.helpers.Keyboard;
import poo.helpers.Utils;
import poo.model.*;
import poo.helpers.Color;

public class App {

    private static JSONObject config; // configuración con tarifas y rutas de archivos
    private static JSONObject tarifas; // las tarifas de las diferentes instalaciones
    private static JSONObject archivos; // los nombres de los archivos que se utilizan

    private static List<Socio> socios;
    private static List<InstalacionDeportiva> piscinas;
    private static List<InstalacionDeportiva> canchasDeTenis;
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
                        listarInstalaciones(canchasDeTenis);
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
        inicializarInstalaciones(CanchaTenis.class);
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
        List<InstalacionDeportiva> instalaciones = new ArrayList<>();
        String nombreArchivo;

        if (Piscina.class.isAssignableFrom(c)) {
            nombreArchivo = archivos.optString("piscina");
            instalaciones = piscinas = new ArrayList<>();
        } else if (...) {
            ...
        } else if (...) {
            ...
        } else {
            throw new IllegalArgumentException("Se esperaba una subclase de InstalacionDeportiva");
        }

        // lo que sigue es similar a las líneas 105 y siguientes de inicializarSocios()
    }

    private static void inicializarAlquileres() throws Exception {
        // similar a inicializarSocios()
    }

    private static void crearInstalaciones() throws IOException {
        String nombreArchivo = "";
        boolean agregar = false;
        InstalacionDeportiva instalacion;
        List<InstalacionDeportiva> instalaciones = null;
        System.out.println("Ingreso de instalaciones deportivas\n");

        do {
            int tipo = Keyboard.readInt(0, 3, "Seleccione el tipo de instalación [1-Piscina 2-Tenis 3-Mutipropósito -0-Salir]: ");

            if (tipo == 0) {
                break;
            } else if (tipo == 1) {
                nombreArchivo = archivos.getString("piscina");
                instalaciones = piscinas;
                instalacion = new Piscina();
                Piscina p = (Piscina) instalacion;
                p.setOlimpica(Keyboard.readBoolean("¿Es olímpica? [S/N]: "));
                p.setValorHora(tarifas.getDouble("piscina"));
            } else if (tipo == 2) {
                ...
            } else {
                ...
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
        // similar a ingresar personas en bioimpedancia
    }

    public static void alquilarInstalaciones() throws Exception {
        // elegirSocio() y salir si null

        // elegir elegirInstalacionDeportiva() y salir si null

        // leer la hora de inicio y de finalización

        if (hayCruce(instalacion, horaInicio, horaFin)) {
            System.out.printf("%nNo se pudo registrar el alquiler:%n");
            System.out.printf(
                    "La instalación \"%s - %s\" está ocupada en ese horario%n%n",
                    instalacion.getId(), instalacion.getTipoInstalacion());
        } else {
            // crear la instancia de Alquiler con el constructor parametrizado
            // agregar la nueva instancia a alquileres
            // actualizar el archivo json
            // informar al usuario del éxito de la operación
        }

    }

    private static Socio elegirSocio() {
        // permitir elegir de una lista, un socio y también de optar por no elegir ninguno
        // devolver el socio elegido o null si no elige uno
    }

    private static InstalacionDeportiva elegirInstalacionDeportiva() {
        // permitir elegir de una lista, una instalación deportiva y también de optar por no elegir ninguna
        // devolver la instalación elegida o null si no elige una
    }

    public static boolean hayCruce(InstalacionDeportiva instalacion, LocalDateTime horaInicio, LocalDateTime horaFin) {
        // recorrer los alquileres y si Utils.OverlapSchedules(...) para la instalación en el horario dado retornar true
        // retornar false si recorrido el array de alquileres no se dieron cruces de horarios
    }

    // -------- listados -----------

    public static void listarSocios() { }

    public static void listarInstalaciones(List<InstalacionDeportiva> instalaciones) { }

    public static void listarAlquileres() {  }

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
                "  1 - Ingresar socios\n" +
                "  2 - Listar socios\n" +
                "  3 - Crear instalaciones\n" +
                "  4 - Listar piscinas\n" +
                "  5 - Listar canchas de tenis\n" +
                "  6 - Listar canchas multipropósito\n" +
                "  7 - Alquilar instalaciones\n" +
                "  8 - Listar alquileres\n" +
                String.format("\nElija una opción (%s0 para salir%s) > ", Color.RED, Color.RESET);

        int opcion = Keyboard.readInt(opciones);
        System.out.println();
        return opcion;
    }
}
