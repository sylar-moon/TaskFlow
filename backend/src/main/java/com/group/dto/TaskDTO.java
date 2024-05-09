package com.group.dto;

import com.group.entity.SubTaskEntity;
import com.group.entity.TaskEntity;
import com.group.enumeration.StateEnum;

import java.util.Set;

public record TaskDTO(Long id, String name, StateEnum state, Long personId, Set<SubTaskEntity> subtasks) {

    public TaskDTO(TaskEntity taskEntity) {
        this(taskEntity.getId(), taskEntity.getName(), taskEntity.getState(), taskEntity.getPersonId(),taskEntity.getSubtasks());
    }
}