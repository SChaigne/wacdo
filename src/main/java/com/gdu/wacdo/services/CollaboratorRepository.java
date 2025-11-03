package com.gdu.wacdo.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gdu.wacdo.models.Collaborator;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

	@Query("""
			    SELECT c FROM Collaborator c
			    WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			       OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			       OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	List<Collaborator> findByKeyword(@Param("keyword") String keyword);

	@Query("""
			    SELECT c FROM Collaborator c
			    WHERE c.id NOT IN (
			        SELECT a.collaborator.id FROM Affectation a
			    )
			""")
	List<Collaborator> findCollaboratorsWithoutAffectation();
}
