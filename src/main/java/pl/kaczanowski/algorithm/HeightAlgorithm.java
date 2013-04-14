package pl.kaczanowski.algorithm;

/**
 * Used to find height for task.
 * @author kaczanowskip
 */
public interface HeightAlgorithm {

	/**
	 * Cost - height for specified task.
	 * @param taskId - task id
	 * @return height of task on graph
	 */
	public abstract int getCost(Integer taskId);

}
