package com.group.controller;

import com.group.dto.AddTaskDTO;
import com.group.dto.TaskDTO;
import com.group.entity.PersonEntity;
import com.group.enumeration.StateEnum;
import com.group.exception.TaskNotFoundException;
import com.group.service.PersonService;
import com.group.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {

    TaskService taskService;

    PersonService personService;

    @Autowired
    public TaskController(TaskService taskService,PersonService personService) {
        this.taskService = taskService;
        this.personService = personService;
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskDTO> getTaskEntityById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }


    @GetMapping("/my")
    public ResponseEntity<List<TaskDTO>> getMyTasks() {
        return ResponseEntity.ok(taskService.getMyTasks(personService.getAuthPersonEntity().getId()));
    }


    @PostMapping
    public ResponseEntity<TaskDTO> addTask(@RequestBody AddTaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.addTask(taskDTO.name(),
                personService.getAuthPersonEntity().getId()));
    }

    @PatchMapping("{id}")
    public ResponseEntity<TaskDTO> changeTaskState(@PathVariable Long id, @RequestParam StateEnum taskState){
        return ResponseEntity.ok(taskService.changeTaskState(id,taskState));
    }

}