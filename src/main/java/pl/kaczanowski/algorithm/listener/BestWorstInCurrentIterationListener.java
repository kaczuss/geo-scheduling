package pl.kaczanowski.algorithm.listener;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.kaczanowski.algorithm.helper.ConfigurationHelper;
import pl.kaczanowski.model.SchedulingConfiguration;
import pl.kaczanowski.utils.FileCreateUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * Nalepszy i najgorszy w danej iteracji.
 * @author pawel
 */
public class BestWorstInCurrentIterationListener implements AlgorithmStepsListener {

	private static class CurrentIterationData {

		private int best;
		private int worst;
		private int mean;
		private int choose = Integer.MAX_VALUE;
		private int bestOveral = Integer.MAX_VALUE;

	}

	private static class RunAlgorithmData implements Comparable<RunAlgorithmData> {

		private int best = Integer.MAX_VALUE;

		private int numberToGetBest = 0;

		// ktore uruchomienie algorytmu
		private int algorithmRunNumber = 0;

		private List<CurrentIterationData> data = Lists.newArrayList();

		@Override
		public int compareTo(final RunAlgorithmData o) {
			if (best != o.best) {
				return best - o.best;
			}
			return o.numberToGetBest = o.numberToGetBest;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof RunAlgorithmData)) {
				return false;
			}
			RunAlgorithmData other = (RunAlgorithmData) obj;
			if (algorithmRunNumber != other.algorithmRunNumber) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + algorithmRunNumber;
			return result;
		}

	}

	private final String fileMask;

	private final Set<RunAlgorithmData> iterationsData = Sets.newHashSet();

	// ktore uruchomienie algorytmy
	private int currentRun = 0;

	private RunAlgorithmData currentAlgoritmRunData = null;

	private CurrentIterationData currentIteration = null;

	private final ConfigurationHelper configurationHelper;

	private Joiner CSV_JOINER = Joiner.on(";").skipNulls();

	// który iteracja wewnątrz algorytmu
	private int currentStep = 0;

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

		if (currentIteration != null) {
			currentIteration.bestOveral = bestConfiguration.getExecutionTime();
		}
	}

	@Override
	public void addCurrentConfiguration(final SchedulingConfiguration currentConfiguration) {
		if (currentIteration != null) {
			currentIteration.choose = currentConfiguration.getExecutionTime();

			if (currentAlgoritmRunData.best > currentIteration.choose) {
				currentAlgoritmRunData.best = currentIteration.choose;
				currentAlgoritmRunData.numberToGetBest = currentStep;
			}
		}
	}

	@Override
	public void addStepConfigurations(final TreeSet<SchedulingConfiguration> configurations) {
		currentIteration = new CurrentIterationData();
		currentIteration.best = Iterables.getFirst(configurations, null).getExecutionTime();
		currentIteration.worst = Iterables.getLast(configurations).getExecutionTime();
		currentIteration.mean = configurationHelper.getMean(configurations);
		currentAlgoritmRunData.data.add(currentIteration);

		currentStep++;
	}

	@Override
	public void endExecution() {

		iterationsData.add(currentAlgoritmRunData);
		currentAlgoritmRunData = null;
		currentIteration = null;
		currentStep = 0;

		currentRun++;
	}

	@Override
	public void saveRaport() throws IOException {

		System.out.println("Sporządzam raporty na najlepszy i najgorszy w iteracji");

		DecimalFormat decimalFormat = new DecimalFormat("000");
		for (RunAlgorithmData algorithmRun : Ordering.natural().sortedCopy(iterationsData)) {
			String fileName = MessageFormat.format(fileMask, decimalFormat.format(algorithmRun.algorithmRunNumber));
			PrintWriter pw = FileCreateUtils.getPrintWriterWithPath(fileName);
			pw.println(CSV_JOINER.join("Numer iteracji", "Najlepszy", "Sredni", "Najgorszy", "Wybrany",
					"Ogolnie najlepszy"));
			int i = 0;
			for (CurrentIterationData iteration : algorithmRun.data) {

				// save best, mean, worst

				pw.println(CSV_JOINER.join(i++, iteration.best, iteration.mean, iteration.worst, iteration.choose,
						iteration.bestOveral));
			}

			pw.flush();
			pw.close();

		}

		System.out.println("Zapisałem raporty na najlepszy i najgorszy w iteracji");

	}

	@Override
	public void startNewExecution() {

		checkState(currentAlgoritmRunData == null, "first stop execution!");
		currentStep = 0;

		currentAlgoritmRunData = new RunAlgorithmData();
		currentAlgoritmRunData.algorithmRunNumber = currentRun;
	}

}
