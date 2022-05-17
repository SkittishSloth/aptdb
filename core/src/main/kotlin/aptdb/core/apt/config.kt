package aptdb.core.apt

import java.nio.file.*

import kotlin.io.path.*

import com.lordcodes.turtle.shellRun

interface ConfigurationProvider {
    fun configDump(): String = shellRun("apt-config", listOf("dump"))
    
    fun aptConfiguration(): AptConfiguration = AptConfiguration.create(this)
}

object DefaultConfigurationProvider : ConfigurationProvider { }

data class ConfigurationProperties(
 private val config: Map<String, String>
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
  
  private fun buildKey(vararg parts: String): String =
    parts.reduce { a: String, b: String -> "$a::$b" }
  
  private fun property(vararg keyParts: String): String? =
    config[buildKey(*keyParts)]
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
            .map { parts -> parts[0] to cleanValue(parts[1]) }
            .toMap()
            .let { ConfigurationProperties(it) }
            .let { AptConfiguration(it) }
  
    private fun cleanValue(value: String): String =
      value.replace("\";", "")
           .replace("\"", "")
  }
}