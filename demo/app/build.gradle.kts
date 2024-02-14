@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.android.app)
	alias(libs.plugins.android.compose)
	alias(libs.plugins.ksp)
}

android {
	namespace = "de.onecode.compass.demo"

	defaultConfig {
		applicationId = "de.onecode.compass.demo"
		versionCode = 1
		versionName = "1.0"
	}
}

dependencies {

	implementation(libs.android.core.ktx)
	implementation(libs.android.lifecycle.runtime.ktx)
	implementation(libs.android.activity.compose)

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.ui)
	implementation(libs.compose.material3)
	implementation(libs.compose.ui.preview)
	implementation(libs.compose.navigation)

	implementation(project(":demo:wizard"))
	implementation(project(":demo:destination-feature"))
	implementation(project(":compass:api"))
	implementation(project(":compass:runtime"))
	ksp(project(":compass:ksp"))

	debugImplementation(libs.compose.ui.tooling)
	debugImplementation(libs.compose.ui.test.manifest)
}