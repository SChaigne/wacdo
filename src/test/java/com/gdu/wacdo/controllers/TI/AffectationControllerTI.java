package com.gdu.wacdo.controllers.TI;

import com.gdu.wacdo.dtos.AffectationDto;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.repositories.JobRepository;
import com.gdu.wacdo.repositories.CollaboratorRepository;
import com.gdu.wacdo.repositories.RestaurantsRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class AffectationControllerTI {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AffectationService affectationService;

    @MockitoBean
    private CollaboratorRepository collaboratorRepository;

    @MockitoBean
    private RestaurantsRepository restaurantsRepository;

    @MockitoBean
    private JobRepository jobRepository;

    // ----------------------------------------------------
    // GET /affectations
    // ----------------------------------------------------
    @Test
    void shouldDisplayAffectationsList() throws Exception {
        when(affectationService.getFilteredAffectations(any(), any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/affectations"))
                .andExpect(status().isOk())
                .andExpect(view().name("affectations/index"))
                .andExpect(model().attributeExists("affectations"))
                .andExpect(model().attributeExists("jobs"));
    }

    @Test
    void shouldRedirectWhenAffectationDetailNotFound() throws Exception {
        when(affectationService.getAffectationById(999L)).thenThrow(new RuntimeException("not found"));

        mockMvc.perform(get("/affectations/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/affectations"));
    }

    @Test
    void shouldCreateAffectation() throws Exception {

        mockMvc.perform(post("/affectations/create")
                        .param("collaboratorId", "1")
                        .param("restaurantId", "1")
                        .param("jobId", "1")
                        .param("startDate", LocalDate.now().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/affectations"));

        verify(affectationService, times(1)).createAffectation(any(AffectationDto.class));
    }

    @Test
    void shouldFailValidationOnCreateAffectation() throws Exception {
        mockMvc.perform(post("/affectations/create").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("affectations/create"));

        verify(affectationService, never()).createAffectation(any());
    }

    // ----------------------------------------------------
    // POST /affectations/update/{id}
    // ----------------------------------------------------
    @Test
    void shouldUpdateAffectation() throws Exception {
        mockMvc.perform(post("/affectations/update/1")
                        .param("collaboratorId", "1")
                        .param("restaurantId", "1")
                        .param("jobId", "1")
                        .param("startDate", LocalDate.now().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/affectations"));

        verify(affectationService).updateAffectation(eq(1L), any());
    }

    @Test
    void shouldFailValidationOnUpdate() throws Exception {
        mockMvc.perform(post("/affectations/update/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("affectations/update"));

        verify(affectationService, never()).updateAffectation(any(), any());
    }

    // ----------------------------------------------------
    // GET /affectations/end/{id}
    // ----------------------------------------------------
    @Test
    void shouldEndAffectation() throws Exception {
        mockMvc.perform(get("/affectations/end/5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/affectations"));

        verify(affectationService).endAffectation(5L);
    }
}
