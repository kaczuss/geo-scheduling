package pl.kaczanowski.algorithm.listener;

import java.io.IOException;
import java.util.Collection;
import java.util.TreeSet;

import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.common.collect.ImmutableList;

public class AlgorithmStepsListenerContainer implements AlgorithmStepsListener {

	private final Collection<AlgorithmStepsListener> listeners;

	/**
	 * @param listeners
	 */
	public AlgorithmStepsListenerContainer(final Collection<AlgorithmStepsListener> listeners) {
		if (listeners == null) {
			this.listeners = ImmutableList.of();
		} else {
			this.listeners = ImmutableList.copyOf(listeners);
		}
	}

	@Override
	public void addBestConfiguration(final SchedulingConfiguration bestConfiguration) {

		for (AlgorithmStepsListener listener : listeners) {
			listener.addBestConfiguration(bestConfiguration);
		}
	}

	@Override
	public void addCurrentConfiguration(final SchedulingConfiguration currentConfiguration) {
		for (AlgorithmStepsListener listener : listeners) {
			listener.addCurrentConfiguration(currentConfiguration);
		}
	}

	@Override
	public void addStepConfigurations(final TreeSet<SchedulingConfiguration> configurations) {
		for (AlgorithmStepsListener listener : listeners) {
			listener.addStepConfigurations(configurations);

		}
	}

	@Override
	public void endExecution() {
		for (AlgorithmStepsListener listener : listeners) {
			listener.endExecution();

		}
	}

	@Override
	public void saveRaport() throws IOException {
		for (AlgorithmStepsListener listener : listeners) {
			listener.saveRaport();
		}
	}

	@Override
	public void startNewExecution() {
		for (AlgorithmStepsListener listener : listeners) {
			listener.startNewExecution();

		}
	}

}
