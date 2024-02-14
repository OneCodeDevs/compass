plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.android.compose)
	alias(libs.plugins.ksp)
}

android {
	namespace = "de.onecode.compass.demo.destinations"
}

dependencies {
	implementation(libs.android.core.ktx)

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.material3)
	implementation(libs.compose.navigation)

	implementation(project(":compass:api"))
	implementation(project(":compass:runtime"))
	ksp(project(":compass:ksp"))
}