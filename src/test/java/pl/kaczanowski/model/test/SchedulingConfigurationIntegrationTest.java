package pl.kaczanowski.model.test;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

import pl.kaczanowski.algorithm.AlgorithmModule;
import pl.kaczanowski.algorithm.SchedulingAlgorithm;
import pl.kaczanowski.algorithm.SchedulingAlgorithm.Factory;
import pl.kaczanowski.graph.dataproviders.SchedulingGraphsDataProvider;
import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;
import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.inject.Guice;
import com.google.inject.Injector;

@Test
public class SchedulingConfigurationIntegrationTest {

	// TODO refactoring, dataprovider

	public void shouldReturnValidExecutionTimeOnGauss18Graph() throws IOException {
		Injector injector = Guice.createInjector(new AlgorithmModule(null));

		Factory scheduleFactory = injector.getInstance(SchedulingAlgorithm.Factory.class);

		ModulesGraph modulesGraph = SchedulingGraphsDataProvider.getModuleGraph("gauss18");

		ProcessorsGraph processorsGraph =
				SchedulingGraphsDataProvider.getProcessorGraphFromFile(new File(
						"src/test/resources/graphs/geoProcessors/gauss18_0.txt"));

		SchedulingConfiguration sut =
				SchedulingConfiguration.create(new byte[]{1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1}, 1,
						scheduleFactory.create(modulesGraph, processorsGraph));

		assertThat(sut.getExecutionTime()).isEqualTo(44);
	}

	public void shouldReturnValidExecutionTimeOnSimpleGraph() throws IOException {
		Injector injector = Guice.createInjector(new AlgorithmModule(null));

		Factory scheduleFactory = injector.getInstance(SchedulingAlgorithm.Factory.class);

		ModulesGraph modulesGraph = SchedulingGraphsDataProvider.getModuleGraph("simple");

		ProcessorsGraph processorsGraph =
				SchedulingGraphsDataProvider.getProcessorGraphFromFile(new File(
						"src/test/resources/graphs/geoProcessors/simple_0.txt"));

		SchedulingConfiguration sut =
				SchedulingConfiguration.create(new byte[]{0, 0, 1, 1, 1}, 1,
						scheduleFactory.create(modulesGraph, processorsGraph));

		assertThat(sut.getExecutionTime()).isEqualTo(103);
	}
}
