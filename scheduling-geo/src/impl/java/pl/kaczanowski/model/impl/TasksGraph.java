package pl.kaczanowski.model.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import pl.kaczanowski.model.Edge;
import pl.kaczanowski.model.Graph;
import pl.kaczanowski.model.Task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Simple implementation of {@link Graph}. This implementation contains of 2 maps: - first - key is task and value is
 * list of next task to execute - second - key is task and value is list of previous task that must be executed.
 * 
 * @author pawel
 * 
 */
public final class TasksGraph implements Graph {

	private final List<Task> tasks;

	private final Map<Task, ? extends Collection<Edge<Task>>> connections;

	private TasksGraph(final List<Task> tasks, final Map<Task, ? extends Collection<Edge<Task>>> connections) {
		this.tasks = tasks;
		this.connections = connections;
	}

	/**
	 * Builder of tasks graph.
	 * 
	 * @author pawel
	 * 
	 */
	public static class GraphBuilder {

		/**
		 * Creates graph from array of connections and task costs.
		 * 
		 * It's important, the tasks are numeration of tasks begins from 0
		 * 
		 * @param connections
		 *            - 2-dimmensional array, if connection between task 3 and 5 exists the value of connections[3][5] =
		 *            cost of processor change
		 * @param costs
		 *            - costs of tasks executions
		 * @return task graph
		 */
		public TasksGraph create(@Nonnull final Integer[][] connections, @Nonnull final int[] costs) {

			List<Task> createTasks = createTasks(costs);
			return new TasksGraph(createTasks, createConnections(connections, createTasks));
		}

		private Map<Task, ? extends Collection<Edge<Task>>> createConnections(final Integer[][] connections,
				final List<Task> createTasks) {

			HashMap<Task, Collection<Edge<Task>>> map = Maps.newHashMapWithExpectedSize(connections.length);

			for (int i = 0; i < connections.length; i++) {
				Integer[] row = connections[i];
				Set<Edge<Task>> edges = Sets.newTreeSet();
				map.put(createTasks.get(i), edges);
				for (int taskId = 0; taskId < row.length; taskId++) {
					Integer weight = row[taskId];
					if (weight != null) {
						edges.add(new Edge<Task>(createTasks.get(taskId), weight));
					}

				}

			}
			return map;
		}

		private List<Task> createTasks(@Nonnull final int[] costs) {
			List<Task> tasks = Lists.newArrayListWithExpectedSize(costs.length);
			for (int i = 0; i < costs.length; i++) {
				int cost = costs[i];
				// numeration of tasks begins from 0
				tasks.add(new Task(i, cost));
			}
			return tasks;
		}
	}

	/**
	 * @return tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * 
	 * @return graph connections
	 */
	public Map<Task, ? extends Collection<Edge<Task>>> getConnections() {
		return connections;
	}

}
