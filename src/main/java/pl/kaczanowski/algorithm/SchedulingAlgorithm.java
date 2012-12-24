package pl.kaczanowski.algorithm;

import java.util.Map;
import java.util.Set;

import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

/**
 * Algorithm for scheduling.
 * @author kaczanowskip
 */
public class SchedulingAlgorithm {

	private final HeightAlgorithm heightAlgorithm;
	private ModulesGraph modulesGraph;
	private ProcessorsGraph processorsGraph;

	public SchedulingAlgorithm(final HeightAlgorithm heightAlgorithm, final ModulesGraph modulesGraph,
			final ProcessorsGraph processorsGraph) {
		this.heightAlgorithm = heightAlgorithm;
		this.modulesGraph = modulesGraph;
		this.processorsGraph = processorsGraph;
	}

	public int getExecutionTime(final Map<Integer, Set<Integer>> processorsPartial) {
		return 0;
	}

}
