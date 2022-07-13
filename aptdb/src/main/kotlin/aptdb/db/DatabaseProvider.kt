package aptdb.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentHashMap

interface DatabaseProvider {
  fun databaseFor(path: String): Database
}

object DefaultDatabaseProvider : DatabaseProvider {
  private val connections: ConcurrentMap<String, Database> = ConcurrentHashMap<String, Database>()
  
  override fun databaseFor(path: String): Database =
    connections.computeIfAbsent(path) { Database.connect("jdbc:h2:$it", "org.h2.Driver") }
}

fun <T> Database?.transaction(statement: Transaction.() -> T): T =
  transaction(this, statement)