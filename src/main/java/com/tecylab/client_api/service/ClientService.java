package com.tecylab.client_api.service;

import com.tecylab.client_api.dto.ClientRequest;
import com.tecylab.client_api.dto.ClientResponse;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(ClientRequest clientRequest);

    List<ClientResponse> findAllClients();

    List<ClientResponse> findClientsByStatus();

    ClientResponse findClientById(Long id) throws Exception;

    ClientResponse updateClient(Long id, ClientRequest updatedClient);

    void deleteClientById(Long id);

    ClientResponse findClientByName(String name);

    ClientResponse findClientByEmail(String email);

    ClientResponse disableClient(Long id);
}