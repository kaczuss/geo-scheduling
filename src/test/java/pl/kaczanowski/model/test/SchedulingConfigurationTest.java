package pl.kaczanowski.model.test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import pl.kaczanowski.algorithm.SchedulingAlgorithm;
import pl.kaczanowski.model.SchedulingConfiguration;

@Test
public class SchedulingConfigurationTest {

	private final byte[] first = {1, 0, 1, 0};
	private final byte[] second = {0, 1, 1, 1};
	private final byte[] third = {0, 0, 0, 0};

	@Mock
	private SchedulingAlgorithm scheduleAlgorithmStub;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	public void shouldSortFromBestToWorstConfiguration() {

		List<SchedulingConfiguration> result = Lists.newArrayList();

		SchedulingConfiguration conf_1 = getConfiguration(4, first);
		result.add(conf_1);

		SchedulingConfiguration conf_2 = getConfiguration(2, second);
		result.add(conf_2);

		SchedulingConfiguration conf_3 = getConfiguration(3, third);
		result.add(conf_3);

		Collections.sort(result);

		assertThat(result).containsSequence(conf_2, conf_3, conf_1);

	}

	@SuppressWarnings("unchecked")
	private SchedulingConfiguration getConfiguration(final int expectedExecutionTime, final byte[] bites) {
		when(scheduleAlgorithmStub.getExecutionTime(anyMap())).thenReturn(expectedExecutionTime);
		return SchedulingConfiguration.create(bites, 1, scheduleAlgorithmStub);
	}

}
