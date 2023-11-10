package org.example.data

import org.example.sample.persistence.entity.SampleEntity
import org.example.sample.persistence.repository.SampleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SampleDataProvider {

  @Autowired
  private SampleRepository repository

  SampleDataProvider clean() {
    repository.deleteAll()

    return this
  }

  List<SampleEntity> getData() {
    return repository.findAll()
  }
}
