package com.gdu.wacdo.controllers;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.wacdo.dtos.RegisterDto;
import com.gdu.wacdo.models.AppUser;
import com.gdu.wacdo.repositories.AppUserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class AccountController {

	@Autowired
	private AppUserRepository userRepo;

	@GetMapping({ "/", "" })
	public String register(Model model) {
		RegisterDto registerDto = new RegisterDto();
		model.addAttribute(registerDto);
		model.addAttribute("success", false);
		return "account/register";
	}

	@PostMapping({ "/", "" })
	public String addAccount(Model model, @Valid @ModelAttribute RegisterDto registerDto, BindingResult result) {

		if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
			result.addError(
					new FieldError("registerDto", "confirmPassword", "Les mots de passe ne sont pas identiques"));
		}

		AppUser appUser = userRepo.findByEmail(registerDto.getEmail());
		if (appUser != null) {
			result.addError(new FieldError("registerDto", "email", "L'adresse mail est deja utilisee"));
		}

		if (result.hasErrors()) {
			return "account/register";
		}

		try {
			var bCryptEncoder = new BCryptPasswordEncoder();
			ModelMapper modelMapper = new ModelMapper();
			AppUser appUserSave = modelMapper.map(registerDto, AppUser.class);
			appUserSave.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
			appUserSave.setCreatedAt(new Date());
			appUserSave.setRole("collaborator");
			userRepo.save(appUserSave);
			
			model.addAttribute("registerDto", new RegisterDto());
			model.addAttribute("success", true);
		} catch (Exception e) {
			new FieldError("registerDto", "firstName", e.getMessage());
		}
		return "index";
	}
}
