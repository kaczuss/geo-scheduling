package pl.kaczanowski.algorithm.runner;

import java.io.IOException;
import java.text.MessageFormat;

import pl.kaczanowski.algorithm.AlgorithmModule;
import pl.kaczanowski.algorithm.GeoSchedulingAlgorithm;
import pl.kaczanowski.algorithm.runner.ConfigurationReader.Configuration;
import pl.kaczanowski.model.SchedulingConfiguration;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GeoSchedulingRunner {

	/**
	 * To run algorithm set properties. Properties are:
	 * -f - file
	 * -prob - probability
	 * -i - algorithm iterations
	 * -ri - run iterations
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {

		Injector injector = Guice.createInjector(new AlgorithmModule(args));

		Configuration configuration = injector.getInstance(ConfigurationReader.class).readConfiguration(args);

		GeoSchedulingAlgorithm.Builder algorithmBuilder = injector.getInstance(GeoSchedulingAlgorithm.Builder.class);

		for (int i = 0; i < configuration.getRunIterations(); ++i) {
			GeoSchedulingAlgorithm algorithm =
					algorithmBuilder.setModulesGraph(configuration.getModulesGraph())
							.setProcessorsGraph(configuration.getProcessorsGraph())
							.setProbabilityParameter(configuration.getProbabilityParamter())
							.setIterations(configuration.getAlgorithmIterations())
							.setStepsListener(configuration.getListener()).build();

			SchedulingConfiguration scheduling = algorithm.execute();

			System.out.println(MessageFormat.format("Uruchomienie {0} znalazło konfigurację: {1}", (i + 1),
					scheduling.toString()));

		}

		configuration.getListener().saveRaport();

	}

}
