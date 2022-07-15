package aptdb.indexes

import kotlinx.datetime.*

import java.time.LocalDateTime as jtLocalDateTime
import java.time.ZoneId
import java.nio.file.*
import java.nio.file.attribute.*

data class AptIndexTarget(
  val uri: IndexUri,
  val metaKey: MetaKey,
  val shortDesc: ShortDescription,
  val description: Description,
  val fileName: FileName,
  val optional: IsOptional,
  val keepCompressed: KeepCompressed
) {
  val fileModified: LocalDateTime by lazy {
    val path = Paths.get(fileName.value)
    val attributes = Files.readAttributes(path, BasicFileAttributes::class.java)
    val modified = attributes.lastModifiedTime()
    
    jtLocalDateTime.ofInstant(modified.toInstant(), ZoneId.systemDefault()).toKotlinLocalDateTime()
  }
}