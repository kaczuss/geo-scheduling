package pl.kaczanowski.utils.test;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.kaczanowski.utils.MathUtils;

@Test
public class MathUtilsTest {

	@DataProvider
	public Object[][] getByteArrayToStringData() {

		return new Object[][] { {new byte[] {1, 1, 0 }, 6

		}, {new byte[] {1, 1, 1 }, 7

		}, {new byte[] {0, 0, 0 }, 0

		}, {new byte[] {1 }, 1

		}

		};

	}

	@Test(dataProvider = "getByteArrayToStringData")
	public void shouldReturnProperInteger(final byte[] array, final int expectedValue) {

		int result = MathUtils.getProcNumber(array);

		assertThat(result).isEqualTo(expectedValue);

	}

}
