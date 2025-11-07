package com.gdu.wacdo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gdu.wacdo.models.Restaurant;

public interface RestaurantsRepository extends JpaRepository<Restaurant, Long> {

	@Query("""
			    SELECT r FROM Restaurant r
			    WHERE (:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')))
			      AND (:zipCode IS NULL OR r.zipCode LIKE CONCAT('%', :zipCode, '%'))
			      AND (:city IS NULL OR LOWER(r.city) LIKE LOWER(CONCAT('%', :city, '%')))
			""")
	List<Restaurant> searchRestaurants(@Param("name") String name, @Param("zipCode") String zipCode,
			@Param("city") String city);

}
