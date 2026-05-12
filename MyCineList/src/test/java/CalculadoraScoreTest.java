import model.Filme;
import model.Perfil;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import service.CalculadoraScore;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
@DisplayName("Testes de CalculadoraScore")
class CalculadoraScoreTest {

    private CalculadoraScore calculadora;
    private List<Idioma> idiomas;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraScore();
        idiomas = Arrays.asList(Idioma.INGLES, Idioma.PORTUGUES);
    }

    private Perfil perfilComPesos(double pesoFC, double pesoDrama, int durMin, int durMax) {
        Map<Genero, Double> pesos = new EnumMap<>(Genero.class);
        pesos.put(Genero.FICCAO_CIENTIFICA, pesoFC);
        pesos.put(Genero.DRAMA, pesoDrama);
        return new Perfil(pesos, durMin, durMax, ClassificacaoEtaria.DEZOITO, idiomas);
    }

    private Filme filmeFC(int duracao) {
        return new Filme("F01", "Teste", 2020, duracao,
                Arrays.asList(Genero.FICCAO_CIENTIFICA),
                ClassificacaoEtaria.LIVRE, Idioma.INGLES, 80.0);
    }

    @Test
    @DisplayName("deve_RetornarScoreAlto_Quando_GeneroAmado")
    void deve_RetornarScoreAlto_Quando_GeneroAmado() {
        Perfil perfil = perfilComPesos(1.0, 1.0, 90, 150);
        Filme filme = new Filme("F01", "Filme FC+Drama", 2020, 120,
                Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.LIVRE, Idioma.INGLES, 100.0);

        double score = calculadora.calcular(filme, perfil);

        assertTrue(score > 80.0, "Score deveria ser alto para gênero com peso 1.0");
    }

    @Test
    @DisplayName("deve_RetornarScoreBaixo_Quando_GeneroNaoPreferido")
    void deve_RetornarScoreBaixo_Quando_GeneroNaoPreferido() {
        Map<Genero, Double> pesos = new EnumMap<>(Genero.class);
        pesos.put(Genero.FICCAO_CIENTIFICA, 0.1);
        Perfil perfil = new Perfil(pesos, 90, 150, ClassificacaoEtaria.DEZOITO, idiomas);
        Filme filme = filmeFC(120);

        double score = calculadora.calcular(filme, perfil);

        assertTrue(score < 50.0, "Score deveria ser baixo para gênero pouco preferido");
    }

    @Test
    @DisplayName("deve_RetornarComponenteDuracaoMaximo_Quando_DentroFaixa")
    void deve_RetornarComponenteDuracaoMaximo_Quando_DentroFaixa() {
        Perfil perfil = perfilComPesos(1.0, 1.0, 90, 150);
        Filme filme = filmeFC(120);

        double componente = calculadora.calcularComponenteDuracao(filme, perfil);

        assertEquals(1.0, componente);
    }

    @Test
    @DisplayName("deve_ReduzirComponenteDuracao_Quando_FilmeAcimaFaixa")
    void deve_ReduzirComponenteDuracao_Quando_FilmeAcimaFaixa() {
        Perfil perfil = perfilComPesos(1.0, 1.0, 90, 150);
        Filme filmeForaDaFaixa = filmeFC(180); // 30 min acima

        double componente = calculadora.calcularComponenteDuracao(filmeForaDaFaixa, perfil);

        assertTrue(componente < 1.0, "Componente duração deveria ser menor que 1.0");
        assertTrue(componente >= 0.0, "Componente duração nunca deve ser negativo");
    }

    @Test
    @DisplayName("deve_MantarScoreEntre0e100_Quando_QualquerEntrada")
    void deve_MantarScoreEntre0e100_Quando_QualquerEntrada() {
        Perfil perfil = perfilComPesos(1.0, 1.0, 90, 150);
        Filme filmePerfeito = new Filme("F01", "Perfeito", 2020, 120,
                Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.LIVRE, Idioma.INGLES, 100.0);
        Filme filmePessimo = new Filme("F02", "Péssimo", 2020, 300,
                Arrays.asList(Genero.TERROR),
                ClassificacaoEtaria.LIVRE, Idioma.JAPONES, 0.0);

        double scoreAlto = calculadora.calcular(filmePerfeito, perfil);
        double scoreBaixo = calculadora.calcular(filmePessimo, perfil);

        assertAll(
                () -> assertTrue(scoreAlto <= 100.0),
                () -> assertTrue(scoreBaixo >= 0.0)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, 1.0, 1.0",
            "0.5, 0.5, 0.5",
            "0.0, 0.0, 0.0"
    })
    @DisplayName("deve_CalcularComponenteGenero_ConformePesos")
    void deve_CalcularComponenteGenero_ConformePesos(double pesoFC, double pesoDrama, double esperado) {
        Perfil perfil = perfilComPesos(pesoFC, pesoDrama, 90, 150);
        Filme filme = new Filme("F01", "Teste", 2020, 120,
                Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.LIVRE, Idioma.INGLES, 80.0);

        double componente = calculadora.calcularComponenteGenero(filme, perfil);

        assertEquals(esperado, componente, 0.001);
    }

    @ParameterizedTest
    @CsvSource({
            "DOZE, DEZESSEIS, true",
            "DEZESSEIS, DEZESSEIS, true",
            "DEZOITO, DEZESSEIS, false"
    })
    @DisplayName("deve_AceitarFilme_QuandoClassificacaoEhCompativel")
    void deve_AceitarFilme_QuandoClassificacaoEhCompativel(
            ClassificacaoEtaria classificacaoFilme,
            ClassificacaoEtaria classificacaoMaxima,
            boolean esperadoAceito) {

        int idadeUsuario = 28;
        boolean aceito = classificacaoFilme.getIdade() <= classificacaoMaxima.getIdade()
                && classificacaoFilme.getIdade() <= idadeUsuario;

        assertEquals(esperadoAceito, aceito);
    }
}