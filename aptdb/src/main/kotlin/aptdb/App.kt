package aptdb

import aptdb.apt.*
import aptdb.utils.*

import arrow.core.*
import arrow.core.continuations.*

suspend fun main() {
  Test.IndexTargets.go()
}

object Test {
  object IndexTargets {
    suspend fun go() {
      indexTargets().fold(
        { error(it) },
        { targets -> targets.forEach { debug(it) } }
      )
    }
    
    suspend fun error(error: ShellError<*>) {
      println("An error occurred.")
      println(error)
    }
    
    suspend fun debug(target: ValidatedNel<FieldError, AptIndexTarget>) {
      target.tap {
        println("${it.fileName}")
      }.tapInvalid { l ->
        println("Invalid")
        l.forEach { println("\t$it") }
      }
    }
    
    suspend fun indexTargets(): Effect<ShellError<*>, IndexTargetResults> =
      DefaultAptIndexTargetProvider.indexTargets()
  }
}
