package pl.kaczanowski.graphviz;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Set;

import pl.kaczanowski.model.ModulesGraph;

public class ModulesGraphTranslator {

	public Graphviz translate(final String name, final ModulesGraph modulesGraph) {

		List<GraphvizEdge> edges = newArrayList();

		List<GraphvizVertex> vertexes = newArrayList();
		for (int taskFromId = 0; taskFromId < modulesGraph.getTasksNumber(); ++taskFromId) {
			Set<Integer> children = modulesGraph.getChildren(taskFromId);
			vertexes.add(new GraphvizVertex(taskFromId, modulesGraph.getCost(taskFromId)));
			for (Integer taskToId : children) {
				edges.add(new GraphvizEdge(taskFromId, taskToId, modulesGraph.getChangeTime(taskFromId, taskToId)));
			}
		}
		return new Graphviz(name, edges, vertexes);

	}
}
