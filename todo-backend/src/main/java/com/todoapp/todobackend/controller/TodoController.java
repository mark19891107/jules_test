package com.todoapp.todobackend.controller;

import com.todoapp.todobackend.model.Todo;
import com.todoapp.todobackend.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(@AuthenticationPrincipal UserDetails userDetails) {
        List<Todo> todos = todoService.getTodosByUser(userDetails.getUsername());
        return ResponseEntity.ok(todos);
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo, @AuthenticationPrincipal UserDetails userDetails) {
        Todo createdTodo = todoService.createTodo(todo, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Todo updatedTodo = todoService.updateTodo(id, todoDetails, userDetails.getUsername());
            return ResponseEntity.ok(updatedTodo);
        } catch (RuntimeException e) { // Or a more specific "NotFoundException"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or an error object
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        boolean deleted = todoService.deleteTodo(id, userDetails.getUsername());
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
