package com.gdu.wacdo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.services.RestaurantService;
import com.gdu.wacdo.services.RestaurantsRepository;

=======
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

>>>>>>> restore-commit
@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

<<<<<<< HEAD

	
	@Autowired
	private RestaurantsRepository restaurantsRepository;



	@GetMapping({"","/"})
	public String restaurants(Model model) {
		List<Restaurant> listOfRestaurants = restaurantsRepository.findAll();

		model.addAttribute("restaurants", listOfRestaurants); // restaurants , ici on associe a la variable dans mon template

		return "restaurants/index"; // template thymeleaf
	}
	
//	@GetMapping("{id}")
//	public String restaurantDetail(@PathVariable int id, Model model) {
//	    Restaurant restaurant = restaurantsRepository.findById(id);
//	    model.addAttribute("restaurant", restaurant);
//	    
//		return "restaurants/detail";
//		
//	}
//	
//	@GetMapping("update/{id}")
//	public String restaurantUpdate(@PathVariable int id, Model model) {
//	    Restaurant restaurant = restaurantsRepository.getById(id);
//	    model.addAttribute("restaurant", restaurant);
//
//		return "restaurants/update";
//	}
//	
=======
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
		return loadRestaurantOrNotFound(id, model, "restaurants/detail");
	}

	@GetMapping("/create")
	public String createRestaurantPage(Model model) {
		model.addAttribute("restaurantDto", new RestaurantDto());
		return "restaurants/create";
	}

	@PostMapping("/create")
	public String createRestaurant(@Valid @ModelAttribute("restaurantDto") RestaurantDto restaurantDto,
			BindingResult result) {
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
		return loadRestaurantOrNotFound(id, model, "restaurants/update");
	}
	
	

	private String loadRestaurantOrNotFound(int id, Model model, String viewName) {
		return restaurantsRepository.findById(id).map(restaurant -> {
			model.addAttribute("restaurant", restaurant);
			return viewName;
		}).orElse("notfoundressources");
	}
>>>>>>> restore-commit
}
