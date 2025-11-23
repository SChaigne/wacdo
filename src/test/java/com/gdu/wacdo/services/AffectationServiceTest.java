package com.gdu.wacdo.services;

import com.gdu.wacdo.dtos.AffectationDto;
import com.gdu.wacdo.models.*;
import com.gdu.wacdo.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AffectationServiceTest {

    private AffectationService affectationService;
    private AffectationRepository affectationRepository;
    private CollaboratorRepository collaboratorRepository;
    private RestaurantsRepository restaurantsRepository;
    private JobRepository jobRepository;

    @BeforeEach
    void setUp() throws Exception {
        affectationRepository = mock(AffectationRepository.class);
        collaboratorRepository = mock(CollaboratorRepository.class);
        restaurantsRepository = mock(RestaurantsRepository.class);
        jobRepository = mock(JobRepository.class);

        affectationService = new AffectationService();

        inject("affectationsRepository", affectationRepository);
        inject("collaboratorRepository", collaboratorRepository);
        inject("restaurantsRepository", restaurantsRepository);
        inject("jobRepository", jobRepository);
    }

    private void inject(String fieldName, Object value) throws Exception {
        Field field = AffectationService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(affectationService, value);
    }

    @Test
    void testGetFilteredAffectations() {
        Restaurant r1 = new Restaurant();
        r1.setName("McDo");
        r1.setCity("Paris");

        Job job = new Job();
        job.setId(10L);

        Collaborator c = new Collaborator();
        c.setFirstName("John");
        c.setLastName("Doe");

        Affectation a = new Affectation();
        a.setRestaurant(r1);
        a.setJob(job);
        a.setCollaborator(c);
        a.setStartDate(Date.valueOf(LocalDate.of(2024,1,1)));
        a.setEndDate(Date.valueOf(LocalDate.of(2024,12,1)));

        when(affectationRepository.findAll()).thenReturn(new ArrayList<>(List.of(a)));

        List<Affectation> result = affectationService.getFilteredAffectations(
                10L, "Paris", "Mc", "John", LocalDate.of(2023,1,1), LocalDate.of(2025,1,1));

        assertEquals(1, result.size());
    }

    @Test
    void testGetAffectationById() {
        Affectation a = new Affectation();
        when(affectationRepository.findById(1L)).thenReturn(Optional.of(a));

        Affectation result = affectationService.getAffectationById(1L);

        assertNotNull(result);
    }

    @Test
    void testCreateAffectationSuccess() {
        AffectationDto dto = new AffectationDto();
        dto.setCollaboratorId(1L);
        dto.setRestaurantId(2L);
        dto.setJobId(3L);

        Collaborator col = new Collaborator();
        col.setId(1L);

        Restaurant res = new Restaurant();
        res.setId(2L);

        Job job = new Job();
        job.setId(3L);

        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(col));
        when(restaurantsRepository.findById(2L)).thenReturn(Optional.of(res));
        when(jobRepository.findById(3L)).thenReturn(Optional.of(job));
        when(affectationRepository.save(any(Affectation.class))).thenAnswer(inv -> inv.getArgument(0));

        Affectation result = affectationService.createAffectation(dto);

        assertNotNull(result.getCollaborator());
        assertNotNull(result.getRestaurant());
        assertNotNull(result.getJob());
        assertNotNull(result.getStartDate());
    }

    @Test
    void testUpdateAffectation() {
        Affectation existing = new Affectation();
        existing.setId(1L);

        AffectationDto dto = new AffectationDto();
        dto.setCollaboratorId(5L);
        dto.setRestaurantId(6L);
        dto.setJobId(7L);

        Collaborator c = new Collaborator();
        c.setId(5L);

        Restaurant r = new Restaurant();
        r.setId(6L);

        Job j = new Job();
        j.setId(7L);

        when(affectationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(collaboratorRepository.findById(5L)).thenReturn(Optional.of(c));
        when(restaurantsRepository.findById(6L)).thenReturn(Optional.of(r));
        when(jobRepository.findById(7L)).thenReturn(Optional.of(j));
        when(affectationRepository.save(any(Affectation.class))).thenAnswer(inv -> inv.getArgument(0));

        Affectation result = affectationService.updateAffectation(1L, dto);

        assertEquals(5L, result.getCollaborator().getId());
        assertEquals(6L, result.getRestaurant().getId());
        assertEquals(7L, result.getJob().getId());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testEndAffectation() {
        Affectation a = new Affectation();
        when(affectationRepository.findById(1L)).thenReturn(Optional.of(a));
        when(affectationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Affectation result = affectationService.endAffectation(1L);

        assertNotNull(result.getEndDate());
    }

    @Test
    void testGetAllCollaborators() {
        when(collaboratorRepository.findAll()).thenReturn(new ArrayList<>(List.of(new Collaborator())));
        assertEquals(1, affectationService.getAllCollaborators().size());
    }

    @Test
    void testGetAllRestaurants() {
        when(restaurantsRepository.findAll()).thenReturn(new ArrayList<>(List.of(new Restaurant())));
        assertEquals(1, affectationService.getAllRestaurants().size());
    }

    @Test
    void testGetAllJobs() {
        when(jobRepository.findAll()).thenReturn(new ArrayList<>(List.of(new Job())));
        assertEquals(1, affectationService.getAllJobs().size());
    }

    @Test
    void testGetAffectationDtoForUpdate() {
        Collaborator c = new Collaborator();
        c.setId(1L);

        Restaurant r = new Restaurant();
        r.setId(2L);

        Job j = new Job();
        j.setId(3L);

        Affectation a = new Affectation();
        a.setId(10L);
        a.setCollaborator(c);
        a.setRestaurant(r);
        a.setJob(j);

        when(affectationRepository.findById(10L)).thenReturn(Optional.of(a));

        AffectationDto dto = affectationService.getAffectationDtoForUpdate(10L);

        assertEquals(1L, dto.getCollaboratorId());
        assertEquals(2L, dto.getRestaurantId());
        assertEquals(3L, dto.getJobId());
        assertNotNull(dto.getUpdatedAt());
    }
}
