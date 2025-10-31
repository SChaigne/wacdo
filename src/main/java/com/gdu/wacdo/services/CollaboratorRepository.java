package com.gdu.wacdo.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdu.wacdo.models.Collaborator;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

}
