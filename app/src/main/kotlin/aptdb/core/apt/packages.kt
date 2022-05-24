package aptdb.core.apt

import java.nio.file.*

import kotlin.io.path.*

import aptdb.core.*

interface PackageProvider {
  fun packages(packageFile: Path): List<Package> =
    packageFile.readLines()
               .windowedBy { it == "" }
               .map { Package.from(it) }
}

object DefaultPackageProvider : PackageProvider { }

data class Package(
  private val data: Map<String, String>
) {
  val architecture: String? by lazy {
    property("Architecture")
  }
  
  val breaks: List<String>? by lazy {
    csv("Breaks")
  }
  
  val conflicts: List<String>? by lazy {
    csv("Conflicts")
  }
  
  val depends: List<String>? by lazy {
    csv("Depends")
  }
  
  val description: String? by lazy {
    property("Description")
  }
  
  val essentialRaw: String? by lazy {
    property("Essential")
  }
  
  val essential: Boolean by lazy {
    bool("Essential")
  }
  
  val filename: String? by lazy {
    property("Filename")
  }
  
  val homepage: String? by lazy {
    property("Homepage")
  }
  
  val installedSize: String? by lazy {
    property("Installed-Size")
  }
  
  val md5Sum: String? by lazy {
    property("MD5Sum")
  }
  
  val maintainer: String? by lazy {
    property("Maintainer")
  }
  
  val packageName: String? by lazy {
    property("Package")
  }
  
  val preDepends: String? by lazy {
    property("Pre-Depends")
  }
  
  val provides: List<String>? by lazy {
    csv("Provides")
  }
  
  val recommends: List<String>? by lazy {
    csv("Recommends")
  }
  
  val replaces: List<String>? by lazy {
    csv("Replaces")
  }
  
  val tag: List<String>? by lazy {
      csv("Tag")
  }
  
  val sha1: String? by lazy {
    property("SHA1")
  }
  
  val sha256: String? by lazy {
    property("SHA256")
  }
  
  val sha512: String? by lazy {
    property("SHA512")
  }
  
  val size: String? by lazy {
    property("Size")
  }
  
  val suggests: List<String>? by lazy {
    csv("Suggests")
  }
  
  val version: String? by lazy {
    property("Version")
  }
  
  private fun csv(key: String): List<String>? =
    data[key]?.split(", ")?.toList()
  
  private fun property(key: String): String? =
    data[key]
    
  private fun bool(key: String): Boolean = 
    "yes".equals(data[key], false)
  
  companion object {
    fun from(lines: List<String>): Package =
      lines.map { it.packageSplit() }
           .toMap()
           .let { Package(it) }
  }
}

internal fun String.packageSplit(): Pair<String, String> {
  val splitPoint = this.indexOf(":")
  val key = this.substring(0, splitPoint)
  val value = this.substring(splitPoint + 1).trim()
  
  return Pair(key, value)
}