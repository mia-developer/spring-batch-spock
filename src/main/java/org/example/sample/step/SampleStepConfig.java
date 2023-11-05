package org.example.sample.step;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sample.model.Sample;
import org.example.sample.entity.SampleEntity;
import org.example.sample.repository.SampleRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
class SampleStepConfig {

  private static final String STEP_NAME = "sampleStep";
  private final SampleItemReader reader;
  private final EntityManagerFactory entityManagerFactory;

  @JobScope
  @Bean(STEP_NAME)
  public Step step(
      final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
    return new StepBuilder(STEP_NAME, jobRepository)
       .<Sample, SampleEntity>chunk(10, transactionManager)
       .reader(reader)
       .processor(processor())
       .writer(writer())
       .build();
  }

  private ItemProcessor<Sample, SampleEntity> processor(){
    return v -> {
      log.info("processing... "+v);

      return SampleEntity.builder()
         .name(v.getName()+"-"+v.getType())
         .type(v.getType())
         .build();
    };
  }

  private ItemWriter<SampleEntity> writer(){
    return new JpaItemWriterBuilder<SampleEntity>()
       .entityManagerFactory(entityManagerFactory)
       .build();
  }
}
