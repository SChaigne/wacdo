package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dtos.RestaurantDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.services.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Test
    void testRestaurantsList() throws Exception {
        when(restaurantService.searchRestaurants("", "", "")).thenReturn(List.of(new Restaurant()));

        mockMvc.perform(get("/restaurants").param("name", "").param("zipCode", "").param("city", "")).andExpect(status().isOk()).andExpect(view().name("restaurants/index")).andExpect(model().attributeExists("restaurants")).andExpect(model().attributeExists("name")).andExpect(model().attributeExists("zipCode")).andExpect(model().attributeExists("city"));

        verify(restaurantService).searchRestaurants("", "", "");
    }

    @Test
    void testRestaurantDetail() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Le Gourmet");

        Collaborator collaborator = new Collaborator();
        collaborator.setFirstName("John");
        collaborator.setLastName("Doe");

        Job job = new Job();
        job.setName("Serveur");

        Affectation aff = new Affectation();
        aff.setCollaborator(collaborator);
        aff.setRestaurant(restaurant);
        aff.setJob(job);

        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant);
        when(restaurantService.getAffectations(1L, null, null, null)).thenReturn(List.of(aff));
        when(restaurantService.getAllJobs()).thenReturn(List.of());

        mockMvc.perform(get("/restaurants/1").param("jobId", "1").param("startDate", "2025-11-20").param("name", restaurant.getName())).andExpect(status().isOk()).andExpect(view().name("restaurants/detail")).andExpect(model().attributeExists("restaurant")).andExpect(model().attributeExists("affectations")).andExpect(model().attributeExists("jobs")).andExpect(model().attributeExists("jobId")).andExpect(model().attributeExists("name")).andExpect(model().attributeExists("startDate"));

    }


    @Test
    void testCreateRestaurantPage() throws Exception {
        mockMvc.perform(get("/restaurants/create").with(csrf())).andExpect(status().isOk()).andExpect(view().name("restaurants/create")).andExpect(model().attributeExists("restaurantDto"));
    }

    @Test
    void testCreateRestaurantPost() throws Exception {
        mockMvc.perform(post("/restaurants/create").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Chez Moi").param("adress", "1 rue Exemple").param("city", "Paris").param("zipCode", "75001")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/restaurants"));

        verify(restaurantService).createRestaurant(any(RestaurantDto.class));
    }

    @Test
    void testUpdateRestaurantPage() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Le Gourmet");
        restaurant.setAdress("1 rue Exemple");
        restaurant.setCity("Paris");
        restaurant.setZipCode("75001");

        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant);

        mockMvc.perform(get("/restaurants/update/1").with(csrf())).andExpect(status().isOk()).andExpect(view().name("restaurants/update")).andExpect(model().attributeExists("restaurantDto"));
    }

    @Test
    void testUpdateRestaurantPost() throws Exception {
        mockMvc.perform(post("/restaurants/update/1").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "Chez Moi").param("adress", "1 rue Exemple").param("city", "Paris").param("zipCode", "75001")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/restaurants"));

        verify(restaurantService).updateRestaurant(any(Long.class), any(RestaurantDto.class));
    }

    @Test
    void testDeleteRestaurant() throws Exception {
        mockMvc.perform(get("/restaurants/delete/1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/restaurants"));

        verify(restaurantService).deleteRestaurant(1L);
    }
}
