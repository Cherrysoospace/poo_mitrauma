package poo;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.security.Key;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import poo.helpers.Keyboard;
import poo.helpers.Utils;
import poo.model.Categoria;
import poo.model.Producto;

public final class App {
    static ArrayList<Producto> productos = new ArrayList<>();

    public static void main(String[] args) {

        menu();

        /*
         * Producto p1 = new Producto("P001", "USB", 16000, Categoria.ELECTRONICA);
         * System.out.println(p1);
         * 
         * Producto p2 = new Producto(p1);
         * 
         * System.out.println(p1.equals(p2)); //Esto suelta el booleano
         * 
         * System.out.println(p2);
         */
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
                        crearProductos();
                        break;
                    case 2:
                        listarProductos();
                        break;
                    case 3:
                        buscarProducto();
                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

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
        inicializarProducto();
        productos = new ArrayList<>();
        String filename = "data/productos.json";

        if (Utils.fileExists(filename)) {
            String contenidoJson = Utils.readText(filename);
            JSONArray jsonArray = new JSONArray(contenidoJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Producto p = new Producto(json);
                productos.add(p);
            }
        }
        /*
         * Persona p1 = personas.get(0);
         * Persona p2 = p1;
         * System.out.println("iguales = " + (p1 == p2));
         */
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
                + "  1 - Ingresar Producto\n"
                + "  2 - Listar Producto\n" // check
                + "  3 - Buscar un producto por ID\n"
                + "  4 - \n" // check
                + "  5 - \n" //
                + "  6 - \n" // check
                + "  7 - \n"
                + String.format("  %s0 - Salir%s\n", Utils.RED, Utils.RESET) +
                String.format("\nElija una opción (%s0 para salir%s) > ", Utils.RED, Utils.RESET);

        int opcion = Keyboard.readInt(opciones);
        System.out.println();
        return opcion;
    }

    // Falta por detallar
    public static void inicializarProducto() throws Exception {
        String filename = "./data/productos.json";

    }

    private static void listarProductos() {
        System.out.println("Listado de Productos");
        for (Producto p : productos) {
            System.out.println(p.toString());
        }
    }

    private static void crearProductos() throws IOException {
        boolean agregar = false;
        Producto p = new Producto();

        do {
            String id = Keyboard.readString("Ingrese la identificacion de su producto (Enter terminal): ");
            if (id.length() == 0) {
                break;
            }
            p = new Producto(id);

            if (productos.contains(p)) {
                System.out.println("El producto ya existe.");
                continue;
            }
            String nombre = Keyboard.readString("Ingrese el nombre de su producto: ");
            double precioBase = Keyboard.readDouble("Ingrese el precio sin IVA de su producto: ");
            Categoria categoria = Keyboard.readEnum(Categoria.class, "Seleccione la categoria de su producto: ");
            // otra manera para hacer categoria
            /*
             * String categoriaStr = Keyboard.
             * readString("Ingrese la categoria de su producto (Electronica, Alimentos, Ropa): "
             * );
             * Categoria categoria = Categoria.getEnum(categoriaStr);
             */

            p = new Producto(id, nombre, precioBase, categoria);
            productos.add(p);
            agregar = true;

        } while (true);

        if (agregar) {
            Utils.writeJSON(productos, "data/productos.json");
        }
    }

    private static void buscarProducto() {
        String id = Keyboard.readString("Ingrese el ID del producto que busca: ");
        Producto p = new Producto(id);
        boolean existe = false;
        for (Producto producto : productos) {
            if (producto.equals(p)) {
                System.out.println(producto.toString());
                existe = true;
                return;
            }

            if (!existe) {
                System.out.println("No se el econtró el producto con el id: " + id);
            }
        }
    }

}
