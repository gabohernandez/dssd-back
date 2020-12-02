package com.grupo6.dssd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo6.dssd.model.Protocol;

@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, Long> {

	List<Protocol> findByProjectId(Long projectId);
	
	List<Protocol> findByUserId(Long userId);

}
