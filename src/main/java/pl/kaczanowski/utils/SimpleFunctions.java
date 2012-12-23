package pl.kaczanowski.utils;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Strings;

/**
 * Simple functions.
 * @author kaczanowskip
 */
public final class SimpleFunctions {

	private SimpleFunctions() {

	}

	private enum ToInteger implements Function<String, Integer> {
		INSTANCE {
			@Override
			@Nullable
			public Integer apply(@Nullable final String input) {
				return Strings.isNullOrEmpty(input) ? null : Integer.valueOf(input);
			}
		}
	}

	public static Function<String, Integer> stringToInteger() {
		return ToInteger.INSTANCE;
	}

}
