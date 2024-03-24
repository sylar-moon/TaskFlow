package com.group.controller;

import com.group.dto.AddTaskDTO;
import com.group.dto.TaskDTO;
import com.group.enumeration.StateEnum;
import com.group.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {

    TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskDTO> getTaskEntityById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<TaskDTO> addTask(@RequestBody AddTaskDTO taskDTO) {
        log.info("add new task: {} : {}",taskDTO.name(),taskDTO.personId());
        return ResponseEntity.ok(taskService.addTask(taskDTO.name(),taskDTO.personId()));
    }

    @PatchMapping("{id}")
    public ResponseEntity<TaskDTO> changeTaskState(@PathVariable Long id, @RequestParam StateEnum taskState){
        return ResponseEntity.ok(taskService.changeTaskState(id,taskState));
    }

}