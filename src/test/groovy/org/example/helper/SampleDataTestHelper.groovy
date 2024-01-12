package org.example.helper

import org.example.sample.model.enums.SampleType
import org.example.sample.persistence.entity.SampleEntity
import spock.lang.Specification


class SampleDataTestHelper {

  static def createResult(final SampleType type){
    switch (type){
      case SampleType.A:
        return new SampleEntity(name: "test-A", type: SampleType.A)
      case SampleType.B:
        return new SampleEntity(name: "test-B", type: SampleType.B)
    }
  }
}
