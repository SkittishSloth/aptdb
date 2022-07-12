package aptdb.indexes

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.*
import org.jetbrains.exposed.sql.transactions.transaction

object Indexes: IntIdTable("indexes") {
  val uri = varchar("uri", 256)
  val metaKey = varchar("metaKey", 128)
  val shortDesc = varchar("shortDesc", 128)
  val description = text("description")
  val fileName = text("fileName")
  val optional = bool("optional")
  val keepCompressed = bool("keepCompressed")
  val indexAdded = timestamp("indexAdded")
  val fileModified = datetime("fileModified")
}