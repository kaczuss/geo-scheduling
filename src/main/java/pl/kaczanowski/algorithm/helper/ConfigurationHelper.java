package pl.kaczanowski.algorithm.helper;

import java.util.Collection;

import pl.kaczanowski.model.SchedulingConfiguration;

public class ConfigurationHelper {

	public int getMean(final Collection<SchedulingConfiguration> executions) {

		int sum = 0;
		for (SchedulingConfiguration execution : executions) {
			sum += execution.getExecutionTime();
		}

		return Math.round(sum / (float) executions.size());
	}

}
