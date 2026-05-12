package model;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Filme {

    private final String id;
    private final String titulo;
    private final int ano;
    private final int duracao;
    private final List<Genero> generos;
    private final ClassificacaoEtaria classificacao;
    private final Idioma idioma;
    private final double popularidade;

    public Filme(String id, String titulo, int ano, int duracao,
                 List<Genero> generos, ClassificacaoEtaria classificacao,
                 Idioma idioma, double popularidade) {
        this.id = Objects.requireNonNull(id, "id obrigatório");
        this.titulo = Objects.requireNonNull(titulo, "título obrigatório");
        this.ano = ano;
        this.duracao = duracao;
        this.generos = Collections.unmodifiableList(
                Objects.requireNonNull(generos, "gêneros obrigatórios"));
        this.classificacao = Objects.requireNonNull(classificacao, "classificação obrigatória");
        this.idioma = Objects.requireNonNull(idioma, "idioma obrigatório");
        this.popularidade = popularidade;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getAno() { return ano; }
    public int getDuracao() { return duracao; }
    public List<Genero> getGeneros() { return generos; }
    public ClassificacaoEtaria getClassificacao() { return classificacao; }
    public Idioma getIdioma() { return idioma; }
    public double getPopularidade() { return popularidade; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Filme)) return false;
        return Objects.equals(id, ((Filme) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Filme{id='" + id + "', titulo='" + titulo + "'}";
    }
}