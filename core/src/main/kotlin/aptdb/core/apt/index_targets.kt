package aptdb.core.apt

import aptdb.core.*

import java.nio.file.*

import com.lordcodes.turtle.shellRun

interface IndexTargetsProvider {
  fun indexTargets(): List<IndexTarget> =
    shellRun("apt-get", listOf("indextargets")).let { IndexTarget.from(it) }
}

object DefaultIndexTargetsProvider: IndexTargetsProvider { }

data class IndexTarget(
  private val data: Map<String, String>
) {
  val file: Path? by lazy {
    data["Filename"]?.let { Paths.get(it) }
  }
  
  companion object {
    fun from(indexTargets: String): List<IndexTarget> =
      indexTargets.split("\n")
                  .windowedBy { it == "" }
                  .map { from(it) }
    
    fun from(lines: List<String>): IndexTarget =
      lines.map { it.split(": ") }
           .map { it[0] to it[1] }
           .toMap()
           .let { IndexTarget(it) }
  }
}