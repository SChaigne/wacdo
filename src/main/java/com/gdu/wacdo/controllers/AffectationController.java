package com.gdu.wacdo.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.wacdo.dtos.AffectationDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.repositories.CollaboratorRepository;
import com.gdu.wacdo.repositories.JobRepository;
import com.gdu.wacdo.repositories.RestaurantsRepository;
import com.gdu.wacdo.services.AffectationService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/affectations")
public class AffectationController {

	@Autowired
	private AffectationService affectationService;
	@Autowired
	private CollaboratorRepository collaboratorRepository;
	@Autowired
	private RestaurantsRepository restaurantsRepository;
	@Autowired
	private JobRepository jobRepository;

	@GetMapping({ "", "/" })
	public String affectations(@RequestParam(required = false) Long jobId, @RequestParam(required = false) String city,
			@RequestParam(required = false) String restaurantName,
			@RequestParam(required = false) String collaboratorName,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			Model model) {

		List<Affectation> affectations = affectationService.getFilteredAffectations(jobId, city, restaurantName,
				collaboratorName, startDate, endDate);

		model.addAttribute("affectations", affectations);
		model.addAttribute("jobs", jobRepository.findAll());
		model.addAttribute("selectedJobId", jobId);
		model.addAttribute("selectedCity", city);
		model.addAttribute("selectedRestaurantName", restaurantName);
		model.addAttribute("selectedCollaborator", collaboratorName);
		model.addAttribute("selectedStartDate", startDate);
		model.addAttribute("selectedEndDate", endDate);

		return "affectations/index";
	}

	@GetMapping("/{id}")
	public String affectationDetail(@PathVariable Long id, Model model) {
		try {
			Affectation affectation = affectationService.getAffectationById(id);
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

		affectationService.createAffectation(affectationDto);

		return "redirect:/affectations";
	}

	@GetMapping("update/{id}")
	public String updateAffectationPage(Model model, @PathVariable Long id) {
		AffectationDto affectDto = affectationService.getAffectationDtoForUpdate(id);

		model.addAttribute("affectationDto", affectDto);
		model.addAttribute("collaborators", affectationService.getAllCollaborators());
		model.addAttribute("restaurants", affectationService.getAllRestaurants());
		model.addAttribute("jobs", affectationService.getAllJobs());

		return "affectations/update";
	}

	@PostMapping("update/{id}")
	public String updateAffectation(Model model, @PathVariable Long id,
			@Valid @ModelAttribute AffectationDto affectationDto, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("affectationDto", affectationDto);
			return "affectations/update";
		}

		affectationService.updateAffectation(id, affectationDto);

		return "redirect:/affectations";
	}

	@GetMapping("/end/{id}")
	public String endAffectation(@PathVariable Long id) {
		affectationService.endAffectation(id);
		return "redirect:/affectations";
	}
}
