package aptdb.core.apt

import kotlin.io.path.*

interface SourcesProvider {
  fun sources(aptConfig: AptConfiguration): List<Source>? =
    aptConfig.sourceList
             ?.readLines()
             ?.map { Source.from(it) }
}

object DefaultSourcesProvider : SourcesProvider { }

enum class ArchiveType {
  Binary,
  Source
  ;

  companion object {
    fun from(string: String) =
      if (string.equals("deb", true)) {
        Binary
      } else if (string.equals("deb-src", true)) {
        Source
      } else {
        throw IllegalArgumentException("Unrecognized archive type: '$string'")
      }
  }
}

data class Source private constructor(
  val archiveType: ArchiveType,
  val repositoryUrl: String,
  val distribution: String,
  val components: List<String>
) {
  companion object {
    fun from(line: String): Source {
      val parts = line.split(" ")
      val archiveType = ArchiveType.from(parts[0])
      val repositoryUrl = parts[1]
      val distribution = parts[2]
      val components = parts.drop(3)
      
      return Source(
        archiveType,
        repositoryUrl,
        distribution,
        components
      )
    }
  }
}