package pl.kaczanowski.model;

import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.kaczanowski.algorithm.HeightAlgorithm;
import pl.kaczanowski.model.ModulesGraph.Task;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class Processor {

	private static class TaskRank implements Comparable<TaskRank> {

		private final Task task;

		private final int rank;

		public TaskRank(final Task task, final int rank) {
			this.task = task;
			this.rank = rank;
		}

		@Override
		public int compareTo(final TaskRank o) {
			return ComparisonChain.start().compare(o.rank, rank).compare(task.getId(), o.task.getId()).result();
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TaskRank)) {
				return false;
			}
			TaskRank other = (TaskRank) obj;
			if (rank != other.rank) {
				return false;
			}
			if (task == null) {
				if (other.task != null) {
					return false;
				}
			} else if (!task.equals(other.task)) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + rank;
			result = prime * result + ((task == null) ? 0 : task.hashCode());
			return result;
		}

	}

	private final Logger log = LoggerFactory.getLogger(Processor.TaskRank.class);

	// private static final Logger LOG = LoggerFactory.getLogger(Processor.class);

	private final int id;

	private int reservedTillTime;

	private int actualTime;

	private Task activeTask;

	private final ImmutableSet<Task> tasksToExecute;

	Processor(final int id, final Collection<Task> tasksToExecute) {
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
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public void executeNext(@Nonnull final Task nextTask, @Nonnull final ProcessorsGraph processorsGraph,
			final Map<Integer, Set<Task>> tasksParial, final ModulesGraph modulesGraph) {
		checkState(isFree(), "Now executed is other task: " + activeTask);

		checkState(tasksToExecute.contains(nextTask), "Cannot assign task " + nextTask + " on processor " + this);

		int time = nextTask.getCost();

		if (!Iterables.isEmpty(nextTask.getParentTasks())) {
			TreeSet<Integer> waitingTime = Sets.newTreeSet();
			for (Task parentTask : nextTask.getParentTasks()) {
				waitingTime.add(getWaitingTime(nextTask, processorsGraph, tasksParial, modulesGraph, parentTask));
			}
			time += Math.max(0, Ordering.natural().max(waitingTime));
		}
		this.reservedTillTime = this.actualTime + time;

		log.debug("task " + nextTask.getId() + " start running at " + this.actualTime + " on processor " + getId()
				+ " from ticks: " + time);

		this.activeTask = nextTask;

	}

	public int getActualTime() {
		return actualTime;
	}

	private Task getBestTask(final List<Task> candidates, final HeightAlgorithm heightAlgorithm) {

		if (Iterables.isEmpty(candidates)) {
			return null;
		}
		if (candidates.size() == 1) {
			return Iterables.getOnlyElement(candidates);
		}
		TreeSet<TaskRank> taskRanks = Sets.newTreeSet();
		for (Task task : candidates) {
			taskRanks.add(new TaskRank(task, heightAlgorithm.getCost(task.getId())));
		}

		return taskRanks.first().task;

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

	public Task getNextTask(final Collection<Task> allEndedTasks, final HeightAlgorithm heightAlgorithm,
			@Nonnull final ProcessorsGraph processorsGraph, final Map<Integer, Set<Task>> tasksPartial,
			final ModulesGraph modulesGraph) {

		List<Task> candidates = Lists.newArrayList();

		for (Task task : tasksToExecute) {
			if (!task.isEnded() && allEndedTasks.containsAll(task.getParentTasks())
					&& !mustWait(task, processorsGraph, tasksPartial, modulesGraph)) {
				candidates.add(task);
			}
		}
		return getBestTask(candidates, heightAlgorithm);

	}

	private int getWaitingTime(final Task nextTask, final ProcessorsGraph processorsGraph,
			final Map<Integer, Set<Task>> tasksParial, final ModulesGraph modulesGraph, final Task parentTask) {
		int changeTime =
				processorsGraph.getChangeCost(getFromProcessorId(parentTask, tasksParial), id)
						* modulesGraph.getChangeTime(parentTask.getId(), nextTask.getId());
		// it's time that data can be send after tasks ends
		int timeDifference = actualTime - parentTask.getEndTime();
		int waitTimeToExecute = changeTime - timeDifference;
		return waitTimeToExecute < 0 ? 0 : waitTimeToExecute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	public boolean isFree() {
		return reservedTillTime <= actualTime;
	}

	private boolean mustWait(final Task task, final ProcessorsGraph processorsGraph,
			final Map<Integer, Set<Task>> tasksPartial, final ModulesGraph modulesGraph) {
		// check if task don't must wait on change
		if (Iterables.isEmpty(task.getParentTasks())) {
			return false;
		}
		for (Task parentTask : task.getParentTasks()) {
			int waitingTime = getWaitingTime(task, processorsGraph, tasksPartial, modulesGraph, parentTask);
			if (waitingTime > 0) {
				return true;
			}
		}

		return false;
	}

	public void tick() {
		// if (LOG.isDebugEnabled()) {
		// LOG.debug("tick on processor " + this);
		// }
		this.actualTime++;
		if (isFree() && this.activeTask != null) {
			this.activeTask.end(actualTime);
			this.activeTask = null;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Processor [id=").append(id).append(", reservedTillTime=").append(reservedTillTime)
				.append(", actualTime=").append(actualTime).append(", activeTask=").append(activeTask).append("]");
		return builder.toString();
	}

}
