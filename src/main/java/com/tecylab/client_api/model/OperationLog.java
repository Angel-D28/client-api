package com.tecylab.client_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "operation_logs")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OperationLog {

    @Id
    private String id;

    private String operation;

    private String description;

    private LocalDateTime timestamp;
}
