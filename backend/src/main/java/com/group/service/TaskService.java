package com.group.service;

import com.group.dto.TaskDTO;
import com.group.entity.TaskEntity;
import com.group.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTO getTaskById (long id){
        Optional<TaskEntity> task = taskRepository.findById(id);
        if(task.isPresent()){
            TaskEntity taskEntity = task.get();
            return  getTaskDTO(taskEntity);
        }
      return null;
    }


    public TaskDTO addTask (String name){
        TaskEntity task = new TaskEntity(name);
        taskRepository.save(task);
        return getTaskDTO(task);
    }

    public TaskDTO getTaskDTO(TaskEntity taskEntity){
        return new TaskDTO(taskEntity.getId(),
                taskEntity.getName(),
                taskEntity.getDateTimeCreate(),
                taskEntity.getState());
    }
}
