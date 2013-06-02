/**
 * 
 */
package pl.kaczanowski.algorithm;

import com.google.inject.AbstractModule;

public class AlgorithmModule extends AbstractModule {

	public AlgorithmModule() {
		this(null);
	}

	public AlgorithmModule(final String[] args) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void configure() {

		// bind(ConfigurationHelper.class).to(ConfigurationHelper.class);
		// bind(HeightAlgorithmFactory.class).to(StandardHeightAlgorithm.Factory.class);
		// bind(HeightAlgorithmFactory.class).to(DynamicHeightAlgorithm.Factory.class);
		bind(HeightAlgorithmFactory.class).to(ChildNodesAlgorithm.Factory.class);
	}
}
