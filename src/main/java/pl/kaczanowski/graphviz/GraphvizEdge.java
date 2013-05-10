package pl.kaczanowski.graphviz;

public class GraphvizEdge {

	private final int from;

	private final int to;

	private final int cost;

	/**
	 * @param from
	 * @param to
	 */
	public GraphvizEdge(final int from, final int to, final int cost) {
		this.from = from;
		this.to = to;
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

}
