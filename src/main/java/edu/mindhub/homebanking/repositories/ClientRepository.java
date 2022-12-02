package edu.mindhub.homebanking.repositories;


import edu.mindhub.homebanking.entities.Client;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository <Client,Long>{

    @Override
    long count();

    @Override
    void delete(Client entity);

    @Override
    List<Client> findAll();

    @Override
    List<Client> findAll(Sort sort);

    @Override
    Optional<Client> findById(Long aLong);


    @Override
    <S extends Client> S save(S entity);

    @Override
    <S extends Client> List<S> saveAll(Iterable<S> entities);

    @Override
    <S extends Client> Optional<S> findOne(Example<S> example);

}
