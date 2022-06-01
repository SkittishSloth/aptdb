package aptdb.apt

import aptdb.utils.*

import java.nio.file.*

import arrow.core.*

import com.lordcodes.turtle.shellRun

interface AptIndexTargetProvider {
  suspend fun indexTargets() =
    shellRun("apt-get", listOf("indextargets")).let { RawAptIndexTarget.from(it) }
}

object DefaultAptIndexTargetProvider: AptIndexTargetProvider { }

data class RawAptIndexTarget(
  private val data: Map<String, String>
) {
  val file: Path? by lazy {
    data["Filename"]?.let { Paths.get(it) }
  }
  
  companion object {
    fun from(indexTargets: String) =
      indexTargets.split("\n")
                  .windowedBy { it == "" }
                  .map { from(it) }
    
    fun from(lines: List<String>) =
      lines.map { it.split(": ") }
           .map { it[0] to it[1] }
           .toMap()
           .let { it.indexTarget() }
  }
}

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

inline class Uri(val value: String)

fun Data.uri(): ValidatedNel<FieldError, Uri> =
  field("Uri") { Uri(it) }
  
inline class MetaKey(val value: String)

fun Data.metaKey() =
  field("MetaKey") { MetaKey(it) }

inline class ShortDescription(val value: String)

fun Data.shortDescription() =
  field("ShortDesc") { ShortDescription(it) }

inline class Description (val value: String)

fun Data.description() =
  field("Description") { Description(it) }

inline class FileName(val value: String)

fun Data.fileName() =
  field("FileName") { FileName(it) }

inline class IsOptional(val value: Boolean)

fun Data.optional() =
  bool("Optional") { IsOptional(it) }

inline class KeepCompressed(val value: Boolean)

fun Data.keepCompressed() =
  bool("KeepCompressed") { KeepCompressed(it) }

fun Data.indexTarget(): ValidatedNel<FieldError, AptIndexTarget> =
  uri().zip(
    metaKey(),
    shortDescription(),
    description(),
    fileName(),
    optional(),
    keepCompressed()
  ) { u, m, s, d, f, o, k ->
    AptIndexTarget(
      u,
      m,
      s,
      d,
      f,
      o,
      k
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