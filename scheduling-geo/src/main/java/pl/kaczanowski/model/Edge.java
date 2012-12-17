package pl.kaczanowski.model;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ComparisonChain;

/**
 * Represent edge on graph. Contains info of weight and vertex connected with this edge. Info about second vertex should
 * contains other vertex.
 * 
 * @author pawel
 * 
 * @param <T>
 *            - type of vertex
 */
public class Edge<T extends Vertex> implements Comparable<Edge<T>> {

	private final Integer weight;

	private final T vertex;

	/**
	 * @param vertex
	 *            - connected vertex
	 * @param weight
	 *            - weight of connection, use null if no weight
	 */
	public Edge(@Nonnull final T vertex, @Nullable final Integer weight) {
		checkNotNull(vertex, "vertex cannot be null");
		this.weight = weight;
		this.vertex = vertex;
	}

	/**
	 * 
	 * @return weight, could be null
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * 
	 * @return linked vertex
	 */
	public T getVertex() {
		return vertex;
	}

	@Override
	public int compareTo(final Edge<T> o) {
		return ComparisonChain.start().compare(getVertex(), o.getVertex()).compare(getWeight(), o.getWeight()).result();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vertex == null) ? 0 : vertex.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
		if (!(obj instanceof Edge)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Edge<T> other = (Edge<T>) obj;
		if (vertex == null) {
			if (other.vertex != null) {
				return false;
			}
		} else if (!vertex.equals(other.vertex)) {
			return false;
		}
		if (weight == null) {
			if (other.weight != null) {
				return false;
			}
		} else if (!weight.equals(other.weight)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Edge [weight=");
		builder.append(weight);
		builder.append(", vertex=");
		builder.append(vertex);
		builder.append("]");
		return builder.toString();
	}

}
