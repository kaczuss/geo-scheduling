package pl.kaczanowski.algorithm.listener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.TreeSet;

import pl.kaczanowski.algorithm.helper.ConfigurationHelper;
import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class BestMeanWorstIterationListener implements AlgorithmStepsListener {

	private static final Joiner CSV_JOINER = Joiner.on(";");

	private final Multimap<Integer, SchedulingConfiguration> itertationConfigurations = TreeMultimap.create();

	private int currentIteration = -1;

	private final String fileName;

	private final ConfigurationHelper configurationHelper;

	/**
	 * @param fileName
	 */
	public BestMeanWorstIterationListener(final String fileName, final ConfigurationHelper configurationHelper) {
		this.fileName = fileName;
		this.configurationHelper = configurationHelper;
	}

	@Override
	public void addBestConfiguration(final SchedulingConfiguration bestConfiguration) {
		// DO noting
	}

	@Override
	public void addCurrentConfiguration(final SchedulingConfiguration currentConfiguration) {
		itertationConfigurations.put(currentIteration, currentConfiguration);
		currentIteration++;

	}

	@Override
	public void addStepConfigurations(final TreeSet<SchedulingConfiguration> configurations) {
		// DO nothing
	}

	@Override
	public void endExecution() {
		currentIteration = -1;
	}

	@Override
	public void saveRaport() throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(fileName))));

		for (Entry<Integer, Collection<SchedulingConfiguration>> iteration : itertationConfigurations.asMap()
				.entrySet()) {

			// save best, mean, worst

			pw.println(CSV_JOINER.join(iteration.getKey(), Iterables.getFirst(iteration.getValue(), null)
					.getExecutionTime(), configurationHelper.getMean(iteration.getValue()),
					Iterables.getLast(iteration.getValue(), null).getExecutionTime()));
		}

		pw.flush();
		pw.close();

	}

	@Override
	public void startNewExecution() {
		currentIteration = 0;
	}

}
