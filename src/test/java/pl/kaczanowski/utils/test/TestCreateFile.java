package pl.kaczanowski.utils.test;

import java.io.BufferedWriter;
import java.io.File;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

public class TestCreateFile {

	static String path = "c:/temp/a/b/";

	public static void main(final String[] args) throws Exception {

		String fileName = path + "metoda1/m1.txt";
		File file = FileUtils.getFile(fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		BufferedWriter writer = Files.newWriter(file, Charsets.UTF_8);
		writer.write("plik1");
	}

}
