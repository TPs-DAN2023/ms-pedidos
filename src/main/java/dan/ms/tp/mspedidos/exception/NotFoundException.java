package dan.ms.tp.mspedidos.exception;

public class NotFoundException extends Exception{
    public NotFoundException(String resource) {
        super("No se encontro el recurso "+resource);
    }
}
