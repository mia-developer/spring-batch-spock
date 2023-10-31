package org.example.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.example.sample.enums.SampleType;

@Getter
@AllArgsConstructor
@ToString
public class AfterStepSample {

	private String name;
	private SampleType type;
}
