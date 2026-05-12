# MyCineList
> **O filme perfeito pra você.**

Sistema de recomendação de filmes desenvolvido em Java com foco em testes automatizados utilizando **JUnit 5** e **Mockito**.

---

## Sobre o projeto

O **MyCineList** analisa o perfil do usuário e gera recomendações personalizadas com base em:

- Gêneros favoritos (com peso de 0.0 a 1.0)
- Duração preferida (faixa mínima e máxima)
- Classificação etária máxima aceitável
- Idiomas aceitos
- Popularidade dos filmes

Além disso, o sistema:
- Filtra filmes incompatíveis automaticamente
- Calcula score de compatibilidade (0–100)
- Gera ranking ordenado por score
- Registra histórico de recomendações
- Envia notificações opcionais ao usuário
- Suporta modo **Surpreenda-me** (seleção aleatória)

---

## Tecnologias utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Maven | 3.x |
| JUnit Jupiter | 5.10.2 |
| Mockito | 5.11.0 |

---

## Estrutura do projeto

```
src/
├── main/java/
│   ├── model/
│   │   ├── Usuario.java
│   │   ├── Perfil.java
│   │   ├── Filme.java
│   │   ├── Recomendacao.java
│   │   └── enums/
│   │       ├── Genero.java
│   │       ├── ClassificacaoEtaria.java
│   │       └── Idioma.java
│   ├── service/
│   │   ├── RecomendadorService.java
│   │   ├── CalculadoraScore.java
│   │   ├── FiltroFilmes.java
│   │   ├── CatalogoFilmesAPI.java       ← interface (mockável)
│   │   ├── HistoricoUsuarioRepository.java ← interface (mockável)
│   │   └── NotificadorPush.java         ← interface (mockável)
│   ├── exception/
│   │   ├── PesoInvalidoException.java
│   │   ├── DuracaoInvalidaException.java
│   │   └── NotaInvalidaException.java
│   ├── util/
│   │   ├── GeradorAleatorio.java        ← interface (mockável)
│   │   └── GeradorAleatorioImpl.java
│   └── mock/
│       └── CatalogoMock.java            ← 30 filmes para demo
└── test/java/
    ├── PerfilTest.java
    ├── FilmeTest.java
    ├── CalculadoraScoreTest.java
    ├── FiltroFilmesTest.java
    └── RecomendadorServiceTest.java
```

---

## Arquitetura do sistema

O sistema segue arquitetura em camadas com separação clara de responsabilidades. Nenhuma classe de domínio conhece as interfaces externas — toda orquestração passa pelo `RecomendadorService`.

### Fluxo de uma recomendação

```
Usuario / Main
      ↓  recomendar(usuario, topN)
RecomendadorService
      ↓  buscarTodos()
CatalogoFilmesAPI          ← mockada nos testes
      ↓  List<Filme>
FiltroFilmes               ← lógica pura, instância real
      ↓  candidatos filtrados
CalculadoraScore           ← lógica pura, instância real
      ↓  score por filme
[ordena e limita topN]
      ↓  registrarRecomendacao(...)
HistoricoUsuarioRepository ← mockada nos testes
      ↓  [se notificações habilitadas]
NotificadorPush            ← mockada nos testes
      ↓
List<Recomendacao>
```

### Fórmula de score (0–100)

| Componente | Peso |
|---|---|
| Compatibilidade de gênero | 50% |
| Aderência à duração preferida | 20% |
| Popularidade do filme | 15% |
| Bônus de afinidade histórica | 15% |

---

## Como executar

### Pré-requisitos

- Java 17+
- Maven 3.x

### Rodar a aplicação

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="Main"
```

Ou abrir no IntelliJ e rodar `Main.java` diretamente.

### Executar os testes

```bash
mvn test
```

### Compilar o projeto completo

```bash
mvn clean install
```

---

## Testes automatizados

26 métodos `@Test` distribuídos em 5 classes, cobrindo todos os requisitos obrigatórios.

### Recursos utilizados

| Recurso | Finalidade |
|---|---|
| `@Mock` | Simular dependências externas |
| `@Spy` | Espionar instância real de `CalculadoraScore` |
| `ArgumentCaptor` | Inspecionar o que foi passado ao mock |
| `verify()` | Confirmar chamadas nos mocks |
| `when().thenReturn()` | Stub de retorno controlado |
| `when().thenThrow()` | Simular falha de dependência |
| `assertAll()` | Validar múltiplos atributos de uma vez |
| `assertDoesNotThrow()` | Garantir resiliência a exceções |
| `@Nested` | Agrupar cenários relacionados |
| `@ParameterizedTest` | Testar múltiplas entradas com `@CsvSource` |
| `@Tag("unitario")` | Separar tipos de teste |
| `@BeforeEach` | Garantir estado limpo entre testes |

### Dependências mockadas e justificativa

| Interface | Por que mockar |
|---|---|
| `CatalogoFilmesAPI` | Em produção é uma chamada HTTP (TMDB/OMDB). Testes não devem depender de internet. |
| `HistoricoUsuarioRepository` | Persistência em banco. Não queremos gravar dados a cada teste. |
| `NotificadorPush` | Serviço externo (Firebase/OneSignal). Mockar evita disparar push real nos testes. |
| `GeradorAleatorio` | Sem mockar, o modo "Surpreenda-me" seria não-determinístico (testes flaky). |

### O que NÃO foi mockado e por quê

- **`CalculadoraScore`** — lógica pura matemática. Mockar esconderia bugs reais nas fórmulas.
- **`FiltroFilmes`** — lógica pura de filtragem. Mesma razão.
- **`Filme`, `Recomendacao`, `Perfil`** — objetos de domínio. Usamos instâncias reais.
- **Enums** — são valores, não faz sentido mockar.

---

## Cenários de teste

| ID | Cenário | Resultado Esperado | Status |
|---|---|---|---|
| CT-01 | Criar perfil com pesos válidos | Perfil criado corretamente | ✅ |
| CT-02 | Peso fora de [0,1] | `PesoInvalidoException` lançada | ✅ |
| CT-03 | Duração mínima > máxima | `DuracaoInvalidaException` lançada | ✅ |
| CT-04 | Nota fora de [1,5] | `NotaInvalidaException` lançada | ✅ |
| CT-05 | Adicionar nota válida | Nota registrada no perfil | ✅ |
| CT-06 | Filme nunca avaliado | `getNotaPara()` retorna null | ✅ |
| CT-07 | Marcar filme como assistido | Aparece em `jaAssistiu()` | ✅ |
| CT-08 | Filme criado com todos os atributos | Atributos preenchidos corretamente | ✅ |
| CT-09 | Dois filmes com mesmo ID | `equals()` retorna true | ✅ |
| CT-10 | Gênero amado (peso 1.0) | Componente gênero = 1.0 | ✅ |
| CT-11 | Filme dentro da faixa de duração | Componente duração = 1.0 | ✅ |
| CT-12 | Filme 30min acima da faixa | Componente duração reduzido | ✅ |
| CT-13 | Score em qualquer entrada | Score entre 0 e 100 | ✅ |
| CT-14 | Filme já assistido no catálogo | Removido pelo filtro | ✅ |
| CT-15 | Filme acima da classificação | Removido pelo filtro | ✅ |
| CT-16 | Filme em idioma não aceito | Removido pelo filtro | ✅ |
| CT-17 | Filme com gênero de peso 0.0 | Removido pelo filtro | ✅ |
| CT-18 | Catálogo vazio no filtro | Lista vazia (nunca null) | ✅ |
| CT-19 | Top N recomendações | Quantidade máxima respeitada | ✅ |
| CT-20 | Ordenação por score | Ordem decrescente garantida | ✅ |
| CT-21 | Catálogo vazio no service | Lista vazia retornada | ✅ |
| CT-22 | API do catálogo lança exceção | Sistema não derruba, retorna vazio | ✅ |
| CT-23 | Após recomendar | `registrarRecomendacao()` chamado | ✅ |
| CT-24 | Notificações desligadas | `notificador.enviar()` nunca chamado | ✅ |
| CT-25 | Notificações habilitadas | `notificador.enviar()` chamado 1x | ✅ |
| CT-26 | Modo Surpreenda-me | Retorna filme do conjunto filtrado | ✅ |

---

## Bugs encontrados durante os testes

| ID | Bug | Teste que revelou | Correção aplicada |
|---|---|---|---|
| BUG-01 | Construtor de `Perfil` com `durMin` e `durMax` invertidos | `PerfilTest` — duração inválida não lançava exceção | Parâmetros reordenados no construtor |
| BUG-02 | `adicionarNota()` validava mas não salvava no mapa | `PerfilTest` — `getNotaPara()` retornava null após adicionar | `notasPorFilme.put(filmeId, nota)` adicionado |
| BUG-03 | `registrar()` não chamava o notificador | `RecomendadorServiceTest` — verify falhava | Método renomeado para `registrarENotificar()` com lógica de push |
| BUG-04 | `registrarENotificar()` era definido mas nunca chamado em `recomendar()` | `RecomendadorServiceTest` — zero interações com mock | Chamada corrigida dentro de `recomendar()` |
| BUG-05 | Exceção da API derrubava o sistema | `RecomendadorServiceTest` — `assertDoesNotThrow` falhava | Try-catch adicionado em `buscarCatalogo()` |

---

## Principais aprendizados

- Como usar **Mockito** para isolar dependências externas e tornar testes determinísticos
- A diferença entre `@Mock` e `@Spy` na prática
- Por que **lógica pura** (como `CalculadoraScore`) não deve ser mockada
- Como **testes revelam bugs reais** que passariam despercebidos em revisão manual
- A importância de `@BeforeEach` para garantir estado limpo entre testes

---

## Melhorias futuras

- Interface gráfica
- Integração com API real de filmes (TMDB/OMDB)
- Persistência em banco de dados
- Recomendações com IA
- Sistema de autenticação

---

## Autor

**Pedro Graça Carneiro**
