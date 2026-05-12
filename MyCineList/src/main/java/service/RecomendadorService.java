package service;

import model.Filme;
import model.Recomendacao;
import model.Usuario;
import util.GeradorAleatorio;
import util.NotificadorPush;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class RecomendadorService {

    private final CatalogoFilmesAPI catalogo;
    private final HistoricoUsuarioRepository historicoRepo;
    private final NotificadorPush notificador;
    private final GeradorAleatorio gerador;
    private final CalculadoraScore calculadora;
    private final FiltroFilmes filtro;

    public RecomendadorService(CatalogoFilmesAPI catalogo,
                               HistoricoUsuarioRepository historicoRepo,
                               NotificadorPush notificador,
                               GeradorAleatorio gerador,
                               CalculadoraScore calculadora,
                               FiltroFilmes filtro) {
        this.catalogo = catalogo;
        this.historicoRepo = historicoRepo;
        this.notificador = notificador;
        this.gerador = gerador;
        this.calculadora = calculadora;
        this.filtro = filtro;
    }


    public List<Recomendacao> recomendar(Usuario usuario, int topN) {
        List<Filme> filmes = buscarCatalogo();
        if (filmes.isEmpty()) {
            return Collections.emptyList();
        }

        List<Filme> candidatos = filtro.filtrar(filmes, usuario.getPerfil(), usuario.getIdade());
        if (candidatos.isEmpty()) {
            return Collections.emptyList();
        }

        List<Recomendacao> recomendacoes = candidatos.stream()
                .map(f -> criarRecomendacao(f, usuario))
                .sorted(ordenacaoScore())
                .limit(topN)
                .collect(Collectors.toList());

        registrarENotificar(usuario, recomendacoes);
        return recomendacoes;
    }


    public Optional<Recomendacao> recomendarAleatorio(Usuario usuario) {
        List<Filme> filmes = buscarCatalogo();
        List<Filme> candidatos = filtro.filtrar(filmes, usuario.getPerfil(), usuario.getIdade());

        if (candidatos.isEmpty()) {
            return Optional.empty();
        }

        int indice = gerador.sortearInteiro(0, candidatos.size() -1);

        Recomendacao recomendacao = criarRecomendacao(candidatos.get(indice), usuario);
        return Optional.of(recomendacao);
    }


    private List<Filme> buscarCatalogo() {
        try {
            List<Filme> filmes = catalogo.buscarTodos();
            return filmes != null ? filmes : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Recomendacao criarRecomendacao(Filme filme, Usuario usuario) {
        double score = calculadora.calcular(filme, usuario.getPerfil());
        String justificativa = gerarJustificativa(filme, usuario, score);
        return new Recomendacao(filme, score, justificativa);
    }

    private String gerarJustificativa(Filme filme, Usuario usuario, double score) {
        return String.format(
                "Recomendamos '%s' porque combina com seus gêneros preferidos e tem score %.1f.",
                filme.getTitulo(), score);
    }

    private Comparator<Recomendacao> ordenacaoScore() {
        return Comparator
                .comparingDouble(Recomendacao::getScore).reversed()
                .thenComparingDouble(r -> -r.getFilme().getPopularidade());
    }

    private void registrarENotificar(Usuario usuario, List<Recomendacao> recomendacoes) {
        historicoRepo.registrarRecomendacao(usuario, recomendacoes);

        if (usuario.isNotificacoesHabilitadas()) {
            notificador.enviar(usuario, "Suas recomendações do dia estão prontas!");
        }
    }
}