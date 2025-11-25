package com.gdu.wacdo.services;

import com.gdu.wacdo.dtos.CollaboratorDto;
import com.gdu.wacdo.models.Affectation;
import com.gdu.wacdo.models.Collaborator;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.repositories.AffectationRepository;
import com.gdu.wacdo.repositories.CollaboratorRepository;
import com.gdu.wacdo.repositories.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class CollaboratorServiceTest {

    private CollaboratorService collaboratorService;
    private CollaboratorRepository collaboratorRepository;
    private AffectationRepository affectationRepository;
    private JobRepository jobRepository;

    @BeforeEach
    void setUp() throws Exception {
        collaboratorRepository = mock(CollaboratorRepository.class);
        affectationRepository = mock(AffectationRepository.class);
        jobRepository = mock(JobRepository.class);

        collaboratorService = new CollaboratorService();

        inject("collaboratorRepository", collaboratorRepository);
        inject("affectationRepository", affectationRepository);
        inject("jobRepository", jobRepository);
    }

    private void inject(String fieldName, Object value) throws Exception {
        var field = CollaboratorService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(collaboratorService, value);
    }

    @Test
    void testGetCollaborators_Unassigned() {
        Collaborator c = new Collaborator();
        List<Collaborator> list = new ArrayList<>();
        list.add(c);

        when(collaboratorRepository.findCollaboratorsWithoutAffectation()).thenReturn(list);

        List<Collaborator> result = collaboratorService.getCollaborators(null, true);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCollaborators_WithKeyword() {
        Collaborator c = new Collaborator();
        List<Collaborator> list = new ArrayList<>();
        list.add(c);

        when(collaboratorRepository.findByKeyword("John")).thenReturn(list);

        List<Collaborator> result = collaboratorService.getCollaborators("John", false);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCollaborators_All() {
        Collaborator c = new Collaborator();
        List<Collaborator> list = new ArrayList<>();
        list.add(c);

        when(collaboratorRepository.findAll()).thenReturn(list);

        List<Collaborator> result = collaboratorService.getCollaborators(null, false);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCollaboratorById_Found() {
        Collaborator c = new Collaborator();
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(c));

        Collaborator result = collaboratorService.getCollaboratorById(1L);
        assertNotNull(result);
    }

    @Test
    void testGetCollaboratorById_NotFound() {
        when(collaboratorRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> collaboratorService.getCollaboratorById(2L));
    }

    @Test
    void testGetCurrentAffectations() {
        Affectation a = new Affectation();
        List<Affectation> list = new ArrayList<>();
        list.add(a);

        when(affectationRepository.findCurrentByCollaborator(1L, 2L, LocalDate.now())).thenReturn(list);

        List<Affectation> result = collaboratorService.getCurrentAffectations(1L, 2L, LocalDate.now());
        assertEquals(1, result.size());
    }

    @Test
    void testGetPastAffectations() {
        Affectation a = new Affectation();
        List<Affectation> list = new ArrayList<>();
        list.add(a);

        when(affectationRepository.findPastByCollaborator(1L, 2L, LocalDate.now())).thenReturn(list);

        List<Affectation> result = collaboratorService.getPastAffectations(1L, 2L, LocalDate.now());
        assertEquals(1, result.size());
    }

    @Test
    void testCreateCollaborator() {
        CollaboratorDto dto = new CollaboratorDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("test@test.com");

        when(collaboratorRepository.save(any(Collaborator.class))).thenAnswer(inv -> inv.getArgument(0));

        Collaborator result = collaboratorService.createCollaborator(dto);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void testUpdateCollaborator() {
        Collaborator existing = new Collaborator();
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setEmail("old@test.com");

        CollaboratorDto dto = new CollaboratorDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setEmail("new@test.com");

        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(collaboratorRepository.save(any(Collaborator.class))).thenAnswer(inv -> inv.getArgument(0));

        Collaborator result = collaboratorService.updateCollaborator(1L, dto);

        assertEquals("New", result.getFirstName());
        assertEquals("Name", result.getLastName());
        assertEquals("new@test.com", result.getEmail());
    }

    @Test
    void testDeleteCollaborator() {
        doNothing().when(collaboratorRepository).deleteById(1L);

        collaboratorService.deleteCollaborator(1L);

        verify(collaboratorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllJobs() {
        Job job = new Job();
        List<Job> list = new ArrayList<>();
        list.add(job);

        when(jobRepository.findAll()).thenReturn(list);

        List<?> result = collaboratorService.getAllJobs();
        assertEquals(1, result.size());
    }
}
