package pl.kaczanowski.graphviz;

import static java.text.MessageFormat.format;

import java.io.PrintWriter;

import com.google.common.base.Joiner;

public class GraphWriter {

	private static class BaseBuilder extends Builder {

		private final String name;

		/**
		 * @param name
		 */
		public BaseBuilder(final String name) {
			this.name = name;
		}

		@Override
		public String build() {
			return build(null);
		}

		@Override
		protected String build(final String string) {
			return joiner.join(format("digraph {0} {1}", name, "{"), "graph [page=\"11.7,8.3\"];", string, "}");
		}

	}

	private static abstract class Builder {

		public static Builder start(final String name) {
			return new BaseBuilder(name);
		}

		protected Builder parent;

		protected final Builder addEdge(final GraphvizEdge edge) {
			return new EdgeBuilder(edge).parent(this);
		}

		protected Builder addVertex(final GraphvizVertex vertex) {
			return new VertexBuilder(vertex).parent(this);
		}

		public abstract String build();

		protected abstract String build(String string);

		public final Builder parent(final Builder parent) {
			this.parent = parent;
			return this;
		}

	}

	private static class EdgeBuilder extends Builder {

		private GraphvizEdge edge;

		public EdgeBuilder(final GraphvizEdge edge) {
			this.edge = edge;
		}

		@Override
		public String build() {
			return parent.build(getVal());
		}

		@Override
		protected String build(final String childString) {
			return parent.build(joiner.join(getVal(), childString));
		}

		private String getVal() {
			return format("{0} -> {1} [label=\"{2}\"];", edge.getFrom(), edge.getTo(), edge.getCost());
		}
	}

	private static class VertexBuilder extends Builder {
		private final GraphvizVertex vertex;

		/**
		 * @param vertex
		 */
		public VertexBuilder(final GraphvizVertex vertex) {
			this.vertex = vertex;
		}

		@Override
		public String build() {
			return parent == null ? getVal() : parent.build(getVal());
		}

		@Override
		protected String build(final String string) {
			return parent.build(joiner.join(getVal(), string));
		}

		private String getVal() {
			return format(
					"{0} [label=<<TABLE border=\"0\"><TR><TD>{0}</TD></TR><TR><TD border=\"1\" bgcolor=\"grey\">{1}</TD></TR></TABLE>>];",
					vertex.getNumber(), vertex.getCost());
		}

	}

	private static final Joiner joiner = Joiner.on("\r\n").skipNulls();

	public void writeGraph(final Graphviz graph, final PrintWriter out) {

		Builder builder = Builder.start(graph.getName());
		for (GraphvizEdge edge : graph.getEdges()) {
			builder = builder.addEdge(edge);
		}
		for (GraphvizVertex vertex : graph.getVertex()) {
			builder = builder.addVertex(vertex);
		}
		out.println(builder.build());
	}
}
