package pl.kaczanowski.model;

public class ProcessorsGraph {

	private final Integer[][] connections;

	public ProcessorsGraph(final Integer[][] connections) {
		super();
		this.connections = connections;
	}

	public Integer[][] getConnections() {
		return connections;
	}

}
