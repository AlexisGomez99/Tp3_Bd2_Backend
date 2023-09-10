package ar.unrn.tp.excepciones;

public class NotNumException extends Exception{

    public NotNumException(){
        super("Debe ser un valor numerico");
    }
}
