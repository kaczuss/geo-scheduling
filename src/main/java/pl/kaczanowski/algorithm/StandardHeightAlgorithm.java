package pl.kaczanowski.algorithm;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class StandardHeightAlgorithm implements HeightAlgorithm {

	public static class Factory implements HeightAlgorithmFactory {

		@Override
		public StandardHeightAlgorithm create(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph,
				final Map<Integer, Set<Integer>> processorDevision) {
			return new StandardHeightAlgorithm(modulesGraph, processorsGraph, processorDevision);
		}
	}

	// private final Logger log = LoggerFactory.getLogger(DynamicHeightAlgorithm.class);

	private final ModulesGraph modulesGraph;

	private final ProcessorsGraph processorsGraph;

	private final Map<Integer, Integer> taskToProcessor;

	private StandardHeightAlgorithm(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph,
			final Map<Integer, Set<Integer>> processorDevision) {
		this.modulesGraph = modulesGraph;
		this.processorsGraph = processorsGraph;
		taskToProcessor = ImmutableMap.copyOf(getDevisionMap(processorDevision));

	}

	/*
	 * (non-Javadoc)
	 * @see pl.kaczanowski.algorithm.HeightAlgorithm#getCost(java.lang.Integer)
	 */
	@Override
	public int getCost(final Integer taskId) {

		Set<Integer> parentsTasks = modulesGraph.getParents(taskId);

		Set<Integer> changeWeight = Sets.newTreeSet();
		changeWeight.add(0);
		for (Integer parentTaskId : parentsTasks) {
			changeWeight.add(processorsGraph.getConnectionCost(taskToProcessor.get(parentTaskId),
					taskToProcessor.get(taskId))
					* modulesGraph.getChangeTime(parentTaskId, taskId));
		}

		return Ordering.natural().max(changeWeight) + getSimpleCost(taskId);
	}

	private Map<Integer, Integer> getDevisionMap(final Map<Integer, Set<Integer>> processorDevision) {
		Map<Integer, Integer> result = Maps.newTreeMap();

		for (Entry<Integer, Set<Integer>> entry : processorDevision.entrySet()) {
			for (Integer taskId : entry.getValue()) {
				result.put(taskId, entry.getKey());
			}
		}

		return result;
	}

	private int getSimpleCost(final Integer taskId) {

		int weight = modulesGraph.getCost(taskId);
		Set<Integer> childrenTasks = modulesGraph.getChildren(taskId);
		if (childrenTasks.isEmpty()) {
			return weight;
		}
		// log.info("weight on taskId[" + taskId + "] = " + weight);

		Set<Integer> values = Sets.newTreeSet();

		for (Integer childTaskId : childrenTasks) {
			int changeWeight =
					processorsGraph.getConnectionCost(taskToProcessor.get(taskId), taskToProcessor.get(childTaskId))
							* modulesGraph.getChangeTime(taskId, childTaskId);
			changeWeight += getSimpleCost(childTaskId);
			// log.info("on child " + childTaskId + " weight= " + changeWeight);

			values.add(changeWeight);
		}

		return weight + Ordering.natural().max(values);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StandardHeightAlgorithm [modulesGraph=");
		builder.append(modulesGraph);
		builder.append(", processorsGraph=");
		builder.append(processorsGraph);
		builder.append(", taskToProcessor=");
		builder.append(taskToProcessor);
		builder.append("]");
		return builder.toString();
	}

}
