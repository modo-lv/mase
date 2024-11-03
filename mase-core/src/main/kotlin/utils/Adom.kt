package utils

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.lang3.SystemUtils
import java.nio.file.Path
import javax.swing.filechooser.FileSystemView
import kotlin.io.path.exists

/**
 * ADOM-specific helpers that don't belong anywhere else
 */
object Adom {
    private val logger = KotlinLogging.logger { }

    /**
     * Try to determine the location of ADOM save files.
     */
    fun defaultSaveFolderPath(): Path {
        // ADOM_DATA overrides everything
        System.getenv("ADOM_DATA")?.let { return Path.of(it) }

        val adomHome = Path.of(
            if (SystemUtils.IS_OS_WINDOWS)
                "${FileSystemView.getFileSystemView().getDefaultDirectory().path}/ADOM"
            else if (SystemUtils.IS_OS_MAC)
                "${SystemUtils.USER_HOME}/Library/ADOM"
            else
                SystemUtils.USER_HOME
        )

        if (!adomHome.exists()) {
            return Path.of("")
        }

        logger.debug { "Found ADOM home in [${adomHome}]." }

        val adomData =
            if (SystemUtils.IS_OS_WINDOWS) {
                listOf(
                    adomHome.resolve("adom_dat"),
                    adomHome.resolve("adom_steam"),
                    adomHome.resolve("adom_gog"),
                )
            } else if (SystemUtils.IS_OS_MAC) {
                listOf(
                    adomHome.resolve("adom_data"),
                    adomHome.resolve("adom_steam"),
                    adomHome.resolve("adom_gog"),
                )
            } else {
                listOf(
                    adomHome.resolve(".adom.data"),
                    adomHome.resolve(".adom.steam"),
                    adomHome.resolve(".adom.gog"),
                )
            }

        return adomData.map { it.resolve("savedg") }.singleOrNull { it.exists() }
            ?.also { logger.debug { "Found save folder in [${it}]." } }
            ?: adomHome.also {
                logger.debug { "Failed to find a single save game folder, falling back to [${it}]." }
            }
    }
}