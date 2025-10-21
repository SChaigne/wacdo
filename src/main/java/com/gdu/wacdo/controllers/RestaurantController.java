package com.gdu.wacdo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.services.RestaurantService;
import com.gdu.wacdo.services.RestaurantsRepository;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {


	
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
}
