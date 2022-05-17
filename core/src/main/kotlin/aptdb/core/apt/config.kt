package aptdb.core.apt

import com.lordcodes.turtle.shellRun

interface ConfigurationProvider {
    fun configDump(): String = shellRun("apt-config", listOf("dump"))
    
    fun aptConfiguration(): AptConfiguration = AptConfiguration.create(this)
}

object DefaultConfigurationProvider : ConfigurationProvider { }

data class AptConfiguration private constructor(
  private val config: Map<String, String>
) {
    
    val rootDir: String by lazy {
        config["Dir"]
    }

  fun str(): List<String> =
    config.map{ (k, v) ->
      val value = v.replace("\"", "")
      "$k ->> $value"
    }
  
  companion object {
    fun create(configProvider: ConfigurationProvider): AptConfiguration =
        configProvider.configDump()
            .split("\n")
            .map { line -> line.split(" ") }
            .map { parts -> parts[0] to parts[1].replace("\"", "") }
            .toMap()
            .let { AptConfiguration(it) }
  }
  
}