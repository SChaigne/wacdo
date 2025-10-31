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

import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.models.JobDto;
import com.gdu.wacdo.services.JobRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/jobs")
public class JobController {
	
	@Autowired
	private JobRepository jobRepository; 
	
	@GetMapping({ "", "/" })
	public String jobs(Model model) {
		List<Job> listOfJobs = jobRepository.findAll();

		model.addAttribute("jobs", listOfJobs);

		return "jobs/index";
	}

	@GetMapping("/{id}")
	public String jobDetail(@PathVariable Long id, Model model) {
		try {
			Job job = jobRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("job non trouvé avec id: " + id));

			model.addAttribute("job", job);
			return "jobs/detail";
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/jobs";
		}
	}
	
	@GetMapping("/create")
	public String createJobPage(Model model) {
		model.addAttribute("jobDto", new JobDto());
		return "jobs/create";
	}
	
	@PostMapping("/create")
	public String createJob(@Valid @ModelAttribute JobDto jobDto, BindingResult result) {
		if (result.hasErrors()) {
			return "jobs/create";
		}

		Job job = new Job();
		job.setName(jobDto.getName());
		job.setUpdatedAt(new Date());

		jobRepository.save(job);

		return "redirect:/jobs";
	}

	@GetMapping("/update/{id}")
	public String updateJobPage(@PathVariable Long id, Model model) {
		try {
			Job job = jobRepository.findById(id).get();

			JobDto jobDto = new JobDto();
			jobDto.setName(job.getName());

			model.addAttribute("jobDto", jobDto);
			return "jobs/update";

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/jobs";
		}
	}
	
	@PostMapping("/update/{id}")
	public String updateJob(Model model, @PathVariable Long id,
			@Valid @ModelAttribute JobDto jobDto, BindingResult result) {

		if (result.hasErrors()) {
			model.addAttribute("restaurantDto", jobDto);
			return "jobs/update";
		}

		Job jobToSave = jobRepository.findById(id).get();
		jobToSave.setName(jobDto.getName());
		jobToSave.setUpdatedAt(new Date());

		jobRepository.save(jobToSave);

		return "redirect:/jobs";
	}

	@GetMapping("/delete/{id}")
	public String deleteJob(@PathVariable Long id) {
		jobRepository.deleteById(id);

		return "redirect:/jobs";
	}
	
}
