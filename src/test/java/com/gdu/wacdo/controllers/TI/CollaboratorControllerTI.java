package com.gdu.wacdo.controllers.TI;

import com.gdu.wacdo.dtos.CollaboratorDto;
import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.repositories.JobRepository;
import com.gdu.wacdo.services.CollaboratorService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CollaboratorControllerTI {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CollaboratorService collaboratorService;

    @MockitoBean
    private JobRepository jobRepository;

    // ----------------------------------------------------
    // GET /collaborators
    // ----------------------------------------------------
    @Test
    void shouldDisplayCollaboratorsList() throws Exception {
        when(collaboratorService.getCollaborators(any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/collaborators").param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("collaborators/index"))
                .andExpect(model().attributeExists("collaborators"))
                .andExpect(model().attributeExists("keyword"));

    }

    // ----------------------------------------------------
    // GET /collaborators/{id}
    // ----------------------------------------------------
    @Test
    void shouldDisplayCollaboratorDetail() throws Exception {
        Collaborator collaborator = new Collaborator();
        collaborator.setId(1L);
        collaborator.setFirstName("John");
        collaborator.setLastName("Doe");
        collaborator.setEmail("john@example.com");

        when(collaboratorService.getCollaboratorById(1L)).thenReturn(collaborator);
        when(collaboratorService.getCurrentAffectations(eq(1L), any(), any())).thenReturn(Collections.emptyList());
        when(collaboratorService.getPastAffectations(eq(1L), any(), any())).thenReturn(Collections.emptyList());
        when(collaboratorService.getAllJobs()).thenReturn(Collections.emptyList());

        Long jobId = 1L;
        LocalDate startDate = LocalDate.now();

        mockMvc.perform(get("/collaborators/1")
                        .param("jobId", jobId.toString())
                        .param("startDate", startDate.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("collaborators/detail"))
                .andExpect(model().attributeExists("collaborator"))
                .andExpect(model().attributeExists("currentAffectations"))
                .andExpect(model().attributeExists("pastAffectations"))
                .andExpect(model().attributeExists("jobs"))
                .andExpect(model().attribute("selectedJobId", jobId))
                .andExpect(model().attribute("selectedStartDate", startDate));
    }

    @Test
    void shouldDisplayUpdateCollaboratorPage() throws Exception {
        Collaborator collaborator = new Collaborator();
        collaborator.setId(1L);
        collaborator.setFirstName("John");
        collaborator.setLastName("Doe");
        collaborator.setEmail("john@example.com");

        when(collaboratorService.getCollaboratorById(1L)).thenReturn(collaborator);

        mockMvc.perform(get("/collaborators/update/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("collaborators/update"))
                .andExpect(model().attributeExists("collaboratorDto"));
    }

    @Test
    void shouldUpdateCollaborator() throws Exception {
        mockMvc.perform(post("/collaborators/update/1")
                        .param("firstName", "Jane")
                        .param("lastName", "Doe")
                        .param("email", "jane@example.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/collaborators"));

        verify(collaboratorService).updateCollaborator(eq(1L), any(CollaboratorDto.class));
    }

    @Test
    void shouldFailValidationOnUpdate() throws Exception {
        mockMvc.perform(post("/collaborators/update/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("collaborators/update"));

        verify(collaboratorService, never()).updateCollaborator(any(), any());
    }

    @Test
    void shouldDisplayCreateCollaboratorPage() throws Exception {
        mockMvc.perform(get("/collaborators/create").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("collaborators/create"))
                .andExpect(model().attributeExists("collaboratorDto"));
    }

    @Test
    void shouldCreateCollaborator() throws Exception {
        mockMvc.perform(post("/collaborators/create")
                        .param("firstName", "Alice")
                        .param("lastName", "Smith")
                        .param("email", "alice@example.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/collaborators"));

        verify(collaboratorService).createCollaborator(any(CollaboratorDto.class));
    }

    @Test
    void shouldFailValidationOnCreate() throws Exception {
        mockMvc.perform(post("/collaborators/create").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("collaborators/create"));

        verify(collaboratorService, never()).createCollaborator(any());
    }

    @Test
    void shouldDeleteCollaborator() throws Exception {
        mockMvc.perform(get("/collaborators/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/collaborators"));

        verify(collaboratorService).deleteCollaborator(1L);
    }
}
