package pl.kaczanowski.utils;

public final class MathUtils {

	public static int getProcNumber(final byte[] intByteArray) {

		StringBuilder sb = new StringBuilder();
		for (byte b : intByteArray) {
			sb.append(b);
		}
		return Integer.valueOf(sb.toString(), 2);
	}

	private MathUtils() {

	}
}
