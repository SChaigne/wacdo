package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dtos.CollaboratorDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.models.Restaurant;
import com.gdu.wacdo.services.CollaboratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollaboratorController.class)
@AutoConfigureMockMvc(addFilters = false)

class CollaboratorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CollaboratorService collaboratorService;

	@Test
	void testListCollaborators() throws Exception {
		when(collaboratorService.getCollaborators(null, null)).thenReturn(List.of(new Collaborator()));

		mockMvc.perform(get("/collaborators")).andExpect(status().isOk()).andExpect(view().name("collaborators/index"))
				.andExpect(model().attributeExists("collaborators"));

		verify(collaboratorService).getCollaborators(null, null);
	}

	@Test
	void testCollaboratorDetail() throws Exception {

		Restaurant restaurant = new Restaurant();
		restaurant.setName("Chez Paul");

		Job job = new Job();
		job.setName("Serveur");

		Collaborator collab = new Collaborator();
		collab.setId(1L);
		collab.setFirstName("John");
		collab.setLastName("Doe");

		Affectation aff = new Affectation();
		aff.setRestaurant(restaurant);
		aff.setJob(job);

		when(collaboratorService.getCollaboratorById(1L)).thenReturn(collab);
		when(collaboratorService.getCurrentAffectations(1L, 2L, LocalDate.parse("2025-11-20")))
				.thenReturn(List.of(aff));
		when(collaboratorService.getPastAffectations(1L, null, null)).thenReturn(List.of(new Affectation()));
		when(collaboratorService.getAllJobs()).thenReturn(List.of());

		mockMvc.perform(get("/collaborators/1").param("jobId", "2").param("startDate", "2025-11-20"))

				.andExpect(status().isOk()).andExpect(view().name("collaborators/detail"))
				.andExpect(model().attributeExists("collaborator"))
				.andExpect(model().attributeExists("currentAffectations"))
				.andExpect(model().attributeExists("pastAffectations")).andExpect(model().attributeExists("jobs"))
				.andExpect(model().attributeExists("selectedJobId"))
				.andExpect(model().attributeExists("selectedStartDate"));
	}

	@Test
	void testCreateCollaboratorPage() throws Exception {
		mockMvc.perform(get("/collaborators/create")).andExpect(status().isOk())
				.andExpect(view().name("collaborators/create")).andExpect(model().attributeExists("collaboratorDto"));
	}

	@Test
	void testCreateCollaboratorPost() throws Exception {
		mockMvc.perform(post("/collaborators/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", "John").param("lastName", "Doe").param("email", "john.doe@example.com"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/collaborators"));

		verify(collaboratorService).createCollaborator(any(CollaboratorDto.class));
	}

	@Test
	void testUpdateCollaboratorPage() throws Exception {
		Collaborator collab = new Collaborator();
		collab.setId(1L);
		collab.setFirstName("John");
		collab.setLastName("Doe");
		collab.setEmail("john.doe@example.com");

		when(collaboratorService.getCollaboratorById(1L)).thenReturn(collab);

		mockMvc.perform(get("/collaborators/update/1")).andExpect(status().isOk())
				.andExpect(view().name("collaborators/update")).andExpect(model().attributeExists("collaboratorDto"));
	}

	@Test
	void testUpdateCollaboratorPost() throws Exception {
		mockMvc.perform(post("/collaborators/update/1").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", "John").param("lastName", "Doe").param("email", "john.doe@example.com"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/collaborators"));

		verify(collaboratorService).updateCollaborator(any(Long.class), any(CollaboratorDto.class));
	}

	@Test
	void testDeleteCollaborator() throws Exception {
		mockMvc.perform(get("/collaborators/delete/1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/collaborators"));

		verify(collaboratorService).deleteCollaborator(1L);
	}
}
