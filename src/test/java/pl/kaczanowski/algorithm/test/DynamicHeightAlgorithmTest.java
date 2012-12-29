package pl.kaczanowski.algorithm.test;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import pl.kaczanowski.algorithm.DynamicHeightAlgorithm;
import pl.kaczanowski.algorithm.HeightAlgorithm;
import pl.kaczanowski.graph.dataproviders.SchedulingGraphsDataProvider;
import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

public class DynamicHeightAlgorithmTest {

	@Test(dataProviderClass = SchedulingGraphsDataProvider.class, dataProvider = "getSchedulingPolicyData")
	public void shouldReturnLongestPathForTwoProcessors(final ModulesGraph modulesGraph,
			final ProcessorsGraph processorsGraph, final Map<Integer, Set<Integer>> processorDevision,
			final Integer startModule, final Integer expectedCost) {

		HeightAlgorithm sut =
				new DynamicHeightAlgorithm.Factory().create(modulesGraph, processorsGraph, processorDevision);

		int cost = sut.getCost(startModule);

		assertThat(cost).isEqualTo(expectedCost);

	}
}
