package br.com.todo.desafio_todolist.dto;

import java.time.LocalDateTime;

public class TodoFiltroDTO {
    private Boolean realizado;
    private Integer prioridade;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    public TodoFiltroDTO() {}

    public TodoFiltroDTO(Boolean realizado, Integer prioridade, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.realizado = realizado;
        this.prioridade = prioridade;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Boolean getRealizado() {
        return realizado;
    }

    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }
}

