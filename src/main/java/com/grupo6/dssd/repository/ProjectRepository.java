package com.grupo6.dssd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.grupo6.dssd.model.Project;

/**
 * @author nahuel.barrena on 20/10/20
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
