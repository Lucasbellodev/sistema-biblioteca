package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.model.Emprestimo;
import com.biblioteca.sistema_biblioteca.service.EmprestimoService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    // 1. Realizar novo empréstimo
    @PostMapping
    public Emprestimo realizarEmprestimo(@RequestParam Long livroId,
                                         @RequestParam String nomeUsuario) {
        return emprestimoService.realizarEmprestimo(livroId, nomeUsuario);
    }

    // 2. Devolver livro
    @PutMapping("/{id}/devolver")
    public Emprestimo devolverLivro(@PathVariable Long id) {
        return emprestimoService.devolverLivro(id);
    }

    // 3. Buscar empréstimo por ID
    @GetMapping("/{id}")
    public Emprestimo buscarPorId(@PathVariable Long id) {
        return emprestimoService.buscarPorId(id);
    }

    // 4. Listar todos os empréstimos
    @GetMapping
    public List<Emprestimo> listarTodos() {
        return emprestimoService.listarTodos();
    }

    // 5. Listar empréstimos ativos
    @GetMapping("/ativos")
    public List<Emprestimo> listarAtivos() {
        return emprestimoService.listarAtivos();
    }

    // 6. Listar empréstimos atrasados
    @GetMapping("/atrasados")
    public List<Emprestimo> listarAtrasados() {
        return emprestimoService.listarAtrasados();
    }

    // 7. Buscar empréstimos por usuário
    @GetMapping("/buscar/usuario")
    public List<Emprestimo> buscarPorUsuario(@RequestParam String nomeUsuario) {
        return emprestimoService.buscarPorUsuario(nomeUsuario);
    }

    // 8. Buscar empréstimos por livro
    @GetMapping("/buscar/livro")
    public List<Emprestimo> buscarPorLivro(@RequestParam Long livroId) {
        return emprestimoService.buscarPorLivro(livroId);
    }

    // 9. Verificar disponibilidade de livro
    @GetMapping("/disponibilidade")
    public boolean verificarDisponibilidade(@RequestParam Long livroId) {
        return emprestimoService.verificarDisponibilidadeLivro(livroId);
    }

    // 10. Calcular multa
    @GetMapping("/{id}/multa")
    public double calcularMulta(@PathVariable Long id) {
        return emprestimoService.calcularMulta(id);
    }

    // 11. Renovar empréstimo
    @PutMapping("/{id}/renovar")
    public Emprestimo renovarEmprestimo(@PathVariable Long id) {
        return emprestimoService.renovarEmprestimo(id);
    }

    // 12. Buscar empréstimos por período
    @GetMapping("/periodo")
    public List<Emprestimo> buscarPorPeriodo(@RequestParam LocalDate inicio,
                                             @RequestParam LocalDate fim) {
        return emprestimoService.buscarPorPeriodo(inicio, fim);
    }
}