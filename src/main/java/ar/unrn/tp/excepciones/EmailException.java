package ar.unrn.tp.excepciones;

public class EmailException extends Exception{

    public EmailException(){
        super("Ingrese un email valido.");
    }
}
