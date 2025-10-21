package com.gdu.wacdo.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdu.wacdo.models.Restaurant;

public interface RestaurantsRepository extends JpaRepository<Restaurant,  Integer> {

}
