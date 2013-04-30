package pl.kaczanowski.analyze;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import pl.kaczanowski.algorithm.AlgorithmModule;
import pl.kaczanowski.algorithm.runner.ParameterUtils;
import pl.kaczanowski.algorithm.runner.Parameters;
import pl.kaczanowski.utils.FileCreateUtils;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Zaczytuje plik CSV zwracany przez IterationsToFoundBestResultListener i zapisuje plik z rozkładem.
 * @author pawel
 */
public class AnalyzeDistribution {

	public static void main(final String[] args) throws IOException {

		Injector injector = Guice.createInjector(new AlgorithmModule(args));

		Map<Parameters, String> readParamters = injector.getInstance(ParameterUtils.class).readParamters(args);

		AnalyzeDistribution ad =
				new AnalyzeDistribution(Integer.valueOf(readParamters.get(Parameters.BEST_RESULT)),
						Integer.valueOf(readParamters.get(Parameters.ALGORITHM_ITERATIONS)));

		PrintWriter out =
				FileCreateUtils.getPrintWriterWithPath(readParamters.get(Parameters.DISTRIBUTION_REPORT_FILE));
		ad.convert(Files.newReaderSupplier(new File(readParamters.get(Parameters.INPUT_FILE)), Charsets.UTF_8), out);
		out.close();
	}

	private final int bestResult;

	private final int iterations;
	private final Splitter splitter = Splitter.on(";");

	private final Joiner joiner = Joiner.on(";");

	/**
	 * @param in
	 * @param outFileName
	 * @param bestResult
	 */
	public AnalyzeDistribution(final int bestResult, final int iterations) {
		this.bestResult = bestResult;
		this.iterations = iterations;
	}

	public void convert(final InputSupplier<InputStreamReader> inputSupplier, final PrintWriter out)
			throws IOException {

		List<String> readLines = CharStreams.readLines(inputSupplier);

		Multiset<Integer> results = readIn(readLines);

		out.println(joiner.join("Liczba iteracji", "Ile razy otrzymano najlepszy w tej iteracji"));
		for (int i = 0; i < iterations; i++) {
			out.println(joiner.join(i, results.count(i)));
		}

	}

	private Multiset<Integer> readIn(final List<String> readLines) {
		Multiset<Integer> results = TreeMultiset.create();
		for (String line : readLines) {
			Iterable<String> split = splitter.split(line);
			Integer executionTime = Integer.valueOf(Iterables.get(split, 2));
			if (executionTime == bestResult) {
				results.add(Integer.valueOf(Iterables.get(split, 1)));
			}
		}
		return results;
	}
}
