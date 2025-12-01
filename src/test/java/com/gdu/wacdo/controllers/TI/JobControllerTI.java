package com.gdu.wacdo.controllers.TI;

import com.gdu.wacdo.dtos.JobDto;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.repositories.JobRepository;
import com.gdu.wacdo.services.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
class JobControllerTI {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @MockitoBean
    private JobRepository jobRepository;

    @Test
    void shouldDisplayJobsList() throws Exception {
        when(jobService.getAllJobs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/index"))
                .andExpect(model().attributeExists("jobs"));
    }

    @Test
    void shouldDisplayJobDetail() throws Exception {
        Job job = new Job();
        job.setId(1L);
        job.setName("Developer");

        when(jobService.getJobById(1L)).thenReturn(job);

        mockMvc.perform(get("/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/detail"))
                .andExpect(model().attributeExists("job"))
                .andExpect(model().attribute("job", job));
    }

    @Test
    void shouldRedirectJobDetailWhenNotFound() throws Exception {
        when(jobService.getJobById(999L)).thenThrow(new RuntimeException("not found"));

        mockMvc.perform(get("/jobs/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jobs"));
    }

    @Test
    void shouldDisplayCreateJobPage() throws Exception {
        mockMvc.perform(get("/jobs/create").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/create"))
                .andExpect(model().attributeExists("jobDto"));
    }

    @Test
    void shouldCreateJob() throws Exception {
        mockMvc.perform(post("/jobs/create")
                        .param("name", "New Job")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jobs"));

        verify(jobService).createJob(any(JobDto.class));
    }

    @Test
    void shouldFailValidationOnCreate() throws Exception {
        mockMvc.perform(post("/jobs/create").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/create"));

        verify(jobService, never()).createJob(any());
    }

    @Test
    void shouldDisplayUpdateJobPage() throws Exception {
        Job job = new Job();
        job.setId(1L);
        job.setName("Developer");

        when(jobService.getJobById(1L)).thenReturn(job);

        mockMvc.perform(get("/jobs/update/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/update"))
                .andExpect(model().attributeExists("jobDto"));
    }

    @Test
    void shouldUpdateJob() throws Exception {
        mockMvc.perform(post("/jobs/update/1")
                        .param("name", "Updated Job")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jobs"));

        verify(jobService).updateJob(eq(1L), any(JobDto.class));
    }

    @Test
    void shouldFailValidationOnUpdate() throws Exception {
        mockMvc.perform(post("/jobs/update/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/update"));

        verify(jobService, never()).updateJob(any(), any());
    }

    @Test
    void shouldDeleteJob() throws Exception {
        mockMvc.perform(get("/jobs/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jobs"));

        verify(jobService).deleteJob(1L);
    }
}
