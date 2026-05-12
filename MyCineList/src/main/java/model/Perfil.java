package model;

import exception.DuracaoInvalidaException;
import exception.NotaInvalidaException;
import exception.PesoInvalidoException;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;

import java.util.*;

public class Perfil {

    private final Map<Genero, Double> pesosPorGenero;
    private final int duracaoMinima;
    private final int duracaoMaxima;
    private final ClassificacaoEtaria classificacaoMaxima;
    private final List<Idioma> idiomasAceitos;
    private final Set<String> filmesAssistidos;
    private final Map<String, Integer> notasPorFilme;

    public Perfil(Map<Genero, Double> pesosPorGenero,
                  int duracaoMinima, int duracaoMaxima,
                  ClassificacaoEtaria classificacaoMaxima,
                  List<Idioma> idiomasAceitos) {
        validarPesos(pesosPorGenero);
        validarDuracao(duracaoMinima, duracaoMaxima);

        this.pesosPorGenero = new EnumMap<>(pesosPorGenero);
        this.duracaoMinima = duracaoMinima;
        this.duracaoMaxima = duracaoMaxima;
        this.classificacaoMaxima = Objects.requireNonNull(classificacaoMaxima);
        this.idiomasAceitos = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(idiomasAceitos)));
        this.filmesAssistidos = new HashSet<>();
        this.notasPorFilme = new HashMap<>();
    }

    private void validarPesos(Map<Genero, Double> pesos) {
        for (Map.Entry<Genero, Double> entry : pesos.entrySet()) {
            double peso = entry.getValue();
            if (peso < 0.0 || peso > 1.0) {
                throw new PesoInvalidoException(peso);
            }
        }
    }

    private void validarDuracao(int min, int max) {
        if (min > max) {
            throw new DuracaoInvalidaException(min, max);
        }
    }

    public void marcarComoAssistido(String filmeId) {
        filmesAssistidos.add(Objects.requireNonNull(filmeId));
    }

    public boolean jaAssistiu(String filmeId) {
        return filmesAssistidos.contains(filmeId);
    }

    public void adicionarNota(String filmeId, int nota) {
        if (nota < 1 || nota > 5) {
            throw new NotaInvalidaException(nota);
        }
        notasPorFilme.put(filmeId, nota); // bug corrigido
    }

    public Integer getNotaPara(String filmeId) {
        return notasPorFilme.get(filmeId);
    }

    public double getPesoGenero(Genero genero) {
        return pesosPorGenero.getOrDefault(genero, 0.0);
    }

    public Map<Genero, Double> getPesosPorGenero() {
        return Collections.unmodifiableMap(pesosPorGenero);
    }

    public int getDuracaoMinima() { return duracaoMinima; }
    public int getDuracaoMaxima() { return duracaoMaxima; }
    public ClassificacaoEtaria getClassificacaoMaxima() { return classificacaoMaxima; }
    public List<Idioma> getIdiomasAceitos() { return idiomasAceitos; }
    public Set<String> getFilmesAssistidos() { return Collections.unmodifiableSet(filmesAssistidos); }
    public Map<String, Integer> getNotasPorFilme() { return Collections.unmodifiableMap(notasPorFilme); }
}