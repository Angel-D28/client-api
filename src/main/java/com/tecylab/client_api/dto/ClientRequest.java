package com.tecylab.client_api.dto;

import com.tecylab.client_api.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ClientRequest(

        @NotBlank(message = "Name is required")
        @Schema(description = "The first name of the client", example = "Angel")
        String name,

        @NotBlank(message = "Last name is required")
        @Schema(description = "The last name of the client", example = "Diaz")
        String lastName,

        @NotBlank(message = "Email is required")
        @Schema(description = "The email address of the client", example = "angel@example.com")
        String email,

        @Schema(description = "The status of the client", example = "ACTIVE")
        Status status
) {
}
