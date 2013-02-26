package pl.kaczanowski.algorithm.runner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;
import pl.kaczanowski.utils.SimpleFunctions;
import pl.kaczanowski.utils.SimplePredicates;

import com.google.common.base.Charsets;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;

public class InputDataReader {

	protected static class InputData {

		private final ModulesGraph modulesGraph;

		private final ProcessorsGraph processorsGraph;

		/**
		 * @param modulesGraph
		 * @param processorsGraph
		 */
		private InputData(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph) {
			this.modulesGraph = modulesGraph;
			this.processorsGraph = processorsGraph;
		}

		public ModulesGraph getModulesGraph() {
			return modulesGraph;
		}

		public ProcessorsGraph getProcessorsGraph() {
			return processorsGraph;
		}

	}

	public InputData readData(final File file) throws IOException {
		List<String> lines = CharStreams.readLines(CharStreams.newReaderSupplier(new InputSupplier<InputStream>() {

			@Override
			public InputStream getInput() throws IOException {
				return new BufferedInputStream(new FileInputStream(file));
			}
		}, Charsets.UTF_8));
		lines =
				Lists.newArrayList(Collections2.filter(lines,
						Predicates.and(SimplePredicates.notStartsFrom('#'), SimplePredicates.isNotEmptyString())));

		Integer[] taskCosts = toIntegerArray(lines.get(0));
		int processorNumberLine = 1 + taskCosts.length;
		Integer[][] modulesGraph = toIntegerMatrix(lines.subList(1, processorNumberLine));

		Integer processors = Integer.valueOf(lines.get(processorNumberLine));

		Integer[][] processorsGraph =
				toIntegerMatrix(lines.subList(processorNumberLine + 1, processorNumberLine + 1 + processors));

		return new InputData(new ModulesGraph(file.getName(), taskCosts, modulesGraph), new ProcessorsGraph(
				processorsGraph));
	}

	private Integer[] toIntegerArray(final String costs) {
		return Iterables.toArray(
				Iterables.transform(Splitter.on(';').split(costs), SimpleFunctions.stringToInteger()), Integer.class);
	}

	private Integer[][] toIntegerMatrix(final List<String> subList) {
		Integer[][] result = new Integer[subList.size()][];

		for (int i = 0; i < subList.size(); i++) {
			String integers = subList.get(i);
			result[i] = toIntegerArray(integers);
		}

		return result;
	}
}
