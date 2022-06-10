package aptdb.indexes

import aptdb.utils.*

import arrow.core.*
import arrow.core.continuations.*

typealias IndexTargetResults = List<ValidatedNel<IndexTargetError, AptIndexTarget>>

interface AptIndexTargetProvider {
  suspend fun indexTargets(): Effect<ShellError<*>, IndexTargetResults> =
    shellRun(ShellCommand("apt-get", listOf("indextargets"))) { indexTargets(it) }
  
  suspend fun indexTargets(text: String): List<ValidatedNel<IndexTargetError, AptIndexTarget>> =
    text.split("\n")
        .windowedBy { it == "" }
        .map { toIndexTarget(it) }
  
  suspend fun toIndexTarget(lines: List<String>): ValidatedNel<IndexTargetError, AptIndexTarget> =
    lines.map { it.split(": ") }
      .map { it[0] to it[1] }
      .toMap()
      .let { IndexTargetBuilder(it) }
      .build()
  
  suspend fun splitLine(line: String): ValidatedNel<IndexTargetError, Pair<String, String>> =
    line.split(": ").let {
      if (it.size != 2) {
        IndexTargetError.InvalidConfigurationLine(line).invalidNel()
      } else {
        Valid(it[0] to it[1])
      }
    }
}

object DefaultAptIndexTargetProvider: AptIndexTargetProvider { }