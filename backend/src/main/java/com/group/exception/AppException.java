package com.group.exception;

import java.time.LocalDateTime;

public record AppException(int status, String message, LocalDateTime timeStamp)  {
    public AppException(int status, String message) {
        this(status, message, LocalDateTime.now());
    }
}

