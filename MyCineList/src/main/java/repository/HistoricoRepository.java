package repository;

import model.Filme;
import model.Usuario;

import java.util.List;

public interface HistoricoRepository {
    List<Filme> buscarHistorico(Usuario usuario);
}
