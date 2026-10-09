package com.tecylab.client_api.controller;

import com.tecylab.client_api.dto.ClientResponse;
import com.tecylab.client_api.model.Client;
import com.tecylab.client_api.dto.ClientRequest;
import com.tecylab.client_api.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Client API", description = "API for managing clients")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Create a new client", description = "Creates a new client and returns the created client object.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/create")
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest client){
        return ResponseEntity.status(HttpStatus.CREATED).
                body(clientService.createClient(client));

    }

    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of clients"),
            @ApiResponse(responseCode = "404", description = "No clients found")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ClientResponse>> getAllClients(){
        return ResponseEntity.ok(clientService.findAllCLients());
    }

    @Operation(summary = "Get client by ID", description = "Retrieves a client by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved client"),
            @ApiResponse(responseCode = "404", description = "Client not found with the given ID")
    })
    @GetMapping("{id}")
    public ResponseEntity<ClientResponse> getClientById(@Positive(message = "Client Id must be a positive number")
            @PathVariable long id){
        return ResponseEntity.ok(clientService.findClientById(id));
    }

    @Operation(summary = "Get client by name", description = "Retrieves a client by their name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved client"),
            @ApiResponse(responseCode = "404", description = "Client not found with the given name")
    })
    @GetMapping("/getClientByName/{name}")
    public ResponseEntity<List<ClientResponse>> getClientByName(@PathVariable String name){
        return ResponseEntity.ok(clientService.findClientByName(name));
    }

    @Operation(summary = "Update client", description = "Updates an existing client with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Client not found with the given ID")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @Valid @RequestBody ClientRequest clientRequest) {
        return ResponseEntity.ok(clientService.updateClient(id, clientRequest));
    }

    @Operation(summary = "Delete client", description = "Deletes a client by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Client not found with the given ID")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
