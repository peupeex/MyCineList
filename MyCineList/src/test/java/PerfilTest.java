import exception.DuracaoInvalidaException;
import exception.NotaInvalidaException;
import exception.PesoInvalidoException;
import model.Perfil;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
@DisplayName("Testes de Perfil")
class PerfilTest {

    private Map<Genero, Double> pesosValidos;
    private List<Idioma> idiomas;

    @BeforeEach
    void setUp() {
        pesosValidos = new EnumMap<>(Genero.class);
        pesosValidos.put(Genero.FICCAO_CIENTIFICA, 0.9);
        pesosValidos.put(Genero.DRAMA, 0.6);
        pesosValidos.put(Genero.TERROR, 0.0);
        idiomas = Arrays.asList(Idioma.PORTUGUES, Idioma.INGLES);
    }

    @Test
    @DisplayName("deve_CriarPerfil_Quando_DadosValidos")
    void deve_CriarPerfil_Quando_DadosValidos() {
        Perfil perfil = new Perfil(pesosValidos, 90, 150, ClassificacaoEtaria.DEZESSEIS, idiomas);

        assertAll(
                () -> assertEquals(90, perfil.getDuracaoMinima()),
                () -> assertEquals(150, perfil.getDuracaoMaxima()),
                () -> assertEquals(ClassificacaoEtaria.DEZESSEIS, perfil.getClassificacaoMaxima()),
                () -> assertEquals(0.9, perfil.getPesoGenero(Genero.FICCAO_CIENTIFICA))
        );
    }

    @Test
    @DisplayName("deve_LancarExcecao_Quando_PesoAcimaDeUm")
    void deve_LancarExcecao_Quando_PesoAcimaDeUm() {
        pesosValidos.put(Genero.ACAO, 1.5);

        assertThrows(PesoInvalidoException.class,
                () -> new Perfil(pesosValidos, 90, 150, ClassificacaoEtaria.DEZESSEIS, idiomas));
    }

    @Test
    @DisplayName("deve_LancarExcecao_Quando_PesoNegativo")
    void deve_LancarExcecao_Quando_PesoNegativo() {
        pesosValidos.put(Genero.COMEDIA, -0.1);

        assertThrows(PesoInvalidoException.class,
                () -> new Perfil(pesosValidos, 90, 150, ClassificacaoEtaria.DEZESSEIS, idiomas));
    }

    @Test
    @DisplayName("deve_LancarExcecao_Quando_DuracaoMinimaMAiorQueMaxima")
    void deve_LancarExcecao_Quando_DuracaoMinimaMaiorQueMaxima() {
        assertThrows(DuracaoInvalidaException.class,
                () -> new Perfil(pesosValidos, 150, 90, ClassificacaoEtaria.DEZESSEIS, idiomas));
    }

    @Test
    @DisplayName("deve_MarcarFilmeComoAssistido_Quando_Solicitado")
    void deve_MarcarFilmeComoAssistido_Quando_Solicitado() {
        Perfil perfil = new Perfil(pesosValidos, 90, 150, ClassificacaoEtaria.DEZESSEIS, idiomas);

        perfil.marcarComoAssistido("F01");

        assertTrue(perfil.jaAssistiu("F01"));
        assertFalse(perfil.jaAssistiu("F99"));
    }

    @Test
    @DisplayName("deve_SalvarNota_Quando_NotaValida")
    void deve_SalvarNota_Quando_NotaValida() {
        Perfil perfil = new Perfil(pesosValidos, 90, 150, ClassificacaoEtaria.DEZESSEIS, idiomas);

        perfil.adicionarNota("F01", 5);

        assertEquals(5, perfil.getNotaPara("F01"));
    }

    @Test
    @DisplayName("deve_LancarExcecao_Quando_NotaForaDoIntervalo")
    void deve_LancarExcecao_Quando_NotaForaDoIntervalo() {
        Perfil perfil = new Perfil(pesosValidos, 90, 150, ClassificacaoEtaria.DEZESSEIS, idiomas);

        assertThrows(NotaInvalidaException.class, () -> perfil.adicionarNota("F01", 6));
        assertThrows(NotaInvalidaException.class, () -> perfil.adicionarNota("F02", 0));
    }

    @Test
    @DisplayName("deve_RetornarNull_Quando_FilmeNuncaAvaliado")
    void deve_RetornarNull_Quando_FilmeNuncaAvaliado() {
        Perfil perfil = new Perfil(pesosValidos, 90, 150, ClassificacaoEtaria.DEZESSEIS, idiomas);

        assertNull(perfil.getNotaPara("filme-inexistente"));
    }

    @Test
    @DisplayName("deve_RetornarPesoZero_Quando_GeneroNaoCadastrado")
    void deve_RetornarPesoZero_Quando_GeneroNaoCadastrado() {
        Perfil perfil = new Perfil(pesosValidos, 90, 150, ClassificacaoEtaria.DEZESSEIS, idiomas);

        assertEquals(0.0, perfil.getPesoGenero(Genero.AVENTURA));
    }
}