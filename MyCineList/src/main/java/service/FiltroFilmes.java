package service;


import model.Filme;
import model.Perfil;
import model.enums.Genero;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroFilmes {
    public List<Filme> filtrar(List<Filme> filmes, Perfil perfil, int idade){
        if (filmes == null || filmes.isEmpty()){
            return Collections.emptyList();
        }
        return filmes.stream()
                .filter(f -> naoFoiAssistido(f, perfil))
                .filter(f -> classificacaoCompativel(f, perfil, idade))
                .filter(f -> idiomaAceito(f, perfil))
                .filter(f -> semGeneroBloqueado(f, perfil))
                .collect(Collectors.toList());
    }

    private boolean naoFoiAssistido (Filme filme, Perfil perfil){
        return !perfil.jaAssistiu(filme.getId());
    }

    private boolean classificacaoCompativel(Filme filme, Perfil perfil, int idade){
        int idadeMaximaPerfil = perfil.getClassificacaoMaxima().getIdade();
        int classificacaoFilme = filme.getClassificacao().getIdade();
        return classificacaoFilme <= idadeMaximaPerfil && classificacaoFilme <= idade;
    }

    private boolean idiomaAceito(Filme filme, Perfil perfil) {
        return perfil.getIdiomasAceitos().contains(filme.getIdioma());
    }
    private boolean semGeneroBloqueado(Filme filme, Perfil perfil) {
        for (Genero genero : filme.getGeneros()) {
            if (perfil.getPesoGenero(genero) == 0.0
                    && perfil.getPesosPorGenero().containsKey(genero)){
                return false;
            }
        }
        return true;
    }
}

