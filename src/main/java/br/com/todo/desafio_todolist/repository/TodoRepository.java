package br.com.todo.desafio_todolist.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.todo.desafio_todolist.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    List<Todo> findByTarefaPaiIsNull();
    
    List<Todo> findByRealizadoAndTarefaPaiIsNull(Boolean realizado);
    
    List<Todo> findByPrioridadeAndTarefaPaiIsNull(Integer prioridade);
    
    @Query("SELECT t FROM Todo t WHERE t.dataVencimento < :agora AND t.realizado = false AND t.tarefaPai IS NULL")
    List<Todo> findTarefasVencidas(@Param("agora") LocalDateTime agora);
    
    @Query("SELECT t FROM Todo t WHERE t.dataVencimento BETWEEN :inicio AND :fim AND t.realizado = false AND t.tarefaPai IS NULL")
    List<Todo> findTarefasComVencimentoProximo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT t FROM Todo t WHERE " +
           "(:realizado IS NULL OR t.realizado = :realizado) AND " +
           "(:prioridade IS NULL OR t.prioridade = :prioridade) AND " +
           "(:dataInicio IS NULL OR t.dataVencimento >= :dataInicio) AND " +
           "(:dataFim IS NULL OR t.dataVencimento <= :dataFim) AND " +
           "t.tarefaPai IS NULL")
    Page<Todo> findWithFilters(
        @Param("realizado") Boolean realizado,
        @Param("prioridade") Integer prioridade,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim,
        Pageable pageable
    );
    
    List<Todo> findByTarefaPaiId(Long tarefaPaiId);
    
    @Query("SELECT COUNT(s) FROM Todo s WHERE s.tarefaPai.id = :tarefaPaiId AND s.realizado = false")
    long countSubtarefasPendentes(@Param("tarefaPaiId") Long tarefaPaiId);
}