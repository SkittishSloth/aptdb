package aptdb.indexes


sealed class IndexTargetError {
  data class InvalidConfigurationLine(
    val line: String
  ): IndexTargetError()
}

sealed class FieldError: IndexTargetError() {
  abstract val field: String

  data class Missing(
    override val field: String
  ): FieldError()
  
  data class Invalid(
    override val field: String,
    val value: String,
    val reason: String
  ): FieldError()
}