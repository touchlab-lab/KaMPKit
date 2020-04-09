package co.touchlab.secondlib

import kotlinx.coroutines.runBlocking

internal actual fun <T> runTest(block: suspend () -> T) { runBlocking { block() } }

internal class SqlDelightTestJvm : SqlDelightTest()

internal class BreedModelTestJvm: BreedModelTest()