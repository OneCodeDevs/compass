package de.onecode.compass.ksp

import com.google.common.truth.Truth.assertThat
import de.onecode.compass.ksp.descriptions.GraphDescription
import de.onecode.compass.ksp.descriptions.SubGraphDescription
import de.onecode.compass.ksp.generator.common.destinationDescription
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CheckGraphStateTest {
	@Test
	fun `Fail because of multiple homes`() {
		val description1 = destinationDescription(
			name = "foo",
			isHome = true,
		)
		val description2 = destinationDescription(
			name = "bar",
			isHome = true,
		)
		val graph = GraphDescription(listOf(description1, description2), emptyList())

		assertThrows<IllegalStateException> {
			graph.checkGraphState()
		}
	}

	@Test
	fun `Standard graph`() {
		val description1 = destinationDescription(
			name = "foo",
			isHome = true,
		)
		val description2 = destinationDescription(
			name = "bar",
		)
		val graph = GraphDescription(listOf(description1, description2), emptyList())

		val graphState = graph.checkGraphState()

		assertThat(graphState)
			.isEqualTo(GraphState.Standard)
	}

	@Test
	fun `No home graph`() {
		val description1 = destinationDescription(
			name = "foo",
		)
		val description2 = destinationDescription(
			name = "bar",
		)
		val graph = GraphDescription(listOf(description1, description2), emptyList())

		val graphState = graph.checkGraphState()

		assertThat(graphState)
			.isEqualTo(GraphState.NoHome)
	}

	@Test
	fun `Empty graph`() {
		val graph = GraphDescription(emptyList(), emptyList())

		val graphState = graph.checkGraphState()

		assertThat(graphState)
			.isEqualTo(GraphState.NoDestinations)
	}

	@Test
	fun `Subgraph with multiple homes`() {
		val sub1 = destinationDescription(
			name = "sub1",
			isHome = true,
		)
		val sub2 = destinationDescription(
			name = "sub2",
			isHome = true,
		)
		val sub = SubGraphDescription("sub", listOf(sub1, sub2))
		val graph = GraphDescription(emptyList(), listOf(sub))

		assertThrows<IllegalStateException> {
			graph.checkGraphState()
		}
	}

	@Test
	fun `Subgraph with no home`() {
		val sub1 = destinationDescription(
			name = "sub1",
		)
		val sub2 = destinationDescription(
			name = "sub2",
		)
		val sub = SubGraphDescription("sub", listOf(sub1, sub2))
		val graph = GraphDescription(emptyList(), listOf(sub))

		assertThrows<IllegalStateException> {
			graph.checkGraphState()
		}
	}

	@Test
	fun `Subgraph with unallowed tob destination`() {
		val sub1 = destinationDescription(
			name = "sub1",
			isHome = true,
		)
		val sub2 = destinationDescription(
			name = "sub2",
			isTop = true
		)
		val sub = SubGraphDescription("sub", listOf(sub1, sub2))
		val graph = GraphDescription(emptyList(), listOf(sub))

		assertThrows<IllegalStateException> {
			graph.checkGraphState()
		}
	}
}
