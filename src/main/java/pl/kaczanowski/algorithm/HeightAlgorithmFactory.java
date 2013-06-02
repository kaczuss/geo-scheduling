package pl.kaczanowski.algorithm;

import java.util.Map;
import java.util.Set;

import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

public interface HeightAlgorithmFactory {

	HeightAlgorithm create(ModulesGraph modulesGraph, ProcessorsGraph processorsGraph,
			Map<Integer, Set<Integer>> processorsPartial);

}
