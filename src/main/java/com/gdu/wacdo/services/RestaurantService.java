package com.gdu.wacdo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gdu.wacdo.models.Restaurant;

@Service
public class RestaurantService {
	public List<Restaurant> getAll() {
		return List.of(new Restaurant("Pinochio", "8 rue des oliviers", "44000", "Nantes"),
				new Restaurant("Le clan des Mamas", "3 avenues des frenes", "44000", "Nantes"),
				new Restaurant("Le paradis du fruit", "45 place de commerce", "44000", "Nantes"),
				new Restaurant("Wok to Walk", "67 place de commerce", "44000", "Nantes"));
	}

	public Restaurant getById(int id) {
		List<Restaurant> list = getAll();
		if (id >= 0 && id < list.size()) {
			return list.get(id);
		} else {
			return null;
		}
	}
}
