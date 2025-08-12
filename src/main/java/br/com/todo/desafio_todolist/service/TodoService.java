package br.com.todo.desafio_todolist.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.todo.desafio_todolist.dto.TodoFiltroDTO;
import br.com.todo.desafio_todolist.entity.Todo;
import br.com.todo.desafio_todolist.exception.BusinessException;
import br.com.todo.desafio_todolist.exception.ResourceNotFoundException;
import br.com.todo.desafio_todolist.repository.TodoRepository;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo create(Todo todo) {
        if (todo.getTarefaPai() != null && todo.getTarefaPai().getId() != null) {
            Todo tarefaPai = findById(todo.getTarefaPai().getId());
            todo.setTarefaPai(tarefaPai);
        }
        
        todo.setDataCriacao(LocalDateTime.now());
        return todoRepository.save(todo);
    }

    public Page<Todo> list(TodoFiltroDTO filtros, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return todoRepository.findWithFilters(
            filtros.getRealizado(),
            filtros.getPrioridade(),
            filtros.getDataInicio(),
            filtros.getDataFim(),
            pageable
        );
    }

    public List<Todo> listAll() {
        Sort sort = Sort.by("prioridade").descending()
                .and(Sort.by("nome").ascending());
        return todoRepository.findAll(sort);
    }

    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo não encontrado com id: " + id));
    }

    public Todo update(Long id, Todo todoAtualizado) {
        Todo todoExistente = findById(id);
        
        todoExistente.setNome(todoAtualizado.getNome());
        todoExistente.setDescricao(todoAtualizado.getDescricao());
        todoExistente.setPrioridade(todoAtualizado.getPrioridade());
        todoExistente.setDataVencimento(todoAtualizado.getDataVencimento());
        todoExistente.setDataAtualizacao(LocalDateTime.now());
        
        return todoRepository.save(todoExistente);
    }

    public Todo updateStatus(Long id, Boolean realizado) {
        Todo todo = findById(id);
        
        if (realizado && !todo.podeSerConcluida()) {
            throw new BusinessException(
                String.format("Não é possível concluir a tarefa. Existem %d subtarefa(s) pendente(s).", 
                              todo.getSubtarefasPendentes())
            );
        }
        
        todo.setRealizado(realizado);
        todo.setDataAtualizacao(LocalDateTime.now());
        
        return todoRepository.save(todo);
    }

    public void delete(Long id) {
        Todo todo = findById(id);
        
        if (!todo.getSubtarefas().isEmpty()) {
            throw new BusinessException("Não é possível excluir uma tarefa que possui subtarefas.");
        }
        
        todoRepository.deleteById(id);
    }

    public Todo adicionarSubtarefa(Long tarefaPaiId, Todo subtarefa) {
        Todo tarefaPai = findById(tarefaPaiId);
        subtarefa.setTarefaPai(tarefaPai);
        return todoRepository.save(subtarefa);
    }

    public List<Todo> listarSubtarefas(Long tarefaPaiId) {
        findById(tarefaPaiId); 
        return todoRepository.findByTarefaPaiId(tarefaPaiId);
    }

    public List<Todo> findTarefasVencidas() {
        return todoRepository.findTarefasVencidas(LocalDateTime.now());
    }

    public List<Todo> findTarefasComVencimentoProximo(int dias) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limite = agora.plusDays(dias);
        return todoRepository.findTarefasComVencimentoProximo(agora, limite);
    }

    public List<Todo> findByStatus(Boolean realizado) {
        return todoRepository.findByRealizadoAndTarefaPaiIsNull(realizado);
    }

    public List<Todo> findByPrioridade(Integer prioridade) {
        return todoRepository.findByPrioridadeAndTarefaPaiIsNull(prioridade);
    }
}