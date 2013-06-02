package pl.kaczanowski.algorithm;

import java.util.Map;
import java.util.Set;

import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

public class ChildNodesAlgorithm implements HeightAlgorithm {

	public static class Factory implements HeightAlgorithmFactory {

		@Override
		public ChildNodesAlgorithm create(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph,
				final Map<Integer, Set<Integer>> processorDevision) {
			return new ChildNodesAlgorithm(modulesGraph);
		}
	}

	private final ModulesGraph modulesGraph;

	private ChildNodesAlgorithm(final ModulesGraph modulesGraph) {
		this.modulesGraph = modulesGraph;

	}

	/*
	 * (non-Javadoc)
	 * @see pl.kaczanowski.algorithm.HeightAlgorithm#getCost(java.lang.Integer)
	 */
	@Override
	public int getCost(final Integer taskId) {
		Set<Integer> children = modulesGraph.getChildren(taskId);
		int childrenCount = 0;
		for (Integer childTaskId : children) {
			childrenCount += getCost(childTaskId);

		}
		return children.size() + childrenCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChildNodesAlgorithm [modulesGraph=");
		builder.append(modulesGraph);
		builder.append("]");
		return builder.toString();
	}

}
