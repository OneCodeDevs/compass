package de.onecode.navigator.ksp

import com.google.common.truth.Truth.assertThat
import de.onecode.navigator.ksp.descriptions.DestinationDescription
import de.onecode.navigator.ksp.descriptions.GraphDescription
import de.onecode.navigator.ksp.descriptions.SubGraphDescription
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CheckGraphStateTest {
	@Test
	fun `Fail because of multiple homes`() {
		val description1 = DestinationDescription(
			name = "foo",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)
		val description2 = DestinationDescription(
			name = "bar",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)
		val graph = GraphDescription(listOf(description1, description2), emptyList())

		assertThrows<IllegalStateException> {
			graph.checkGraphState()
		}
	}

	@Test
	fun `Standard graph`() {
		val description1 = DestinationDescription(
			name = "foo",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)
		val description2 = DestinationDescription(
			name = "bar",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
		)
		val graph = GraphDescription(listOf(description1, description2), emptyList())

		val graphState = graph.checkGraphState()

		assertThat(graphState)
			.isEqualTo(GraphState.Standard)
	}

	@Test
	fun `No home graph`() {
		val description1 = DestinationDescription(
			name = "foo",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
		)
		val description2 = DestinationDescription(
			name = "bar",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
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
		val sub1 = DestinationDescription(
			name = "sub1",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)
		val sub2 = DestinationDescription(
			name = "sub2",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)
		val sub = SubGraphDescription("sub", listOf(sub1, sub2))
		val graph = GraphDescription(emptyList(), listOf(sub))

		assertThrows<IllegalStateException> {
			graph.checkGraphState()
		}
	}

	@Test
	fun `Subgraph with no home`() {
		val sub1 = DestinationDescription(
			name = "sub1",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
		)
		val sub2 = DestinationDescription(
			name = "sub2",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = false
		)
		val sub = SubGraphDescription("sub", listOf(sub1, sub2))
		val graph = GraphDescription(emptyList(), listOf(sub))

		assertThrows<IllegalStateException> {
			graph.checkGraphState()
		}
	}

	@Test
	fun `Subgraph with unallowed tob destination`() {
		val sub1 = DestinationDescription(
			name = "sub1",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = true,
			isTop = false
		)
		val sub2 = DestinationDescription(
			name = "sub2",
			parameters = emptyList(),
			navigationTargets = emptyList(),
			isHome = false,
			isTop = true
		)
		val sub = SubGraphDescription("sub", listOf(sub1, sub2))
		val graph = GraphDescription(emptyList(), listOf(sub))

		assertThrows<IllegalStateException> {
			graph.checkGraphState()
		}
	}
}