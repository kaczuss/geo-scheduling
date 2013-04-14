package pl.kaczanowski.algorithm.listener;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.TreeSet;

import pl.kaczanowski.algorithm.helper.ConfigurationHelper;
import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class BestWorstInCurrentIterationListener implements AlgorithmStepsListener {

	private static class CurrentIterationData {

		private int best;
		private int worst;
		private int mean;

	}

	private final String fileMask;

	private final Multimap<Integer, CurrentIterationData> iterationsData = ArrayListMultimap.create();

	// ktore uruchomienie algorytmy
	private int currentRun = 0;

	private final ConfigurationHelper configurationHelper;

	private Joiner CSV_JOINER = Joiner.on(";").skipNulls();

	/**
	 * @param fileMask
	 */
	public BestWorstInCurrentIterationListener(final String fileMask, final ConfigurationHelper configurationHelper) {
		this.configurationHelper = configurationHelper;
		checkArgument(!Strings.isNullOrEmpty(fileMask), "File mask cannot be empty");
		checkArgument(fileMask.contains("{0}"), "Mask not set properly!, Use {0} as run number in file name!");

		this.fileMask = fileMask;
	}

	@Override
	public void addBestConfiguration(final SchedulingConfiguration bestConfiguration) {
		// Do noting
	}

	@Override
	public void addCurrentConfiguration(final SchedulingConfiguration currentConfiguration) {
		// Do nothing
	}

	@Override
	public void addStepConfigurations(final TreeSet<SchedulingConfiguration> configurations) {
		CurrentIterationData iterationData = new CurrentIterationData();
		iterationData.best = Iterables.getFirst(configurations, null).getExecutionTime();
		iterationData.worst = Iterables.getLast(configurations).getExecutionTime();
		iterationData.mean = configurationHelper.getMean(configurations);

		iterationsData.put(currentRun, iterationData);

	}

	@Override
	public void endExecution() {
		currentRun++;
	}

	@Override
	public void saveRaport() throws IOException {
		for (Entry<Integer, Collection<CurrentIterationData>> iterationDataList : iterationsData.asMap().entrySet()) {
			PrintWriter pw =
					new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(MessageFormat.format(
							fileMask, iterationDataList.getKey())))));
			int i = 0;
			for (CurrentIterationData iteration : iterationDataList.getValue()) {

				// save best, mean, worst

				pw.println(CSV_JOINER.join(i++, iteration.worst, iteration.mean, iteration.best));
			}

			pw.flush();
			pw.close();
		}

	}

	@Override
	public void startNewExecution() {
		// Do nothing
	}

}
