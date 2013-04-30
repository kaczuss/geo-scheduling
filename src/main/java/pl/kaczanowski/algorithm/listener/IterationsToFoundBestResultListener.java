package pl.kaczanowski.algorithm.listener;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import pl.kaczanowski.model.SchedulingConfiguration;
import pl.kaczanowski.utils.FileCreateUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * Zapisuje wynik w postaci:
 * numer testu;liczba iteracji do osiągnięcia najlepszego (lub numer ostatniej iteracji);wynik (najlepszy lub
 * znaleziony)
 * @author pawel
 */
public class IterationsToFoundBestResultListener implements AlgorithmStepsListener {

	private class Execution {

		SchedulingConfiguration best;

		int iterations = -1;

		boolean foundBest = false;
	}

	private final int bestTime;

	private final String fileName;

	private final List<Execution> executions = Lists.newArrayList();

	private Execution actual;

	private static final Joiner CSV_JOINER = Joiner.on(";");

	/**
	 * @param bestTime
	 * @param fileName
	 */
	public IterationsToFoundBestResultListener(final String fileName, final int bestTime) {
		checkNotNull(fileName);
		this.bestTime = bestTime;
		this.fileName = fileName;
	}

	@Override
	public void addBestConfiguration(final SchedulingConfiguration bestConfiguration) {
		if (!actual.foundBest) {
			actual.iterations++;
			if (actual.best == null) {
				actual.best = bestConfiguration;
				if (actual.best.getExecutionTime() == bestTime) {
					actual.foundBest = true;
				}
			} else if (bestConfiguration.getExecutionTime() < actual.best.getExecutionTime()) {
				actual.best = bestConfiguration;
				if (actual.best.getExecutionTime() == bestTime) {
					actual.foundBest = true;
				}
			}
		}
	}

	@Override
	public void addCurrentConfiguration(final SchedulingConfiguration currentConfiguration) {
		// ignore

	}

	@Override
	public void addStepConfigurations(final List<SchedulingConfiguration> configurations) {
		// ignore

	}

	@Override
	public void endExecution() {
		checkState(actual != null, "start execution first!");
		executions.add(actual);
		actual = null;
	}

	@Override
	public void saveRaport() throws IOException {
		PrintWriter pw = FileCreateUtils.getPrintWriterWithPath(fileName);

		pw.println(CSV_JOINER.join("Numer iteracji", "Liczba iteracji", "Wynik"));
		for (int i = 0; i < executions.size(); i++) {
			Execution execution = executions.get(i);
			pw.println(CSV_JOINER.join(i, execution.iterations, execution.best.getExecutionTime()));

		}

		pw.flush();
		pw.close();
	}

	@Override
	public void startNewExecution() {
		checkState(actual == null, "stop execution first!");
		actual = new Execution();
	}

}
