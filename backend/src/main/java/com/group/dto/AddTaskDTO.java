package com.group.dto;

import jakarta.validation.constraints.Size;

public record AddTaskDTO(@Size (min = 5, message = "Your name task must be min 5 ") String name) {
}
