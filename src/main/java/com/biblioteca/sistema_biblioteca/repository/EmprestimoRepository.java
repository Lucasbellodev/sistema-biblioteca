package com.biblioteca.sistema_biblioteca.repository;

import com.biblioteca.sistema_biblioteca.model.Emprestimo;
import com.biblioteca.sistema_biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // 1. Buscar empréstimos por usuário (MVP)
    List<Emprestimo> findByNomeUsuario(String nomeUsuario);

    // 2. Buscar empréstimos ativos (não devolvidos)
    List<Emprestimo> findByDevolvidoFalse();

    // 3. Buscar empréstimos de um livro específico
    List<Emprestimo> findByLivro(Livro livro);

    // 4. Buscar empréstimos com data de empréstimo específica
    List<Emprestimo> findByDataEmprestimo(LocalDate dataEmprestimo);

    // 5. Buscar empréstimos que vencem em uma data específica
    List<Emprestimo> findByDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista);

    // 6. Buscar empréstimos atrasados (usando JPQL)
    @Query("SELECT e FROM Emprestimo e WHERE e.devolvido = false AND e.dataDevolucaoPrevista < :hoje")
    List<Emprestimo> findEmprestimosAtrasados(@Param("hoje") LocalDate hoje);

    // 7. Verificar se um livro está emprestado no momento
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Emprestimo e WHERE e.livro = :livro AND e.devolvido = false")
    boolean isLivroEmprestado(@Param("livro") Livro livro);

    // 8. Buscar empréstimo ativo de um livro específico
    @Query("SELECT e FROM Emprestimo e WHERE e.livro = :livro AND e.devolvido = false")
    Optional<Emprestimo> findEmprestimoAtivoPorLivro(@Param("livro") Livro livro);

    // 9. Buscar empréstimos por período (data início e fim)
    List<Emprestimo> findByDataEmprestimoBetween(LocalDate inicio, LocalDate fim);

    // 10. Contar empréstimos ativos por usuário
    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.nomeUsuario = :nomeUsuario AND e.devolvido = false")
    Long countEmprestimosAtivosPorUsuario(@Param("nomeUsuario") String nomeUsuario);

}