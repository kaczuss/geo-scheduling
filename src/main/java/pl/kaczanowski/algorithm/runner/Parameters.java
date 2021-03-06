package pl.kaczanowski.algorithm.runner;

import java.text.MessageFormat;

public enum Parameters {

	INPUT_FILE("-f"),
	PROBABILITY("-prob"),
	ALGORITHM_ITERATIONS("-i"),
	RUN_ITERATIONS("-ri"),
	BEST_RESULT("-best"),
	ACHIEVEMENT_REPORT_FILE("-achievement"),
	ITERATIONS_TO_BEST_REPORT_FILE("-iterToBest"),
	ITERATIONS_BEST_MEAN_WORST_REPORT_FILE("-bestMeanWorst"),
	BEST_WORST_CURRENT_REPORT_FILE("-currentBestWorst"),
	DISTRIBUTION_REPORT_FILE("-distribution"),
	STATISTIC_DATA_DIR("-statDir"),
	STATISTIC_NOT_FILTERED_FILE_NAME("-notFilteredStats"),
	STATISTIC_WITHOUT_ZEROS_FILE_NAME("-withoutZerosStats"),
	STATISTIC_WITHOUT_ZEROS_AND_NOT_FOUNDED_FILE_NAME("-withoutZerosAndNotFundedStats"),
	STATISTIC_WITHOUT_NOT_FOUNDED_FILE_NAME("-withoutNotFundedStats"),
	GRAPHVIZ_FILE("-graphviz");

	public static Parameters getByPrefix(final String key) {
		for (Parameters param : values()) {
			if (param.prefix.equals(key)) {
				return param;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("The paramter {0} isn''t defined!", key));
	}

	private final String prefix;

	/**
	 * @param prefix
	 */
	private Parameters(final String prefix) {
		this.prefix = prefix;
	}

}
