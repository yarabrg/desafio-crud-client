package com.mooncake.crud_clients.services;

import com.mooncake.crud_clients.dto.ClientDTO;
import com.mooncake.crud_clients.entities.Client;
import com.mooncake.crud_clients.repositories.ClientRepository;
import com.mooncake.crud_clients.services.exceptions.DatabaseException;
import com.mooncake.crud_clients.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    //Busca um cliente por Id
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o ID: " + id));
        return new ClientDTO(entity);
    }

    //Busca de todos os clientes (paginada)
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ClientDTO::new);
    }

    //Insere um novo cliente
    @Transactional()
    public ClientDTO insert(ClientDTO dto) {
        try {
            Client entity = new Client();
            copyDtoToEntity(dto, entity);
            repository.save(entity);
            return new ClientDTO(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Já existe um registro com o CPF informado");
        }
    }

    //Atualiza o registro de um cliente
    @Transactional()
    public ClientDTO update(Long id, ClientDTO dto) {
        try {
            Client entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            repository.save(entity);
            return new ClientDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Cliente não encontrado para o ID: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Já existe um registro com o CPF informado");
        }
    }

    //Deleta um cliente pelo Id
    @Transactional
    public void delete(Long id){
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado para o ID: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha na integridade referencial");
        }
    }

    //Metodo auxliar para copiar os dados de dto para a entity
    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }
}
