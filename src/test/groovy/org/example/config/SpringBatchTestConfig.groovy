package org.example.config

import org.example.Application
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBatchTest
@SpringBootTest
@ContextConfiguration(classes = [Application.class])
class SpringBatchTestConfig extends Specification {

}
