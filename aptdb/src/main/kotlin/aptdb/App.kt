package aptdb

import aptdb.apt.*

import arrow.core.*

suspend fun main() = either {
  val indexTargets = DefaultAptIndexTargetProvider.indexTargets()
  println("Index Targets:")
  indexTargets.forEach { println("\t${it.bind().fileName}") }
}