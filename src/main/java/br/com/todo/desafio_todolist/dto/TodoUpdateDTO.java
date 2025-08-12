package br.com.todo.desafio_todolist.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TodoUpdateDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 1, max = 100, message = "Nome deve ter entre 1 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "Prioridade é obrigatória")
    @Min(value = 1, message = "Prioridade deve ser no mínimo 1")
    @Max(value = 5, message = "Prioridade deve ser no máximo 5")
    private Integer prioridade;

    private LocalDateTime dataVencimento;

    public TodoUpdateDTO() {}

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
}