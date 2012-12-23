package pl.kaczanowski.algorithm;

import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

public class DynamicHeightAlgorithm {

	private final ModulesGraph modulesGraph;

	private final ProcessorsGraph processorsGraph;

	private final Integer[][] processorDevision;

	public DynamicHeightAlgorithm(final ModulesGraph modulesGraph, final ProcessorsGraph processorsGraph,
			final Integer[][] processorDevision) {
		this.modulesGraph = modulesGraph;
		this.processorsGraph = processorsGraph;
		this.processorDevision = processorDevision;
	}

	public int getCost(final Integer startModule) {
		// TODO Auto-generated method stub
		return 0;
	}

}
