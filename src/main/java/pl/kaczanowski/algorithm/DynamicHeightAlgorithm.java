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

public class DynamicHeightAlgorithm implements HeightAlgorithm {

	public static class Factory {

		public DynamicHeightAlgorithm create(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph,
				final Map<Integer, Set<Integer>> processorDevision) {
			return new DynamicHeightAlgorithm(modulesGraph, processorsGraph, processorDevision);
		}
	}

	// private final Logger log = LoggerFactory.getLogger(DynamicHeightAlgorithm.class);

	private final ModulesGraph modulesGraph;

	private final ProcessorsGraph processorsGraph;

	private final Map<Integer, Integer> taskToProcessor;

	private DynamicHeightAlgorithm(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph,
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

		int weight = modulesGraph.getCost(taskId);
		Set<Integer> childrenTasks = modulesGraph.getChildren(taskId);
		if (childrenTasks.isEmpty()) {
			return weight;
		}
		// log.info("weight on taskId[" + taskId + "] = " + weight);

		Set<Integer> values = Sets.newTreeSet();

		for (Integer childTaskId : childrenTasks) {
			int changeWeight =
					processorsGraph.getChangeCost(taskToProcessor.get(taskId), taskToProcessor.get(childTaskId))
							* modulesGraph.getChangeTime(taskId, childTaskId);
			changeWeight += getCost(childTaskId);
			// log.info("on child " + childTaskId + " weight= " + changeWeight);

			values.add(changeWeight);
		}

		return weight + Ordering.natural().max(values);
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
}
