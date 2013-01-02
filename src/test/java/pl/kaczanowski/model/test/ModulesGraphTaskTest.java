package pl.kaczanowski.model.test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.kaczanowski.model.ModulesGraph.Task;

import com.google.common.base.Predicate;

@Test
public class ModulesGraphTaskTest {

	private Task taskStub;
	private Predicate<? super Task> endFunctionSut;
	private Predicate<? super Task> nonEndFunctionSut;

	@BeforeMethod
	public void setUp() {

		taskStub = mock(Task.class);

		endFunctionSut = Task.isEndedFn();

		nonEndFunctionSut = Task.isNotEndedFn();
	}

	public void shouldReturnTrueOnTaskEndFunction() {

		when(taskStub.isEnded()).thenReturn(true);

		assertThat(endFunctionSut.apply(taskStub)).isTrue();

	}

	public void shouldReturnFalseOnTaskEndTunction() {

		when(taskStub.isEnded()).thenReturn(false);

		assertThat(endFunctionSut.apply(taskStub)).isFalse();

	}

	public void shouldReturnTrueOnNullTaskEndFunction() {

		assertThat(endFunctionSut.apply(null)).isTrue();

	}

	public void shouldReturnFalseOnTaskNonendFunction() {
		when(taskStub.isEnded()).thenReturn(true);

		assertThat(nonEndFunctionSut.apply(taskStub)).isFalse();
	}

	public void shouldReturnTrueOnTaskNonendFunction() {
		when(taskStub.isEnded()).thenReturn(false);

		assertThat(nonEndFunctionSut.apply(taskStub)).isTrue();
	}

	public void shouldReturnFalseOnNullTaskNonendFunction() {
		assertThat(nonEndFunctionSut.apply(null)).isFalse();
	}

}
