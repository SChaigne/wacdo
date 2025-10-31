package com.gdu.wacdo.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.wacdo.models.*;
import com.gdu.wacdo.services.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/affectations")
public class AffectationController {

	@Autowired
	private AffectationRepository affectationsRepository;
	@Autowired
	private CollaboratorRepository collaboratorRepository;
	@Autowired
	private RestaurantsRepository restaurantsRepository;
	@Autowired
	private JobRepository jobRepository;

	@GetMapping({ "", "/" })
	public String affectations(Model model) {
		List<Affectation> listOfAffectations = affectationsRepository.findAll();

		model.addAttribute("affectations", listOfAffectations);

		return "affectations/index";
	}
	
	@GetMapping("/{id}")
	public String affectationDetail(@PathVariable Long id, Model model) {
		try {
			Affectation affectation = affectationsRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("affectation non trouvé avec id: " + id));

			model.addAttribute("affectation", affectation);
			return "affectations/detail";
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/affectations";
		}
	}

	@GetMapping("/create")
	public String createAffectationPage(Model model) {
		model.addAttribute("affectationDto", new AffectationDto());
	    model.addAttribute("collaborators", collaboratorRepository.findAll());
	    model.addAttribute("restaurants", restaurantsRepository.findAll());
	    model.addAttribute("jobs", jobRepository.findAll());
		return "affectations/create";
	}

	@PostMapping("/create")
	public String createAffectation(@Valid @ModelAttribute AffectationDto affectationDto, BindingResult result) {
		if (result.hasErrors()) {
			return "affectations/create";
		}
		Collaborator collaborator = collaboratorRepository.findById(affectationDto.getCollaboratorId())
				.orElseThrow(() -> new RuntimeException("Collaborator not found"));
		Restaurant restaurant = restaurantsRepository.findById(affectationDto.getRestaurantId())
				.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
		Job job = jobRepository.findById(affectationDto.getJobId())
				.orElseThrow(() -> new IllegalArgumentException("Job not found"));

		Affectation affectation = new Affectation();
		affectation.setCollaborator(collaborator);
		affectation.setRestaurant(restaurant);
		affectation.setJob(job);
		affectation.setStartDate(new Date());

		affectationsRepository.save(affectation);

		return "redirect:/affectations";
	}
	
	@GetMapping("/end/{id}")
	public String endAffectation(@PathVariable Long id) {
		Affectation affect = affectationsRepository.findById(id).get();
		affect.setEndDate(new Date());
		affectationsRepository.save(affect);

		return "redirect:/affectations";
	}
}
