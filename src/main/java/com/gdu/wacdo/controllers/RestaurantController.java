package com.gdu.wacdo.controllers;

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

import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.models.RestaurantDto;
import com.gdu.wacdo.services.RestaurantsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

	@Autowired
	private RestaurantsRepository restaurantsRepository;

	@GetMapping({ "", "/" })
	public String restaurants(Model model) {
		List<Restaurant> listOfRestaurants = restaurantsRepository.findAll();

		model.addAttribute("restaurants", listOfRestaurants); // restaurants , ici on associe a la variable dans mon
																// template

		return "restaurants/index"; // template thymeleaf
	}

	@GetMapping("/{id}")
	public String restaurantDetail(@PathVariable int id, Model model) {
		try {
			Restaurant restaurant = restaurantsRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Restaurant non trouvé avec id: " + id));

			model.addAttribute("restaurant", restaurant);
			return "restaurants/detail";
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/restaurants";
		}
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
	public String updateRestaurantPage(@PathVariable int id, Model model) {
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
	public String updateRestaurant(Model model, @PathVariable int id,
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
	public String deleteRestaurant(@PathVariable int id) {
		restaurantsRepository.deleteById(id);

		return "redirect:/restaurants";
	}

}
