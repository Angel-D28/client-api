package com.tecylab.client_api.service.impl;


import com.tecylab.client_api.annotation.TecyLabLog;
import com.tecylab.client_api.dto.ClientRequest;
import com.tecylab.client_api.dto.ClientResponse;
import com.tecylab.client_api.exception.ClientNotFoundException;
import com.tecylab.client_api.mapper.ClientMapper;
import com.tecylab.client_api.model.Client;
import com.tecylab.client_api.model.Status;
import com.tecylab.client_api.repository.ClientRepository;
import com.tecylab.client_api.service.ClientService;
import com.tecylab.client_api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    // Inject the ClientRepository using constructor injection
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final NotificationService notificationService;

    @Override
    @CacheEvict(value = "clients", key = "'allClients'")
    @TecyLabLog(type = TecyLabLog.OperationType.CREATE, description = "Client created: #id")
    public ClientResponse createClient(ClientRequest clientRequest) {
        log.info("LOG: Client to create: " + clientRequest.name());
        Client savedClient = clientRepository.save(clientMapper.toEntity(clientRequest));

        notificationService.sendEmail(savedClient.getEmail(), "Welcome to our service!")
                .exceptionally(ex -> {
                    log.error("No se pudo enviar el correo a {}", savedClient.getEmail(), ex);
                    return null;
                });
        log.info("LOG: Client created: " + savedClient.getId());
        return clientMapper.toResponse(savedClient);
    }

    @Override
    @Cacheable(value = "clients", key = "'allClients'")
    public List<ClientResponse> findAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    @Override
    public List<ClientResponse> findClientsByStatus() {
        return clientRepository.findClientByStatus(Status.ACTIVE)
                .stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "client", key = "#id")
    public ClientResponse findClientById(Long id) throws Exception {
        return clientRepository.findById(id)
                .map(clientMapper::toResponse)
                .orElseThrow(() -> new Exception("Client not found with id: " + id));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "client", key = "#id"),
            @CacheEvict(value = "clients", key = "'allClients'")
    })
    public ClientResponse updateClient(Long id, ClientRequest updatedClient) {
        Client existingClient = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
        clientMapper.updateEntity(updatedClient, existingClient);

        Client savedClient = clientRepository.save(existingClient);
        return clientMapper.toResponse(savedClient);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "client", key = "#id"),
            @CacheEvict(value = "clients", key = "'allClients'")
    })
    public void deleteClientById(Long id) {
        Client existingClient = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
        clientRepository.delete(existingClient);
    }

    @Override
    public ClientResponse findClientByName(String name) {
        return clientRepository.findClientByName(name)
                .map(clientMapper::toResponse)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with name: " + name));
    }

    @Override
    public ClientResponse findClientByEmail(String email) {
        return clientRepository.findClientByEmail(email)
                .map(clientMapper::toResponse)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with email: " + email));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "client", key = "#id"),
            @CacheEvict(value = "clients", key = "'allClients'")
    })
    public ClientResponse disableClient(Long id) {
        Client existingClient = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
        existingClient.setStatus(Status.INACTIVE);
        Client savedClient = clientRepository.save(existingClient);
        return clientMapper.toResponse(savedClient);
    }

}