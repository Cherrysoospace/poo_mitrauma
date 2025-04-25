package poo;

public class App {
    public static void main(String[] args) {
        Task task = new Task();

        // pasarle tres "tareas" a task.performTask() para que la ejecute en algún momento.

        task.performTask((int a, int b) -> {
            String result = String.format("%d = %d + %d", a + b, a, b);
            System.out.println(result);
        });

        task.performTask((int a, int b) -> {
            String result = String.format("%d = %d x %d", a * b, a, b);
            System.out.println(result);
        });

        task.performTask((int a, int b) -> {
            System.out.printf("Naturales entre %d y %d: [ ", a, b);
            for (int i = a; i <= b; i++) {
                System.out.printf("%d ", i);
            }
            System.out.println("]");
        });
    }
}
