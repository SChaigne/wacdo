package com.gdu.wacdo.services;

import com.gdu.wacdo.dtos.RestaurantDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.repositories.AffectationRepository;
import com.gdu.wacdo.repositories.JobRepository;
import com.gdu.wacdo.repositories.RestaurantsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    private RestaurantService restaurantService;
    private RestaurantsRepository restaurantsRepository;
    private AffectationRepository affectationsRepository;
    private JobRepository jobRepository;

    @BeforeEach
    void setUp() throws Exception {
        restaurantsRepository = mock(RestaurantsRepository.class);
        affectationsRepository = mock(AffectationRepository.class);
        jobRepository = mock(JobRepository.class);

        restaurantService = new RestaurantService();

        inject("restaurantsRepository", restaurantsRepository);
        inject("affectationsRepository", affectationsRepository);
        inject("jobRepository", jobRepository);
    }

    private void inject(String fieldName, Object value) throws Exception {
        var field = RestaurantService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(restaurantService, value);
    }

    @Test
    void testSearchRestaurants() {
        Restaurant r = new Restaurant();
        List<Restaurant> list = new ArrayList<>();
        list.add(r);

        when(restaurantsRepository.searchRestaurants("McDo", "75000", "Paris")).thenReturn(list);

        List<Restaurant> result = restaurantService.searchRestaurants("McDo", "75000", "Paris");
        assertEquals(1, result.size());
    }

    @Test
    void testGetRestaurantById_Found() {
        Restaurant r = new Restaurant();
        when(restaurantsRepository.findById(1L)).thenReturn(Optional.of(r));

        Restaurant result = restaurantService.getRestaurantById(1L);
        assertNotNull(result);
    }

    @Test
    void testGetRestaurantById_NotFound() {
        when(restaurantsRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> restaurantService.getRestaurantById(2L));
        assertEquals("Restaurant not found", ex.getMessage());
    }

    @Test
    void testGetAffectations() {
        Affectation a = new Affectation();
        List<Affectation> list = new ArrayList<>();
        list.add(a);

        Date fixedDate = new Date();

        when(affectationsRepository.findCurrentAffectationsByRestaurant(1L, 2L, "John", fixedDate))
                .thenReturn(list);

        List<Affectation> result = restaurantService.getAffectations(1L, 2L, "John", fixedDate);

        assertEquals(1, result.size());
    }
    @Test
    void testCreateRestaurant() {
        RestaurantDto dto = new RestaurantDto();
        dto.setName("McDo");
        dto.setCity("Paris");
        dto.setZipCode("75000");
        dto.setAdress("1 rue de Paris");

        when(restaurantsRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        Restaurant result = restaurantService.createRestaurant(dto);

        assertEquals("McDo", result.getName());
        assertEquals("Paris", result.getCity());
        assertEquals("75000", result.getZipCode());
        assertEquals("1 rue de Paris", result.getAdress());
    }

    @Test
    void testUpdateRestaurant() {
        Restaurant existing = new Restaurant();
        existing.setName("Old Name");

        RestaurantDto dto = new RestaurantDto();
        dto.setName("New Name");
        dto.setCity("New City");
        dto.setZipCode("75001");
        dto.setAdress("2 rue de Paris");

        when(restaurantsRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantsRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        Restaurant result = restaurantService.updateRestaurant(1L, dto);

        assertEquals("New Name", result.getName());
        assertEquals("New City", result.getCity());
        assertEquals("75001", result.getZipCode());
        assertEquals("2 rue de Paris", result.getAdress());
    }

    @Test
    void testDeleteRestaurant() {
        doNothing().when(restaurantsRepository).deleteById(1L);

        restaurantService.deleteRestaurant(1L);

        verify(restaurantsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllJobs() {
        Job job = new Job();
        List<Job> list = new ArrayList<>();
        list.add(job);

        when(jobRepository.findAll()).thenReturn(list);

        List<Job> result = restaurantService.getAllJobs();
        assertEquals(1, result.size());
    }
}
