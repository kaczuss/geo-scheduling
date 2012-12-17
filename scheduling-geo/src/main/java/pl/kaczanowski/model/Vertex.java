package pl.kaczanowski.model;

import com.google.common.collect.ComparisonChain;

/**
 * Represents graph vertex.
 * 
 * @author pawel
 * 
 */
public class Vertex implements Comparable<Vertex> {

	private final int id;

	/**
	 * @param id
	 *            - vertex id
	 */
	public Vertex(final int id) {
		this.id = id;
	}

	/**
	 * @return vertex id
	 */
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Vertex [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Vertex)) {
			return false;
		}
		Vertex other = (Vertex) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final Vertex o) {
		return ComparisonChain.start().compare(this.getId(), o.getId()).result();
	}

}
