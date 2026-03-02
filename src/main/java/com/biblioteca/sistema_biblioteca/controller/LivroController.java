package com.biblioteca.sistema_biblioteca.controller;
import com.biblioteca.sistema_biblioteca.model.Livro;
import com.biblioteca.sistema_biblioteca.service.LivroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService){
        this.livroService = livroService;
    }

    //GET - Listar todos os livros
    @GetMapping
    public List<Livro> listarTodos() {
        return livroService.listarTodos();
    }

    //GET - Buscar livro por ID
    @GetMapping("/{id}")
    public Optional<Livro> buscarPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    //POST - Criar novo livro
    @PostMapping
    public Livro criarLivro(@RequestBody Livro livro) {
        return livroService.criarLivro(livro);
    }

    //PUT - Atualizar livro existente
    @PutMapping("/{id}")
    public Livro atualizarLivro(@PathVariable Long id, @RequestBody Livro livroAtualizado){
        return livroService.atualizarLivro(id, livroAtualizado);
    }

    //DELETE - Deletar livro
    @DeleteMapping("/{id}")
    public String deletarLivro(@PathVariable Long id) {
        boolean deletado = livroService.deletarLivro(id);
        return deletado ? "Livro deletado com sucesso" : "Livro não encontrado";
    }

    //GET - Buscar livros disponíveis
    @GetMapping("/disponiveis")
    public List<Livro> listarDisponiveis() {
        return livroService.listarDisponiveis();
    }
}