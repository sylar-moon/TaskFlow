package com.group.util;

import com.group.enumeration.StateEnum;
import org.springframework.stereotype.Component;

@Component
public class TaskStateValidator {
    public boolean validateTaskState(StateEnum current,StateEnum target){
        switch (current){
            case NEW -> {
                return target==StateEnum.IN_PROGRESS||target==StateEnum.CLOSED;
            }
            case IN_PROGRESS -> {
                return target==StateEnum.COMPLETED||target==StateEnum.CLOSED;
            }
            case COMPLETED -> {
                return target==StateEnum.CLOSED;
            }
            default -> {
                return false;
            }
        }
    }
}
