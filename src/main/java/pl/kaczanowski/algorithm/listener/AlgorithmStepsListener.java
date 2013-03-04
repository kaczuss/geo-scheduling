package pl.kaczanowski.algorithm.listener;

import java.io.IOException;
import java.util.TreeSet;

import pl.kaczanowski.model.SchedulingConfiguration;

public interface AlgorithmStepsListener {

	void addBestConfiguration(SchedulingConfiguration bestConfiguration);

	void addCurrentConfiguration(SchedulingConfiguration currentConfiguration);

	void addStepConfigurations(TreeSet<SchedulingConfiguration> configurations);

	void endExecution();

	void saveRaport() throws IOException;

	void startNewExecution();

}
