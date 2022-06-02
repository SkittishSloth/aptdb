package aptdb.utils

import arrow.core.*
import arrow.core.continuations.*

import com.lordcodes.turtle.*

data class ShellCommand(
  val command: String,
  val arguments: List<String>
) {
  fun execute(): String = shellRun(command, arguments)
}

sealed class ShellError<E: RuntimeException> {
  abstract val exception: E
  abstract val command: ShellCommand

  data class Failure(
    override val exception: ShellFailedException,
    override val command: ShellCommand
  ): ShellError<ShellFailedException>() {
    
  }
  
  data class Error(
    override val exception: ShellRunException,
    override val command: ShellCommand
  ): ShellError<ShellRunException>() {
    val exitCode: Int by lazy {
      exception.exitCode
    }
    
    val errorText: String by lazy {
      exception.errorText
    }
  }
}

suspend fun <T> shellRun(command: ShellCommand, f: suspend (String) -> T): Effect<ShellError<*>, T> = effect {
  try {
    command.execute().let { f(it) }
  } catch (e: ShellFailedException) {
    shift(ShellError.Failure(e, command))
  } catch (e: ShellRunException) {
    shift(ShellError.Error(e, command))
  }
}
