package pl.kaczanowski.graphviz;

import java.util.List;

public class Graphviz {

	private final String name;

	private final List<GraphvizEdge> edges;
	private final List<GraphvizVertex> vertex;

	/**
	 * @param name
	 * @param edges
	 */
	public Graphviz(final String name, final List<GraphvizEdge> edges, final List<GraphvizVertex> vertex) {
		this.name = name;
		this.edges = edges;
		this.vertex = vertex;
	}

	public List<GraphvizEdge> getEdges() {
		return edges;
	}

	public String getName() {
		return name;
	}

	public List<GraphvizVertex> getVertex() {
		return vertex;
	}

}
