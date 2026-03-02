package com.biblioteca.sistema_biblioteca.service;

import com.biblioteca.sistema_biblioteca.model.Emprestimo;
import com.biblioteca.sistema_biblioteca.model.Livro;
import com.biblioteca.sistema_biblioteca.repository.EmprestimoRepository;
import com.biblioteca.sistema_biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;

    // Injeção de dependência via construtor
    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
    }

    /**
     * 1. Realizar um novo empréstimo
     * Validações:
     * - Livro deve existir
     * - Livro não pode estar emprestado
     * - Usuário não pode ter mais de 3 empréstimos ativos
     */
    public Emprestimo realizarEmprestimo(Long livroId, String nomeUsuario) {
        // 1. Validar nome do usuário
        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do usuário não pode ser vazio");
        }
        nomeUsuario = nomeUsuario.trim();

        // 2. Buscar livro
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado com ID: " + livroId));

        // 3. Verificar se livro está disponível
        if (emprestimoRepository.isLivroEmprestado(livro)) {
            throw new IllegalStateException("Livro '" + livro.getTitulo() + "' já está emprestado");
        }

        // 4. Verificar limite de empréstimos do usuário (máximo 3)
        Long emprestimosAtivos = emprestimoRepository.countEmprestimosAtivosPorUsuario(nomeUsuario);
        if (emprestimosAtivos >= 3) {
            throw new IllegalStateException(
                    "Usuário '" + nomeUsuario + "' já possui 3 empréstimos ativos. Devolva algum primeiro."
            );
        }

        // 5. Criar e salvar o empréstimo
        Emprestimo emprestimo = new Emprestimo(livro, nomeUsuario);
        return emprestimoRepository.save(emprestimo);
    }

    /**
     * 2. Devolver um livro
     * Validações:
     * - Empréstimo deve existir
     * - Não pode já ter sido devolvido
     */
    public Emprestimo devolverLivro(Long emprestimoId) {
        // 1. Buscar empréstimo
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado com ID: " + emprestimoId));

        // 2. Verificar se já foi devolvido
        if (emprestimo.isDevolvido()) {
            throw new IllegalStateException("Este livro já foi devolvido em: " + emprestimo.getDataDevolucaoReal());
        }

        // 3. Realizar devolução
        emprestimo.devolver();
        return emprestimoRepository.save(emprestimo);
    }

    /**
     * 3. Buscar empréstimo por ID
     */
    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado com ID: " + id));
    }

    /**
     * 4. Listar todos os empréstimos
     */
    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    /**
     * 5. Listar empréstimos ativos (não devolvidos)
     */
    public List<Emprestimo> listarAtivos() {
        return emprestimoRepository.findByDevolvidoFalse();
    }

    /**
     * 6. Listar empréstimos atrasados
     */
    public List<Emprestimo> listarAtrasados() {
        return emprestimoRepository.findEmprestimosAtrasados(LocalDate.now());
    }

    /**
     * 7. Buscar empréstimos por usuário
     */
    public List<Emprestimo> buscarPorUsuario(String nomeUsuario) {
        return emprestimoRepository.findByNomeUsuario(nomeUsuario);
    }

    /**
     * 8. Buscar empréstimos por livro
     */
    public List<Emprestimo> buscarPorLivro(Long livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado com ID: " + livroId));
        return emprestimoRepository.findByLivro(livro);
    }

    /**
     * 9. Verificar disponibilidade de um livro
     */
    public boolean verificarDisponibilidadeLivro(Long livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado com ID: " + livroId));
        return !emprestimoRepository.isLivroEmprestado(livro);
    }

    /**
     * 10. Calcular multa por atraso (R$ 2,00 por dia de atraso)
     * Regra: Apenas para empréstimos ativos e atrasados
     */
    public double calcularMulta(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado com ID: " + emprestimoId));

        if (emprestimo.isDevolvido()) {
            return 0.0; // Já devolvido, sem multa
        }

        if (emprestimo.isAtrasado()) {
            long diasAtraso = emprestimo.calcularDiasAtraso();
            double valorPorDia = 2.0; // R$ 2,00 por dia de atraso
            return diasAtraso * valorPorDia;
        }

        return 0.0; // Não está atrasado
    }

    /**
     * 11. Renovar empréstimo (estender prazo por mais 7 dias)
     * Validações:
     * - Empréstimo deve existir
     * - Não pode estar devolvido
     * - Não pode estar atrasado
     */
    public Emprestimo renovarEmprestimo(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado com ID: " + emprestimoId));

        if (emprestimo.isDevolvido()) {
            throw new IllegalStateException("Não é possível renovar um empréstimo já devolvido");
        }

        if (emprestimo.isAtrasado()) {
            throw new IllegalStateException("Não é possível renovar um empréstimo atrasado. Pague a multa primeiro.");
        }

        // Renovar por mais 7 dias
        LocalDate novaDataPrevista = emprestimo.getDataDevolucaoPrevista().plusDays(7);
        emprestimo.setDataDevolucaoPrevista(novaDataPrevista);

        return emprestimoRepository.save(emprestimo);
    }

    /**
     * 12. Buscar empréstimos por período
     */
    public List<Emprestimo> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser posterior à data final");
        }
        return emprestimoRepository.findByDataEmprestimoBetween(inicio, fim);
    }
}