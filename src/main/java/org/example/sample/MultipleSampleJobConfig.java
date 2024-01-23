package org.example.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MultipleSampleJobConfig {

	public static final String JOB_NAME = "multipleSampleStep";

	private final Step sampleStep;

	@Bean(JOB_NAME)
	public Job job(final JobRepository jobRepository) {
		return new JobBuilder(JOB_NAME, jobRepository).start(sampleStep).build();
	}
}
