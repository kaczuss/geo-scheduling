package pl.kaczanowski.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

public final class FileCreateUtils {

	public static PrintWriter getPrintWriterWithPath(final String fileName) throws IOException {
		File file = FileUtils.getFile(fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		return new PrintWriter(Files.newWriter(file, Charsets.UTF_8));
	}

	private FileCreateUtils() {

	}

}
