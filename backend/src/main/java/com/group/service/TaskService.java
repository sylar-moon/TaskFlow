package com.group.service;

import com.group.dto.TaskDTO;
import com.group.entity.TaskEntity;
import com.group.enumeration.StateEnum;
import com.group.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTO getTaskById (long id){
        Optional<TaskEntity> task = taskRepository.findById(id);
        return task.map(TaskService::getTaskDTO).orElse(null);
    }


    public TaskDTO addTask (String name){
        TaskEntity task = new TaskEntity(name);
        taskRepository.save(task);
        return getTaskDTO(task);
    }

    public static TaskDTO getTaskDTO(TaskEntity taskEntity){
        return new TaskDTO(taskEntity.getId(),
                taskEntity.getName(),
                taskEntity.getState());
    }

    public List<TaskDTO> getAllTasks() {

       return taskRepository.findAll().stream().map(TaskService::getTaskDTO).collect(Collectors.toList());
    }

    public TaskDTO changeTaskState(Long id, StateEnum taskState) {
        TaskEntity entity = taskRepository.findById(id).orElse(null);
        if(entity!=null){
            entity.setState(taskState);
            taskRepository.save(entity);
            return getTaskDTO(entity);
        }
     return null;
    }
}
