package pl.kaczanowski.algorithm.test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import pl.kaczanowski.algorithm.HeightAlgorithm;
import pl.kaczanowski.algorithm.SchedulingAlgorithm;
import pl.kaczanowski.graph.dataproviders.SchedulingGraphsDataProvider;
import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

public class SchedulingAlgorithmTest {
	@Test(dataProviderClass = SchedulingGraphsDataProvider.class, dataProvider = "getSchedulingData")
	public void shouldReturnProperExecutionTime(final ModulesGraph modulesGraph,
			final ProcessorsGraph processorsGraph, final Map<Integer, Set<Integer>> processorsPartial,
			final int expectedExecutionTime) {

		HeightAlgorithm heightAlgorithmStub = mock(HeightAlgorithm.class);
		when(heightAlgorithmStub.getCost(anyInt())).thenReturn(1);

		SchedulingAlgorithm sut = new SchedulingAlgorithm(heightAlgorithmStub, modulesGraph, processorsGraph);

		int executionTime = sut.getExecutionTime(processorsPartial);

		assertThat(executionTime).isEqualTo(expectedExecutionTime);

	}
}
