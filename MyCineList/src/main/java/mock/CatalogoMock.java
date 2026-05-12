package mock;

import model.Filme;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import service.CatalogoFilmesAPI;

import java.util.Arrays;
import java.util.List;

public class CatalogoMock implements CatalogoFilmesAPI {

    @Override
    public List<Filme> buscarTodos() {
        return Arrays.asList(
                new Filme("F01", "A Chegada", 2016, 116,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                        ClassificacaoEtaria.DOZE, Idioma.INGLES, 84.0),
                new Filme("F02", "Ela (Her)", 2013, 126,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA, Genero.ROMANCE),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 78.0),
                new Filme("F03", "Duna: Parte Dois", 2024, 166,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                        ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92.0),
                new Filme("F04", "Interestelar", 2014, 169,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                        ClassificacaoEtaria.DOZE, Idioma.INGLES, 95.0),
                new Filme("F05", "O Iluminado", 1980, 146,
                        Arrays.asList(Genero.TERROR),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 88.0),
                new Filme("F06", "Tropa de Elite", 2007, 115,
                        Arrays.asList(Genero.ACAO, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.PORTUGUES, 80.0),
                new Filme("F07", "Click", 2006, 107,
                        Arrays.asList(Genero.COMEDIA, Genero.DRAMA),
                        ClassificacaoEtaria.DOZE, Idioma.INGLES, 65.0),
                new Filme("F08", "Clube da Luta", 1999, 139,
                        Arrays.asList(Genero.DRAMA, Genero.THRILLER),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 90.0),
                new Filme("F09", "Matrix", 1999, 136,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.ACAO),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 89.0),
                new Filme("F10", "Parasita", 2019, 132,
                        Arrays.asList(Genero.DRAMA, Genero.THRILLER),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.COREANO, 85.0),
                new Filme("F11", "Coco", 2017, 105,
                        Arrays.asList(Genero.ANIMACAO, Genero.AVENTURA),
                        ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 88.0),
                new Filme("F12", "Toy Story", 1995, 81,
                        Arrays.asList(Genero.ANIMACAO, Genero.AVENTURA),
                        ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 92.0),
                new Filme("F13", "Blade Runner 2049", 2017, 164,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 80.0),
                new Filme("F14", "Oppenheimer", 2023, 180,
                        Arrays.asList(Genero.DRAMA),
                        ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 91.0),
                new Filme("F15", "Whiplash", 2014, 107,
                        Arrays.asList(Genero.DRAMA),
                        ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 87.0),
                new Filme("F16", "O Grande Lebowski", 1998, 117,
                        Arrays.asList(Genero.COMEDIA, Genero.THRILLER),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 72.0),
                new Filme("F17", "Cidade de Deus", 2002, 130,
                        Arrays.asList(Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.PORTUGUES, 88.0),
                new Filme("F18", "Pulp Fiction", 1994, 154,
                        Arrays.asList(Genero.THRILLER, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 90.0),
                new Filme("F19", "Midsommar", 2019, 148,
                        Arrays.asList(Genero.TERROR, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 69.0),
                new Filme("F20", "Annihilation", 2018, 115,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.TERROR),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 73.0),
                new Filme("F21", "Donnie Darko", 2001, 113,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 75.0),
                new Filme("F22", "La La Land", 2016, 128,
                        Arrays.asList(Genero.ROMANCE, Genero.DRAMA),
                        ClassificacaoEtaria.LIVRE, Idioma.INGLES, 79.0),
                new Filme("F23", "Perdido em Marte", 2015, 144,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.AVENTURA),
                        ClassificacaoEtaria.DOZE, Idioma.INGLES, 79.0),
                new Filme("F24", "Gravidade", 2013, 91,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.THRILLER),
                        ClassificacaoEtaria.DOZE, Idioma.INGLES, 77.0),
                new Filme("F25", "1917", 2019, 119,
                        Arrays.asList(Genero.DRAMA, Genero.AVENTURA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 83.0),
                new Filme("F26", "Sete Vidas", 2008, 123,
                        Arrays.asList(Genero.DRAMA, Genero.ROMANCE),
                        ClassificacaoEtaria.DOZE, Idioma.INGLES, 68.0),
                new Filme("F27", "O Auto da Compadecida", 2000, 104,
                        Arrays.asList(Genero.COMEDIA, Genero.AVENTURA),
                        ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 86.0),
                new Filme("F28", "Bacurau", 2019, 131,
                        Arrays.asList(Genero.THRILLER, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.PORTUGUES, 74.0),
                new Filme("F29", "Monty Python em Busca do Cálice Sagrado", 1975, 91,
                        Arrays.asList(Genero.COMEDIA, Genero.AVENTURA),
                        ClassificacaoEtaria.DEZ, Idioma.INGLES, 71.0),
                new Filme("F30", "Everything Everywhere All at Once", 2022, 139,
                        Arrays.asList(Genero.FICCAO_CIENTIFICA, Genero.COMEDIA, Genero.DRAMA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 86.0),
                new Filme("F31", "O Poderoso Chefão", 1972, 175,
                        Arrays.asList(Genero.DRAMA, Genero.THRILLER),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 97.0),

                new Filme("F32", "O Cavaleiro das Trevas", 2008, 152,
                        Arrays.asList(Genero.ACAO, Genero.THRILLER),
                        ClassificacaoEtaria.DOZE, Idioma.INGLES, 94.0),

                new Filme("F33", "Forrest Gump", 1994, 142,
                        Arrays.asList(Genero.DRAMA, Genero.ROMANCE),
                        ClassificacaoEtaria.DOZE, Idioma.INGLES, 91.0),

                new Filme("F34", "O Show de Truman", 1998, 103,
                        Arrays.asList(Genero.DRAMA, Genero.COMEDIA),
                        ClassificacaoEtaria.LIVRE, Idioma.INGLES, 88.0),

                new Filme("F35", "Drive", 2011, 100,
                        Arrays.asList(Genero.THRILLER, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 85.0),

                new Filme("F36", "O Lagosta", 2015, 119,
                        Arrays.asList(Genero.DRAMA, Genero.COMEDIA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 78.0),

                new Filme("F37", "A Bruxa", 2015, 92,
                        Arrays.asList(Genero.TERROR, Genero.DRAMA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 84.0),

                new Filme("F38", "Hereditário", 2018, 127,
                        Arrays.asList(Genero.TERROR, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 87.0),

                new Filme("F39", "The Lighthouse", 2019, 109,
                        Arrays.asList(Genero.TERROR, Genero.DRAMA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 83.0),

                new Filme("F40", "Garota Exemplar", 2014, 149,
                        Arrays.asList(Genero.THRILLER, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 86.0),

                new Filme("F41", "Oldboy", 2003, 120,
                        Arrays.asList(Genero.THRILLER, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.COREANO, 91.0),

                new Filme("F42", "Memórias de um Assassino", 2003, 132,
                        Arrays.asList(Genero.THRILLER, Genero.DRAMA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.COREANO, 89.0),

                new Filme("F43", "A Criada", 2016, 145,
                        Arrays.asList(Genero.THRILLER, Genero.ROMANCE),
                        ClassificacaoEtaria.DEZOITO, Idioma.COREANO, 88.0),

                new Filme("F44", "Akira", 1988, 124,
                        Arrays.asList(Genero.ANIMACAO, Genero.FICCAO_CIENTIFICA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.JAPONES, 90.0),

                new Filme("F45", "Ghost in the Shell", 1995, 83,
                        Arrays.asList(Genero.ANIMACAO, Genero.FICCAO_CIENTIFICA),
                        ClassificacaoEtaria.DEZESSEIS, Idioma.JAPONES, 88.0),

                new Filme("F46", "A Viagem de Chihiro", 2001, 125,
                        Arrays.asList(Genero.ANIMACAO, Genero.AVENTURA),
                        ClassificacaoEtaria.LIVRE, Idioma.JAPONES, 96.0),

                new Filme("F47", "Your Name", 2016, 106,
                        Arrays.asList(Genero.ANIMACAO, Genero.ROMANCE),
                        ClassificacaoEtaria.DOZE, Idioma.JAPONES, 91.0),


                new Filme("F50", "Trainspotting", 1996, 93,
                        Arrays.asList(Genero.DRAMA, Genero.COMEDIA),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 88.0),

                new Filme("F51", "O Sacrifício do Cervo Sagrado", 2017, 121,
                        Arrays.asList(Genero.THRILLER, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 79.0),

                new Filme("F52", "Climax", 2018, 97,
                        Arrays.asList(Genero.TERROR, Genero.DRAMA),
                        ClassificacaoEtaria.DEZOITO, Idioma.FRANCES, 77.0),

                new Filme("F53", "Enter the Void", 2009, 161,
                        Arrays.asList(Genero.DRAMA, Genero.FICCAO_CIENTIFICA),
                        ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 75.0)
        );
    }
}