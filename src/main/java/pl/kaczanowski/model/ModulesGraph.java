package pl.kaczanowski.model;

public class ModulesGraph {

	private final String name;

	private final Integer[] taskCosts;

	private final Integer[][] connections;

	public ModulesGraph(final String name, final Integer[] taskCosts, final Integer[][] graphConnections) {
		this.name = name;
		this.taskCosts = taskCosts;
		this.connections = graphConnections;

	}

	public String getName() {
		return name;
	}

	public Integer[] getTaskCosts() {
		return taskCosts;
	}

	public Integer[][] getConnections() {
		return connections;
	}

}
