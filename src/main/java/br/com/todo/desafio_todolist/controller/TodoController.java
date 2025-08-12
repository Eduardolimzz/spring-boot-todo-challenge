package br.com.todo.desafio_todolist.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.todo.desafio_todolist.dto.TodoCreateDTO;
import br.com.todo.desafio_todolist.dto.TodoFiltroDTO;
import br.com.todo.desafio_todolist.dto.TodoUpdateDTO;
import br.com.todo.desafio_todolist.entity.Todo;
import br.com.todo.desafio_todolist.service.TodoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
}

    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody TodoCreateDTO todoDTO) {
        Todo todo = convertToEntity(todoDTO);
        Todo todoSalvo = todoService.create(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoSalvo);
    }

    @GetMapping
    public ResponseEntity<Page<Todo>> list(
            @RequestParam(required = false) Boolean realizado,
            @RequestParam(required = false) Integer prioridade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "prioridade") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        TodoFiltroDTO filtros = new TodoFiltroDTO(realizado, prioridade, dataInicio, dataFim);
        Page<Todo> todos = todoService.list(filtros, page, size, sortBy, sortDir);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Todo>> listAll() {
        List<Todo> todos = todoService.listAll();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findById(@PathVariable Long id) {
        Todo todo = todoService.findById(id);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @Valid @RequestBody TodoUpdateDTO todoDTO) {
        Todo todo = convertToEntityForUpdate(todoDTO);
        Todo todoAtualizado = todoService.update(id, todo);
        return ResponseEntity.ok(todoAtualizado);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Todo> updateStatus(@PathVariable Long id, @RequestParam Boolean realizado) {
        Todo todoAtualizado = todoService.updateStatus(id, realizado);
        return ResponseEntity.ok(todoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints para subtarefas
    @PostMapping("/{tarefaPaiId}/subtarefas")
    public ResponseEntity<Todo> adicionarSubtarefa(
            @PathVariable Long tarefaPaiId, 
            @Valid @RequestBody TodoCreateDTO subtarefaDTO) {
        
        Todo subtarefa = convertToEntity(subtarefaDTO);
        Todo subtarefaSalva = todoService.adicionarSubtarefa(tarefaPaiId, subtarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(subtarefaSalva);
    }

    @GetMapping("/{tarefaPaiId}/subtarefas")
    public ResponseEntity<List<Todo>> listarSubtarefas(@PathVariable Long tarefaPaiId) {
        List<Todo> subtarefas = todoService.listarSubtarefas(tarefaPaiId);
        return ResponseEntity.ok(subtarefas);
    }

    // Endpoints de consulta específicos
    @GetMapping("/vencidas")
    public ResponseEntity<List<Todo>> listarTarefasVencidas() {
        List<Todo> tarefasVencidas = todoService.findTarefasVencidas();
        return ResponseEntity.ok(tarefasVencidas);
    }

    @GetMapping("/vencimento-proximo")
    public ResponseEntity<List<Todo>> listarTarefasComVencimentoProximo(@RequestParam(defaultValue = "7") int dias) {
        List<Todo> tarefas = todoService.findTarefasComVencimentoProximo(dias);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/status/{realizado}")
    public ResponseEntity<List<Todo>> listarPorStatus(@PathVariable Boolean realizado) {
        List<Todo> todos = todoService.findByStatus(realizado);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/prioridade/{prioridade}")
    public ResponseEntity<List<Todo>> listarPorPrioridade(@PathVariable Integer prioridade) {
        List<Todo> todos = todoService.findByPrioridade(prioridade);
        return ResponseEntity.ok(todos);
    }

    // Métodos auxiliares para conversão
    private Todo convertToEntity(TodoCreateDTO todoDTO) {
        Todo todo = new Todo();
        todo.setNome(todoDTO.getNome());
        todo.setDescricao(todoDTO.getDescricao());
        todo.setPrioridade(todoDTO.getPrioridade());
        todo.setDataVencimento(todoDTO.getDataVencimento());
        todo.setRealizado(false); 
        
        if (todoDTO.getTarefaPaiId() != null) {
            Todo tarefaPai = new Todo();
            tarefaPai.setId(todoDTO.getTarefaPaiId());
            todo.setTarefaPai(tarefaPai);
        }
        
        return todo;
    }

    private Todo convertToEntityForUpdate(TodoUpdateDTO todoDTO) {
        Todo todo = new Todo();
        todo.setNome(todoDTO.getNome());
        todo.setDescricao(todoDTO.getDescricao());
        todo.setPrioridade(todoDTO.getPrioridade());
        todo.setDataVencimento(todoDTO.getDataVencimento());
        return todo;
    }
}