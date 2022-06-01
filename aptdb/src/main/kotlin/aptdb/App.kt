package aptdb

import aptdb.apt.*

import arrow.core.*
import arrow.core.continuations.*

// suspend fun main() = either {
//   val indexTargets = DefaultAptIndexTargetProvider.indexTargets()
//   println("Index Targets:")
//   indexTargets.forEach { println("\t${it.bind().fileName}") }
// }