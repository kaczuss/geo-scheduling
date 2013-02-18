package pl.kaczanowski.algorithm.runner;

import java.io.File;

import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

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

	public InputData readData(final File file) {
		// TODO Auto-generated method stub
		return null;
	}

}
