import model.Filme;
import model.Perfil;
import model.Recomendacao;
import model.Usuario;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import service.*;
import util.GeradorAleatorio;
import util.NotificadorPush;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitario")
@DisplayName("Testes de RecomendadorService")
class RecomendadorServiceTest {

    @Mock private CatalogoFilmesAPI catalogo;
    @Mock private HistoricoUsuarioRepository historicoRepo;
    @Mock private NotificadorPush notificador;
    @Mock private GeradorAleatorio gerador;
    @Spy  private CalculadoraScore calculadora;

    private FiltroFilmes filtro;
    private RecomendadorService service;
    private Usuario maria;

    @BeforeEach
    void setUp() {
        filtro = new FiltroFilmes();
        service = new RecomendadorService(catalogo, historicoRepo, notificador, gerador, calculadora, filtro);

        Map<Genero, Double> pesos = new EnumMap<>(Genero.class);
        pesos.put(Genero.FICCAO_CIENTIFICA, 0.9);
        pesos.put(Genero.DRAMA, 0.6);
        pesos.put(Genero.TERROR, 0.0);

        Perfil perfil = new Perfil(pesos, 90, 150,
                ClassificacaoEtaria.DEZESSEIS,
                Arrays.asList(Idioma.INGLES, Idioma.PORTUGUES));

        maria = new Usuario("Maria", 28, perfil, false);
    }

    // ---- Helpers ----

    private Filme criarFilme(String id, String titulo, Genero genero, int duracao,
                             ClassificacaoEtaria class_, double popularidade) {
        return new Filme(id, titulo, 2020, duracao,
                Arrays.asList(genero), class_, Idioma.INGLES, popularidade);
    }

    // ---- Testes ----

    @Test
    @DisplayName("deve_RetornarTopN_Quando_HaFilmesSuficientes")
    void deve_RetornarTopN_Quando_HaFilmesSuficientes() {
        List<Filme> catalogoMock = Arrays.asList(
                criarFilme("F01", "Filme A", Genero.FICCAO_CIENTIFICA, 120, ClassificacaoEtaria.DOZE, 80),
                criarFilme("F02", "Filme B", Genero.FICCAO_CIENTIFICA, 110, ClassificacaoEtaria.DOZE, 70),
                criarFilme("F03", "Filme C", Genero.FICCAO_CIENTIFICA, 100, ClassificacaoEtaria.DOZE, 60)
        );
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        List<Recomendacao> resultado = service.recomendar(maria, 2);

        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("deve_OrdenarPorScoreDesc_Quando_HaMultiplosFilmes")
    void deve_OrdenarPorScoreDesc_Quando_HaMultiplosFilmes() {
        List<Filme> catalogoMock = Arrays.asList(
                criarFilme("F01", "Alto Score", Genero.FICCAO_CIENTIFICA, 120, ClassificacaoEtaria.DOZE, 90),
                criarFilme("F02", "Baixo Score", Genero.DRAMA, 120, ClassificacaoEtaria.DOZE, 30)
        );
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        List<Recomendacao> resultado = service.recomendar(maria, 5);

        assertTrue(resultado.get(0).getScore()
                >= resultado.get(1).getScore());
    }

    @Test
    @DisplayName("deve_RetornarListaVazia_Quando_CatalogoVazio")
    void deve_RetornarListaVazia_Quando_CatalogoVazio() {
        when(catalogo.buscarTodos()).thenReturn(Collections.emptyList());

        List<Recomendacao> resultado = service.recomendar(maria, 5);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve_RetornarListaVazia_Quando_CatalogoLancaExcecao")
    void deve_RetornarListaVazia_Quando_CatalogoLancaExcecao() {
        when(catalogo.buscarTodos()).thenThrow(new RuntimeException("API offline"));

        assertDoesNotThrow(() -> {
            List<Recomendacao> resultado = service.recomendar(maria, 5);
            assertTrue(resultado.isEmpty());
        });

        verify(notificador, never()).enviar(any(), any());
    }

    @Test
    @DisplayName("deve_ChamarRegistrarRecomendacao_Quando_RecomendacaoGerada")
    void deve_ChamarRegistrarRecomendacao_Quando_RecomendacaoGerada() {
        List<Filme> catalogoMock = Arrays.asList(
                criarFilme("F01", "Filme A", Genero.FICCAO_CIENTIFICA, 120, ClassificacaoEtaria.DOZE, 80)
        );
        when(catalogo.buscarTodos())
                .thenReturn(catalogoMock);

        service.recomendar(maria, 5);

        verify(historicoRepo, times(1))
                .registrarRecomendacao(eq(maria), anyList());
    }

    @Test
    @DisplayName("deve_NaoChamarNotificador_Quando_NotificacoesDesligadas")
    void deve_NaoChamarNotificador_Quando_NotificacoesDesligadas() {
        List<Filme> catalogoMock = Arrays.asList(
                criarFilme("F01", "Filme A", Genero.FICCAO_CIENTIFICA, 120, ClassificacaoEtaria.DOZE, 80)
        );
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        service.recomendar(maria, 5); // maria tem notificações desligadas

        verify(notificador, never()).enviar(any(), any());
    }

    @Test
    @DisplayName("deve_ChamarNotificador_Quando_NotificacoesHabilitadas")
    void deve_ChamarNotificador_Quando_NotificacoesHabilitadas() {
        Map<Genero, Double> pesos = new EnumMap<>(Genero.class);
        pesos.put(Genero.FICCAO_CIENTIFICA, 0.9);
        Perfil perfil = new Perfil(pesos, 90, 150, ClassificacaoEtaria.DEZESSEIS,
                Arrays.asList(Idioma.INGLES));
        Usuario usuarioComNotificacao = new Usuario("João", 25, perfil, true);

        List<Filme> catalogoMock = Arrays.asList(
                criarFilme("F01", "Filme A", Genero.FICCAO_CIENTIFICA, 120, ClassificacaoEtaria.DOZE, 80)
        );
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        service.recomendar(usuarioComNotificacao, 5);

        verify(notificador, times(1)).enviar(eq(usuarioComNotificacao), anyString());
    }

    @Test
    @DisplayName("deve_UsarArgumentCaptor_Para_VerificarRecomendacoesRegistradas")
    void deve_UsarArgumentCaptor_Para_VerificarRecomendacoesRegistradas() {
        List<Filme> catalogoMock = Arrays.asList(
                criarFilme("F01", "Filme A", Genero.FICCAO_CIENTIFICA, 120, ClassificacaoEtaria.DOZE, 80),
                criarFilme("F02", "Filme B", Genero.FICCAO_CIENTIFICA, 110, ClassificacaoEtaria.DOZE, 70)
        );
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        service.recomendar(maria, 2);

        ArgumentCaptor<List<Recomendacao>> captor = ArgumentCaptor.forClass(List.class);
        verify(historicoRepo).registrarRecomendacao(eq(maria), captor.capture());

        List<Recomendacao> registradas = captor.getValue();
        assertAll(
                () -> assertEquals(2, registradas.size()),
                () -> assertNotNull(registradas.get(0).getJustificativa()),
                () -> assertTrue(registradas.get(0).getScore() >= registradas.get(1).getScore())
        );
    }

    @Test
    @DisplayName("deve_RetornarFilmeAleatorio_Quando_ModeSurpreendaMe")
    void deve_RetornarFilmeAleatorio_Quando_ModeSurpreendaMe() {
        List<Filme> catalogoMock = Arrays.asList(
                criarFilme("F01", "Filme A", Genero.FICCAO_CIENTIFICA, 120, ClassificacaoEtaria.DOZE, 80),
                criarFilme("F02", "Filme B", Genero.FICCAO_CIENTIFICA, 110, ClassificacaoEtaria.DOZE, 70),
                criarFilme("F03", "Filme C", Genero.FICCAO_CIENTIFICA, 100, ClassificacaoEtaria.DOZE, 60)
        );
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);
        when(gerador.sortearInteiro(0, 2)).thenReturn(1); // força índice 1

        Optional<Recomendacao> resultado = service.recomendarAleatorio(maria);

        assertTrue(resultado.isPresent());
        assertEquals("Filme B", resultado.get().getFilme().getTitulo());
    }

    @Test
    @DisplayName("deve_RetornarEmpty_Quando_SurpreendaMeSemCandidatos")
    void deve_RetornarEmpty_Quando_SurpreendaMeSemCandidatos() {
        when(catalogo.buscarTodos()).thenReturn(Collections.emptyList());

        Optional<Recomendacao> resultado = service.recomendarAleatorio(maria);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("deve_UsarSpy_Para_VerificarCalculadoraChamada")
    void deve_UsarSpy_Para_VerificarCalculadoraChamada() {
        List<Filme> catalogoMock = Arrays.asList(
                criarFilme("F01", "Filme A", Genero.FICCAO_CIENTIFICA, 120, ClassificacaoEtaria.DOZE, 80),
                criarFilme("F02", "Filme B", Genero.FICCAO_CIENTIFICA, 110, ClassificacaoEtaria.DOZE, 70)
        );
        when(catalogo.buscarTodos()).thenReturn(catalogoMock);

        service.recomendar(maria, 5);

        // Spy verifica que calculadora foi chamada uma vez por filme
        verify(calculadora, times(2))
                .calcular(any(Filme.class), any(Perfil.class));
    }

    @Nested
    @DisplayName("Quando catálogo está vazio")
    class QuandoCatalogoEstaVazio {

        @Test
        @DisplayName("deve_RetornarListaVazia_Quando_NenhumFilmeNoCatalogo")
        void deve_RetornarListaVazia_Quando_NenhumFilmeNoCatalogo() {
            when(catalogo.buscarTodos()).thenReturn(Collections.emptyList());

            List<Recomendacao> resultado = service.recomendar(maria, 5);

            assertTrue(resultado.isEmpty());
            verify(historicoRepo, never()).registrarRecomendacao(any(), any());
        }

        @Test
        @DisplayName("deve_NaoChamarNotificador_Quando_CatalogoVazio")
        void deve_NaoChamarNotificador_Quando_CatalogoVazio() {
            when(catalogo.buscarTodos()).thenReturn(Collections.emptyList());

            service.recomendar(maria, 5);

            verify(notificador, never()).enviar(any(), any());
        }
    }
}