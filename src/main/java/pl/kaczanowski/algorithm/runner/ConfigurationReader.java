package pl.kaczanowski.algorithm.runner;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

import pl.kaczanowski.algorithm.helper.ConfigurationHelper;
import pl.kaczanowski.algorithm.listener.AlgorithmStepsListener;
import pl.kaczanowski.algorithm.listener.AlgorithmStepsListenerContainer;
import pl.kaczanowski.algorithm.listener.BestIterationAchievementResultListener;
import pl.kaczanowski.algorithm.listener.BestMeanWorstIterationListener;
import pl.kaczanowski.algorithm.listener.BestWorstInCurrentIterationListener;
import pl.kaczanowski.algorithm.listener.DistributionNumberToBest;
import pl.kaczanowski.algorithm.listener.IterationsToFoundBestResultListener;
import pl.kaczanowski.algorithm.runner.InputDataReader.InputData;
import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class ConfigurationReader {

	public static class Configuration {

		private final ModulesGraph modulesGraph;

		private final ProcessorsGraph processorsGraph;

		private final double probabilityParamter;

		private final int algorithmIterations;

		private final int runIterations;

		private final AlgorithmStepsListener listenerCollection;

		/**
		 * @param modulesGraph
		 * @param processorsGraph
		 * @param probabilityParamter
		 * @param algorithmIterations
		 * @param runIterations
		 * @param listenersClasses
		 */
		private Configuration(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph,
				final double probabilityParamter, final int algorithmIterations, final int runIterations,
				final Collection<AlgorithmStepsListener> listeners) {
			this.modulesGraph = modulesGraph;
			this.processorsGraph = processorsGraph;
			this.probabilityParamter = probabilityParamter;
			this.algorithmIterations = algorithmIterations;
			this.runIterations = runIterations;
			this.listenerCollection = new AlgorithmStepsListenerContainer(listeners);

		}

		public int getAlgorithmIterations() {
			return algorithmIterations;
		}

		public AlgorithmStepsListener getListener() {
			return listenerCollection;
		}

		public ModulesGraph getModulesGraph() {
			return modulesGraph;
		}

		public double getProbabilityParamter() {
			return probabilityParamter;
		}

		public ProcessorsGraph getProcessorsGraph() {
			return processorsGraph;
		}

		public int getRunIterations() {
			return runIterations;
		}

	}

	private enum Parameters {

		INPUT_FILE("-f"),
		PROBABILITY("-prob"),
		ALGORITHM_ITERATIONS("-i"),
		RUN_ITERATIONS("-ri"),
		BEST_RESULT("-best"),
		ACHIEVEMENT_REPORT_FILE("-achievement"),
		ITERATIONS_TO_BEST_REPORT_FILE("-iterToBest"),
		ITERATIONS_BEST_MEAN_WORST_REPORT_FILE("-bestMeanWorst"),
		BEST_WORST_CURRENT_REPORT_FILE("-currentBestWorst"),
		DISTRIBUTION_REPORT_FILE("-distribution");

		public static Parameters getByPrefix(final String key) {
			for (Parameters param : values()) {
				if (param.prefix.equals(key)) {
					return param;
				}
			}
			throw new IllegalArgumentException(MessageFormat.format("The paramter {0} isn''t defined!", key));
		}

		private final String prefix;

		/**
		 * @param prefix
		 */
		private Parameters(final String prefix) {
			this.prefix = prefix;
		}

	}

	private final InputDataReader dataReader;

	private final ConfigurationHelper configurationHelper;

	private static final String PARAM_VALUE_DELIMITER = "=";

	/**
	 * @param dataReader
	 */
	@Inject
	public ConfigurationReader(final InputDataReader dataReader, final ConfigurationHelper configurationHelper) {
		this.dataReader = dataReader;
		this.configurationHelper = configurationHelper;
	}

	private AlgorithmStepsListener getBestAchievementListener(final Map<Parameters, String> parameters) {

		if (parameters.containsKey(Parameters.ACHIEVEMENT_REPORT_FILE)) {
			Integer bestValue = getBestExecutionTimeParameter(parameters);

			return new BestIterationAchievementResultListener(parameters.get(Parameters.ACHIEVEMENT_REPORT_FILE),
					bestValue);

		}

		return null;
	}

	public Integer getBestExecutionTimeParameter(final Map<Parameters, String> parameters) {
		String bestResult = parameters.get(Parameters.BEST_RESULT);
		checkArgument(!Strings.isNullOrEmpty(bestResult), "If use achievement listener specify best result!");

		Integer bestValue = Integer.valueOf(bestResult);

		checkArgument(bestValue > 0, "Are you kidding?:)");
		return bestValue;
	}

	private AlgorithmStepsListener getBestMeanWorstListener(final Map<Parameters, String> parameters) {
		if (parameters.containsKey(Parameters.ITERATIONS_BEST_MEAN_WORST_REPORT_FILE)) {

			return new BestMeanWorstIterationListener(
					parameters.get(Parameters.ITERATIONS_BEST_MEAN_WORST_REPORT_FILE), configurationHelper);

		}

		return null;
	}

	private AlgorithmStepsListener getCurrentBestWorstListener(final Map<Parameters, String> parameters) {
		String fileMask = parameters.get(Parameters.BEST_WORST_CURRENT_REPORT_FILE);
		if (fileMask == null) {
			return null;
		}
		return new BestWorstInCurrentIterationListener(fileMask, configurationHelper);
	}

	private AlgorithmStepsListener getDistributionListener(final Map<Parameters, String> parameters,
			final int algorithmIterations) {
		String fileName = parameters.get(Parameters.DISTRIBUTION_REPORT_FILE);
		if (fileName == null) {
			return null;
		}
		Integer bestValue = getBestExecutionTimeParameter(parameters);

		return new DistributionNumberToBest(fileName, bestValue, algorithmIterations);
	}

	private AlgorithmStepsListener getIterationsToBestListener(final Map<Parameters, String> parameters) {
		if (parameters.containsKey(Parameters.ITERATIONS_TO_BEST_REPORT_FILE)) {
			String bestResult = parameters.get(Parameters.BEST_RESULT);
			checkArgument(!Strings.isNullOrEmpty(bestResult),
					"If use iterations to best listener specify best result!");

			Integer bestValue = Integer.valueOf(bestResult);

			checkArgument(bestValue > 0, "Are you kidding?:)");

			return new IterationsToFoundBestResultListener(parameters.get(Parameters.ITERATIONS_TO_BEST_REPORT_FILE),
					bestValue);

		}

		return null;

	}

	public Configuration readConfiguration(final String[] args) throws IOException {

		Map<Parameters, String> parameters = readParamters(args);

		checkArgument(parameters.containsKey(Parameters.INPUT_FILE), "Input file wasn't defined!");

		InputData inputData = dataReader.readData(new File(parameters.get(Parameters.INPUT_FILE)));

		double probability =
				parameters.containsKey(Parameters.PROBABILITY) ? Double.valueOf(parameters
						.get(Parameters.PROBABILITY)) : 0.8;
		int algorithmIterations =
				parameters.containsKey(Parameters.ALGORITHM_ITERATIONS) ? Integer.valueOf(parameters
						.get(Parameters.ALGORITHM_ITERATIONS)) : 20;
		int runIterations =
				parameters.containsKey(Parameters.RUN_ITERATIONS) ? Integer.valueOf(parameters
						.get(Parameters.RUN_ITERATIONS)) : 1;

		Collection<AlgorithmStepsListener> listeners = Lists.newArrayList();
		listeners.add(getBestAchievementListener(parameters));
		listeners.add(getIterationsToBestListener(parameters));
		listeners.add(getBestMeanWorstListener(parameters));
		listeners.add(getCurrentBestWorstListener(parameters));
		listeners.add(getDistributionListener(parameters, algorithmIterations));

		Iterables.removeIf(listeners, Predicates.isNull());

		return new Configuration(inputData.getModulesGraph(), inputData.getProcessorsGraph(), probability,
				algorithmIterations, runIterations, listeners);
	}

	private Map<Parameters, String> readParamters(final String[] args) {
		Map<Parameters, String> result = Maps.newHashMap();

		for (String param : args) {
			String key = param.substring(0, param.indexOf(PARAM_VALUE_DELIMITER));

			Parameters paramters = Parameters.getByPrefix(key);

			String value = param.substring(param.indexOf(PARAM_VALUE_DELIMITER) + 1);
			result.put(paramters, value);

		}

		return result;
	}

}
