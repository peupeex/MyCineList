package exception;

public class NotaInvalidaException extends RuntimeException{
    public NotaInvalidaException(int nota){
        super ("Nota inválida: " + nota + ". Deve estar entre 1 e 5.");
    }
}
