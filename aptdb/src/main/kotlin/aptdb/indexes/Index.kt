package aptdb.indexes

import kotlinx.datetime.*

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.*
import org.jetbrains.exposed.sql.transactions.transaction

fun currentDateTime(): Instant = Clock.System.now()

object Indexes: IntIdTable("indexes") {
  val uri = varchar("uri", 256)
  val metaKey = varchar("metaKey", 128)
  val shortDesc = varchar("shortDesc", 128)
  val description = text("description")
  val fileName = varchar("fileName", 4096).uniqueIndex()
  val optional = bool("optional")
  val keepCompressed = bool("keepCompressed")
  val indexAdded = timestamp("indexAdded").clientDefault { currentDateTime() }
  val fileModified = datetime("fileModified")
}

class Index(id: EntityID<Int>): IntEntity(id) {
  
  var uri by Indexes.uri
    private set

  var metaKey by Indexes.metaKey
    private set

  var shortDesc by Indexes.shortDesc
    private set

  var description by Indexes.description
    private set

  var fileName by Indexes.fileName
    private set

  var optional by Indexes.optional
    private set

  var keepCompressed by Indexes.keepCompressed
    private set

  var indexAdded by Indexes.indexAdded
    private set

  var fileModified by Indexes.fileModified
    private set

  
  companion object: IntEntityClass<Index>(Indexes) {
    fun new(index: AptIndexTarget): Index =
      Index.new {
        uri = index.uri.value
        metaKey = index.metaKey.value
        shortDesc = index.shortDesc.value
        description = index.description.value
        fileName = index.fileName.value
        optional = index.optional.value
        keepCompressed = index.keepCompressed.value
        fileModified = index.fileModified
      }
  }
}