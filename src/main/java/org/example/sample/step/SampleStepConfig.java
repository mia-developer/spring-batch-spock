package org.example.sample.step;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.sample.enums.SampleType;
import org.example.sample.model.AfterStepSample;
import org.example.sample.model.BeforeStepSample;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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
       .<BeforeStepSample, AfterStepSample>chunk(10, transactionManager)
       .reader(this.reader())
       .processor(processor())
       .writer(writer())
        .build();
  }

  private ItemReader<BeforeStepSample> reader(){
    return () -> {
      Map<String, Object> jobParameters =
         Objects.requireNonNull(StepSynchronizationManager.getContext()).getJobParameters();
      SampleType sampleType = SampleType.valueOf(String.valueOf(jobParameters.get("type")));

      return new BeforeStepSample(randomAlphabetic(10), sampleType);
    };
  }

  private ItemProcessor<BeforeStepSample, AfterStepSample> processor(){
    return v -> new AfterStepSample(v.getName(), v.getType());
  }

  private ItemWriter<AfterStepSample> writer(){
    return v -> v.getItems().forEach(i -> log.info(i.toString()));
  }
}
