package pl.kaczanowski.graphviz;

public class GraphvizVertex {

	private final int number;
	private final int cost;

	/**
	 * @param number
	 * @param cost
	 */
	public GraphvizVertex(final int number, final int cost) {
		this.number = number;
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

	public int getNumber() {
		return number;
	}

}
