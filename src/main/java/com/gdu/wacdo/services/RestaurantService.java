package com.gdu.wacdo.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.wacdo.dtos.RestaurantDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.repositories.AffectationRepository;
import com.gdu.wacdo.repositories.JobRepository;
import com.gdu.wacdo.repositories.RestaurantsRepository;

@Service
public class RestaurantService {
	@Autowired
	private RestaurantsRepository restaurantsRepository;

	@Autowired
	private AffectationRepository affectationsRepository;

	@Autowired
	private JobRepository jobRepository;

	public List<Restaurant> searchRestaurants(String name, String zipCode, String city) {
		return restaurantsRepository.searchRestaurants(name, zipCode, city);
	}

	public Restaurant getRestaurantById(Long id) {
		return restaurantsRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant not found"));
	}

	public List<Affectation> getAffectations(Long restaurantId, Long jobId, String name, Date startDate) {
		return affectationsRepository.findCurrentAffectationsByRestaurant(restaurantId, jobId, name, startDate);
	}

	public Restaurant createRestaurant(RestaurantDto restaurantDto) {
		Restaurant restaurant = new Restaurant();
		restaurant.setAdress(restaurantDto.getAdress());
		restaurant.setCity(restaurantDto.getCity());
		restaurant.setName(restaurantDto.getName());
		restaurant.setZipCode(restaurantDto.getZipCode());
		try {
			return restaurantsRepository.save(restaurant);
		} catch (Exception e) {
			throw new RuntimeException("Erreur dans la sauvegarde ");
		}
	}

	public Restaurant updateRestaurant(Long id, RestaurantDto restaurantDto) {
		Restaurant restaurant = restaurantsRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Restaurant not found"));

		restaurant.setAdress(restaurantDto.getAdress());
		restaurant.setCity(restaurantDto.getCity());
		restaurant.setName(restaurantDto.getName());
		restaurant.setZipCode(restaurantDto.getZipCode());
		try {
			return restaurantsRepository.save(restaurant);
		} catch (Exception e) {
			throw new RuntimeException("Erreur dans la sauvegarde ");
		}
	}

	public void deleteRestaurant(Long id) {
		restaurantsRepository.deleteById(id);
	}
	
	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}
}