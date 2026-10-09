package com.tecylab.client_api.dto;

import java.nio.file.Path;
import java.time.LocalDateTime;

public record ErrorDTO(

        String status,
        String title,
        String details,
        LocalDateTime timestamp,
        String path
){}
