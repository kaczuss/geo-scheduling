package pl.kaczanowski.utils.test;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.Test;

import pl.kaczanowski.utils.StatisticsUtils;

import pl.kaczanowski.utils.StatisticsUtilsTest;
@Test
public class StatisticsUtilsModeTest extends StatisticsUtilsTest {

	public void shouldReturnMoodeValueOnElementsValues() {
		assertThat(StatisticsUtils.getMode(newArrayList(3, 4, 3, 4, 3, 4, 5, 5, 5, 5, 5, 5, 32, 2, 1, 0))).isEqualTo(
				5);
	}

	public void shouldReturnProperModePercentage() {
		assertThat(
				StatisticsUtils.getModePtc(newArrayList(3, 4, 3, 4, 3, 4, 5, 5, 5, 5, 5, 5, 32, 2, 1, 0, 5, 5, 5, 5)))
				.isEqualTo(0.5, delta);
	}

	public void shouldReturnProperModePercentageSameElements() {
		assertThat(StatisticsUtils.getModePtc(newArrayList(3, 4, 3, 4, 5))).isEqualTo(0.4, delta);
	}

	public void shouldReturnSingleElementOnSingleElementList() {
		assertThat(StatisticsUtils.getMode(singleElementList)).isNotNull().isEqualTo(singleElementList.get(0));
	}

	public void shouldReturnSmallerValueOnFewSameValues() {
		assertThat(StatisticsUtils.getMode(newArrayList(3, 4, 3, 4, 3, 4))).isEqualTo(3);
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void shouldThrowNullPointerOnNullList() {
		StatisticsUtils.getMode(null);
	}
}
