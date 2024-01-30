package dan.ms.tp.mspedidos.exception;

public class UnexpectedResponseException extends Exception {
    public UnexpectedResponseException(String message) {
        super("Respuesta inesperada. message: " + message);
    }
}
