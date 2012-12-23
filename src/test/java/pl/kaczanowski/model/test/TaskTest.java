package pl.kaczanowski.model.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.kaczanowski.model.Task;

public class TaskTest {

	@DataProvider
	private Object[][] getInvalidTicks() {
		return new Object[][]{{-1}, {0}};
	}

	@Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "getInvalidTicks")
	public void testTask(final int ticks) {
		new Task(1, ticks);

	}

}
