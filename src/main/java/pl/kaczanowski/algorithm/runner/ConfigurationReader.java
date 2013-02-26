package pl.kaczanowski.algorithm.runner;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import pl.kaczanowski.algorithm.runner.InputDataReader.InputData;
import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class ConfigurationReader {

	public static class Configuration {

		private final ModulesGraph modulesGraph;

		private final ProcessorsGraph processorsGraph;

		private final double probabilityParamter;

		private final int algorithmIterations;

		private final int runIterations;

		/**
		 * @param modulesGraph
		 * @param processorsGraph
		 * @param probabilityParamter
		 * @param algorithmIterations
		 * @param runIterations
		 * @param listenersClasses
		 */
		private Configuration(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph,
				final double probabilityParamter, final int algorithmIterations, final int runIterations) {
			this.modulesGraph = modulesGraph;
			this.processorsGraph = processorsGraph;
			this.probabilityParamter = probabilityParamter;
			this.algorithmIterations = algorithmIterations;
			this.runIterations = runIterations;
		}

		public int getAlgorithmIterations() {
			return algorithmIterations;
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

	private enum Paramters {

		INPUT_FILE("-f"),
		PROBABILITY("-prob"),
		ALGORITHM_ITERATIONS("-i"),
		RUN_ITERATIONS("-ri");
		;

		public static Paramters getByPrefix(final String key) {
			for (Paramters param : values()) {
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
		private Paramters(final String prefix) {
			this.prefix = prefix;
		}

	}

	private final InputDataReader dataReader;

	private static final String PARAM_VALUE_DELIMITER = "=";

	/**
	 * @param dataReader
	 */
	@Inject
	public ConfigurationReader(final InputDataReader dataReader) {
		this.dataReader = dataReader;
	}

	public Configuration readConfiguration(final String[] args) throws IOException {

		Map<Paramters, String> paramters = readParamters(args);

		checkArgument(paramters.containsKey(Paramters.INPUT_FILE), "Input file wasn't defined!");

		InputData inputData = dataReader.readData(new File(paramters.get(Paramters.INPUT_FILE)));

		double probability =
				paramters.containsKey(Paramters.PROBABILITY) ? Double.valueOf(paramters.get(Paramters.PROBABILITY))
						: 0.8;
		int algorithmIterations =
				paramters.containsKey(Paramters.ALGORITHM_ITERATIONS) ? Integer.valueOf(paramters
						.get(Paramters.ALGORITHM_ITERATIONS)) : 20;
		int runIterations =
				paramters.containsKey(Paramters.RUN_ITERATIONS) ? Integer.valueOf(paramters
						.get(Paramters.RUN_ITERATIONS)) : 1;
		return new Configuration(inputData.getModulesGraph(), inputData.getProcessorsGraph(), probability,
				algorithmIterations, runIterations);
	}

	private Map<Paramters, String> readParamters(final String[] args) {
		Map<Paramters, String> result = Maps.newHashMap();

		for (String param : args) {
			String key = param.substring(0, param.indexOf(PARAM_VALUE_DELIMITER));

			Paramters paramters = Paramters.getByPrefix(key);

			String value = param.substring(param.indexOf(PARAM_VALUE_DELIMITER) + 1);
			result.put(paramters, value);

		}

		return result;
	}

}
