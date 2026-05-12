import model.Filme;
import model.Perfil;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.*;
import service.FiltroFilmes;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
@DisplayName("Testes de FiltroFilmes")
class FiltroFilmesTest {

    private FiltroFilmes filtro;
    private Perfil perfil;
    private static final int IDADE_USUARIO = 28;

    @BeforeEach
    void setUp() {
        filtro = new FiltroFilmes();

        Map<Genero, Double> pesos = new EnumMap<>(Genero.class);
        pesos.put(Genero.FICCAO_CIENTIFICA, 0.9);
        pesos.put(Genero.DRAMA, 0.6);
        pesos.put(Genero.TERROR, 0.0); // bloqueado

        perfil = new Perfil(pesos, 90, 150,
                ClassificacaoEtaria.DEZESSEIS,
                Arrays.asList(Idioma.INGLES, Idioma.PORTUGUES));
    }

    private Filme filmeValido(String id) {
        return new Filme(id, "Filme " + id, 2020, 120,
                Arrays.asList(Genero.FICCAO_CIENTIFICA),
                ClassificacaoEtaria.DOZE, Idioma.INGLES, 80.0);
    }

    @Test
    @DisplayName("deve_RemoverFilme_Quando_JaFoiAssistido")
    void deve_RemoverFilme_Quando_JaFoiAssistido() {
        perfil.marcarComoAssistido("F01");
        List<Filme> catalogo = Arrays.asList(filmeValido("F01"), filmeValido("F02"));

        List<Filme> resultado = filtro.filtrar(catalogo, perfil, IDADE_USUARIO);

        assertFalse(resultado.stream().anyMatch(f -> f.getId().equals("F01")));
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("deve_RemoverFilme_Quando_ClassificacaoAcimaDoUsuario")
    void deve_RemoverFilme_Quando_ClassificacaoAcimaDoUsuario() {
        Filme filmeAdulto = new Filme("F03", "Filme 18+", 2020, 120,
                Arrays.asList(Genero.FICCAO_CIENTIFICA),
                ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 80.0);

        List<Filme> resultado = filtro.filtrar(Arrays.asList(filmeAdulto), perfil, IDADE_USUARIO);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve_RemoverFilme_Quando_IdiomaForaDosPreferidos")
    void deve_RemoverFilme_Quando_IdiomaForaDosPreferidos() {
        Filme filmeJapones = new Filme("F04", "Anime", 2020, 120,
                Arrays.asList(Genero.FICCAO_CIENTIFICA),
                ClassificacaoEtaria.LIVRE, Idioma.JAPONES, 80.0);

        List<Filme> resultado = filtro.filtrar(Arrays.asList(filmeJapones), perfil, IDADE_USUARIO);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve_RemoverFilme_Quando_GeneroEstaBloqueado")
    void deve_RemoverFilme_Quando_GeneroEstaBloqueado() {
        Filme filmeTerror = new Filme("F05", "Terror", 2020, 120,
                Arrays.asList(Genero.TERROR),
                ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 80.0);

        List<Filme> resultado = filtro.filtrar(Arrays.asList(filmeTerror), perfil, IDADE_USUARIO);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve_RetornarListaVazia_Quando_CatalogoVazio")
    void deve_RetornarListaVazia_Quando_CatalogoVazio() {
        List<Filme> resultado = filtro.filtrar(Collections.emptyList(), perfil, IDADE_USUARIO);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve_ManterFilme_Quando_PassaEmTodosOsFiltros")
    void deve_ManterFilme_Quando_PassaEmTodosOsFiltros() {
        List<Filme> resultado = filtro.filtrar(Arrays.asList(filmeValido("F10")), perfil, IDADE_USUARIO);

        assertFalse(resultado.isEmpty());
        assertEquals("F10", resultado.get(0).getId());
    }
}