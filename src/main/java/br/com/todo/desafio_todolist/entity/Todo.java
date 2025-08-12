package br.com.todo.desafio_todolist.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 1, max = 100, message = "Nome deve ter entre 1 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @NotNull(message = "Status é obrigatório")
    @Column(nullable = false)
    private Boolean realizado = false;

    @NotNull(message = "Prioridade é obrigatória")
    @Min(value = 1, message = "Prioridade deve ser no mínimo 1")
    @Max(value = 5, message = "Prioridade deve ser no máximo 5")
    @Column(nullable = false)
    private Integer prioridade;

    @Column(name = "data_vencimento")
    private LocalDateTime dataVencimento;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarefa_pai_id")
    private Todo tarefaPai;

    @OneToMany(mappedBy = "tarefaPai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> subtarefas = new ArrayList<>();

    public Todo() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Todo(String nome, String descricao, Boolean realizado, Integer prioridade) {
        this.nome = nome;
        this.descricao = descricao;
        this.realizado = realizado != null ? realizado : false;
        this.prioridade = prioridade;
        this.dataCriacao = LocalDateTime.now();
    }

    public Todo(String nome, String descricao, Boolean realizado, Integer prioridade, LocalDateTime dataVencimento) {
        this(nome, descricao, realizado, prioridade);
        this.dataVencimento = dataVencimento;
    }

    // Método para verificar se pode ser concluída
    public boolean podeSerConcluida() {
        return subtarefas.stream().allMatch(subtarefa -> subtarefa.isRealizado());
    }

    // Método para contar subtarefas pendentes
    public long getSubtarefasPendentes() {
        return subtarefas.stream().filter(subtarefa -> !subtarefa.isRealizado()).count();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Todo getTarefaPai() {
        return tarefaPai;
    }

    public void setTarefaPai(Todo tarefaPai) {
        this.tarefaPai = tarefaPai;
    }

    public List<Todo> getSubtarefas() {
        return subtarefas;
    }

    public void setSubtarefas(List<Todo> subtarefas) {
        this.subtarefas = subtarefas;
    }

    // Método auxiliar para adicionar subtarefa
    public void adicionarSubtarefa(Todo subtarefa) {
        subtarefas.add(subtarefa);
        subtarefa.setTarefaPai(this);
    }

    // Método auxiliar para remover subtarefa
    public void removerSubtarefa(Todo subtarefa) {
        subtarefas.remove(subtarefa);
        subtarefa.setTarefaPai(null);
    }
}