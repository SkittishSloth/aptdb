package aptdb.apt

import aptdb.utils.*

import java.nio.file.*

import arrow.core.*
import arrow.core.continuations.*

typealias IndexTargetResults = List<ValidatedNel<IndexTargetError, AptIndexTarget>>

sealed class IndexTargetError {
  data class InvalidConfigurationLine(
    val line: String
  ): IndexTargetError()
}

interface AptIndexTargetProvider {
  suspend fun indexTargets(): Effect<ShellError<*>, IndexTargetResults> =
    shellRun(ShellCommand("apt-get", listOf("indextargets"))) { indexTargets(it) }
  
  suspend fun indexTargets(text: String): List<ValidatedNel<IndexTargetError, AptIndexTarget>> =
    text.split("\n")
        .windowedBy { it == "" }
        .map { toIndexTarget(it) }
  
  suspend fun toIndexTarget(lines: List<String>): ValidatedNel<IndexTargetError, AptIndexTarget> =
    lines.map { it.split(": ") }
      .map { it[0] to it[1] }
      .toMap()
      .let { IndexTargetBuilder(it) }
      .build()
  
  suspend fun splitLine(line: String): ValidatedNel<IndexTargetError, Pair<String, String>> =
    line.split(": ").let {
      if (it.size != 2) {
        IndexTargetError.InvalidConfigurationLine(line).invalidNel()
      } else {
        Valid(it[0] to it[1])
      }
    }
}

object DefaultAptIndexTargetProvider: AptIndexTargetProvider { }

class IndexTargetBuilder(private val data: Map<String, String>) {
  suspend fun build(): ValidatedNel<FieldError, AptIndexTarget> = 
    uri().zip(
      metaKey(),
      shortDescription(),
      description(),
      fileName(),
      optional(),
      keepCompressed()
    ) { uri, metaKey, shortDescription, description, fileName, optional, keepCompressed ->
      AptIndexTarget(
        uri, metaKey, shortDescription, description, fileName, optional, keepCompressed
      )
    }
  
  private suspend fun uri() =
    field("URI", ::Uri)
  
  private suspend fun metaKey() =
    field("MetaKey", ::MetaKey)
  
  private suspend fun shortDescription() =
    field("ShortDesc") { ShortDescription(it) }
  
  private suspend fun description() =
    field("Description") { Description(it) }

  private suspend fun fileName() =
    field("Filename") { FileName(it) }
  
  private suspend fun optional() =
    bool("Optional") { IsOptional(it) }
  
  private suspend fun keepCompressed() =
    bool("KeepCompressed") { KeepCompressed(it) }

  private suspend fun <T> field(field: String, ctor: (String) -> T): ValidatedNel<FieldError, T> =
    data[field]?.let { Valid(ctor(it)) } ?: FieldError.Missing(field).invalidNel()
  
  private suspend fun <T> bool(field: String, ctor: (Boolean) -> T): ValidatedNel<FieldError, T> =
    data[field]?.let {
      val parsed = if (it == "yes") {
        true
      } else if (it == "no") {
        false
      } else {
        null
      }
      
      parsed?.let { p -> Valid(ctor(p)) } ?: FieldError.Invalid(field, it, "Expected yes or no.").invalidNel()
    } ?: FieldError.Missing(field).invalidNel()
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

@JvmInline value class Uri(val value: String)

@JvmInline value class MetaKey(val value: String)

@JvmInline value class ShortDescription(val value: String)

@JvmInline value class Description (val value: String)

@JvmInline value class FileName(val value: String)

@JvmInline value class IsOptional(val value: Boolean)

@JvmInline value class KeepCompressed(val value: Boolean)

data class AptIndexTarget(
  val uri: Uri,
  val metaKey: MetaKey,
  val shortDesc: ShortDescription,
  val description: Description,
  val fileName: FileName,
  val optional: IsOptional,
  val keepCompressed: KeepCompressed
) 