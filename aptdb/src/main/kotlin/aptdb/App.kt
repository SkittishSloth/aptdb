package aptdb

import aptdb.apt.*

import arrow.core.*
import arrow.core.continuations.*

// suspend fun main() = either {
//   val indexTargets = DefaultAptIndexTargetProvider.indexTargets()
//   println("Index Targets:")
//   indexTargets.forEach { println("\t${it.bind().fileName}") }
// }

suspend fun main() {
  val targets = indexTargets()
  targets.forEach { debug(it) }
}

suspend fun debug(target: ValidatedNel<FieldError, AptIndexTarget>) {
  target.tap {
    println("${it.fileName}")
  }.tapInvalid { l ->
    println("Invalid")
    l.forEach { println("\t$it") }
  }
}

suspend fun indexTargets(): List<ValidatedNel<FieldError, AptIndexTarget>> =
  DefaultAptIndexTargetProvider.indexTargets()