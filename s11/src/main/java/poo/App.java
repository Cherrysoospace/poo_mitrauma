package poo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;
import poo.helpers.Keyboard;
import poo.helpers.Utils;
import poo.listas.TipoCancha;
import poo.helpers.Color;
import poo.model.Alquiler;
import poo.model.Socio;
import poo.services.AlquilerService;
import poo.services.InstalacionDeportivaService;
import poo.services.SocioService;
import poo.model.InstalacionDeportiva;
import poo.model.Piscina;
import poo.model.CanchaTennis;
import poo.model.CanchaMultiproposito;

public class App {

    private static JSONObject config; // configuración con tarifas y rutas de archivos

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

      

        do {
           
            try {
                int opcion = leerOpcion();

                switch (opcion) {
                    case 1:
                        // Es necesario inicializar en cada caso la instancia o hacerlo fuera del switch
                        // and case?
                        SocioService socioService = new SocioService();
                        String nuevoSocioJson = """
                                {
                                    "nombre": "Laura Mejía",
                                    "direccion": "Cra 9 #45-67",
                                    "id": "ABC22",
                                    "telefono": "3007896541"
                                }
                                    """;
                        JSONObject resultado = socioService.add(nuevoSocioJson);
                        System.out.println(Color.GREEN_BOLD + "Socio agregado con éxito:" + Color.RESET);
                        System.out.println(resultado.toString(2));
                        break;
                    case 2:
                    SocioService s = new SocioService();
                        System.out.println(s.get(1));
                        System.out.println(s.getItem("S0011"));
                        System.out.println(s.get("S0011"));

                        break;
                    case 3:
                        SocioService ss = new SocioService();
                        System.out.println(ss.getAll().toString(2));
                        break;
                    case 4:
                        SocioService socioServiceUpdate = new SocioService();

                        String idActualizar = Keyboard.readString("Ingrese el ID del socio que desea actualizar: ");
                        String nuevoNombre = Keyboard.readString("Nuevo nombre (dejar vacío si no se cambia): ");
                        String nuevaDireccion = Keyboard.readString("Nueva dirección (dejar vacío si no se cambia): ");
                        String nuevoTelefono = Keyboard.readString("Nuevo teléfono (dejar vacío si no se cambia): ");

                        JSONObject datosActualizar = new JSONObject();
                        if (!nuevoNombre.isBlank())
                            datosActualizar.put("nombre", nuevoNombre);
                        if (!nuevaDireccion.isBlank())
                            datosActualizar.put("direccion", nuevaDireccion);
                        if (!nuevoTelefono.isBlank())
                            datosActualizar.put("telefono", nuevoTelefono);

                        if (datosActualizar.isEmpty()) {
                            System.out.println(
                                    Color.YELLOW_BOLD + "No se ingresaron datos para actualizar." + Color.RESET);
                        } else {
                            JSONObject resultadoActualizacion = socioServiceUpdate.update(idActualizar,
                                    datosActualizar.toString());
                            System.out.println(Color.GREEN_BOLD + "Socio actualizado exitosamente:" + Color.RESET);
                            System.out.println(resultadoActualizacion.toString(2));
                        }
                        break;
                    case 5:
                        try {
                            SocioService socioServiceRemove = new SocioService();
                            String idEliminar = Keyboard.readString("Ingrese el ID del socio que desea eliminar: ");
                        
                            JSONObject socioAEliminar = socioServiceRemove.get(idEliminar);
                            System.out.println(Color.YELLOW + "Datos del socio a eliminar:" + Color.RESET);
                            System.out.println(socioAEliminar.toString(2));

                            String confirmacion = Keyboard
                                    .readString("¿Está seguro que desea eliminar este socio? (s/n): ");
                            if (!confirmacion.equalsIgnoreCase("s")) {
                                System.out.println(
                                        Color.CYAN_BOLD + "Eliminación cancelada por el usuario." + Color.RESET);
                                break;
                            }

                            JSONObject eliminado = socioServiceRemove.remove(idEliminar);
                            System.out.println(Color.GREEN_BOLD + "Socio eliminado con éxito:" + Color.RESET);
                            System.out.println(eliminado.toString(2));
                        } catch (NoSuchElementException e) {
                            System.out.println(Color.RED_BOLD + "No se encontró el socio con ese ID." + Color.RESET);
                        } catch (Exception e) {
                            System.out.println(
                                    Color.RED_BOLD + "No se pudo eliminar el socio: " + e.getMessage() + Color.RESET);
                        }
                        break;
                    case 6:
                        InstalacionDeportivaService piscinaService = new InstalacionDeportivaService(Piscina.class);
                        System.out.println("JSON para nueva piscina:");
                        String pJson = Keyboard.readString("");
                        System.out.println(piscinaService.add(pJson).toString(2));
                        break;
                    case 7:
                        InstalacionDeportivaService ps1 = new InstalacionDeportivaService (Piscina.class);
                        System.out.print(Color.CYAN_BRIGHT + "ID piscina: " + Color.RESET);
                        System.out.println(ps1.get(Keyboard.readString("")).toString(2));
                        break;
                    case 8:
                        InstalacionDeportivaService ps2 = new InstalacionDeportivaService(Piscina.class);
                        System.out.println(ps2.getAll().toString(2));
                        break;
                    case 9:
                        InstalacionDeportivaService ps3 = new InstalacionDeportivaService (Piscina.class);
                        System.out.print(Color.GREEN_BOLD + "ID piscina a actualizar: " + Color.RESET);
                        String pid = Keyboard.readString("");
                        System.out.println(Color.GREEN_BOLD+"JSON con patch: " + Color.RESET);
                        String pPatch = Keyboard.readString("");
                        System.out.println(ps3.update(pid, pPatch).toString(2));
                        break;
                    case 10:
                        InstalacionDeportivaService ps4 = new InstalacionDeportivaService(Piscina.class);
                        System.out.print(Color.RED_BRIGHT + "ID piscina a eliminar: " + Color.RESET);
                        System.out.println(ps4.remove(Keyboard.readString("")).toString(2));
                        break;
                    case 11:
                        InstalacionDeportivaService ts = new InstalacionDeportivaService(CanchaTennis.class);
                        System.out.println(Color.GREEN_BOLD+"JSON para nueva cancha tenis: " + Color.RESET);
                        String tJson = Keyboard.readString("");
                        System.out.println(ts.add(tJson).toString(2));
                        break;
                    case 12:
                        InstalacionDeportivaService ts1 = new InstalacionDeportivaService (CanchaTennis.class);
                        System.out.print(Color.CYAN_BOLD + "ID tennis: " + Color.RESET);
                        System.out.println(ts1.get(Keyboard.readString("")).toString(2));
                        break;
                    case 13:
                        InstalacionDeportivaService ts2 = new InstalacionDeportivaService (CanchaTennis.class);
                        System.out.println(ts2.getAll().toString(2));
                        break;
                    case 14:
                        InstalacionDeportivaService ts3 = new InstalacionDeportivaService (CanchaTennis.class);
                        System.out.print(Color.YELLOW_BOLD + "ID tenis a actualizar: " + Color.RESET);
                        String tid = Keyboard.readString("");
                        System.out.println("JSON con patch> ");
                        String tPatch = Keyboard.readString("");
                        System.out.println(ts3.update(tid, tPatch).toString(2));
                        break;
                    case 15:
                        InstalacionDeportivaService ts4 = new InstalacionDeportivaService (CanchaTennis.class);
                        System.out.print(Color.RED_BOLD + "ID de Cancha tennis a eliminar: "+ Color.RESET);
                        System.out.println(ts4.remove(Keyboard.readString("")).toString(2));
                        break;
                    case 16:
                        InstalacionDeportivaService cm = new InstalacionDeportivaService (CanchaMultiproposito.class);
                        System.out.println(Color.YELLOW_BOLD + "JSON para nueva cancha multipropósito: " + Color.RESET);
                        String mJson = Keyboard.readString("");
                        System.out.println(cm.add(mJson).toString(2));
                        break;
                    case 17:
                        InstalacionDeportivaService cm1 = new InstalacionDeportivaService(CanchaMultiproposito.class);
                        System.out.print(Color.CYAN_BOLD + "ID de cancha multipropósito: " + Color.RESET);
                        System.out.println(cm1.get(Keyboard.readString("")).toString(2));
                        break;
                    case 18:
                        InstalacionDeportivaService cm2 = new InstalacionDeportivaService(CanchaMultiproposito.class);
                        System.out.println(cm2.getAll().toString(2));
                        break;
                    case 19:
                        InstalacionDeportivaService cm3 = new InstalacionDeportivaService(CanchaMultiproposito.class);
                        System.out.print(Color.GREEN_BOLD+ "ID de cancha multipropósito a actualizar: " + Color.RESET);
                        String mid = Keyboard.readString("");
                        System.out.println(Color.YELLOW_BOLD + "JSON con patch: " + Color.RESET);
                        String mPatch = Keyboard.readString("");
                        System.out.println(cm3.update(mid, mPatch).toString(2));
                        break;
                    case 20:
                        InstalacionDeportivaService cm4 = new InstalacionDeportivaService(CanchaMultiproposito.class);
                        System.out.print(Color.RED_BOLD + "ID de cancha multipropósito a eliminar: " + Color.RESET);
                        System.out.println(cm4.remove(Keyboard.readString("")).toString(2));
                        break;
                    case 21:
                        AlquilerService aSvc = new AlquilerService(socios, canchasTennis);
                        System.out.println(Color.YELLOW_BOLD + "Creando nuevo alquiler" + Color.RESET);
                        System.out.println("Introduce el JSON completo del alquiler:");
                        String nuevoAlqJson = Keyboard.readString("");
                        System.out.println(aSvc.add(nuevoAlqJson).toString(2));
                        break;
                    case 22:
                        AlquilerService aSvc1 = new AlquilerService(socios, canchasTennis);
                        System.out.println(Color.YELLOW_BOLD + "Buscar alquiler por ID" + Color.RESET);
                        System.out.print("ID del alquiler > ");
                        String idBusq = Keyboard.readString("");
                        System.out.println(aSvc1.get(idBusq).toString(2));
                        break;
                    case 23:
                        AlquilerService aSvc2 = new AlquilerService(socios, canchasTennis);
                        System.out.println(Color.YELLOW_BOLD + "Listado de todos los alquileres" + Color.RESET);
                        System.out.println(aSvc2.getAll().toString(2));
                        break;
                    case 24:
                        AlquilerService aSvc3 = new AlquilerService(socios, canchasTennis);
                        System.out.println(Color.YELLOW_BOLD + "Actualizar alquiler" + Color.RESET);
                        System.out.print("ID del alquiler a actualizar: ");
                        String idUpd = Keyboard.readString("");
                        System.out.println("Introduce el JSON con los campos a modificar:");
                        String patchAlq = Keyboard.readString("");
                        System.out.println(aSvc3.update(idUpd, patchAlq).toString(2));
                        break;
                    case 25:
                        AlquilerService aSvc4 = new AlquilerService(socios, canchasTennis);
                        System.out.println(Color.YELLOW_BOLD + "Eliminar alquiler" + Color.RESET);
                        System.out.print("ID del alquiler a eliminar > ");
                        String idRem = Keyboard.readString("");
                        System.out.println(aSvc4.remove(idRem).toString(2));
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
                "  1 - Prueba de SocioService.add()\n" + 
                "  2 - Búsquedas con SocioService\n" + 
                "  3 - Prueba de SocioService.getAll()\n" +
                "  4 - Prueba de SocioService.update()\n" +
                "  5 - Prueba de SocioService.remove()\n" +
                "  6 - Piscinas > Instalacion DeportivaService.add()\n" + //FUNCIONA 24.04.25
                "  7 - Piscinas > InstalacionDeportivaService búsquedas\n" + //FUNCIONA 24.04.25 INCOMPLETO
                "  8 - Piscinas > InstalacionDeportivaService.getAll()\n" + //FUNCIONA 24.04.25
                "  9 - Piscinas > InstalacionDeportivaService.update()\n" + //FUNCIONA 24.04.25
                " 10 - Piscinas > Instalacion DeportivaService.remove()\n" + //FUNCIONA 24.04.25
                " 11 - Tennis > Instalacion DeportivaService.add()\n" + //FUNCIONA 24.04.25
                " 12 - Tennis InstalacionDeportivaService búsquedas\n" + //FUNCIONA 24.04.25 INCOMPLETO
                " 13 - Tennis > InstalacionDeportivaService.getAll()\n" + // FUNCIONA 24.04.25
                " 14 - Tennis > InstalacionDeportivaService.update()\n" + //FUNCIONA 24.04.25
                " 15 - Tennis > InstalacionDeportivaService.remove()\n" + //FUNCIONA 24.04.25
                " 16 - Multipropósito > Instalacion DeportivaService.add()\n" + //FUNCIONA 24.04.25
                " 17 - Multipropósito > InstalacionDeportivaService búsquedas\n" + //FUNCIONA 24.04.25 INCOMPLETO
                " 18 - Multipropósito > InstalacionDeportivaService.getAll()\n" + //FUNCIONA 24.04.25
                " 19 - Multipropósito > InstalacionDeportivaService.update()\n" + //FUNCIONA 24.04.25 
                " 20 - Multipropósito > Instalacion DeportivaService.remove()\n" + // FUNCIONA 24.04.25
                " 21 - AlquilerService.add()\n" + //FUNCIONA
                " 22 - AlquilerService búsquedas\n" + //FUNCIONA 
                " 23 - AlquilerService.getAll()\n" + //FUNCIONA
                " 24 - AlquilerService.Update()\n" +
                " 25 - AlquilerService.remove()\n" + //FUNCIONA
                String.format("\nElija una opción (%s0 para salir%s) > ", Color.RED, Color.RESET);

        int opcion = Keyboard.readInt(opciones);
        System.out.println();
        return opcion;
    }

}