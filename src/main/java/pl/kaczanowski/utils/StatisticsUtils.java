package pl.kaczanowski.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.StatUtils;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public final class StatisticsUtils {

	private static final Comparator<Entry<? super Long, Long>> FRAQUANCE_COMPARATOR =
			new Comparator<Entry<? super Long, Long>>() {

				@Override
				public int compare(final Entry<? super Long, Long> o1, final Entry<? super Long, Long> o2) {
					int valuesCompare = Longs.compare(o1.getValue(), o2.getValue());
					if (valuesCompare == 0) {
						// zamieniona kolejnosc, wazniejsze sa te, ktore maja mniejsza wartosc
						return Longs.compare((Long) o2.getKey(), (Long) o1.getKey());
					}
					return valuesCompare;
				}
			};

	public static Double getFirstQuartileValue(final Collection<Integer> values) {

		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}
		double[] doubleArray = toDoubleArray(values);

		return StatUtils.percentile(doubleArray, 25);

	}

	private static Frequency getFrequance(final Collection<Integer> values) {
		Frequency f = new Frequency();
		for (Integer val : values) {
			f.addValue(val);
		}
		return f;
	}

	public static Integer getMax(final Collection<Integer> values) {
		return Ints.max(Ints.toArray(values));
	}

	public static Double getMean(final Collection<Integer> values) {

		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}
		double[] doubleArray = toDoubleArray(values);
		return StatUtils.mean(doubleArray);

	}

	public static Double getMedianValue(final Collection<Integer> values) {

		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}
		double[] doubleArray = toDoubleArray(values);

		return StatUtils.percentile(doubleArray, 50);

	}

	public static Integer getMin(final Collection<Integer> values) {
		return Ints.min(Ints.toArray(values));
	}

	/**
	 * Dominanta (moda).
	 * @return
	 */
	public static Integer getMode(final Collection<Integer> values) {
		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}
		Frequency f = getFrequance(values);

		return getMode(f);

	}

	private static int getMode(final Frequency f) {
		return ((Long) Ordering.from(FRAQUANCE_COMPARATOR).max(f.entrySetIterator()).getKey()).intValue();
	}

	public static Double getModePtc(final Collection<Integer> values) {
		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}
		Frequency f = getFrequance(values);
		int mode = getMode(f);
		return f.getPct(mode) * 100;
	}

	public static Double getThirdQuartileValue(final Collection<Integer> values) {

		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}
		double[] doubleArray = toDoubleArray(values);

		return StatUtils.percentile(doubleArray, 75);
	}

	public static Double getVariance(final Collection<Integer> values) {
		checkNotNull(values, "List cannot be null");

		if (values.isEmpty()) {
			return null;
		}
		double[] doubleArray = toDoubleArray(values);
		return StatUtils.variance(doubleArray);
	}

	private static double[] toDoubleArray(final Collection<Integer> values) {
		return Doubles.toArray(values);
	}

	private StatisticsUtils() {

	}
}
