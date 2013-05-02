package pl.kaczanowski.analyze;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Ordering;

public final class StatisticsUtils {

	public static Double getMedianValue(final Collection<Integer> values) {

		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}
		List<Integer> sorted = Ordering.natural().sortedCopy(values);
		if (sorted.size() % 2 == 1) {
			return Double.valueOf(sorted.get(sorted.size() / 2));
		} else {
			return (sorted.get(sorted.size() / 2) + sorted.get(sorted.size() / 2 - 1)) / 2.0;
		}

	}

	private StatisticsUtils() {

	}
}
