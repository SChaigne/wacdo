package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dtos.JobDto;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.services.JobService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
@AutoConfigureMockMvc(addFilters = false)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @Test
    void testJobsList() throws Exception {
        when(jobService.getAllJobs()).thenReturn(List.of(new Job()));

        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/index"))
                .andExpect(model().attributeExists("jobs"));

        verify(jobService).getAllJobs();
    }

    @Test
    void testJobDetailOk() throws Exception {
        Job job = new Job();
        job.setId(1L);
        job.setName("Dev");

        when(jobService.getJobById(1L)).thenReturn(job);

        mockMvc.perform(get("/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/detail"))
                .andExpect(model().attributeExists("job"));
    }


    @Test
    void testCreateJobPage() throws Exception {
        mockMvc.perform(get("/jobs/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/create"))
                .andExpect(model().attributeExists("jobDto"));
    }

    @Test
    void testCreateJobPostSuccess() throws Exception {
        mockMvc.perform(post("/jobs/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Serveur"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jobs"));

        verify(jobService).createJob(any(JobDto.class));
    }

    @Test
    void testUpdateJobPageOk() throws Exception {
        Job job = new Job();
        job.setId(1L);
        job.setName("Manager");

        when(jobService.getJobById(1L)).thenReturn(job);

        mockMvc.perform(get("/jobs/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/update"))
                .andExpect(model().attributeExists("jobDto"));
    }

    @Test
    void testUpdateJobPostSuccess() throws Exception {
        mockMvc.perform(post("/jobs/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Cuisinier"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jobs"));

        verify(jobService).updateJob(any(Long.class), any(JobDto.class));
    }


    @Test
    void testDeleteJob() throws Exception {
        mockMvc.perform(get("/jobs/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jobs"));

        verify(jobService).deleteJob(1L);
    }
}
