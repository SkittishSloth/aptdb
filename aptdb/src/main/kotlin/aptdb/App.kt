package aptdb

import kotlinx.coroutines.*
import aptdb.apt.*
import aptdb.indexes.*
import aptdb.utils.*
import aptdb.io.*

import aptdb.db.*

import arrow.core.*
import arrow.core.continuations.*

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Index as exposedIndex

import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

suspend fun main() {
  // Test.Paths.debug()
  // Test.IndexTargets.go()
  Test.Database.database()
}

object Test {
  object Paths {
    suspend fun debug() {
      println("Application Paths:")
      println(DefaultApplicationPaths.projectDirectories())
    }
  }
  object IndexTargets {
    suspend fun go() {
      indexTargets().fold(
        { error(it) },
        { targets -> targets.forEach { debug(it) } }
      )
    }
    
    suspend fun debug(target: ValidatedNel<IndexTargetError, AptIndexTarget>) {
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
  
  object Database {
    suspend fun database() {
      DefaultDatabaseProvider.databaseFor("~/projects/aptdb/testdata.db")
      newSuspendedTransaction(Dispatchers.Default) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create ( Indexes )
        
        IndexTargets.indexTargets().fold(
          { error(it) },
          { targets -> targets.forEach { insert(it) } }
        )
      }
    }
    
    suspend fun insert(target: ValidatedNel<IndexTargetError, AptIndexTarget>) {
      target.tap { Index.new(it) }
            .tapInvalid{ l ->
              println("Invalid")
              l.forEach { println("\t$it") }
            }
    }
  }
}

suspend fun error(error: ShellError<*>) {
  println("An error occurred.")
  println(error)
}