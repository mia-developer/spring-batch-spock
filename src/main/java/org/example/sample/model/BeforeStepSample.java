package org.example.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.sample.enums.SampleType;

@Getter
@AllArgsConstructor
public class BeforeStepSample {

	private String name;
	private SampleType type;
}
