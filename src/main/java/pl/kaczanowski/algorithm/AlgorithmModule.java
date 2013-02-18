/**
 * 
 */
package pl.kaczanowski.algorithm;

import pl.kaczanowski.algorithm.listener.AlgorithmStepsListener;
import pl.kaczanowski.algorithm.listener.AlgorithmStepsListenerAdapter;

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
		bind(AlgorithmStepsListener.class).to(AlgorithmStepsListenerAdapter.class);
	}
}
