package pl.kaczanowski.model;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents task (module) that will be execute on processor. Task have information about time of execution (ticks).
 * 
 * @author pawel
 * 
 */
public class Task extends Vertex {

	private final int ticks;

	/**
	 * @param id
	 *            - task id
	 * @param ticks
	 *            - number of ticks to execute task (time of task execution)
	 */
	public Task(final int id, final int ticks) {
		super(id);
		checkArgument(ticks > 0, "ticks must be positive value");
		this.ticks = ticks;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Task [getId()=");
		builder.append(getId());
		builder.append(", getTicks()=");
		builder.append(getTicks());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Task)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return number of ticks
	 */
	public int getTicks() {
		return ticks;
	}

}
