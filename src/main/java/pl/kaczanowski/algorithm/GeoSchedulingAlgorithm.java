package pl.kaczanowski.algorithm;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Random;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import pl.kaczanowski.algorithm.SchedulingAlgorithm.Factory;
import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;
import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * Geo algorithm for scheduling problem.
 * @author kaczanowskip
 */
public class GeoSchedulingAlgorithm {
	public static class Builder {

		private ModulesGraph modulesGraph;
		private ProcessorsGraph processorsGraph;
		private double probabilityParamter;
		private int iterations;

		private final Factory schedulingAlgorithmFactory;

		@Inject
		public Builder(@Nonnull final Factory scheduleFactory) {
			this.schedulingAlgorithmFactory = scheduleFactory;
		}

		public GeoSchedulingAlgorithm build() {
			checkNotNull(modulesGraph, "modules graph must be set");
			checkNotNull(processorsGraph, "processors graph must be set");
			checkArgument(iterations > 0, "iterations must be positive value");
			checkArgument(probabilityParamter > 0, "parameter must be positive value");

			GeoSchedulingAlgorithm ret = new GeoSchedulingAlgorithm(schedulingAlgorithmFactory);
			ret.modulesGraph = modulesGraph;
			ret.processorsGraph = processorsGraph;
			ret.iterations = iterations;
			ret.probabilityParamter = probabilityParamter;

			return ret;
		}

		public Builder setIterations(final int iterations) {
			this.iterations = iterations;
			return this;
		}

		public Builder setModulesGraph(final ModulesGraph modulesGraph) {
			this.modulesGraph = modulesGraph;
			return this;
		}

		public Builder setProbabilityParameter(final double probabilityParamter) {
			this.probabilityParamter = probabilityParamter;
			return this;
		}

		public Builder setProcessorsGraph(final ProcessorsGraph processorsGraph) {
			this.processorsGraph = processorsGraph;
			return this;
		}
	}

	private ModulesGraph modulesGraph;
	private ProcessorsGraph processorsGraph;
	private double probabilityParamter;
	private int iterations;

	private final SchedulingAlgorithm.Factory schedulingAlgorithmFactory;

	private GeoSchedulingAlgorithm(final Factory schedulingAlgorithmFactory) {
		this.schedulingAlgorithmFactory = schedulingAlgorithmFactory;
	}

	private SchedulingConfiguration chooseNextConfiguration(final TreeSet<SchedulingConfiguration> configurations) {
		Random rand = new Random();

		int toChange = rand.nextInt(configurations.size());

		double randProb = rand.nextDouble();

		// FIXME is change to infinite loop?

		double changeIntProb = 1 / Math.pow(toChange + 1, probabilityParamter);
		while (changeIntProb < randProb) {
			toChange = rand.nextInt(configurations.size());
			changeIntProb = 1 / Math.pow(toChange + 1, probabilityParamter);

		}

		return Iterables.get(configurations, toChange);
	}

	public SchedulingConfiguration execute() {

		SchedulingAlgorithm schedulingAlgorithm = schedulingAlgorithmFactory.create(modulesGraph, processorsGraph);

		SchedulingConfiguration bestConfiguration =
				SchedulingConfiguration.createRandomConfiguration(modulesGraph.getTasksNumber(),
						processorsGraph.getProcessorsCount(), schedulingAlgorithm);

		SchedulingConfiguration currentConfiguration = bestConfiguration;
		for (int i = 0; i < iterations; ++i) {

			currentConfiguration.resetEvolution();

			TreeSet<SchedulingConfiguration> configurations = Sets.newTreeSet();

			while (currentConfiguration.hasNextEvolution()) {
				configurations.add(currentConfiguration.evaluate(schedulingAlgorithm));
			}

			currentConfiguration = chooseNextConfiguration(configurations);

			if (currentConfiguration.getExecutionTime() < bestConfiguration.getExecutionTime()) {
				bestConfiguration = currentConfiguration;
			}
		}

		return bestConfiguration;
	}
}
