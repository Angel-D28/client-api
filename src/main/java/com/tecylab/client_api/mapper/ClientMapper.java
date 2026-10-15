package com.tecylab.client_api.mapper;

import com.tecylab.client_api.dto.ClientRequest;
import com.tecylab.client_api.dto.ClientResponse;
import com.tecylab.client_api.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    Client toEntity(ClientRequest clientRequest);

    ClientResponse toResponse(Client client);

    void updateEntity(ClientRequest clientRequest, @MappingTarget Client client);
}