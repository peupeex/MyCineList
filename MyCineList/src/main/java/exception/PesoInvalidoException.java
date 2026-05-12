package exception;

public class PesoInvalidoException  extends RuntimeException{
    public PesoInvalidoException(double peso) {
        super(" Peso inválido: " + peso + ". Deve estar entre 0.0 e 1.0.");
    }
}
