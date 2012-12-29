package pl.kaczanowski.algorithm.test;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.Test;

import pl.kaczanowski.algorithm.AlgorithmModule;
import pl.kaczanowski.algorithm.GeoSchedulingAlgorithm;
import pl.kaczanowski.algorithm.SchedulingAlgorithm;
import pl.kaczanowski.algorithm.SchedulingAlgorithm.Factory;
import pl.kaczanowski.graph.dataproviders.SchedulingGraphsDataProvider;
import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;
import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GeoSchedulingAlgoritmIntegrationTest {

	/**
	 * Problem with this method is that function return random results.
	 */
	@Test(dataProvider = "getGeoData", dataProviderClass = SchedulingGraphsDataProvider.class)
	public void shouldReturnAnyConfiguration(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph) {

		Injector injector = Guice.createInjector(new AlgorithmModule());

		Factory scheduleFactory = injector.getInstance(SchedulingAlgorithm.Factory.class);

		GeoSchedulingAlgorithm sut =
				new GeoSchedulingAlgorithm.Builder(scheduleFactory).setModulesGraph(modulesGraph)
						.setProcessorsGraph(processorsGraph).setProbabilityParameter(0.8).setIterations(100).build();

		SchedulingConfiguration configuration = sut.execute();

		assertThat(configuration).isNotNull();

		assertThat(configuration.getProcessorsPartial()).isNotNull();

		assertThat(configuration.getExecutionTime()).isGreaterThan(0);

	}
}
