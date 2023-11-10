package org.example.sample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.sample.model.enums.SampleType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sample {

	private Long id;
	private String name;
	private SampleType type;
}
