package org.example

import org.example.config.SpringBatchTestConfig
import org.example.data.SampleDataProvider
import org.example.sample.enums.SampleType
import org.example.sample.model.Sample
import org.example.sample.step.SampleItemReader
import org.spockframework.spring.SpringBean
import org.spockframework.spring.SpringSpy
import org.spockframework.spring.StubBeans
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.JobRepositoryTestUtils
import org.springframework.beans.factory.annotation.Autowired

@StubBeans(SampleItemReader)
class SampleJobSpec extends SpringBatchTestConfig {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils

  @Autowired
  private SampleDataProvider dataProvider

  @SpringBean
  private SampleItemReader reader = Stub()

  def setup(){
    expect:
    reader.read() >>> [new Sample(name: "test", type: SampleType.A), null]
  }

  def cleanup() {
    //jobRepositoryTestUtils.removeJobExecutions()
  }

  def "whenJobExecuted thenSuccess"(){
    given:
    def jobParameters = new JobParametersBuilder()
        .addString("type", jobType.name())
        .toJobParameters()

    when:
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

    then:
    jobExecution.status == BatchStatus.COMPLETED

    and:
    result == dataProvider.data.first().name

    where:
    jobType          | result
    SampleType.A     | jobType.name()+"test"
  }
}

