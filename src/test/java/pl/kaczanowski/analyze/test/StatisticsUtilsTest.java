package pl.kaczanowski.analyze.test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Delta.delta;

import java.util.List;

import org.fest.assertions.Delta;
import org.testng.annotations.Test;

import pl.kaczanowski.analyze.StatisticsUtils;

import com.google.common.collect.Lists;

@Test
public class StatisticsUtilsTest {

	private List<Integer> emptyList = Lists.newArrayList();

	private List<Integer> singleElementList = Lists.newArrayList(3);

	private List<Integer> doubleElementList = Lists.newArrayList(6, 2);
	private List<Integer> doubleNotDividedElementList = Lists.newArrayList(7, 2);

	private List<Integer> thirdElementList = Lists.newArrayList(7, 4, 9);
	private List<Integer> fourElementList = Lists.newArrayList(7, 4, 9, 15);
	Delta delta = delta(0.000001);

	public void shouldReturnElementOnFirstPositionOnSortedThirdElementList() {
		assertThat(StatisticsUtils.getMedianValue(thirdElementList)).isNotNull().isEqualTo(7, delta);
	}

	public void shouldReturnHalfOfBothElementsInTwoElementList() {
		assertThat(StatisticsUtils.getMedianValue(doubleElementList)).isNotNull().isEqualTo(4, delta);
	}

	public void shouldReturnHalfOfBothNotDividableElementsInTwoElementList() {
		assertThat(StatisticsUtils.getMedianValue(doubleNotDividedElementList)).isNotNull().isEqualTo(4.5, delta);
	}

	public void shouldReturnMeanFromToElementsOnPositionOneAndTwoInFourElementList() {
		assertThat(StatisticsUtils.getMedianValue(fourElementList)).isNotNull().isEqualTo(8, delta);
	}

	public void shouldReturnNullOnEmptyList() {
		assertThat(StatisticsUtils.getMedianValue(emptyList)).isNull();
	}

	public void shouldReturnSingleElementOnSingleElementList() {
		assertThat(StatisticsUtils.getMedianValue(singleElementList)).isNotNull().isEqualTo(
				Double.valueOf(singleElementList.get(0)), delta);
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void shouldThrowNullPointerOnNull() {
		StatisticsUtils.getMedianValue(null);
	}

}
