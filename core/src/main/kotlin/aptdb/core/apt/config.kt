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
  
  private fun buildKey(vararg parts: String): String =
    parts.reduce { a: String, b: String -> "$a::$b" }
  
  private fun property(vararg keyParts: String): String? = this.let {
    println("Key: ${buildKey(*keyParts)}")
    println("Contained? ${config.contains(buildKey(*keyParts))}")
    config[buildKey(*keyParts)]
  }
}

data class AptConfiguration private constructor(
  private val properties: ConfigurationProperties
) {
  val dir: Path? by lazy {
    properties.dir?.let {
      println("dir from properties: '$it'")
      Paths.get(it)
    }
  }
  
  val state: Path? by lazy {
    dir?.let { d ->
      properties.state?.let { s -> d.resolve(s) }
    }
  }
  
  val lists: Path? by lazy {
    state?.let { s ->
      properties.lists?.let { l -> s.resolve(l) }
    }
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