package com.todoapp.todobackend.repository;

import com.todoapp.todobackend.model.Todo;
import com.todoapp.todobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
}
