package pl.kaczanowski.model;

import java.util.Arrays;

public class ProcessorsGraph {

	private final Integer[][] connections;

	public ProcessorsGraph(final Integer[][] connections) {
		// can be assimetric
		this.connections = connections;
	}

	public int getChangeCost(final int processorA, final int processorB) {
		// FIXME - it could be not peer connections, so in this case i should find shortest path between processors,
		// it's important in scheduling algorithm too, where in this case transfer can take both all processors on
		// the path. Maybe i'll exclude this situation from project, and i must validate connections on the
		// constructor

		return processorA == processorB ? 0 : connections[processorA][processorB];
	}

	@Override
	public String toString() {
		return "ProcessorsGraph [connections=" + Arrays.toString(connections) + "]";
	}

}
