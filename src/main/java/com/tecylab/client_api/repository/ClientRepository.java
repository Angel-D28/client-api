package com.tecylab.client_api.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.tecylab.client_api.model.Client;
import com.tecylab.client_api.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findClientByStatus(Status status);
    Optional<Client> findClientByName(String name);
    Optional<Client> findClientByEmail(String email);
}
