package com.group.dto;

import jakarta.validation.constraints.Size;

public record AddSubtaskDTO(Long taskId, @Size(min = 3, message = "Your name subtask must be min 3 ")  String subtaskName) {
}
