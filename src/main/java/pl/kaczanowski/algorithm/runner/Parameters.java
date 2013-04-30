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
	DISTRIBUTION_REPORT_FILE("-distribution");

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
