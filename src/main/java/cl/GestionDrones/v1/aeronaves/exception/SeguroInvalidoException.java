package cl.GestionDrones.v1.aeronaves.exception;

public class SeguroInvalidoException extends RuntimeException {

    public SeguroInvalidoException(String mensaje) {
        super(mensaje);
    }

    public SeguroInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
