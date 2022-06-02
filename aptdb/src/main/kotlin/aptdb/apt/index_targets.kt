package aptdb.apt

import aptdb.utils.*

import java.nio.file.*

import arrow.core.*
import arrow.core.continuations.*

typealias IndexTargetResults = List<ValidatedNel<FieldError, AptIndexTarget>>

interface AptIndexTargetProvider {
  suspend fun indexTargets(): Effect<ShellError<*>, IndexTargetResults> =
    shellRun(ShellCommand("apt-get", listOf("indextargets"))) { aptIndexTargets(it) }
}

object DefaultAptIndexTargetProvider: AptIndexTargetProvider { }

suspend fun aptIndexTargets(indexTargets: String): List<ValidatedNel<FieldError, AptIndexTarget>> =
  indexTargets.split("\n")
              .windowedBy { it == "" }
              .map { aptIndexTarget(it) }
              
suspend fun aptIndexTarget(lines: List<String>): ValidatedNel<FieldError, AptIndexTarget> =
  lines.map { it.split(": ") }
       .map { it[0] to it[1] }
       .toMap()
       .let { it.indexTarget() }

internal typealias Data = Map<String, String>

internal fun <T> Data.field(field: String, ctor: (String) -> T): ValidatedNel<FieldError, T> =
  this[field]?.let { Valid(ctor(it)) } ?: FieldError.Missing(field).invalidNel()

internal fun <T> Data.bool(field: String, ctor: (Boolean) -> T) =
  this[field]?.let {
    val parsed = if (it == "yes") {
      true
    } else if (it == "no") {
      false
    } else {
      null
    }
    
    parsed?.let { p -> Valid(ctor(p)) } ?: FieldError.Invalid(field, it, "Expected yes or no.").invalidNel()
  } ?: FieldError.Missing(field).invalidNel()

sealed class FieldError {
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

internal fun Data.uri(): ValidatedNel<FieldError, Uri> =
  field("URI") { Uri(it) }
  
@JvmInline value class MetaKey(val value: String)

internal fun Data.metaKey() =
  field("MetaKey") { MetaKey(it) }

@JvmInline value class ShortDescription(val value: String)

internal fun Data.shortDescription() =
  field("ShortDesc") { ShortDescription(it) }

@JvmInline value class Description (val value: String)

internal fun Data.description() =
  field("Description") { Description(it) }

@JvmInline value class FileName(val value: String)

internal fun Data.fileName() =
  field("Filename") { FileName(it) }

@JvmInline value class IsOptional(val value: Boolean)

internal fun Data.optional() =
  bool("Optional") { IsOptional(it) }

@JvmInline value class KeepCompressed(val value: Boolean)

internal fun Data.keepCompressed() =
  bool("KeepCompressed") { KeepCompressed(it) }

internal fun Data.indexTarget(): ValidatedNel<FieldError, AptIndexTarget> =
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

data class AptIndexTarget(
  val uri: Uri,
  val metaKey: MetaKey,
  val shortDesc: ShortDescription,
  val description: Description,
  val fileName: FileName,
  val optional: IsOptional,
  val keepCompressed: KeepCompressed
) 