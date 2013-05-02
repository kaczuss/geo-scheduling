package pl.kaczanowski.utils.test;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.Test;

import pl.kaczanowski.utils.StatisticsUtils;

import pl.kaczanowski.utils.StatisticsUtilsTest;
@Test
public class StatisticsUtilsMeanTest extends StatisticsUtilsTest {

	public void shouldReturnMeanOfFourElementList() {
		assertThat(StatisticsUtils.getMean(fourElementList)).isEqualTo(8.75, delta);
	}

	public void shouldReturnNullOnEmptyList() {

		assertThat(StatisticsUtils.getMean(emptyList)).isNull();
	}

	public void shouldReturnSingleElementOnOneElementList() {
		assertThat(StatisticsUtils.getMean(singleElementList)).isEqualTo(singleElementList.get(0), delta);
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void shouldThrowNullPointerNoNullList() {

		StatisticsUtils.getMean(null);

	}
}
