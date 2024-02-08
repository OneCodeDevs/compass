package de.onecode.navigator.ksp

import com.squareup.kotlinpoet.FileSpec

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