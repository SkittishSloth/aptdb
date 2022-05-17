package aptdb.core.apt

import com.lordcodes.turtle.shellRun


data class ConfigNode(
  val key: String,
  val value: String?,
  val children: List<ConfigNode>
);

sealed class Node(open val key: String) {
  data class Leaf(
    override val key: String,
    val value: String
  ): Node(key)
  
  data class NonLeaf(
    override val key: String,
    val children: List<Node>
  ): Node(key)
}

data class AptConfiguration private constructor(
  private val config: Map<String, String>
) {

  fun str(): List<String> =
    config.map{ (k, v) ->
      val value = v.replace("\"", "")
      "$k ->> $value"
    }
  
  companion object {
    fun create(): AptConfiguration {
      val dump = shellRun("apt-config", listOf("dump"))
      val lines = dump.split("\n")
      val config = lines.map { it.split(" ") }
           .map { it[0] to it[1] }
           .toMap()
      return AptConfiguration(config)
    }
  }
  
}

fun configDump(): String = shellRun("apt-config", listOf("dump"))
