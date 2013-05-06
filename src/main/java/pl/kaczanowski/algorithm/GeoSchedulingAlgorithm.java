package pl.kaczanowski.algorithm;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.kaczanowski.algorithm.SchedulingAlgorithm.Factory;
import pl.kaczanowski.algorithm.listener.AlgorithmStepsListener;
import pl.kaczanowski.algorithm.listener.AlgorithmStepsListenerContainer;
import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;
import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
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
		private AlgorithmStepsListener stepsListener;

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

			GeoSchedulingAlgorithm ret = new GeoSchedulingAlgorithm(schedulingAlgorithmFactory, stepsListener);
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

		public Builder setStepsListener(final AlgorithmStepsListener stepsListener) {
			this.stepsListener = stepsListener;
			return this;
		}

	}

	private ModulesGraph modulesGraph;
	private ProcessorsGraph processorsGraph;
	private double probabilityParamter;
	private int iterations;

	private final SchedulingAlgorithm.Factory schedulingAlgorithmFactory;

	private final Logger log = LoggerFactory.getLogger(GeoSchedulingAlgorithm.class);
	private final AlgorithmStepsListener algorithmStepsListener;

	private GeoSchedulingAlgorithm(final Factory schedulingAlgorithmFactory,
			final AlgorithmStepsListener stepsListener) {
		this.schedulingAlgorithmFactory = schedulingAlgorithmFactory;
		this.algorithmStepsListener =
				stepsListener == null ? new AlgorithmStepsListenerContainer(null) : stepsListener;
	}

	private SchedulingConfiguration chooseNextConfiguration(final List<SchedulingConfiguration> configurations) {
		Random rand = new Random();

		int toChange = rand.nextInt(configurations.size());

		double randProb = rand.nextDouble();

		// FIXME is change to infinite loop?

		double changeIntProb = Math.pow(1.0 / (toChange + 1), probabilityParamter);
		while (changeIntProb < randProb) {
			toChange = rand.nextInt(configurations.size());
			changeIntProb = Math.pow(1.0 / (toChange + 1), probabilityParamter);

		}
		return Iterables.get(configurations, toChange);
	}

	public SchedulingConfiguration execute() {

		SchedulingAlgorithm schedulingAlgorithm = schedulingAlgorithmFactory.create(modulesGraph, processorsGraph);

		SchedulingConfiguration bestConfiguration =
				SchedulingConfiguration.createRandomConfiguration(modulesGraph.getTasksNumber(),
						processorsGraph.getProcessorsCount(), schedulingAlgorithm);

		SchedulingConfiguration currentConfiguration = bestConfiguration;

		log.debug("start configuration is " + currentConfiguration);

		algorithmStepsListener.startNewExecution();

		algorithmStepsListener.addCurrentConfiguration(currentConfiguration);
		algorithmStepsListener.addBestConfiguration(bestConfiguration);
		for (int i = 0; i < iterations; ++i) {

			currentConfiguration.resetEvolution();

			List<SchedulingConfiguration> configurations = Lists.newArrayList();

			while (currentConfiguration.hasNextEvolution()) {
				configurations.add(currentConfiguration.evaluate(schedulingAlgorithm));
			}

			configurations = Ordering.natural().sortedCopy(configurations);
			algorithmStepsListener.addStepConfigurations(configurations);

			currentConfiguration = chooseNextConfiguration(configurations);

			algorithmStepsListener.addCurrentConfiguration(currentConfiguration);

			log.debug("current configuration is " + currentConfiguration);
			SchedulingConfiguration bestInCurrentIteration = configurations.get(0);
			// sprawdzam czy czas wykonania jest ten sam najlepszego i wybrenej konfiguracji, jesli tak to
			// przypisuje wybrany jako najlepsza konfiguracja
			if (bestInCurrentIteration != currentConfiguration
					&& bestInCurrentIteration.getExecutionTime() == currentConfiguration.getExecutionTime()) {
				bestInCurrentIteration = currentConfiguration;
			}

			if (bestInCurrentIteration.getExecutionTime() < bestConfiguration.getExecutionTime()) {
				bestConfiguration = bestInCurrentIteration;
			}

			algorithmStepsListener.addBestConfiguration(bestConfiguration);

		}

		algorithmStepsListener.endExecution();

		return bestConfiguration;
	}
}
