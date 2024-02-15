package de.onecode.compass.ksp

import com.google.common.truth.Truth.assertThat
import com.squareup.kotlinpoet.FileSpec
import org.intellij.lang.annotations.Language

fun buildTestFile(fileBuilder: FileSpec.Builder.() -> Unit): String =
	writeToString {
		FileSpec.builder("", "Test")
			.apply(fileBuilder)
			.build()
	}

fun writeToString(fileSpec: () -> FileSpec): String {
	val stringBuffer = StringBuffer()
	fileSpec()
		.writeTo(stringBuffer)

	return stringBuffer.toString()
}

fun assertGeneratedCode(generated: String, @Language("kotlin") expected: String) {
	val generatedLines = generated.lines().map { it.trimStart().trimEnd() }.filter { it.isNotBlank() }
	val expectedLines = expected.lines().map { it.trimStart().trimEnd() }.filter { it.isNotBlank() }

	assertThat(generatedLines)
		.isEqualTo(expectedLines)
}