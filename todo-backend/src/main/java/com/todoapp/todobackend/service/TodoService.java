package com.todoapp.todobackend.service;

import com.todoapp.todobackend.model.Todo;
import com.todoapp.todobackend.model.User;
import com.todoapp.todobackend.repository.TodoRepository;
import com.todoapp.todobackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository; // To fetch User for Todo operations

    public List<Todo> getTodosByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username)); // Or custom exception
        return todoRepository.findByUser(user);
    }

    public Todo createTodo(Todo todo, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public Optional<Todo> getTodoByIdAndUser(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return todoRepository.findById(id).filter(todo -> todo.getUser().equals(user));
    }
    
    public Todo updateTodo(Long id, Todo todoDetails, String username) {
        Todo todo = getTodoByIdAndUser(id, username)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id + " for user: " + username));
        
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setCompleted(todoDetails.isCompleted());
        // The user of the todo should not change during an update by this method
        return todoRepository.save(todo);
    }

    public boolean deleteTodo(Long id, String username) {
        Optional<Todo> todoOptional = getTodoByIdAndUser(id, username);
        if (todoOptional.isPresent()) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
