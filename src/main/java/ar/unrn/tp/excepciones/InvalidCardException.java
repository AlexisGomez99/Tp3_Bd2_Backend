package ar.unrn.tp.excepciones;

public class InvalidCardException extends Exception{

    public InvalidCardException(){
        super("La tarjeta no tiene un estado valido.");
    }
}
