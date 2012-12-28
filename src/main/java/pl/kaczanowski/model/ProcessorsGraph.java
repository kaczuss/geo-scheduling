package pl.kaczanowski.model;

import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.kaczanowski.algorithm.HeightAlgorithm;
import pl.kaczanowski.model.ModulesGraph.Task;

import com.google.common.base.Function;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ranges;

public class ProcessorsGraph {

	public class Processor {

		private final int id;

		private int reservedTillTime;

		private int actualTime;

		private Task activeTask;

		private final ImmutableSet<Task> tasksToExecute;

		private Processor(final int id, final Collection<Task> tasksToExecute) {
			this.id = id;
			reservedTillTime = 0;
			actualTime = 0;
			this.tasksToExecute =
					tasksToExecute == null ? ImmutableSet.<Task> of() : ImmutableSet.<Task> copyOf(tasksToExecute);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Processor other = (Processor) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (id != other.id) {
				return false;
			}
			return true;
		}

		public void executeNext(@Nonnull final Task nextTask, @Nonnull final ProcessorsGraph processorsGraph,
				final Map<Integer, Set<Task>> tasksParial) {
			checkState(isFree(), "Now executed is other task: " + activeTask);

			checkState(tasksToExecute.contains(nextTask), "Cannot assign task " + nextTask + " on processor " + this);

			int time = nextTask.getCost();

			if (!Iterables.isEmpty(nextTask.getParentTasks())) {
				for (Task parentTask : nextTask.getParentTasks()) {
					time += processorsGraph.getChangeCost(getFromProcessorId(parentTask, tasksParial), id);
				}
			}
			reservedTillTime = actualTime + time;

			activeTask = nextTask;

		}

		private Task getBestTask(final List<Task> candidates, final HeightAlgorithm heightAlgorithm) {

			if (Iterables.isEmpty(candidates)) {
				return null;
			}
			if (candidates.size() == 1) {
				return Iterables.getOnlyElement(candidates);
			}
			Task best = Iterables.get(candidates, 0);
			int maxHeight = heightAlgorithm.getCost(best.getId());

			for (int i = 1; i < candidates.size(); i++) {
				Task task = candidates.get(i);
				int height = heightAlgorithm.getCost(task.getId());
				if (height > maxHeight) {
					maxHeight = height;
					best = task;
				}
			}
			return best;

		}

		private Integer getFromProcessorId(final Task parentTask, final Map<Integer, Set<Task>> tasksParial) {
			for (Entry<Integer, Set<Task>> entry : tasksParial.entrySet()) {
				if (entry.getValue().contains(parentTask)) {
					return entry.getKey();
				}

			}
			return null;
		}

		public int getId() {
			return id;
		}

		public Task getNextTask(final Collection<Task> allEndedTasks, final HeightAlgorithm heightAlgorithm) {

			List<Task> candidates = Lists.newArrayList();
			for (Task task : tasksToExecute) {
				if (!task.isEnded() && allEndedTasks.containsAll(task.getParentTasks())) {
					candidates.add(task);
				}
			}

			return getBestTask(candidates, heightAlgorithm);
		}

		private ProcessorsGraph getOuterType() {
			return ProcessorsGraph.this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + id;
			return result;
		}

		public boolean isFree() {
			return reservedTillTime <= actualTime;
		}

		public void tick() {
			if (LOG.isDebugEnabled()) {
				LOG.debug("tick on processor " + this);
			}
			actualTime++;
			if (isFree() && activeTask != null) {
				activeTask.end();
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Processor [id=").append(id).append(", reservedTillTime=").append(reservedTillTime)
					.append(", actualTime=").append(actualTime).append(", activeTask=").append(activeTask)
					.append(", tasksToExecute=").append(tasksToExecute).append("]");
			return builder.toString();
		}

	}

	private static final Logger LOG = LoggerFactory.getLogger(ProcessorsGraph.class);

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

		return Lists.transform(
				Lists.newArrayList(Ranges.closed(0, connections.length).asSet(DiscreteDomains.integers())),
				new Function<Integer, Processor>() {

					@Override
					@Nullable
					public Processor apply(@Nullable final Integer processorId) {
						return processorId == null ? null : new Processor(processorId, processorsPartial
								.get(processorId));
					}
				});
	}

	public int getProcessorsCount() {
		return connections.length;
	}

	@Override
	public String toString() {
		return "ProcessorsGraph [connections=" + Arrays.toString(connections) + "]";
	}

}
