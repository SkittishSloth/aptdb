package aptdb.apt

import aptdb.utils.*

import java.nio.file.*

import com.lordcodes.turtle.shellRun

interface AptIndexTargetProvider {
  fun indexTargets(): List<AptIndexTarget> =
    shellRun("apt-get", listOf("indextargets")).let { AptIndexTarget.from(it) }
}

object DefaultAptIndexTargetProvider: AptIndexTargetProvider { }

data class AptIndexTarget(
  private val data: Map<String, String>
) {
  val file: Path? by lazy {
    data["Filename"]?.let { Paths.get(it) }
  }
  
  companion object {
    fun from(indexTargets: String): List<AptIndexTarget> =
      indexTargets.split("\n")
                  .windowedBy { it == "" }
                  .map { from(it) }
    
    fun from(lines: List<String>): AptIndexTarget =
      lines.map { it.split(": ") }
           .map { it[0] to it[1] }
           .toMap()
           .let { AptIndexTarget(it) }
  }
}