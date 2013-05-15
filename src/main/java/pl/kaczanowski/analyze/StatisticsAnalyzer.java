package pl.kaczanowski.analyze;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static com.google.common.collect.Sets.newTreeSet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.math.NumberUtils;

import pl.kaczanowski.algorithm.AlgorithmModule;
import pl.kaczanowski.algorithm.runner.ParameterUtils;
import pl.kaczanowski.algorithm.runner.Parameters;
import pl.kaczanowski.analyze.IterationStatData.Builder;
import pl.kaczanowski.utils.FileCreateUtils;
import pl.kaczanowski.utils.StatisticsUtils;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class StatisticsAnalyzer {

	private static class IterationData {
		private int executionTime;
		private int iterationsNumber;
	}

	private static final class NotFoundFilter implements Predicate<StatisticsAnalyzer.IterationData> {

		private final int bestResult;

		/**
		 * @param bestResult
		 */
		public NotFoundFilter(final int bestResult) {
			this.bestResult = bestResult;
		}

		@Override
		public boolean apply(@Nullable final IterationData input) {
			return input == null ? false : input.executionTime == bestResult;
		}
	}

	private static final class ZerosFilter implements Predicate<StatisticsAnalyzer.IterationData> {

		@Override
		public boolean apply(@Nullable final IterationData input) {
			return input == null ? false : input.iterationsNumber != 0;
		}
	}

	private static final DecimalFormat df = new DecimalFormat("###0.000");

	private static final AbstractFileFilter DIR_NAMES_FILTER = new AbstractFileFilter() {

		@Override
		public boolean accept(final File dir, final String name) {
			if (!name.contains("_")) {
				return false;
			}
			String[] parts = name.split("_");
			if (parts.length != 2) {
				return false;
			}
			return NumberUtils.isDigits(parts[0]) && NumberUtils.isDigits(parts[1]);
		}

	};;

	public static IterationStatData analyze(final BigDecimal probabilityParameter, final List<Integer> data,
			final Double bestEvaulatedPerventage, final List<IterationData> iterationsData) {

		Builder builder = IterationStatData.build(probabilityParameter);
		builder.setBestEvaluatedProcentage(bestEvaulatedPerventage);
		builder.setMean(StatisticsUtils.getMean(data)).setVariance(StatisticsUtils.getVariance(data));

		builder.setFirstQuartile(StatisticsUtils.getFirstQuartileValue(data))
				.setMedian(StatisticsUtils.getMedianValue(data))
				.setThirdQuartile(StatisticsUtils.getThirdQuartileValue(data));

		builder.setMax(StatisticsUtils.getMax(data)).setMin(StatisticsUtils.getMin(data));

		builder.setMode(StatisticsUtils.getMode(data)).setModeProcentage(StatisticsUtils.getModePtc(data));

		List<Integer> executionTimes = newArrayListWithCapacity(iterationsData.size());
		for (IterationData id : iterationsData) {
			executionTimes.add(id.executionTime);
		}
		builder.setExecutionTimeMean(StatisticsUtils.getMean(executionTimes));
		builder.setBestResult(StatisticsUtils.getMin(executionTimes));

		return builder.build();
	}

	public static IterationStatData analyze(final BigDecimal probabilityParameter, final List<Integer> data,
			final List<IterationData> iterationsData) {

		return analyze(probabilityParameter, data, null, iterationsData);
	}

	private static List<Integer> asIterationsList(final List<IterationData> value) {
		List<Integer> result = newArrayListWithExpectedSize(value.size());

		for (IterationData d : value) {
			result.add(d.iterationsNumber);

		}
		return result;
	}

	private static Object format(final BigDecimal value) {
		return value == null ? "null" : value;
	}

	private static String format(final Double value) {
		return value == null ? "null" : df.format(value);
	}

	private static Object format(final Integer value) {
		return value == null ? "null" : value;
	}

	private static int getBestCount(final List<IterationData> value, final Integer bestResult) {
		int i = 0;
		for (IterationData data : value) {
			if (bestResult.equals(data.executionTime)) {
				i++;
			}
		}
		return i;
	}

	public static void main(final String[] args) throws IOException {

		System.out.println("Analizy rozpoczeto!");
		Injector injector = Guice.createInjector(new AlgorithmModule(args));

		Map<Parameters, String> readParamters = injector.getInstance(ParameterUtils.class).readParamters(args);

		Integer bestResult = Integer.valueOf(readParamters.get(Parameters.BEST_RESULT));

		String dataDirectory = readParamters.get(Parameters.STATISTIC_DATA_DIR);

		Map<Double, File> probabilityFiles = readFiles(dataDirectory);
		Map<Double, List<IterationData>> baseData = Maps.newHashMap();
		for (Entry<Double, File> entry : probabilityFiles.entrySet()) {
			List<IterationData> iterationData = readFile(entry.getValue());
			baseData.put(entry.getKey(), iterationData);
		}
		saveNotFiltered(bestResult, baseData, readParamters.get(Parameters.STATISTIC_NOT_FILTERED_FILE_NAME));
		saveWithoutZeros(bestResult, baseData, readParamters.get(Parameters.STATISTIC_WITHOUT_ZEROS_FILE_NAME));
		saveWithoutZerosAndNotFounded(bestResult, baseData,
				readParamters.get(Parameters.STATISTIC_WITHOUT_ZEROS_AND_NOT_FOUNDED_FILE_NAME));
		saveWithoutNotFounded(bestResult, baseData,
				readParamters.get(Parameters.STATISTIC_WITHOUT_NOT_FOUNDED_FILE_NAME));

		System.out.println("Analizy zakonczono!");

	}

	private static List<IterationData> readFile(final File value) throws IOException {

		List<String> lines = Files.readLines(value, Charsets.UTF_8);
		if (lines.isEmpty()) {
			return Lists.newArrayList();
		}
		Splitter splitter = Splitter.on(";");
		// pominiecie naglowka
		lines = lines.subList(1, lines.size());
		List<IterationData> results = newArrayListWithExpectedSize(lines.size());
		for (String string : lines) {
			Iterator<String> splited = splitter.split(string).iterator();
			IterationData data = new IterationData();

			// ignoruje iteracje
			splited.next();
			data.iterationsNumber = Integer.valueOf(splited.next());
			data.executionTime = Integer.valueOf(splited.next());
			results.add(data);
		}
		return results;
	}

	public static Map<Double, File> readFiles(final String dataDirectory) {

		IOFileFilter nameFileFilter = FileFilterUtils.nameFileFilter("iterations_to_best.csv");
		Collection<File> dirs =
				FileUtils.listFiles(new File(dataDirectory), nameFileFilter,
						FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), DIR_NAMES_FILTER));
		Map<Double, File> result = Maps.newHashMap();
		for (File file : dirs) {
			String dirName = file.getParentFile().getName();
			String probString = dirName.replace("_", ".");
			result.put(Double.valueOf(probString), file);
		}

		return result;
	}

	private static void saveNotFiltered(final Integer bestResult, final Map<Double, List<IterationData>> baseData,
			final String fileName) throws IOException {
		if (!Strings.isNullOrEmpty(fileName)) {

			Joiner joiner = Joiner.on(";");

			PrintWriter pw = FileCreateUtils.getPrintWriterWithPath(fileName);
			pw.println(joiner.join("parametr prawdopodobienstwa", "srednia", "wariancja", "min", "dolny kwartyl",
					"mediana", "gorny kwartyl", "max", "moda", "moda procent", "procent poprawnych", "sredni czas",
					"najlepszy wynik"));
			Set<IterationStatData> stats = newTreeSet();
			for (Entry<Double, List<IterationData>> entry : baseData.entrySet()) {
				int bestCount = getBestCount(entry.getValue(), bestResult);

				stats.add(analyze(BigDecimal.valueOf(entry.getKey()), asIterationsList(entry.getValue()), bestCount
						* 100 / (double) entry.getValue().size(), entry.getValue()));
			}
			for (IterationStatData stat : stats) {
				pw.println(joiner.join(format(stat.getProbabilityParameter()), format(stat.getMean()),
						format(stat.getVariance()), format(stat.getMin()), format(stat.getFirstQuartile()),
						format(stat.getMedian()), format(stat.getThirdQuartile()), format(stat.getMax()),
						format(stat.getMode()), format(stat.getModeProcentage()),
						format(stat.getBestEvaluatedProcentage()), format(stat.getExecutionTimeMean()),
						format(stat.getBestResult())));
			}
			pw.close();

		}

	}

	private static void saveWithoutNotFounded(final Integer bestResult,
			final Map<Double, List<IterationData>> baseData, final String fileName) throws IOException {
		if (!Strings.isNullOrEmpty(fileName)) {

			Map<Double, List<IterationData>> filtered = Maps.newHashMap();
			for (Entry<Double, List<IterationData>> entry : baseData.entrySet()) {
				List<IterationData> filteredList =
						newArrayList(Iterables.filter(entry.getValue(), new NotFoundFilter(bestResult)));
				filtered.put(entry.getKey(), filteredList);
			}

			saveNotFiltered(bestResult, filtered, fileName);
		}

	}

	private static void saveWithoutZeros(final Integer bestResult, final Map<Double, List<IterationData>> baseData,
			final String fileName) throws IOException {
		if (!Strings.isNullOrEmpty(fileName)) {

			Map<Double, List<IterationData>> filtered = Maps.newHashMap();
			for (Entry<Double, List<IterationData>> entry : baseData.entrySet()) {
				List<IterationData> filteredList =
						newArrayList(Iterables.filter(entry.getValue(), new ZerosFilter()));
				filtered.put(entry.getKey(), filteredList);
			}

			saveNotFiltered(bestResult, filtered, fileName);
		}

	}

	private static void saveWithoutZerosAndNotFounded(final Integer bestResult,
			final Map<Double, List<IterationData>> baseData, final String fileName) throws IOException {
		if (!Strings.isNullOrEmpty(fileName)) {

			Map<Double, List<IterationData>> filtered = Maps.newHashMap();
			for (Entry<Double, List<IterationData>> entry : baseData.entrySet()) {
				List<IterationData> filteredList =
						newArrayList(Iterables.filter(entry.getValue(),
								Predicates.and(new ZerosFilter(), new NotFoundFilter(bestResult))));
				filtered.put(entry.getKey(), filteredList);
			}

			saveNotFiltered(bestResult, filtered, fileName);
		}

	}

}
