package com.gdu.wacdo.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.wacdo.dtos.AffectationDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.repositories.AffectationRepository;
import com.gdu.wacdo.repositories.CollaboratorRepository;
import com.gdu.wacdo.repositories.JobRepository;
import com.gdu.wacdo.repositories.RestaurantsRepository;

@Service
public class AffectationService {
	@Autowired
	private AffectationRepository affectationsRepository;

	@Autowired
	private CollaboratorRepository collaboratorRepository;
	@Autowired
	private RestaurantsRepository restaurantsRepository;
	@Autowired
	private JobRepository jobRepository;

	public List<Affectation> getFilteredAffectations(Long jobId, String city, String restaurantName,
			String collaboratorName, LocalDate startDate, LocalDate endDate) {

		List<Affectation> affectations = affectationsRepository.findAll();

		if (restaurantName != null && !restaurantName.isEmpty()) {
			String lowerRestaurant = restaurantName.toLowerCase();
			affectations.removeIf(a -> a.getRestaurant() == null || a.getRestaurant().getName() == null
					|| !a.getRestaurant().getName().toLowerCase().contains(lowerRestaurant));
		}

		if (jobId != null) {
			affectations.removeIf(a -> a.getJob() == null || !a.getJob().getId().equals(jobId));
		}

		if (city != null && !city.isEmpty()) {
			affectations.removeIf(a -> a.getRestaurant() == null || a.getRestaurant().getCity() == null
					|| !a.getRestaurant().getCity().toLowerCase().contains(city.toLowerCase()));
		}

		if (collaboratorName != null && !collaboratorName.isEmpty()) {
			String lowerName = collaboratorName.toLowerCase();
			affectations.removeIf(a -> a.getCollaborator() == null
					|| !(a.getCollaborator().getFirstName().toLowerCase().contains(lowerName)
							|| a.getCollaborator().getLastName().toLowerCase().contains(lowerName)));
		}

		if (startDate != null) {
			Date start = java.sql.Date.valueOf(startDate);
			affectations.removeIf(a -> a.getStartDate() != null && a.getStartDate().before(start));
		}

		if (endDate != null) {
			Date end = java.sql.Date.valueOf(endDate);
			affectations.removeIf(a -> a.getEndDate() != null && a.getEndDate().after(end));
		}

		return affectations;
	}

	public Affectation getAffectationById(Long id) {
		return affectationsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Affectation non trouvée avec id: " + id));
	}

	public Affectation createAffectation(AffectationDto dto) {

        List<Affectation> active = affectationsRepository.findActiveAffectations(dto.getCollaboratorId());

        if (!active.isEmpty()) {
            throw new IllegalStateException("Ce collaborateur a déjà une affectation active.");
        }

		ModelMapper modMap = new ModelMapper();
		Affectation affectation = modMap.map(dto, Affectation.class);

		Collaborator collaborator = collaboratorRepository.findById(dto.getCollaboratorId())
				.orElseThrow(() -> new RuntimeException("Collaborator not found"));
		Restaurant restaurant = restaurantsRepository.findById(dto.getRestaurantId())
				.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
		Job job = jobRepository.findById(dto.getJobId())
				.orElseThrow(() -> new IllegalArgumentException("Job not found"));

		affectation.setCollaborator(collaborator);
		affectation.setRestaurant(restaurant);
		affectation.setJob(job);
		affectation.setStartDate(new Date());
		try {
			return affectationsRepository.save(affectation);
		} catch (Exception e) {
			throw new RuntimeException("Erreur dans la sauvegarde ");
		}
	}

	public AffectationDto getAffectationDtoForUpdate(Long id) {
		Affectation affectation = affectationsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Affectation non trouvée avec id: " + id));

		AffectationDto dto = new AffectationDto();
		dto.setCollaboratorId(affectation.getCollaborator().getId());
		dto.setRestaurantId(affectation.getRestaurant().getId());
		dto.setJobId(affectation.getJob().getId());
		dto.setUpdatedAt(new Date());

		return dto;
	}

	public Affectation updateAffectation(Long id, AffectationDto dto) {
		Affectation affectToSave = affectationsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Affectation non trouvée avec id: " + id));

		Collaborator collaborator = collaboratorRepository.findById(dto.getCollaboratorId())
				.orElseThrow(() -> new RuntimeException("Collaborator not found"));

		Restaurant restaurant = restaurantsRepository.findById(dto.getRestaurantId())
				.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		Job job = jobRepository.findById(dto.getJobId())
				.orElseThrow(() -> new IllegalArgumentException("Job not found"));

		affectToSave.setCollaborator(collaborator);
		affectToSave.setRestaurant(restaurant);
		affectToSave.setJob(job);
		affectToSave.setUpdatedAt(new Date());

		try {
			return affectationsRepository.save(affectToSave);
		} catch (Exception e) {
			throw new RuntimeException("Erreur dans la sauvegarde ");
		}
	}

	public Affectation endAffectation(Long id) {
		Affectation affect = affectationsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Affectation non trouvée avec id: " + id));

		affect.setEndDate(new Date());

		return affectationsRepository.save(affect);
	}

	public List<Collaborator> getAllCollaborators() {
		return collaboratorRepository.findAll();
	}

	public List<Restaurant> getAllRestaurants() {
		return restaurantsRepository.findAll();
	}

	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}
}
