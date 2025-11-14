package com.gdu.wacdo.controllers;

import java.time.LocalDate;

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

import com.gdu.wacdo.dtos.CollaboratorDto;
import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.services.CollaboratorService;

import jakarta.validation.Valid;
@Controller
@RequestMapping("/collaborators")
public class CollaboratorController {

    @Autowired
    private CollaboratorService collaboratorService;

    @GetMapping({ "", "/" })
    public String listCollaborators(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean unassigned,
            Model model) {

        model.addAttribute("collaborators", collaboratorService.getCollaborators(keyword, unassigned));
        model.addAttribute("keyword", keyword);
        return "collaborators/index";
    }

    @GetMapping("/{id}")
    public String collaboratorById(
            @PathVariable Long id,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            Model model) {

        Collaborator collaborator = collaboratorService.getCollaboratorById(id);

        model.addAttribute("collaborator", collaborator);
        model.addAttribute("currentAffectations", collaboratorService.getCurrentAffectations(id, jobId, startDate));
        model.addAttribute("pastAffectations", collaboratorService.getPastAffectations(id, jobId, startDate));
        model.addAttribute("jobs", collaboratorService.getAllJobs());
        model.addAttribute("selectedJobId", jobId);
        model.addAttribute("selectedStartDate", startDate);

        return "collaborators/detail";
    }

    @GetMapping("update/{id}")
    public String updateCollaboratorPage(Model model, @PathVariable Long id) {
        Collaborator collaborator = collaboratorService.getCollaboratorById(id);

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
            model.addAttribute("collaboratorDto", collaboratorDto);
            return "collaborators/update";
        }

        collaboratorService.updateCollaborator(id, collaboratorDto);
        return "redirect:/collaborators";
    }

    @GetMapping("/create")
    public String createCollaboratorPage(Model model) {
        model.addAttribute("collaboratorDto", new CollaboratorDto());
        return "collaborators/create";
    }

    @PostMapping("/create")
    public String createCollaborator(@Valid @ModelAttribute CollaboratorDto collaboratorDto, BindingResult result) {
        if (result.hasErrors()) {
            return "collaborators/create";
        }

        collaboratorService.createCollaborator(collaboratorDto);
        return "redirect:/collaborators";
    }

    @GetMapping("/delete/{id}")
    public String deleteCollaborator(@PathVariable Long id) {
        collaboratorService.deleteCollaborator(id);
        return "redirect:/collaborators";
    }
}
