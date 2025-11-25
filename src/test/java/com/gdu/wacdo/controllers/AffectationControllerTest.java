package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dtos.AffectationDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.repositories.CollaboratorRepository;
import com.gdu.wacdo.repositories.JobRepository;
import com.gdu.wacdo.repositories.RestaurantsRepository;
import com.gdu.wacdo.services.AffectationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AffectationControllerTest.class)
@AutoConfigureMockMvc(addFilters = false)
class AffectationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AffectationService affectationService;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private RestaurantsRepository restaurantsRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private AffectationController affectationController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(affectationController).build();
    }

    @Test
    void testAffectationsList() throws Exception {
        Affectation aff = new Affectation();

        Job job = new Job();
        job.setId(1L);
        job.setName("Serveur");

        when(affectationService.getFilteredAffectations(
                eq(1L),
                eq("Paris"),
                eq("Chez Paul"),
                eq("John Doe"),
                eq(LocalDate.parse("2025-11-20")),
                eq(LocalDate.parse("2025-11-25"))
        )).thenReturn(List.of(aff));

        when(jobRepository.findAll()).thenReturn(List.of(job));

        mockMvc.perform(get("/affectations")
                .param("jobId", "1")
                .param("city", "Paris")
                .param("restaurantName", "Chez Paul")
                .param("collaboratorName", "John Doe")
                .param("startDate", "2025-11-20")
                .param("endDate", "2025-11-25")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("affectations/index"))
                .andExpect(model().attributeExists("affectations"))
                .andExpect(model().attributeExists("jobs"))
                .andExpect(model().attributeExists("selectedJobId"))
                .andExpect(model().attributeExists("selectedCity"))
                .andExpect(model().attributeExists("selectedRestaurantName"))
                .andExpect(model().attributeExists("selectedCollaborator"))
                .andExpect(model().attributeExists("selectedStartDate"))
                .andExpect(model().attributeExists("selectedEndDate"));

        verify(affectationService).getFilteredAffectations(
                1L, "Paris", "Chez Paul", "John Doe",
                LocalDate.parse("2025-11-20"),
                LocalDate.parse("2025-11-25")
        );
        verify(jobRepository).findAll();
    }


    @Test
    void testAffectationDetail() throws Exception {
        Affectation aff = new Affectation();
        aff.setId(1L);
        when(affectationService.getAffectationById(1L)).thenReturn(aff);

        mockMvc.perform(get("/affectations/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("affectations/detail"))
                .andExpect(model().attributeExists("affectation"));
    }

    @Test
    void testCreateAffectationPage() throws Exception {
        when(collaboratorRepository.findAll()).thenReturn(List.of(new Collaborator()));
        when(restaurantsRepository.findAll()).thenReturn(List.of(new Restaurant()));
        when(jobRepository.findAll()).thenReturn(List.of(new Job()));

        mockMvc.perform(get("/affectations/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("affectations/create"))
                .andExpect(model().attributeExists("affectationDto"))
                .andExpect(model().attributeExists("collaborators"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(model().attributeExists("jobs"));
    }

    @Test
    void testCreateAffectationPost() throws Exception {

        when(affectationService.createAffectation(any(AffectationDto.class)))
                .thenReturn(new Affectation());

        mockMvc.perform(post("/affectations/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("collaboratorId", "1")
                .param("restaurantId", "1")
                .param("jobId", "1")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/affectations"));

        verify(affectationService).createAffectation(any(AffectationDto.class));
    }


    @Test
    void testUpdateAffectationPage() throws Exception {
        AffectationDto dto = new AffectationDto();
        when(affectationService.getAffectationDtoForUpdate(1L)).thenReturn(dto);
        when(affectationService.getAllCollaborators()).thenReturn(List.of(new Collaborator()));
        when(affectationService.getAllRestaurants()).thenReturn(List.of(new Restaurant()));
        when(affectationService.getAllJobs()).thenReturn(List.of(new Job()));

        mockMvc.perform(get("/affectations/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("affectations/update"))
                .andExpect(model().attributeExists("affectationDto"))
                .andExpect(model().attributeExists("collaborators"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(model().attributeExists("jobs"));
    }

    @Test
    void testUpdateAffectationPost() throws Exception {

        when(affectationService.updateAffectation(eq(1L), any(AffectationDto.class)))
                .thenReturn(new Affectation());

        mockMvc.perform(post("/affectations/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("collaboratorId", "1")
                        .param("restaurantId", "1")
                        .param("jobId", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/affectations"));

        verify(affectationService).updateAffectation(eq(1L), any(AffectationDto.class));
    }


    @Test
    void testEndAffectation() throws Exception {
        mockMvc.perform(get("/affectations/end/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/affectations"));

        verify(affectationService).endAffectation(1L);
    }
}
