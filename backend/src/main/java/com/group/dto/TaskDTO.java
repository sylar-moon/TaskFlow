package com.group.dto;

import com.group.enumeration.StateEnum;

public record TaskDTO(Long id, String name, StateEnum state, Long personId) {
}
