package model;

import java.util.Objects;

public class Usuario {
    private final String nome;
    private final int idade;
    private final Perfil perfil;
    private final boolean notificacoesHabilitadas;

    public Usuario(String nome, int idade, Perfil perfil, boolean notificacoesHabilitadas) {
        this.nome = Objects.requireNonNull(nome, "nome obrigatório");
        this.idade = idade;
        this.perfil = Objects.requireNonNull(perfil, "perfil obrigatório");
        this.notificacoesHabilitadas = notificacoesHabilitadas;
    }

    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public Perfil getPerfil() { return perfil; }
    public boolean isNotificacoesHabilitadas() { return notificacoesHabilitadas; }
}