/**
 * 
 */
package pl.kaczanowski.algorithm;

import pl.kaczanowski.algorithm.listner.AlgorithmStepsListener;
import pl.kaczanowski.algorithm.listner.AlgorithmStepsListenerAdapter;

import com.google.inject.AbstractModule;

public class AlgorithmModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(AlgorithmStepsListener.class).to(AlgorithmStepsListenerAdapter.class);
	}
}
