package com.biblioteca.sistema_biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimos") // nome da tabela no banco
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relacionamento com livro
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    //Usuario: MVP(Só nome, depois vira entidade separada)
    @Column(nullable = false)
    private String nomeUsuario;

    //Datas importantes
    @Column(nullable = false)
    private LocalDate dataEmprestimo;

    @Column(nullable = false)
    private LocalDate dataDevolucaoPrevista;

    private LocalDate dataDevolucaoReal; //pode ser null ate devolver

    //Controle de estado
    private boolean devolvido = false;

    //Construtores

    //Construtor vazio, obrigatorio para o JPA criar objeto
    public Emprestimo() {

    }

    //Construtor para novo emprestimo
    public Emprestimo(Livro livro, String nomeUsuario) {
        // Validações
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }
        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do usuário não pode ser vazio");
        }

        // Inicialização
        this.livro = livro;
        this.nomeUsuario = nomeUsuario.trim();  // Remove espaços extras
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = this.dataEmprestimo.plusDays(14);  // 14 dias
        this.devolvido = false;
        // dataDevolucaoReal = null (Padrão do Java, não precisa setar)
    }

    //Métodos auxiliares

    public void devolver() {
        this.dataDevolucaoReal = LocalDate.now(); //Metodo para devolver o livro, atualiza a data real e marca como devolvido
        this.devolvido = true;
    }

    public boolean isAtrasado() {
        if (devolvido) {
            return false; //ja devolvido, nao esta atrasado
        }
        return LocalDate.now().isAfter(dataDevolucaoPrevista);
    }

    public long calcularDiasAtraso() {
        if (!isAtrasado()) {
            return 0;
        }
        return LocalDate.now().toEpochDay() - dataDevolucaoPrevista.toEpochDay();
    }

    //Getters e Setters

    public Long getId() {
        return id;  //return id unico do emprestimo(gerado automaticamente)
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro){
        this.livro = livro;
    }

    public String getNomeUsuario() {
        return nomeUsuario;  //nome do usuario que pegou o livro emprestado
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public boolean isDevolvido() {
        return devolvido;  //return true se foi devolvido, false se ainda emprestado
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    // ToString() para debug

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", livro=" + (livro != null ? livro.getTitulo() : "null") +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucaoPrevista=" + dataDevolucaoPrevista +
                ", devolvido=" + devolvido +
                '}';
    }
}