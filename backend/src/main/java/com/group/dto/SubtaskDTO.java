package com.group.dto;

import com.group.entity.SubTaskEntity;

public record SubtaskDTO(Long id, Boolean status) {
    public SubtaskDTO(SubTaskEntity subTask) {
        this(subTask.getId(),subTask.isComplete());
    }
}
