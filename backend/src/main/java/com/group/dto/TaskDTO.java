package com.group.dto;

import com.group.enumeration.StateEnum;

import java.time.LocalDateTime;

public record TaskDTO(Long id, String name, StateEnum state) {
}
