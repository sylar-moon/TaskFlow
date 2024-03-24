package com.group.dto;

import com.group.entity.TaskEntity;
import com.group.enumeration.StateEnum;

public record TaskDTO(Long id, String name, StateEnum state, Long personId) {

    public TaskDTO(TaskEntity taskEntity) {
        this(taskEntity.getId(), taskEntity.getName(), taskEntity.getState(), taskEntity.getPersonId());
    }
}