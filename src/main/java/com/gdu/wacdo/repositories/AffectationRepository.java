package com.gdu.wacdo.repositories;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gdu.wacdo.models.Affectation;

public interface AffectationRepository extends JpaRepository<Affectation, Long> {

	@Query("""
			    SELECT a FROM Affectation a
			    WHERE a.collaborator.id = :collaboratorId
			      AND a.endDate IS NULL
			      AND (:jobId IS NULL OR a.job.id = :jobId)
			      AND (:startDate IS NULL OR a.startDate >= :startDate)
			    ORDER BY a.startDate DESC
			""")
	List<Affectation> findCurrentByCollaborator(@Param("collaboratorId") Long collaboratorId,
			@Param("jobId") Long jobId, @Param("startDate") LocalDate startDate);

	@Query("""
			    SELECT a FROM Affectation a
			    WHERE a.collaborator.id = :collaboratorId
			      AND a.endDate IS NOT NULL
			      AND (:jobId IS NULL OR a.job.id = :jobId)
			      AND (:startDate IS NULL OR a.startDate >= :startDate)
			    ORDER BY a.endDate DESC
			""")
	List<Affectation> findPastByCollaborator(@Param("collaboratorId") Long collaboratorId, @Param("jobId") Long jobId,
			@Param("startDate") LocalDate startDate);

	@Query("""
			    SELECT a FROM Affectation a
			    WHERE a.restaurant.id = :restaurantId
			    AND a.endDate IS NULL
			    AND (:jobId IS NULL OR a.job.id = :jobId)
			    AND (:name IS NULL OR LOWER(a.collaborator.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
			         OR LOWER(a.collaborator.lastName) LIKE LOWER(CONCAT('%', :name, '%')))
			    AND (:startDate IS NULL OR a.startDate >= :startDate)
			""")
	List<Affectation> findCurrentAffectationsByRestaurant(@Param("restaurantId") Long restaurantId,
			@Param("jobId") Long jobId, @Param("name") String name, @Param("startDate") Date startDate);

    @Query("""
    SELECT a FROM Affectation a
    WHERE a.collaborator.id = :collabId
      AND (a.endDate IS NULL OR a.endDate >= CURRENT_DATE)
    """)
    List<Affectation> findActiveAffectations(Long collabId);

}
