package org.example.sample.step;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sample.model.enums.SampleType;
import org.example.sample.model.Sample;
import org.example.sample.persistence.entity.SampleEntity;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
class SampleStepConfig {

  private static final String STEP_NAME = "sampleStep";
  private final EntityManagerFactory entityManagerFactory;

  @JobScope
  @Bean(STEP_NAME)
  public Step step(
      final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
    return new StepBuilder(STEP_NAME, jobRepository)
       .<Sample, SampleEntity>chunk(10, transactionManager)
       .reader(reader(null))
       .processor(processor())
       .writer(writer())
       .build();
  }

  @Bean
  @StepScope
  public ItemReader<Sample> reader(@Value("#{jobParameters['type']}") String type){
    return new ListItemReader<>(this.randomList(SampleType.valueOf(String.valueOf(type))));
  }

  private List<Sample> randomList(final SampleType sampleType){
    return IntStream.range(0, 10)
       .mapToObj(i -> Sample.builder().name(randomAlphabetic(10)).type(sampleType).build())
       .collect(Collectors.toList());
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
