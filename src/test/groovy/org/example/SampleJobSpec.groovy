package org.example

import org.example.config.SpringBatchTestConfig
import org.example.data.SampleDataProvider
import org.example.sample.model.enums.SampleType
import org.example.sample.model.Sample
import org.example.sample.persistence.entity.SampleEntity
import org.spockframework.spring.SpringBean
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.item.support.ListItemReader
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.JobRepositoryTestUtils
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

class SampleJobSpec extends SpringBatchTestConfig {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils

  @Autowired
  private SampleDataProvider dataProvider

  @SpringBean
  private ListItemReader<Sample> reader = Stub()

  def cleanup() {
    dataProvider.clean()
    jobRepositoryTestUtils.removeJobExecutions()
  }

  @Unroll
  def "whenJobExecuted thenSuccess"(){
    reader.read() >>> [new Sample(name: "test", type: jobType), null]

    given:
    def jobParameters = new JobParametersBuilder()
        .addString("type", jobType.name())
        .toJobParameters()

    when:
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

    then:
    jobExecution.status == BatchStatus.COMPLETED

    and:
    with(dataProvider.data.first()){ actual ->  // grouping step result
      def expected = this.createResult(jobType)
      expected.name == actual.name
      expected.type == actual.type
    }

    where:
    jobType << [SampleType.A, SampleType.B] // job parameter
  }

  def createResult(final SampleType type){
    switch (type){
      case SampleType.A:
        return new SampleEntity(name: "test-A", type: SampleType.A)
      case SampleType.B:
        return new SampleEntity(name: "test-B", type: SampleType.B)
    }
  }
}

