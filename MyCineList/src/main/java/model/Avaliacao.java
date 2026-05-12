package model;

public class Avaliacao {
    private Usuario usuario;
    private Filme filme;
    private int nota;

    public Avaliacao(Usuario usuario, Filme filme, int nota){
        this.usuario = usuario;
        this.filme = filme;
        this.nota = nota;
    }

    public int getNota(){
        return nota;
    }
}
