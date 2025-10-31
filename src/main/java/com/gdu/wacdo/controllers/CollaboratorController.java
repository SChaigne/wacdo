package com.gdu.wacdo.controllers;

import java.time.LocalDate;
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

import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.models.CollaboratorDto;
import com.gdu.wacdo.services.CollaboratorRepository;

import jakarta.validation.Valid;

@Controller()
@RequestMapping("/collaborators")
public class CollaboratorController {

	@Autowired()
	CollaboratorRepository collaboratorRepository;

	@GetMapping({ "", "/" })
	public String collaborator(Model model) {
		List<Collaborator> collaborators = collaboratorRepository.findAll();
		model.addAttribute("collaborators", collaborators);
		return "collaborators/index";
	}

	@GetMapping("/{id}")
	public String collaboratorById(Model model, @PathVariable Long id) {
		Collaborator collaborator = collaboratorRepository.findById(id).get();
		model.addAttribute("collaborator", collaborator);
		return "collaborators/detail";
	}

	@GetMapping("update/{id}")
	public String updateCollaboratorPage(Model model, @PathVariable Long id) {
		Collaborator collaborator = collaboratorRepository.findById(id).get();

		CollaboratorDto collabDto = new CollaboratorDto();
		collabDto.setEmail(collaborator.getEmail());
		collabDto.setFirstName(collaborator.getFirstName());
		collabDto.setLastName(collaborator.getLastName());

		model.addAttribute("collaboratorDto", collabDto);
		return "collaborators/update";
	}

	@PostMapping("update/{id}")
	public String updateCollaborator(Model model, @PathVariable Long id,
			@Valid @ModelAttribute CollaboratorDto collaboratorDto, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("restaurantDto", collaboratorDto);
			return "collaborators/update";
		}

		Collaborator collabToSave = collaboratorRepository.findById(id).get();
		collabToSave.setEmail(collaboratorDto.getEmail());
		collabToSave.setFirstName(collaboratorDto.getFirstName());
		collabToSave.setLastName(collaboratorDto.getLastName());

		collaboratorRepository.save(collabToSave);

		return "redirect:/collaborators";
	}

	@GetMapping("/create")
	public String createCollaboratorPage(Model model) {
		model.addAttribute("collaboratorDto", new Collaborator());
		return "collaborators/create";
	}

	@PostMapping("/create")
	public String createCollaborator(@Valid @ModelAttribute CollaboratorDto collaboratorDto, BindingResult result) {
		if (result.hasErrors()) {
			return "collaborators/create";
		}

		Collaborator collabToSave = new Collaborator();
		collabToSave.setEmail(collaboratorDto.getEmail());
		collabToSave.setFirstName(collaboratorDto.getFirstName());
		collabToSave.setLastName(collaboratorDto.getLastName());
		collabToSave.setHireDate(LocalDate.now());

		collaboratorRepository.save(collabToSave);

		return "redirect:/collaborators";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCollaborator(@PathVariable Long id) {
		collaboratorRepository.deleteById(id);

		return "redirect:/collaborators";
	}
}
