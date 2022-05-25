package aptdb.data.kodein

import aptdb.core.apt.config.Package

import java.time.*

import org.kodein.db.model.*

data class DbPackage(
  val architecture: String?,
  val breaks: List<String>?,
  val conflicts: List<String>?,
  val depends: List<String>?,
  val description: String?,
  val essential: Boolean,
  val filename: String?,
  val homepage: String?,
  val installedSize: String?,
  val md5Sum: String?,
  val maintainer: String?,
  @Id val packageName: String,
  val preDepends: String?,
  val provides: List<String>?,
  val recommends: List<String>?,
  val replaces: List<String>?,
  val tag: Set<String>?,
  val sha1: String?,
  val sha256: String?,
  val sha512: String?,
  val size: String?,
  val suggests: List<String>?,
  val version: String?,
  val added: ZonedDateTime,
  val updated: ZonedDateTime,
  val missing: ZonedDateTime?
) {
  companion object {
    fun from(package: Package) {
      
    }
  }
}