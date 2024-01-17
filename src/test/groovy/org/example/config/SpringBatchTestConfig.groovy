package org.example.config

import org.example.Application
import org.springframework.batch.core.job.SimpleJob
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBatchTest
@SpringBootTest
@ContextConfiguration(classes = [Application.class])
class SpringBatchTestConfig extends Specification {

  @Bean
  public JobLauncherTestUtils jobLauncherTestUtils() {
    JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
    jobLauncherTestUtils.setJob(new SimpleJob("sampleJob"));
    return jobLauncherTestUtils;
  }

}
