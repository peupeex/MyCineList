import model.Filme;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
@DisplayName("Testes de Filme")
class FilmeTest {

    private Filme filme;

    @BeforeEach
    void setUp() {
        filme = new Filme("F01", "A Chegada", 2016, 116,
                Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 84.0);
    }

    @Test
    @DisplayName("deve_PreencherTodosAtributos_Quando_FilmeCriado")
    void deve_PreencherTodosAtributos_Quando_FilmeCriado() {
        assertAll(
                () -> assertEquals("F01", filme.getId()),
                () -> assertEquals("A Chegada", filme.getTitulo()),
                () -> assertEquals(2016, filme.getAno()),
                () -> assertEquals(116, filme.getDuracao()),
                () -> assertEquals(ClassificacaoEtaria.DOZE, filme.getClassificacao()),
                () -> assertEquals(Idioma.INGLES, filme.getIdioma()),
                () -> assertEquals(84.0, filme.getPopularidade())
        );
    }

    @Test
    @DisplayName("deve_ConsiderarFilmesIguais_Quando_MesmoId")
    void deve_ConsiderarFilmesIguais_Quando_MesmoId() {
        Filme outroFilme = new Filme("F01", "Título Diferente", 2020, 90,
                Arrays.asList(Genero.COMEDIA),
                ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 50.0);

        assertEquals(filme, outroFilme);
        assertEquals(filme.hashCode(), outroFilme.hashCode());
    }

    @Test
    @DisplayName("deve_ConsiderarFilmesDiferentes_Quando_IdsDiferentes")
    void deve_ConsiderarFilmesDiferentes_Quando_IdsDiferentes() {
        Filme outroFilme = new Filme("F99", "A Chegada", 2016, 116,
                Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 84.0);

        assertNotEquals(filme, outroFilme);
    }
}