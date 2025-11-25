package com.gdu.wacdo.controllers.TI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.repositories.RestaurantsRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class RestaurantControllerTI {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantsRepository restaurantsRepository;

    private Long existingRestaurantId;
    private String existingRestaurantName = "Restaurant Test Initial";

    @BeforeEach
    void setUp() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(existingRestaurantName);
        restaurant.setCity("Paris");
        restaurant.setZipCode("75001");
        restaurant.setAdress("1 Rue de la Paix");
        restaurantsRepository.save(restaurant);
        this.existingRestaurantId = restaurant.getId();
    }

    @Test
    void shouldCreateRestaurantSuccessfully() throws Exception {
        long initialCount = restaurantsRepository.count();
        final String expectedName = "Nouveau Restaurant";
        final String expectedCity = "Lyon";

        mockMvc.perform(
                        post("/restaurants/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", expectedName)
                                .param("city", expectedCity)
                                .param("zipCode", "69002")
                                .param("adress", "2 Place Bellecour")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants"));

        assertThat(restaurantsRepository.count()).isEqualTo(initialCount + 1);

        // Récupérer le restaurant créé en supposant que c'est celui avec l'ID le plus élevé
        Restaurant createdRestaurant = restaurantsRepository.findAll().stream()
                .max(Comparator.comparing(Restaurant::getId))
                .orElse(null);

        assertThat(createdRestaurant).isNotNull();
        assertThat(createdRestaurant.getName()).isEqualTo(expectedName);
        assertThat(createdRestaurant.getCity()).isEqualTo(expectedCity);
    }

    @Test
    void shouldFailCreationOnMissingName() throws Exception {
        long initialCount = restaurantsRepository.count();

        mockMvc.perform(
                        post("/restaurants/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", "")
                                .param("city", "Marseille")
                                .param("zipCode", "13008")
                                .param("adress", "Rue du Port")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/create"))
                .andExpect(model().attributeHasFieldErrors("restaurantDto", "name"));

        assertThat(restaurantsRepository.count()).isEqualTo(initialCount);
    }


    @Test
    void shouldUpdateRestaurantSuccessfully() throws Exception {
        final String newCity = "Bordeaux";
        final String newZipCode = "33000";

        mockMvc.perform(
                        post("/restaurants/update/{id}", existingRestaurantId)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", existingRestaurantName)
                                .param("city", newCity)
                                .param("zipCode", newZipCode)
                                .param("adress", "Adresse inchangée")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants"));

        Optional<Restaurant> updatedRestaurantOpt = restaurantsRepository.findById(existingRestaurantId);
        assertThat(updatedRestaurantOpt).isPresent();
        Restaurant updatedRestaurant = updatedRestaurantOpt.get();

        assertThat(updatedRestaurant.getCity()).isEqualTo(newCity);
        assertThat(updatedRestaurant.getZipCode()).isEqualTo(newZipCode);
        assertThat(updatedRestaurant.getName()).isEqualTo(existingRestaurantName);
    }

    @Test
    void shouldFailUpdateOnInvalidZipCode() throws Exception {
        final String originalCity = restaurantsRepository.findById(existingRestaurantId).get().getCity();

        mockMvc.perform(
                        post("/restaurants/update/{id}", existingRestaurantId)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", existingRestaurantName)
                                .param("city", "Toulouse")
                                .param("zipCode", "INVALID")
                                .param("adress", "1 Rue de la Paix")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/update"))
                .andExpect(model().attributeHasFieldErrors("restaurantDto", "zipCode"));

        Optional<Restaurant> unchangedRestaurantOpt = restaurantsRepository.findById(existingRestaurantId);
        assertThat(unchangedRestaurantOpt).isPresent();
        assertThat(unchangedRestaurantOpt.get().getCity()).isEqualTo(originalCity);
    }

    @Test
    void shouldDeleteRestaurantSuccessfully() throws Exception {
        long initialCount = restaurantsRepository.count();

        mockMvc.perform(
                        get("/restaurants/delete/{id}", existingRestaurantId)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants"));

        assertThat(restaurantsRepository.count()).isEqualTo(initialCount - 1);
        assertThat(restaurantsRepository.findById(existingRestaurantId)).isNotPresent();
    }
}