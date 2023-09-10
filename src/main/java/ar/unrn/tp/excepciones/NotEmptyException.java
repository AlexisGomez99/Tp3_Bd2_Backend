package ar.unrn.tp.excepciones;

public class NotEmptyException extends Exception{

    public NotEmptyException(){
        super("El campo no debe estar vacio.");
    }
}
