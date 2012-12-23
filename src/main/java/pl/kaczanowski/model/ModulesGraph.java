package pl.kaczanowski.model;

import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class ModulesGraph {

	private final String name;

	private final Integer[] taskCosts;

	private final Table<Integer, Integer, Integer> taskChildrenWeight;

	public ModulesGraph(final String name, final Integer[] taskCosts, final Integer[][] taskConnections) {
		this.name = name;
		this.taskCosts = taskCosts;
		taskChildrenWeight = ImmutableTable.copyOf(getTaskChildrenWeight(taskConnections));

	}

	/**
	 * @param taskConnections
	 * @return table - row is task, column is children task and value is weight
	 */
	private Table<Integer, Integer, Integer> getTaskChildrenWeight(final Integer[][] taskConnections) {
		Table<Integer, Integer, Integer> result = TreeBasedTable.create();

		for (int i = 0; i < taskConnections.length; i++) {
			Integer[] singleTaskConnections = taskConnections[i];
			for (int j = 0; j < singleTaskConnections.length; j++) {
				if (singleTaskConnections[j] != null) {
					result.put(i, j, singleTaskConnections[j]);
				}
			}
		}

		return result;
	}

	public String getName() {
		return name;
	}

	public Set<Integer> getChildren(final Integer taskId) {
		return taskChildrenWeight.row(taskId).keySet();
	}

	public int getCost(final Integer taskId) {
		return taskCosts[taskId];
	}

	public int getChangeTime(final Integer taskId, final Integer childTaskId) {
		return taskChildrenWeight.get(taskId, childTaskId);
	}

	@Override
	public String toString() {
		return "ModulesGraph [name=" + name + ", taskCosts=" + Arrays.toString(taskCosts) + ", taskChildrenWeight="
				+ taskChildrenWeight + "]";
	}

}
