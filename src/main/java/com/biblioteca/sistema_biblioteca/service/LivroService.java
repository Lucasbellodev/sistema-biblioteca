package com.biblioteca.sistema_biblioteca.service;

import com.biblioteca.sistema_biblioteca.model.Livro;
import com.biblioteca.sistema_biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository){
        this.livroRepository = livroRepository;
    }

    // Listar todos os livros
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    // Buscar livro por ID
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    // Criar novo livro
    public Livro criarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    // Atualizar livro existente
    public Livro atualizarLivro(Long id, Livro livroAtualizado){
        if(livroRepository.existsById(id)){
            livroAtualizado.setId(id);
            livroRepository.save(livroAtualizado);
        }
        return null;
    }


    // Deletar livro
    public boolean deletarLivro(Long id) {
        if (livroRepository.existsById(id)) {
            livroRepository.deleteById(id);
            return true;
        }
        return false; // Livro não encontrado
    }

    // Buscar livros disponíveis
    public List<Livro> listarDisponiveis() {
        return livroRepository.findByDisponivelTrue();
    }
}