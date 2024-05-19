package com.group.service;

import com.group.dto.AddSubtaskDTO;
import com.group.dto.SubtaskDTO;
import com.group.dto.TaskDTO;
import com.group.entity.SubTaskEntity;
import com.group.entity.TaskEntity;
import com.group.exception.SubtaskNotFoundException;
import com.group.repository.SubTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class SubtaskService {
    private final TaskService taskService;

    private final SubTaskRepository subTaskRepository;


    @Autowired
    public SubtaskService(TaskService taskService,
                          SubTaskRepository subTaskRepository) {
        this.taskService = taskService;
        this.subTaskRepository = subTaskRepository;
    }


    public TaskDTO addSubtask(AddSubtaskDTO subtaskDTO) {
        TaskEntity task = taskService.tryGetTaskEntity(subtaskDTO.taskId());
        task.addSubtask(subTaskRepository.save(new SubTaskEntity(subtaskDTO.subtaskName())));
        return taskService.saveTask(task);
    }


    public SubtaskDTO changeStatus(Long id) {
        SubTaskEntity subTask = tryGetSubtaskEntity(id);
        subTask.changeStatus();
        return new SubtaskDTO(subTaskRepository.save(subTask));
    }

    public SubTaskEntity tryGetSubtaskEntity(Long id) {
        return subTaskRepository.findById(id)
                .orElseThrow(() -> new SubtaskNotFoundException(
                        String.format("Subtask with id %d was not found", id)));
    }

    public Set<SubTaskEntity> getSubtasksForTaskId(Long id) {
        TaskEntity task = taskService.tryGetTaskEntity(id);
        return task.getSubtasks();
    }
}
