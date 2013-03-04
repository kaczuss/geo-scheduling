package pl.kaczanowski.algorithm.listener;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.TreeSet;

import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * Zapisuje kiedy osiągnięto najlepszy wynik i czy osiągnięto.
 * @author pawel
 */
public class BestIterationAchievementResultListener implements AlgorithmStepsListener {

	private final static class Execution {

		private int iteration = 0;

		private boolean foundBest = false;

	}

	private static final Joiner CSV_JOINER = Joiner.on(";");

	private Execution current;

	private List<Execution> executions = Lists.newArrayList();
	private final String fileName;

	private int bestResult;

	public BestIterationAchievementResultListener(final String fileName, final int bestResult) {
		this.fileName = fileName;
		this.bestResult = bestResult;
	}

	@Override
	public void addBestConfiguration(final SchedulingConfiguration bestConfiguration) {
		checkNotNull(bestConfiguration, "best configurations cannot be null");
		checkNotNull(current, "call start method first!");

		if (!current.foundBest) {
			current.iteration++;
			current.foundBest = bestConfiguration.getExecutionTime() <= bestResult;
		}
	}

	@Override
	public void addCurrentConfiguration(final SchedulingConfiguration currentConfiguration) {
		// ignore
	}

	@Override
	public void addStepConfigurations(final TreeSet<SchedulingConfiguration> configurations) {
		// ignore
	}

	@Override
	public void endExecution() {
		checkNotNull(current, "call start method first!");
		executions.add(current);
		current = null;
	}

	@Override
	public void saveRaport() throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(fileName))));

		for (int i = 0; i < executions.size(); i++) {
			Execution execution = executions.get(i);
			pw.println(CSV_JOINER.join(i, execution.iteration, execution.foundBest));

		}

		pw.flush();
		pw.close();

	}

	@Override
	public void startNewExecution() {
		checkState(current == null, "first end current execution!");
		current = new Execution();
	}

}
