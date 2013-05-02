package pl.kaczanowski.analyze.test;

import static org.fest.assertions.Delta.delta;

import java.util.List;

import org.fest.assertions.Delta;

import com.google.common.collect.ImmutableList;

public abstract class StatisticsUtilsTest {

	// nie zmieniac wartosci
	protected final List<Integer> emptyList = ImmutableList.<Integer> of();
	protected final List<Integer> singleElementList = ImmutableList.<Integer> of(3);
	protected final List<Integer> doubleElementList = ImmutableList.<Integer> of(6, 2);
	protected final List<Integer> doubleNotDividedElementList = ImmutableList.<Integer> of(7, 2);
	protected final List<Integer> thirdElementList = ImmutableList.<Integer> of(7, 4, 9);
	protected final List<Integer> fourElementList = ImmutableList.<Integer> of(7, 4, 9, 15);
	protected final Delta delta = delta(0.000001);

}
