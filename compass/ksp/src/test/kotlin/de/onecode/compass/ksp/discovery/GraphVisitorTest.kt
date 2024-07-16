package de.onecode.compass.ksp.discovery

import com.google.common.truth.Truth.assertThat
import de.onecode.compass.ksp.util.destination
import org.junit.jupiter.api.Test

class GraphVisitorTest {
	@Test
	fun `create graph description without sub graph`() {
		val destinations = listOf(
			destination(name = "D1", isHome = true),
			destination(name = "D2")
		)
		val graphVisitor = GraphVisitor(destinations)

		val graph = graphVisitor.graph
		assertThat(graph.subGraphs).isEmpty()
		assertThat(graph.destinations).hasSize(2)
		assertThat(graph.destinations[0].name).isEqualTo("D1")
		assertThat(graph.destinations[1].name).isEqualTo("D2")
	}

	@Test
	fun `create graph description with sub graph`() {

		val s1 = destination(name = "S1", isHome = true)
		val s2 = destination(name = "S2")
		val destinations = listOf(
			destination(name = "D1", isHome = true),
			destination(name = "D2"),
			s1,
			s2
		)
		val graphVisitor = GraphVisitor(destinations)

		createSubGraphType(graphVisitor, "Sub", listOf(s1, s2)).accept(graphVisitor, Unit)

		val graph = graphVisitor.graph
		assertThat(graph.destinations).hasSize(2)
		assertThat(graph.destinations[0].name).isEqualTo("D1")
		assertThat(graph.destinations[1].name).isEqualTo("D2")
		assertThat(graph.subGraphs).hasSize(1)
		val subGraph = graph.subGraphs[0]
		assertThat(subGraph.name).isEqualTo("Sub")
		assertThat(subGraph.destinations[0].name).isEqualTo("S1")
		assertThat(subGraph.destinations[1].name).isEqualTo("S2")
	}
}
