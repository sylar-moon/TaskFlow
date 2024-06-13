package com.group.service;

import com.group.dto.TaskDTO;
import com.group.entity.TaskEntity;
import com.group.enumeration.SortedEnum;
import com.group.enumeration.StateEnum;
import com.group.exception.TaskNotFoundException;
import com.group.exception.TaskValidateException;
import com.group.repository.SubTaskRepository;
import com.group.repository.TaskRepository;
import com.group.util.TaskStateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    private final SubTaskRepository subTaskRepository;

    private final TaskStateValidator stateValidator;


    @Autowired
    public TaskService(TaskRepository taskRepository, TaskStateValidator stateValidator,
                       SubTaskRepository subTaskRepository) {
        this.taskRepository = taskRepository;
        this.stateValidator = stateValidator;
        this.subTaskRepository = subTaskRepository;
    }

    public TaskDTO saveTask(TaskEntity task) {
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


    public List<TaskDTO> getMyTasks(long personId, SortedEnum sort) {
        return taskRepository.findAllByPersonId(personId).
                orElse(new ArrayList<>()).stream()
                .sorted(getComparator(sort))
                .map(TaskService::getTaskDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(long id) {
        return new TaskDTO(tryGetTaskEntity(id));
    }


    public TaskEntity tryGetTaskEntity(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        String.format("Task with id %d was not found", id)));
    }

    public TaskDTO changeTaskName(Long id, String newName) {
        TaskEntity entity = tryGetTaskEntity(id);
        log.info("get task complete");
        entity.setName(newName);
        log.info("set task complete");

        return saveTask(entity);
    }

    public List<TaskDTO> getMyClosedTasks(long id, SortedEnum sort) {

        return taskRepository.findAllByPersonId(id).
                orElse(new ArrayList<>())
                .stream()
                .filter(task -> task.getState() == StateEnum.CLOSED)
                .sorted(getComparator(sort))
                .map(TaskService::getTaskDTO)
                .collect(Collectors.toList());
    }

    private Comparator<TaskEntity> getComparator(SortedEnum sort) {
        switch (sort) {
            case NAME_UP:
                return Comparator.comparing(TaskEntity::getName);
            case NAME_DOWN:
                return Comparator.comparing(TaskEntity::getName).reversed();
            case DATE_UP:
                return Comparator.comparing(TaskEntity::getDateTimeCreate); // Предполагается, что у TaskEntity есть метод getDate
            case DATE_DOWN:
                return Comparator.comparing(TaskEntity::getDateTimeCreate).reversed(); // Предполагается, что у TaskEntity есть метод getDate
            case STATE_UP:
                return Comparator.comparing(TaskEntity::getState);
            case STATE_DOWN:
                return Comparator.comparing(TaskEntity::getState).reversed();
            default:
                throw new IllegalArgumentException("Invalid sorting option");
        }
    }
}