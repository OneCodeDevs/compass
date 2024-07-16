package de.onecode.compass.ksp.discovery

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DestinationVisitorTest {
	@Test
	fun `create Destination description from type name (no home, no top, no parameters and no navigation)`() {
		val destinationVisitor = DestinationVisitor()
		val destination = declareDestination(
			destinationVisitor = destinationVisitor,
			typeName = "TestType",
		)

		destination.accept(destinationVisitor, Unit)

		assertThat(destinationVisitor.destinations).hasSize(1)
		val description = destinationVisitor.destinations[0]
		assertThat(description.name).isEqualTo("TestType")
		assertThat(description.isHome).isFalse()
		assertThat(description.isTop).isFalse()
		assertThat(description.parameters).isEmpty()
		assertThat(description.navigationTargets).isEmpty()
	}

	@Test
	fun `create Destination description from given name (no home, no top, no parameters and no navigation)`() {
		val destinationVisitor = DestinationVisitor()
		val destination = declareDestination(
			destinationVisitor = destinationVisitor,
			name = "OverriddenName",
			typeName = "TestType",
		)

		destination.accept(destinationVisitor, Unit)

		assertThat(destinationVisitor.destinations).hasSize(1)
		val description = destinationVisitor.destinations[0]
		assertThat(description.name).isEqualTo("OverriddenName")
		assertThat(description.isHome).isFalse()
		assertThat(description.isTop).isFalse()
		assertThat(description.parameters).isEmpty()
		assertThat(description.navigationTargets).isEmpty()
	}

	@Test
	fun `create Destination description as home (no top, no parameters and no navigation)`() {
		val destinationVisitor = DestinationVisitor()
		val destination = declareDestination(
			destinationVisitor = destinationVisitor,
			name = "MyName",
			isHome = true,
		)

		destination.accept(destinationVisitor, Unit)

		assertThat(destinationVisitor.destinations).hasSize(1)
		val description = destinationVisitor.destinations[0]
		assertThat(description.name).isEqualTo("MyName")
		assertThat(description.isHome).isTrue()
		assertThat(description.isTop).isFalse()
		assertThat(description.parameters).isEmpty()
		assertThat(description.navigationTargets).isEmpty()
	}

	@Test
	fun `create Destination description as top (no home, no parameters and no navigation)`() {
		val destinationVisitor = DestinationVisitor()
		val destination = declareDestination(
			destinationVisitor = destinationVisitor,
			name = "MyName",
			isTop = true,
		)

		destination.accept(destinationVisitor, Unit)

		assertThat(destinationVisitor.destinations).hasSize(1)
		val description = destinationVisitor.destinations[0]
		assertThat(description.name).isEqualTo("MyName")
		assertThat(description.isHome).isFalse()
		assertThat(description.isTop).isTrue()
		assertThat(description.parameters).isEmpty()
		assertThat(description.navigationTargets).isEmpty()
	}

	@Test
	fun `create Destination description with parameters (no navigation)`() {
		val destinationVisitor = DestinationVisitor()
		val destination = declareDestination(
			destinationVisitor = destinationVisitor,
			name = "MyName",
			isTop = true,
			parameters = {
				listOf(
					createParameter(parameterName = "param1", parameterType = String::class, required = true),
					createParameter(parameterName = "param2", parameterType = Int::class, required = true)
				)
			}
		)

		destination.accept(destinationVisitor, Unit)

		assertThat(destinationVisitor.destinations).hasSize(1)
		val description = destinationVisitor.destinations[0]
		assertThat(description.name).isEqualTo("MyName")
		assertThat(description.isHome).isFalse()
		assertThat(description.isTop).isTrue()
		assertThat(description.navigationTargets).isEmpty()

		assertThat(description.parameters).hasSize(2)
		val param1 = description.parameters[0]
		assertThat(param1.name).isEqualTo("param1")
		assertThat(param1.type).isEqualTo("java.lang.String")
		val param2 = description.parameters[1]
		assertThat(param2.name).isEqualTo("param2")
		assertThat(param2.type).isEqualTo("int")
	}

	@Test
	fun `create Destination description with navigation`() {
		val destinationVisitor = DestinationVisitor()
		val destination = declareDestination(
			destinationVisitor = destinationVisitor,
			name = "MyName",
			isTop = true,
			navigationTargets = {
				listOf(createNavigationTarget {
					createAnnotationsForDestination(
						name = "Second",
						parameters = { listOf(createParameter(parameterName = "nav_param", parameterType = Int::class, required = true)) }
					)
				})
			}
		)

		destination.accept(destinationVisitor, Unit)

		assertThat(destinationVisitor.destinations).hasSize(1)
		val description = destinationVisitor.destinations[0]
		assertThat(description.name).isEqualTo("MyName")
		assertThat(description.isHome).isFalse()
		assertThat(description.isTop).isTrue()
		assertThat(description.parameters).isEmpty()

		assertThat(description.navigationTargets).hasSize(1)
		val navigationTarget = description.navigationTargets[0]
		assertThat(navigationTarget.name).isEqualTo("Second")
		assertThat(navigationTarget.parameters).hasSize(1)
		val navigationParameter1 = navigationTarget.parameters[0]
		assertThat(navigationParameter1.name).isEqualTo("nav_param")
		assertThat(navigationParameter1.type).isEqualTo("int")
	}

	@Test
	fun `multiple destinations`() {
		val destinationVisitor = DestinationVisitor()

		val destination1 = declareDestination(destinationVisitor, name = "Destination1", isHome = true)
		val destination2 = declareDestination(destinationVisitor, name = "Destination2")
		val destination3 = declareDestination(destinationVisitor, name = "Destination3", isTop = true)

		listOf(
			destination1,
			destination2,
			destination3
		).forEach {
			it.accept(destinationVisitor, Unit)
		}

		assertThat(destinationVisitor.destinations).hasSize(3)
		val destinationDescription1 = destinationVisitor.destinations[0]
		assertThat(destinationDescription1.name).isEqualTo("Destination1")
		assertThat(destinationDescription1.isHome).isTrue()
		assertThat(destinationDescription1.isTop).isFalse()
		val destinationDescription2 = destinationVisitor.destinations[1]
		assertThat(destinationDescription2.name).isEqualTo("Destination2")
		assertThat(destinationDescription2.isHome).isFalse()
		assertThat(destinationDescription2.isTop).isFalse()
		val destinationDescription3 = destinationVisitor.destinations[2]
		assertThat(destinationDescription3.name).isEqualTo("Destination3")
		assertThat(destinationDescription3.isHome).isFalse()
		assertThat(destinationDescription3.isTop).isTrue()
	}

	@Test
	fun `Dialog can't be also Home`() {
		val destinationVisitor = DestinationVisitor()
		val destination1 = declareDestination(destinationVisitor, name = "Destination1", isHome = true, isDialog = true)

		assertThrows<IllegalStateException> {
			destination1.accept(destinationVisitor, Unit)
		}
	}
}
