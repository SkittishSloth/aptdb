package aptdb.apt

import io.kotest.core.*
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.*
import io.kotest.assertions.arrow.core.*

class AptIndexTargetProviderTests: FeatureSpec({
  val single = this::class.java.getResourceAsStream("/index-targets/single-index-target")
                          ?.bufferedReader()
                          ?.let { autoClose(it) }
                          ?.readLines()
                          ?: throw IllegalArgumentException("Could not locate resource.")
  feature("toIndexTarget") {
    val provider = DefaultAptIndexTargetProvider
    scenario("happy path") {
      provider.toIndexTarget(single)
        .shouldBeValid()
    }
  }
})