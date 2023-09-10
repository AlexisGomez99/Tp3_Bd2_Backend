package ar.unrn.tp.excepciones;

public class DateException extends Exception{

    public DateException(){
        super("Las fechas no deben superponerse");
    }
}

