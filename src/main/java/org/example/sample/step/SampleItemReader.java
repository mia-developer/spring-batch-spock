package org.example.sample.step;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.example.sample.enums.SampleType;
import org.example.sample.model.Sample;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SampleItemReader implements ItemReader<Sample> {

	private int count = 0;

	@Override
	public Sample read() {
		if (count++ > 10){
			return null;
		}

		Map<String, Object> jobParameters =
			 Objects.requireNonNull(StepSynchronizationManager.getContext()).getJobParameters();
		SampleType sampleType = SampleType.valueOf(String.valueOf(jobParameters.get("type")));

		return Sample.builder()
			 .name(randomAlphabetic(10))
			 .type(sampleType)
			 .build();
	}
}
