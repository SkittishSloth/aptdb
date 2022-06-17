package aptdb.io

import java.nio.file.*
import dev.dirs.*

interface ApplicationPaths {
  fun projectDirectories(): ProjectDirectories = ProjectDirectories.from(
    "",
    "",
    "aptdb"
  )
  
  fun dataPath(): Path = Paths.get(projectDirectories().dataDir)
  
  fun configPath(): Path = Paths.get(projectDirectories().configDir)
}

object DefaultApplicationPaths : ApplicationPaths 