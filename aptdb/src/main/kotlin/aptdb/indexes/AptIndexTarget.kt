package aptdb.indexes

import kotlinx.datetime.*

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
    val attributeView = Files.getFileAttributeView(path, BasicFileAttributeView.javaClass)
    val attributes = attributeView.readAttributes()
    val modified = attributes.lastModifiedTime()
    
    return LocalDateTime.ofInstant(modified.toInstant())
  }
}