package poo;

/**
 * Define el contrato para el callback. Cualquier clase que quiera ser notificada cuando una tarea se 
 * complete debe implementar esta interfaz y proporcionar una implementación para el método onComplete. 
 */

public interface Callback {
    void onComplete(int a, int b);
}
