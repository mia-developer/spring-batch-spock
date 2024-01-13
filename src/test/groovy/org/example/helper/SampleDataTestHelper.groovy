package org.example.helper

import org.example.sample.model.enums.SampleType
import org.example.sample.persistence.entity.SampleEntity
import spock.lang.Specification


class SampleDataTestHelper {

  static def getResult(final SampleType type){
    switch (type){
      case SampleType.A:
        return createSampleEntity("test-A", type)
      case SampleType.B:
        return createSampleEntity("test-B", type)
    }
  }

  static SampleEntity createSampleEntity(final String name, final SampleType type){
    return SampleEntity.builder()
      .name(name)
      .type(type)
      .build()
  }
}
