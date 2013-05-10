package pl.kaczanowski.graphviz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import pl.kaczanowski.algorithm.AlgorithmModule;
import pl.kaczanowski.algorithm.runner.ConfigurationReader;
import pl.kaczanowski.algorithm.runner.ConfigurationReader.Configuration;
import pl.kaczanowski.algorithm.runner.ParameterUtils;
import pl.kaczanowski.algorithm.runner.Parameters;
import pl.kaczanowski.utils.FileCreateUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GraphvizRunner {

	public static void main(final String[] args) throws IOException {

		Injector injector = Guice.createInjector(new AlgorithmModule(args));

		Map<Parameters, String> readParamters = injector.getInstance(ParameterUtils.class).readParamters(args);

		Configuration configuration = injector.getInstance(ConfigurationReader.class).readConfiguration(args);

		GraphWriter writer = injector.getInstance(GraphWriter.class);
		ModulesGraphTranslator translator = injector.getInstance(ModulesGraphTranslator.class);

		PrintWriter out = FileCreateUtils.getPrintWriterWithPath(readParamters.get(Parameters.GRAPHVIZ_FILE));
		String name = configuration.getModulesGraph().getName();
		writer.writeGraph(
				translator.translate(name.substring(0, name.indexOf(".")), configuration.getModulesGraph()), out);
		out.close();
		System.out.println("zapisano graph");
	}

}
