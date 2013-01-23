package pl.kaczanowski.algorithm.listner;

import java.util.TreeSet;

import pl.kaczanowski.model.SchedulingConfiguration;

public interface AlgorithmStepsListener {

	void addBestConfiguration(SchedulingConfiguration bestConfiguration);

	void addCurrentConfiguration(SchedulingConfiguration currentConfiguration);

	void addStepConfigurations(TreeSet<SchedulingConfiguration> configurations);

}
