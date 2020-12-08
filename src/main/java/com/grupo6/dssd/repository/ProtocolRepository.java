package com.grupo6.dssd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo6.dssd.model.Protocol;

@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, Long> {

	Optional<Protocol> findByRandomUUID(String randomUUID);

}
