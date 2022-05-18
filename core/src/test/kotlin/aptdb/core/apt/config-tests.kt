package aptdb.core.apt

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.*

class ConfigurationParserTests: FeatureSpec({
  val parser = DefaultConfigurationParser

  feature("stringProperty") {
    scenario("should return false if key ends with ::") {
      val line = "APT:: \"Hello\""
      parser.stringProperty(line) shouldBe false
    }
    
    scenario("should return false if value is blank") {
      val line = "APT::Test \"\""
      parser.stringProperty(line) shouldBe false
    }
    
    scenario ("should return true if line indicates string property") {
      val line = "APT::Test \"Hello\""
      parser.stringProperty(line) shouldBe true
    }
  }
})