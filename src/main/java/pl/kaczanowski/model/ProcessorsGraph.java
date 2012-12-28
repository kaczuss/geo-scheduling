package pl.kaczanowski.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import pl.kaczanowski.model.ModulesGraph.Task;

import com.google.common.base.Function;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Lists;
import com.google.common.collect.Ranges;

public class ProcessorsGraph {

	private final Integer[][] connections;

	public ProcessorsGraph(final Integer[][] connections) {
		// can be assimetric
		this.connections = connections;
	}

	public int getChangeCost(final int processorA, final int processorB) {
		// FIXME - it could be not peer connections, so in this case i should find shortest path between processors,
		// it's important in scheduling algorithm too, where in this case transfer can take both all processors on
		// the path. Maybe i'll exclude this situation from project, and i must validate connections on the
		// constructor

		return processorA == processorB ? 0 : connections[processorA][processorB];
	}

	public List<Processor> getProcessorCopies(final Map<Integer, Set<Task>> processorsPartial) {

		return Lists.newArrayList(Lists.transform(
				Lists.newArrayList(Ranges.closedOpen(0, connections.length).asSet(DiscreteDomains.integers())),
				new Function<Integer, Processor>() {

					@Override
					@Nullable
					public Processor apply(@Nullable final Integer processorId) {
						return processorId == null ? null : new Processor(processorId, processorsPartial
								.get(processorId));
					}
				}));
	}

	public int getProcessorsCount() {
		return connections.length;
	}

	@Override
	public String toString() {
		return "ProcessorsGraph [connections=" + Arrays.toString(connections) + "]";
	}

}
