package aptdb.apt

import java.nio.file.*
import java.time.*

data class IndexFile(
  val path: Path,
  val lastModified: ZonedDateTime
)

data class IndexId(
  val site: String,
  val metakey: String
)

data class IndexTarget(
  val id: IndexId,
  val file: IndexFile,
  val retrieved: ZonedDateTime,
  val shortDescription: String,
  val description: String,
  val optional: Boolean,
  val keepCompressed: Boolean
)