package aptdb.indexes

import arrow.core.*
import arrow.core.continuations.*

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
    field("URI", ::IndexUri)
  
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