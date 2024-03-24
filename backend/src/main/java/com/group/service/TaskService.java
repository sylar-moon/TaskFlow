package com.group.service;

import com.group.dto.TaskDTO;
import com.group.entity.TaskEntity;
import com.group.enumeration.StateEnum;
import com.group.exception.TaskNotFoundException;
import com.group.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;


    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public TaskDTO addTask(String name, Long personId) {
        TaskEntity task = new TaskEntity(name, personId);
        taskRepository.save(task);
        return getTaskDTO(task);
    }

    public static TaskDTO getTaskDTO(TaskEntity taskEntity) {
        return new TaskDTO(taskEntity.getId(),
                taskEntity.getName(),
                taskEntity.getState(), taskEntity.getPersonId());
    }

    public List<TaskDTO> getAllTasks() {

        return taskRepository.findAll().stream().map(TaskService::getTaskDTO).collect(Collectors.toList());
    }

    public TaskDTO changeTaskState(Long id, StateEnum taskState)  {
        TaskEntity entity = tryGetTaskEntity(id);
        if (entity != null) {
            entity.setState(taskState);
            taskRepository.save(entity);
            return getTaskDTO(entity);
        }
        return null;
    }


    public List<TaskDTO> getMyTasks(long personId) {
        List<TaskEntity> tasks = taskRepository.findAllByPersonId(personId).
                orElse(new ArrayList<>());
        return tasks.stream().map(TaskService::getTaskDTO).collect(Collectors.toList());
    }

    public TaskDTO getTaskById(long id)  {
        return new TaskDTO(tryGetTaskEntity(id));
    }


    private TaskEntity tryGetTaskEntity(Long id)  {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        String.format("Task with id %d was not found", id)));
    }
}