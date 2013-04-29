package pl.kaczanowski.algorithm.listener;

import java.io.IOException;
import java.util.TreeSet;

import pl.kaczanowski.model.SchedulingConfiguration;

public interface AlgorithmStepsListener {

	/**
	 * Najlepsza konfiugracja po n-tym kroku, czyli najlepsza odkryta do tej pory.
	 * @param bestConfiguration
	 */
	void addBestConfiguration(SchedulingConfiguration bestConfiguration);

	void addCurrentConfiguration(SchedulingConfiguration currentConfiguration);

	void addStepConfigurations(TreeSet<SchedulingConfiguration> configurations);

	void endExecution();

	void saveRaport() throws IOException;

	void startNewExecution();

}
