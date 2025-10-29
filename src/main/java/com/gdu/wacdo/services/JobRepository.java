package com.gdu.wacdo.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdu.wacdo.models.Job;

public interface JobRepository extends JpaRepository<Job, Integer>{

}
