package com.mooncake.crud_clients.repositories;

import com.mooncake.crud_clients.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> id(Long id);
}
