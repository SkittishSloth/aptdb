package aptdb

import aptdb.apt.*

fun main() {
  val indexTargets = DefaultAptIndexTargetProvider.indexTargets()
  println("Index Targets:")
  indexTargets.forEach { println("\t${it.file}") }
}