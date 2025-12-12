package com.gdu.wacdo.services;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.wacdo.dtos.CollaboratorDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.repositories.AffectationRepository;
import com.gdu.wacdo.repositories.CollaboratorRepository;
import com.gdu.wacdo.repositories.JobRepository;

@Service
public class CollaboratorService {

	@Autowired
	private CollaboratorRepository collaboratorRepository;

	@Autowired
	private AffectationRepository affectationRepository;

	@Autowired
	private JobRepository jobRepository;

	public List<Collaborator> getCollaborators(String keyword, Boolean unassigned) {
		if (Boolean.TRUE.equals(unassigned)) {
			return collaboratorRepository.findCollaboratorsWithoutAffectation();
		} else if (keyword != null && !keyword.trim().isEmpty()) {
			return collaboratorRepository.findByKeyword(keyword);
		} else {
			return collaboratorRepository.findAll();
		}
	}

	public Collaborator getCollaboratorById(Long id) {
		return collaboratorRepository.findById(id).orElseThrow();
	}

	public List<Affectation> getCurrentAffectations(Long id, Long jobId, LocalDate startDate) {
		return affectationRepository.findCurrentByCollaborator(id, jobId, startDate);
	}

	public List<Affectation> getPastAffectations(Long id, Long jobId, LocalDate startDate) {
		return affectationRepository.findPastByCollaborator(id, jobId, startDate);
	}

	public Collaborator createCollaborator(CollaboratorDto dto) {
		ModelMapper modelMapper = new ModelMapper();
		Collaborator c = modelMapper.map(dto, Collaborator.class);
        c.setHireDate(LocalDate.now());
		try {
			return collaboratorRepository.save(c);
		} catch (Exception e) {
			throw new RuntimeException("Erreur dans la sauvegarde ");
		}
	}

	public Collaborator updateCollaborator(Long id, CollaboratorDto dto) {
		Collaborator c = collaboratorRepository.findById(id).orElseThrow();
		c.setEmail(dto.getEmail());
		c.setFirstName(dto.getFirstName());
		c.setLastName(dto.getLastName());
		try {
			return collaboratorRepository.save(c);
		} catch (Exception e) {
			throw new RuntimeException("Erreur dans la sauvegarde ");
		}
	}

	public void deleteCollaborator(Long id) {
		collaboratorRepository.deleteById(id);
	}

	public List<?> getAllJobs() {
		return jobRepository.findAll();
	}
}
