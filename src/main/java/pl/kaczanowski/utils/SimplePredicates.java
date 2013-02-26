package pl.kaczanowski.utils;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;

public final class SimplePredicates {

	private enum IsNotEmptyString implements Predicate<String> {
		INSTANCE {

			@Override
			public boolean apply(@Nullable final String input) {
				return !Strings.isNullOrEmpty(input);
			}

		}
	}

	public static Predicate<String> isNotEmptyString() {
		return IsNotEmptyString.INSTANCE;
	}

	public static Predicate<String> notStartsFrom(final char prefix) {
		return Predicates.not(startsFrom(String.valueOf(prefix)));
	}

	public static Predicate<String> notStartsFrom(final String prefix) {
		return Predicates.not(startsFrom(String.valueOf(prefix)));
	}

	public static Predicate<String> startsFrom(final char prefix) {
		return startsFrom(String.valueOf(prefix));
	}

	public static Predicate<String> startsFrom(final String prefix) {
		return new Predicate<String>() {

			@Override
			public boolean apply(@Nullable final String input) {
				return input != null && input.startsWith(prefix);
			}
		};
	}

	private SimplePredicates() {

	}

}
