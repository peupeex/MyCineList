package service;

import model.Filme;
import model.Perfil;
import model.enums.Genero;


public class CalculadoraScore {

    static final double PESO_GENERO       = 0.50;
    static final double PESO_DURACAO      = 0.20;
    static final double PESO_POPULARIDADE = 0.15;
    static final double PESO_AFINIDADE    = 0.15;

    private static final double SCORE_MAXIMO = 100.0;
    private static final double NOTA_MAXIMA_USUARIO = 5.0;


    public double calcular(Filme filme, Perfil perfil) {
        double componenteGenero       = calcularComponenteGenero(filme, perfil)       * PESO_GENERO;
        double componenteDuracao      = calcularComponenteDuracao(filme, perfil)      * PESO_DURACAO;
        double componentePopularidade = calcularComponentePopularidade(filme)         * PESO_POPULARIDADE;
        double componenteAfinidade    = calcularComponenteAfinidade(filme, perfil)    * PESO_AFINIDADE;

        double score = (componenteGenero + componenteDuracao + componentePopularidade + componenteAfinidade)
                * SCORE_MAXIMO;

        return Math.min(SCORE_MAXIMO, Math.max(0.0, score));
    }

    public double calcularComponenteGenero(Filme filme, Perfil perfil) {
        if (filme.getGeneros().isEmpty()) return 0.0;

        double somaPesos = 0.0;
        for (Genero genero : filme.getGeneros()) {
            somaPesos += perfil.getPesoGenero(genero);
        }
        return somaPesos / filme.getGeneros().size();
    }

    public double calcularComponenteDuracao(Filme filme, Perfil perfil) {
        int duracao = filme.getDuracao();
        int min = perfil.getDuracaoMinima();
        int max = perfil.getDuracaoMaxima();

        if (duracao >= min && duracao <= max) return 1.0;

        int desvio = duracao < min ? min - duracao : duracao - max;
        double reducao = desvio / (double) max;
        return Math.max(0.0, 1.0 - reducao);
    }

    double calcularComponentePopularidade(Filme filme) {
        return Math.min(1.0, Math.max(0.0, filme.getPopularidade() / 100.0));
    }


    double calcularComponenteAfinidade(Filme filme, Perfil perfil) {
        if (perfil.getNotasPorFilme().isEmpty()) return 0.0;

        double somaAfinidade = 0.0;
        int contador = 0;

        for (Genero genero : filme.getGeneros()) {
            double pesoGenero = perfil.getPesoGenero(genero);
            if (pesoGenero > 0.0) {
                somaAfinidade += pesoGenero;
                contador++;
            }
        }

        if (contador == 0) return 0.0;

        double mediaNotas = perfil.getNotasPorFilme().values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        double notaNormalizada = mediaNotas / NOTA_MAXIMA_USUARIO;
        return (somaAfinidade / contador) * notaNormalizada;
    }
}