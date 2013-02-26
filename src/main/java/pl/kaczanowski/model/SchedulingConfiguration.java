package pl.kaczanowski.model;

import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import pl.kaczanowski.algorithm.SchedulingAlgorithm;
import pl.kaczanowski.utils.MathUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Configuration of scheduling.
 * @author kaczanowskip
 */
public class SchedulingConfiguration implements Comparable<SchedulingConfiguration> {

	public static SchedulingConfiguration create(final byte[] bytes, final int bitsForProcessor,
			final SchedulingAlgorithm schedulingAlgorithm) {

		SchedulingConfiguration configuration = new SchedulingConfiguration(bytes, bitsForProcessor);
		configuration.executionTime = schedulingAlgorithm.getExecutionTime(configuration.getProcessorsPartial());
		return configuration;
	}

	public static SchedulingConfiguration createRandomConfiguration(final int tasksNumber,
			final int processorsNumber, final SchedulingAlgorithm schedulingAlgorithm) {

		int bitsForProcessor = getBitsForProcessor(processorsNumber);
		int bitsCount = tasksNumber * bitsForProcessor;
		byte[] bytes = new byte[bitsCount];
		Random rand = new Random();
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = (byte) rand.nextInt(2);
		}

		SchedulingConfiguration configuration = new SchedulingConfiguration(bytes, bitsForProcessor);
		configuration.executionTime = schedulingAlgorithm.getExecutionTime(configuration.getProcessorsPartial());

		return configuration;

	}

	private static int getBitsForProcessor(final int processorsNumber) {
		// FIXME now it's fast solution, i can handle only processor number 2 ^ n and n <= 6
		for (int i = 1; i <= 6; ++i) {
			int n = 1 << i;
			if (processorsNumber == n) {
				return i;
			}
		}
		throw new IllegalArgumentException("use ony processor number equals to 2 ^ n where n <= 6 ");

	}

	private final byte[] bits;

	private final int bitesForProcessor;

	private int evolutionBit = 0;

	private int executionTime;

	private SchedulingConfiguration(final byte[] bits, final int bitesForProcessor) {
		super();
		this.bits = bits;
		this.bitesForProcessor = bitesForProcessor;
	}

	@Override
	public int compareTo(final SchedulingConfiguration o) {
		return executionTime - o.executionTime;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SchedulingConfiguration other = (SchedulingConfiguration) obj;
		if (bitesForProcessor != other.bitesForProcessor) {
			return false;
		}
		if (!Arrays.equals(bits, other.bits)) {
			return false;
		}
		return true;
	}

	public SchedulingConfiguration evaluate(final SchedulingAlgorithm schedulingAlgorithm) {

		checkState(hasNextEvolution(), "Cannot evaluate more");

		byte[] copy = Arrays.copyOf(bits, bits.length);
		copy[evolutionBit] = (byte) (bits[evolutionBit] == 0 ? 1 : 0);

		SchedulingConfiguration configuration = new SchedulingConfiguration(copy, this.bitesForProcessor);
		configuration.executionTime = schedulingAlgorithm.getExecutionTime(configuration.getProcessorsPartial());

		evolutionBit++;
		return configuration;

	}

	public int getExecutionTime() {
		return executionTime;
	}

	public Map<Integer, Set<Integer>> getProcessorsPartial() {

		Map<Integer, Set<Integer>> partial = Maps.newHashMap();
		int processors = 1 << bitesForProcessor;
		for (int i = 0; i < processors; ++i) {
			partial.put(i, Sets.<Integer> newHashSet());
		}
		int tasks = bits.length / bitesForProcessor;
		for (int task = 0; task < tasks; ++task) {

			byte[] procByteArrayNumber =
					Arrays.copyOfRange(bits, task * bitesForProcessor, (task + 1) * bitesForProcessor);
			partial.get(MathUtils.getProcNumber(procByteArrayNumber)).add(task);
		}

		return partial;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bitesForProcessor;
		result = prime * result + Arrays.hashCode(bits);
		return result;
	}

	public boolean hasNextEvolution() {
		return evolutionBit < bits.length;
	}

	public void resetEvolution() {
		evolutionBit = 0;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SchedulingConfiguration [bits=").append(Arrays.toString(bits)).append(", bitesForProcessor=")
				.append(bitesForProcessor).append(", evolutionBit=").append(evolutionBit).append(", executionTime=")
				.append(executionTime).append("]");
		return builder.toString();
	}

}
