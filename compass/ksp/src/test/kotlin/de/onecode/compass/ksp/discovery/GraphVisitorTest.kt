package de.onecode.compass.ksp.discovery

import com.google.common.truth.Truth.assertThat
import de.onecode.compass.ksp.descriptions.DestinationDescription
import org.junit.jupiter.api.Test

class GraphVisitorTest {
	@Test
	fun `create graph description without sub graph`() {
		val destinations = listOf(
			DestinationDescription(name = "D1", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false),
			DestinationDescription(name = "D2", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)
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

		val s1 = DestinationDescription(name = "S1", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false)
		val s2 = DestinationDescription(name = "S2", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false)
		val destinations = listOf(
			DestinationDescription(name = "D1", parameters = emptyList(), navigationTargets = emptyList(), isHome = true, isTop = false),
			DestinationDescription(name = "D2", parameters = emptyList(), navigationTargets = emptyList(), isHome = false, isTop = false),
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