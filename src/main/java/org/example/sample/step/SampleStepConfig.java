package org.example.sample.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
class SampleStepConfig {

  private static final String STEP_NAME = "sampleStep";

  @JobScope
  @Bean(STEP_NAME)
  public Step step(
      final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .tasklet(sampleTasklet(), transactionManager)
        .build();
  }

  private Tasklet sampleTasklet() {
    return (contribution, chunkContext) -> {
      log.info("Sample Tasklet");
      return RepeatStatus.FINISHED;
    };
  }
}
