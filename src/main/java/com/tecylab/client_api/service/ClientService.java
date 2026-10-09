package com.tecylab.client_api.service;
import com.tecylab.client_api.annotation.TecyLabLog;
import com.tecylab.client_api.dto.ClientRequest;
import com.tecylab.client_api.dto.ClientResponse;
import com.tecylab.client_api.exception.ClientNotFoundException;
import com.tecylab.client_api.model.Client;
import com.tecylab.client_api.model.OperationLog;
import com.tecylab.client_api.repository.ClientRepository;
import com.tecylab.client_api.repository.OperationLogRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final OperationLogRepository operationLogRepository;

    public ClientService(ClientRepository clientRepository, OperationLogRepository operationLogRepository) {
        this.clientRepository = clientRepository;
        this.operationLogRepository = operationLogRepository;
    }

    @CacheEvict(value = "clients", key = "'all clients'")
    @TecyLabLog(type = TecyLabLog.OperationType.CREATE, description = "Client created: #id")
    public ClientResponse createClient(ClientRequest clientRequest){
        Client client = Client.builder().name(clientRequest.name())
                .lastName(clientRequest.lastName())
                .email(clientRequest.email())
                .status(clientRequest.status())
                .build();

        Client savedClient = clientRepository.save(client);

        return new ClientResponse(savedClient.getId(),
                savedClient.getName(),
                savedClient.getLastName(),
                savedClient.getEmail(),
                savedClient.getStatus());
    }

    @Cacheable(value = "clients", key = "'all clients'")
    public List<ClientResponse> findAllCLients(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientResponse(
                        client.getId(),
                        client.getName(),
                        client.getLastName(),
                        client.getEmail(),
                        client.getStatus()
                )).collect(Collectors.toList());
    }

    @Cacheable(value = "clients", key = "#id")
    public ClientResponse findClientById(Long id){
        return clientRepository.findById(id)
                        .map(cliente -> new ClientResponse(
                                cliente.getId(),
                                cliente.getName(),
                                cliente.getLastName(),
                                cliente.getEmail(),
                                cliente.getStatus()
                        ))
                        .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
    }

    public List<ClientResponse> findClientByName(String name){
        return clientRepository.findByName(name).stream().map(client -> new ClientResponse(
                client.getId(),
                client.getName(),
                client.getLastName(),
                client.getEmail(),
                client.getStatus()
        )).toList();
    }

    public ClientResponse updateClient(Long id, ClientRequest updatedClient){
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));

        existingClient.setName(updatedClient.name());
        existingClient.setLastName(updatedClient.lastName());
        existingClient.setEmail(updatedClient.email());
        existingClient.setStatus(updatedClient.status());

        Client savedClient = clientRepository.save(existingClient);

        return new ClientResponse(
                savedClient.getId(),
                savedClient.getName(),
                savedClient.getLastName(),
                savedClient.getEmail(),
                savedClient.getStatus()
        );
    }

    @CacheEvict(value = "clients", allEntries = true )
    public void deleteClient(Long id){
        Client existingCLient = clientRepository.findById(id).
                orElseThrow(() -> new ClientNotFoundException("Client not found with id:" + id));
        clientRepository.delete(existingCLient);
    }
}
