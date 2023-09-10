package ar.unrn.tp.excepciones;

public class NotNullException extends Exception{

    public NotNullException(String campo){
        super("El campo "+campo+" no debe ser un valor nulo.");
    }
}
