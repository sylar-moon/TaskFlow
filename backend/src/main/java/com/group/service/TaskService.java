package com.group.service;

import com.group.dto.AddSubtaskDTO;
import com.group.dto.SendMessageDTO;
import com.group.dto.TaskDTO;
import com.group.entity.SubTaskEntity;
import com.group.entity.TaskEntity;
import com.group.enumeration.StateEnum;
import com.group.exception.TaskNotFoundException;
import com.group.exception.TaskValidateException;
import com.group.repository.SubTaskRepository;
import com.group.repository.TaskRepository;
import com.group.util.TaskStateValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    private final SubTaskRepository subTaskRepository;

    private final TaskStateValidator stateValidator;

    private final EmailSenderService senderService;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskStateValidator stateValidator,
                       SubTaskRepository subTaskRepository, EmailSenderService senderService) {
        this.taskRepository = taskRepository;
        this.stateValidator = stateValidator;
        this.subTaskRepository = subTaskRepository;
        this.senderService = senderService;
    }

    public TaskDTO saveTask (TaskEntity task){
       return getTaskDTO(taskRepository.save(task));
    }

    public TaskDTO addTask(String name, Long personId) {
        return saveTask(new TaskEntity(name, personId));
    }

    public static TaskDTO getTaskDTO(TaskEntity taskEntity) {
        return new TaskDTO(taskEntity.getId(),
                taskEntity.getName(),
                taskEntity.getState(), taskEntity.getPersonId(), taskEntity.getSubtasks());
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(TaskService::getTaskDTO).collect(Collectors.toList());
    }

    public TaskDTO changeTaskState(Long id, StateEnum targetState) {
        TaskEntity entity = tryGetTaskEntity(id);
        StateEnum currentState = entity.getState();
        if (stateValidator.validateTaskState(currentState, targetState)) {
            entity.setState(targetState);
            return saveTask(entity);
        } else {
            throw new TaskValidateException(String.format("Cannot change task status from %s to %s", currentState, targetState));
        }
    }


    public List<TaskDTO> getMyTasks(long personId) {
        log.info("your person id: {}",personId);
        List<TaskEntity> tasks = taskRepository.findAllByPersonId(personId).
                orElse(new ArrayList<>());
        tasks.forEach(s-> log.info("Your task: {}",s));
        return tasks.stream().map(TaskService::getTaskDTO).collect(Collectors.toList());
    }

    public TaskDTO getTaskById(long id) {
        return new TaskDTO(tryGetTaskEntity(id));
    }


    public TaskEntity tryGetTaskEntity(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        String.format("Task with id %d was not found", id)));
    }

}