package aptdb.apt

import java.nio.file.*

import kotlin.collections.*
import kotlin.io.path.*

import com.lordcodes.turtle.*

interface ConfigurationProvider {
    fun configDump(): String = shellRun("apt-config", listOf("dump"))
    
    fun aptConfiguration(): AptConfiguration = AptConfiguration.create(this)
}

object DefaultConfigurationProvider : ConfigurationProvider { }

enum class PropertyType {
  Marker,
  Simple,
  Complex
  ;
}

interface ConfigurationParser {
  fun stringProperty(line: String): Boolean {
    val parts = line.split(" ")
    val key = parts[0]
    val value = parts[1]
    return !(key.endsWith("::") || (value == "\"\""))
  }
  
  fun propertyType(line: String): PropertyType {
    TODO()
  }
}

object DefaultConfigurationParser: ConfigurationParser { }

sealed class ConfigurationProperty() {
  abstract val name: String

  data class StringProperty(
    override val name: String,
    val value: String
  ): ConfigurationProperty() {
    
  }
  
  data class ListProperty(
    override val name: String,
    val value: List<String>
  ): ConfigurationProperty() {
    
  }
  
  data class StructuredProperty(
    override val name: String,
    val value: Map<String, ConfigurationProperty>
  ): ConfigurationProperty() {
    
  }
}



data class ConfigurationProperties(
 private val config: Map<String, List<String>>
) {
  val dir: String? by lazy {
    property("Dir")
  }
  
  val state: String? by lazy {
    property("Dir", "State")
  }
  
  val lists: String? by lazy {
    property("Dir", "State", "lists")
  }
  
  val etc: String? by lazy {
    property("Dir", "Etc")
  }
  
  val sourceList: String? by lazy {
    property("Dir", "Etc", "sourcelist")
  }
  
  val sourceParts: String? by lazy {
    property("Dir", "Etc", "sourceparts")
  }
  
  val architecture: String? by lazy {
    property("APT", "Architecture")
  }
  
  val architectures: List<String>? by lazy {
    list("APT", "Architectures")
  }
  
  private fun buildKey(vararg parts: String): String =
    parts.reduce { a: String, b: String -> "$a::$b" }
  
  private fun property(vararg keyParts: String): String? =
    config[buildKey(*keyParts)]?.firstOrNull()
    
  private fun list(vararg keyParts: String): List<String>? =
    config[buildKey(*keyParts)]?.filter { it != "" }
}

data class AptConfiguration private constructor(
  private val properties: ConfigurationProperties
) {
  val dir: Path? by lazy {
    properties.dir?.let { Paths.get(it) }
  }
  
  val state: Path? by lazy {
    dir.resolve { state }
  }
  
  val lists: Path? by lazy {
    state.resolve { lists }
  }
  
  val etc: Path? by lazy {
    dir.resolve { etc }
  }
  
  val sourceList: Path? by lazy {
    etc.resolve { sourceList }
  }
  
  val sourceParts: Path? by lazy {
    etc.resolve { sourceParts }
  }
  
  private fun Path?.resolve(prop: ConfigurationProperties.() -> String?): Path? =
    this?.let { t ->
      properties.prop()?.let { p -> t.resolve(p) }
    }
  
  companion object {
    fun create(configProvider: ConfigurationProvider): AptConfiguration =
        configProvider.configDump()
            .split("\n")
            .map { line -> line.split(" ") }
            .groupBy(
              { it[0] },
              { cleanValue(it[1]) }
            )
            .let { ConfigurationProperties(it) }
            .let { AptConfiguration(it) }
  
    private fun cleanValue(value: String): String =
      value.replace("\";", "")
           .replace("\"", "")
  }
}