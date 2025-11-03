package com.gdu.wacdo.controllers;

import java.util.Date;
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

import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.models.RestaurantDto;
import com.gdu.wacdo.services.AffectationRepository;
import com.gdu.wacdo.services.JobRepository;
import com.gdu.wacdo.services.RestaurantsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

	@Autowired
	private RestaurantsRepository restaurantsRepository;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private AffectationRepository affectationsRepository;

	@GetMapping({ "", "/" })
	public String restaurants(@RequestParam(required = false) String name,
			@RequestParam(required = false) String zipCode, @RequestParam(required = false) String city, Model model) {

		List<Restaurant> restaurants = restaurantsRepository.searchRestaurants(name, zipCode, city);

		model.addAttribute("restaurants", restaurants);
		model.addAttribute("name", name);
		model.addAttribute("zipCode", zipCode);
		model.addAttribute("city", city);

		return "restaurants/index";
	}

	@GetMapping("/{id}")
	public String restaurantDetail(@PathVariable Long id, @RequestParam(required = false) Long jobId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, Model model) {

		Restaurant restaurant = restaurantsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Restaurant not found"));

		List<Affectation> affectations = affectationsRepository.findCurrentAffectationsByRestaurant(id, jobId, name,
				startDate);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("affectations", affectations);
		model.addAttribute("jobs", jobRepository.findAll());
		model.addAttribute("jobId", jobId);
		model.addAttribute("name", name);
		model.addAttribute("startDate", startDate);

		return "restaurants/detail";
	}

	@GetMapping("/create")
	public String createRestaurantPage(Model model) {
		model.addAttribute("restaurantDto", new RestaurantDto());
		return "restaurants/create";
	}

	@PostMapping("/create")
	public String createRestaurant(@Valid @ModelAttribute RestaurantDto restaurantDto, BindingResult result) {
		if (result.hasErrors()) {
			return "restaurants/create";
		}

		Restaurant restaurant = new Restaurant();
		restaurant.setAdress(restaurantDto.getAdress());
		restaurant.setCity(restaurantDto.getCity());
		restaurant.setName(restaurantDto.getName());
		restaurant.setZipCode(restaurantDto.getZipCode());

		restaurantsRepository.save(restaurant);

		return "redirect:/restaurants";
	}

	@GetMapping("/update/{id}")
	public String updateRestaurantPage(@PathVariable Long id, Model model) {
		try {
			Restaurant restaurant = restaurantsRepository.findById(id).get();

			RestaurantDto restaurantDto = new RestaurantDto();
			restaurantDto.setAdress(restaurant.getAdress());
			restaurantDto.setCity(restaurant.getCity());
			restaurantDto.setName(restaurant.getName());
			restaurantDto.setZipCode(restaurant.getZipCode());

			model.addAttribute("restaurantDto", restaurantDto);
			return "restaurants/update";

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/restaurants";
		}
	}

	@PostMapping("/update/{id}")
	public String updateRestaurant(Model model, @PathVariable Long id,
			@Valid @ModelAttribute RestaurantDto restaurantDto, BindingResult result) {

		if (result.hasErrors()) {
			model.addAttribute("restaurantDto", restaurantDto);
			return "restaurants/update";
		}

		Restaurant restaurantToSave = restaurantsRepository.findById(id).get();
		restaurantToSave.setAdress(restaurantDto.getAdress());
		restaurantToSave.setCity(restaurantDto.getCity());
		restaurantToSave.setName(restaurantDto.getName());
		restaurantToSave.setZipCode(restaurantDto.getZipCode());

		restaurantsRepository.save(restaurantToSave);

		return "redirect:/restaurants";
	}

	@GetMapping("/delete/{id}")
	public String deleteRestaurant(@PathVariable Long id) {
		restaurantsRepository.deleteById(id);

		return "redirect:/restaurants";
	}

}
