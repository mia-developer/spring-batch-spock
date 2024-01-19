package org.example

import org.example.config.SpringBatchTestConfig
import org.example.data.SampleDataProvider
import org.example.helper.SampleDataTestHelper
import org.example.sample.model.enums.SampleType
import org.example.sample.model.Sample
import org.spockframework.spring.SpringBean
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.item.support.ListItemReader
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.JobRepositoryTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import spock.lang.Unroll

@TestPropertySource(properties = "spring.batch.job.enabled=false")
class SampleJobSpec extends SpringBatchTestConfig {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils

  @Autowired
  private Job sampleJob

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
    jobLauncherTestUtils.setJob(sampleJob)
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

    then:
    jobExecution.status == BatchStatus.COMPLETED
    jobExecution.stepExecutions.every { it ->
      it.status == BatchStatus.COMPLETED
      it.readCount = 10
      it.writeCount = 10
    }

    and:
    with(dataProvider.data.first()){ actual ->
      def expected = SampleDataTestHelper.getResult(jobType)
      expected.name == actual.name
      expected.type == actual.type
    }

    where:
    jobType << [SampleType.A, SampleType.B]
  }
}

