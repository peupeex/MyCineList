package model;

import java.util.Objects;

public final class Recomendacao {

    private final Filme filme;
    private final double score;
    private final String justificativa;

    public Recomendacao(Filme filme, double score, String justificativa) {
        this.filme = Objects.requireNonNull(filme, "filme obrigatório");
        this.score = score;
        this.justificativa = Objects.requireNonNull(justificativa, "justificativa obrigatória");
    }

    public Filme getFilme() { return filme; }
    public double getScore() { return score; }
    public String getJustificativa() { return justificativa; }

    @Override
    public String toString() {
        return String.format("Recomendacao{filme='%s', score=%.1f}", filme.getTitulo(), score);
    }
}