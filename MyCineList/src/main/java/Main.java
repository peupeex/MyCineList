import model.*;
import model.enums.*;
import mock.CatalogoMock;
import service.*;
import util.GeradorAleatorioImpl;
import util.NotificadorPush;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== FORMULÁRIO DE PERFIL DE USUÁRIO ===");

        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();

        System.out.print("Digite sua idade: ");
        int idade = Integer.parseInt(scanner.nextLine());

        Map<Genero, Double> pesos = new HashMap<>();
        System.out.println("\n--- Preferências de Gênero (0.0 a 1.0) ---");
        System.out.println("0.0 = Bloquear gênero | 1.0 = Gênero favorito");

        for (Genero g : Genero.values()) {
            System.out.print("Peso para " + g + ": ");
            double peso = Double.parseDouble(scanner.nextLine());
            pesos.put(g, peso);
        }

        System.out.println("\n--- Filtros de Duração ---");
        System.out.print("Duração mínima (minutos): ");
        int durMin = Integer.parseInt(scanner.nextLine());
        System.out.print("Duração máxima (minutos): ");
        int durMax = Integer.parseInt(scanner.nextLine());

        System.out.println("\n--- Classificação Etária Máxima ---");
        for (int i = 0; i < ClassificacaoEtaria.values().length; i++) {
            System.out.println(i + " - " + ClassificacaoEtaria.values()[i]);
        }
        System.out.print("Escolha a opção: ");
        int idxClass = Integer.parseInt(scanner.nextLine());
        ClassificacaoEtaria classMax = ClassificacaoEtaria.values()[idxClass];

        List<Idioma> idiomas = Arrays.asList(Idioma.PORTUGUES, Idioma.INGLES);

        System.out.println("\n--- Notificações ---");
        System.out.print("Habilitar notificações? (s/n): ");
        boolean notificacoes = scanner.nextLine().trim().equalsIgnoreCase("s");

        try {
            Perfil perfil = new Perfil(pesos, durMin, durMax, classMax, idiomas);
            Usuario usuario = new Usuario(nome, idade, perfil, notificacoes);

            CatalogoFilmesAPI catalogo = new CatalogoMock();
            CalculadoraScore calculadora = new CalculadoraScore();
            FiltroFilmes filtro = new FiltroFilmes();

            HistoricoUsuarioRepository historicoRepo = (u, recs) ->
                    System.out.println("\n[LOG] " + recs.size() + " recomendações registradas para " + u.getNome());

            NotificadorPush notificadorPush = (u, msg) ->
                    System.out.println("\n[NOTIFICAÇÃO PARA " + u.getNome() + "]" + msg);

            GeradorAleatorioImpl gerador = new GeradorAleatorioImpl();

            RecomendadorService service = new RecomendadorService(catalogo, historicoRepo, notificadorPush, gerador, calculadora, filtro);

            menuPrincipal(service, usuario, nome);

        } catch (RuntimeException e) {
            System.err.println("Erro ao validar formulário: " + e.getMessage());
        }
    }

    private static void menuPrincipal(RecomendadorService service, Usuario usuario, String nome) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== MENU — " + nome.toUpperCase() + " ===");
            System.out.println("1 - Top 5 recomendações");
            System.out.println("2 - Surpreenda-me (filme aleatório)");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> exibirTopRecomendacoes(service, usuario);
                case "2" -> exibirSurpreendaMe(service, usuario);
                case "0" -> continuar = false;
                default  -> System.out.println("Opção inválida.");
            }
        }
        System.out.println("Até logo!");
    }

    private static void exibirTopRecomendacoes(RecomendadorService service, Usuario usuario) {
        System.out.println("\n=== PROCESSANDO RECOMENDAÇÕES ===");
        List<Recomendacao> recomendacoes = service.recomendar(usuario, 5);

        if (recomendacoes.isEmpty()) {
            System.out.println("Nenhum filme encontrado para os critérios informados.");
        } else {
            recomendacoes.forEach(r ->
                    System.out.printf("-> %s (Score: %.2f) | Gêneros: %s%n",
                            r.getFilme().getTitulo(), r.getScore(), r.getFilme().getGeneros())
            );
        }
    }

    private static void exibirSurpreendaMe(RecomendadorService service, Usuario usuario) {
        System.out.println("\n=== SURPREENDA-ME ===");
        service.recomendarAleatorio(usuario).ifPresentOrElse(
                r -> System.out.printf("-> %s (Score: %.2f) | Gêneros: %s%n",
                        r.getFilme().getTitulo(), r.getScore(), r.getFilme().getGeneros()),
                () -> System.out.println("Nenhum filme disponível para os seus critérios.")
        );
    }
}