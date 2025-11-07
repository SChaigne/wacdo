package com.gdu.wacdo.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.wacdo.dtos.JobDto;
import com.gdu.wacdo.models.Job;
import com.gdu.wacdo.repositories.JobRepository;

@Service
public class JobService {
	@Autowired
	private JobRepository jobRepository;

	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}

	public Job getJobById(Long id) {
		return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job non trouvé avec id: " + id));
	}

	public Job createJob(JobDto jobDto) {
		Job job = new Job();
		job.setName(jobDto.getName());
		job.setUpdatedAt(new Date());

		try {
			return jobRepository.save(job);
		} catch (Exception e) {
			throw new RuntimeException("Erreur dans la sauvegarde ");
		}
	}

	public Job updateJob(Long id, JobDto jobDto) {
		Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job non trouvé avec id: " + id));
		job.setName(jobDto.getName());
		job.setUpdatedAt(new Date());
		try {
			return jobRepository.save(job);
		} catch (Exception e) {
			throw new RuntimeException("Erreur dans la sauvegarde ");
		}
	}

	public void deleteJob(Long id) {
		jobRepository.deleteById(id);
	}
}
