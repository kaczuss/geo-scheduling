package pl.kaczanowski.analyze;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Ordering;

public final class StatisticsUtils {

	public static Double getMean(final Collection<Integer> values) {

		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}

		BigDecimal sum = BigDecimal.ZERO;
		for (Integer val : values) {
			sum = sum.add(BigDecimal.valueOf(val));
		}

		return sum.divide(BigDecimal.valueOf(values.size())).doubleValue();

	}

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
