package poo;

import java.util.Random;

/**
 * Esta clase tiene un método performTask que simula una tarea (en este caso, una pausa de 1 segundo). 
 * Lo importante es que este método recibe un objeto de tipo Callback como parámetro. 
 * Una vez que performTask ejecuta sus instrucciones, llama al método onComplete, 
 * pasándole lo que finalmente quedó asignado en los atributos x y y
 */

public class Task {
    // 
    public void performTask(Callback callback) {
        int x = 0;
        int y = 0;
        // Simular una tarea que tarda tiempo
        try {
            Thread.sleep(1000);
            System.out.println("Procesando. Por favor espere...");
            Random random = new Random();
            // definir límites para generar números aleatorios
            int min = 1;
            int max = 20;
            // generar dos números aleatorios en el límite establecido
            int a = random.nextInt(max - min + 1) + min;
            int b = random.nextInt(max - min + 1) + min;
            // asignar a los atributos x y y el mínimo y el máximo de a y b respectivamente
            x = Math.min(a, b);
            y = Math.max(a, b);
        } catch (InterruptedException e) {
            e.printStackTrace(System.out);
        }

        // ejecutar el callback
        callback.onComplete(x, y);
    }
}
