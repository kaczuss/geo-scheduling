package pl.kaczanowski.algorithm.listener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import pl.kaczanowski.model.SchedulingConfiguration;
import pl.kaczanowski.utils.FileCreateUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class DistributionNumberToBest implements AlgorithmStepsListener {

	private final String fileName;

	private final int bestNumber;

	private final Multiset<Integer> numberOfIterations = TreeMultiset.create();

	private int iterationsToGetBest = -1;

	// liczba iteracji uruchamianch w algorytmie
	private final int iterationsCount;

	private boolean notFoundBest = false;

	/**
	 * @param fileName
	 * @param bestNumber
	 * @param iterationsCount
	 */
	public DistributionNumberToBest(final String fileName, final int bestNumber, final int iterationsCount) {
		this.fileName = fileName;
		this.bestNumber = bestNumber;
		this.iterationsCount = iterationsCount;
	}

	@Override
	public void addBestConfiguration(final SchedulingConfiguration bestConfiguration) {
		if (notFoundBest) {
			iterationsToGetBest++;
			if (bestConfiguration.getExecutionTime() == bestNumber) {
				numberOfIterations.add(iterationsToGetBest);
				notFoundBest = false;
			}
		}
	}

	@Override
	public void addCurrentConfiguration(final SchedulingConfiguration currentConfiguration) {
		// do nothing

	}

	@Override
	public void addStepConfigurations(final List<SchedulingConfiguration> configurations) {
		// do nothing
	}

	@Override
	public void endExecution() {
		notFoundBest = false;
	}

	@Override
	public void saveRaport() throws IOException {

		System.out.println("Sporządzam raport na rozklad ilosci potrzebnych iteracji");

		PrintWriter pw = FileCreateUtils.getPrintWriterWithPath(fileName);

		Joiner joiner = Joiner.on(";");

		pw.println(joiner.join("Liczba iteracji", "Ile razy otrzymano najlepszy w tej iteracji"));

		for (int i = 0; i < iterationsCount; i++) {
			pw.println(joiner.join(i, numberOfIterations.count(i)));
		}

		pw.flush();
		pw.close();
		System.out.println("Zapisałem raport na rozklad ilosci potrzebnych iteracji");

	}

	@Override
	public void startNewExecution() {
		notFoundBest = true;
		iterationsToGetBest = -1;
	}

}
