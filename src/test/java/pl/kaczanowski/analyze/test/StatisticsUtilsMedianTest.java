package pl.kaczanowski.analyze.test;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.Test;

import pl.kaczanowski.analyze.StatisticsUtils;

@Test
public class StatisticsUtilsMedianTest extends StatisticsUtilsTest {

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
