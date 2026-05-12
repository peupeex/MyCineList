package exception;

public class DuracaoInvalidaException extends RuntimeException{
    public DuracaoInvalidaException(int min, int max) {
        super("Duração mínima (" + min + ") não pode ser maior que a máxima (" + max + ").");
    }
}
