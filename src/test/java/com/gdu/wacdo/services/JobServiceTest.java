package com.gdu.wacdo.services;

import com.gdu.wacdo.dtos.JobDto;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.repositories.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceTest {

    private JobRepository jobRepository;
    private JobService jobService;

    @BeforeEach
    void setUp() throws Exception {
        jobRepository = mock(JobRepository.class);
        jobService = new JobService();

        var field = JobService.class.getDeclaredField("jobRepository");
        field.setAccessible(true);
        field.set(jobService, jobRepository);
    }

    @Test
    void testGetAllJobs() {
        List<Job> jobs = Arrays.asList(new Job(), new Job());
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> result = jobService.getAllJobs();

        assertEquals(2, result.size());
        verify(jobRepository).findAll();
    }

    @Test
    void testGetJobByIdSuccess() {
        Job job = new Job();
        job.setId(1L);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        Job result = jobService.getJobById(1L);

        assertEquals(1L, result.getId());
        verify(jobRepository).findById(1L);
    }

    @Test
    void testGetJobByIdNotFound() {
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobService.getJobById(1L));

        assertTrue(exception.getMessage().contains("Job non trouvé"));
    }

    @Test
    void testCreateJobSuccess() {
        JobDto dto = new JobDto();
        dto.setName("Serveur");

        Job savedJob = new Job();
        savedJob.setId(5L);

        when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

        Job result = jobService.createJob(dto);

        assertEquals(5L, result.getId());
        verify(jobRepository).save(any(Job.class));
    }

    @Test
    void testCreateJobError() {
        JobDto dto = new JobDto();
        dto.setName("Serveur");

        when(jobRepository.save(any(Job.class))).thenThrow(new RuntimeException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobService.createJob(dto));

        assertTrue(exception.getMessage().contains("Erreur dans la sauvegarde"));
    }

    @Test
    void testUpdateJobSuccess() {
        JobDto dto = new JobDto();
        dto.setName("Chef");

        Job existing = new Job();
        existing.setId(10L);

        when(jobRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(jobRepository.save(existing)).thenReturn(existing);

        Job result = jobService.updateJob(10L, dto);

        assertEquals("Chef", result.getName());
        verify(jobRepository).findById(10L);
        verify(jobRepository).save(existing);
    }

    @Test
    void testUpdateJobNotFound() {
        when(jobRepository.findById(10L)).thenReturn(Optional.empty());

        JobDto dto = new JobDto();
        dto.setName("Chef");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobService.updateJob(10L, dto));

        assertTrue(exception.getMessage().contains("Job non trouvé"));
    }

    @Test
    void testUpdateJobError() {
        JobDto dto = new JobDto();
        dto.setName("Chef");

        Job existing = new Job();
        existing.setId(10L);

        when(jobRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(jobRepository.save(existing)).thenThrow(new RuntimeException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobService.updateJob(10L, dto));

        assertTrue(exception.getMessage().contains("Erreur dans la sauvegarde"));
    }

    @Test
    void testDeleteJob() {
        jobService.deleteJob(3L);

        verify(jobRepository).deleteById(3L);
    }
}