package com.grupo6.dssd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.model.User;

@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, Long> {

	List<Protocol> findByProjectId(Long projectId);
	
	List<Protocol> findByUserId(Long userId);

	@Query("SELECT p.user, count(p) as most FROM Protocol p GROUP BY p.user.id order by most desc")
	List<User> busiestUser();
}
