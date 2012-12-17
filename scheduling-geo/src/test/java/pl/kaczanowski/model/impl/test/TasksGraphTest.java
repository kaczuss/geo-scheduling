package pl.kaczanowski.model.impl.test;

import static org.fest.assertions.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.kaczanowski.model.Edge;
import pl.kaczanowski.model.Task;
import pl.kaczanowski.model.impl.TasksGraph;

import com.google.common.collect.Lists;

public class TasksGraphTest {

	@DataProvider
	private Object[][] getSampleGraphs() {
		return new Object[][]{{
				new Integer[][]{{null, 1, 1, null}, {null, null, null, 2}, {null, null, null, null},
						{null, null, null, null}}, new int[]{1, 3, 3, 4}

		}};
	}

	@Test(dataProvider = "getSampleGraphs")
	public void shuldConstructGraph(final Integer[][] connections, final int[] executionTime) {

		TasksGraph sut = buildGraph(connections, executionTime);
		List<Task> tasks = sut.getTasks();

		List<Task> expected = Lists.newArrayListWithExpectedSize(executionTime.length);
		for (int i = 0; i < executionTime.length; i++) {
			expected.add(new Task(i, executionTime[i]));

		}

		assertThat(tasks).hasSize(executionTime.length).contains(expected.toArray());

		Map<Task, ? extends Collection<Edge<Task>>> connectionsGraph = sut.getConnections();

		assertThat(connectionsGraph).hasSize(connections.length);

		// FIXME: może trochę lepsze sprawdzanie
		for (Entry<Task, ? extends Collection<Edge<Task>>> row : connectionsGraph.entrySet()) {

			// check if has proper edges
			Integer[] connectRow = connections[row.getKey().getId()];
			for (int i = 0; i < connectRow.length; i++) {
				Integer weight = connectRow[i];
				Edge<Task> found = null;
				for (Edge<Task> edge : row.getValue()) {
					if (edge.getVertex().getId() == i) {
						assertNull(found, "edges cannot duplicate");
						found = edge;
					}
				}
				if (weight == null) {
					assertNull(found, "weight is null, cannot create connection");
				} else {
					assertEquals(found.getWeight(), weight, "weight is invalid");
				}

			}

		}
	}

	private TasksGraph buildGraph(final Integer[][] connections, final int[] executionTime) {
		return new TasksGraph.GraphBuilder().create(connections, executionTime);
	}

}
