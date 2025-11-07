package dev.deftu.omnicore.api.client

import org.apache.logging.log4j.LogManager
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.util.Locale
import java.util.concurrent.TimeUnit

public object OmniDesktop {
    private val LOGGER = LogManager.getLogger(OmniDesktop::class.java)

    private val osName: String? = try {
        System.getProperty("os.name").lowercase(Locale.ROOT)
    } catch (e: Exception) {
        LOGGER.error("Failed to get OS name", e)
        null
    }

    @JvmStatic public val isWindows: Boolean = osName?.startsWith("windows") == true
    @JvmStatic public val isMac: Boolean = osName?.startsWith("mac") == true
    @JvmStatic public val isLinux: Boolean = osName?.startsWith("linux") == true

    @JvmStatic public val isXdg: Boolean = if (isLinux) {
        val hasSessionId = System.getenv("XDG_SESSION_ID")?.isNotEmpty() == true
        val hasDesktop = System.getenv("XDG_CURRENT_DESKTOP")?.isNotEmpty() == true
        hasSessionId || hasDesktop
    } else {
        false
    }

    @JvmStatic
    public val isKde: Boolean = if (isLinux) {
        val session = System.getenv("GDMSESSION")?.lowercase(Locale.ROOT)
            ?: System.getenv("XDG_CURRENT_DESKTOP")?.lowercase(Locale.ROOT)
        session?.contains("kde") == true
    } else {
        false
    }

    @JvmStatic
    public val isGnome: Boolean = if (isLinux) {
        val session = System.getenv("GDMSESSION")?.lowercase(Locale.ROOT)
            ?: System.getenv("XDG_CURRENT_DESKTOP")?.lowercase(Locale.ROOT)
        session?.contains("gnome") == true
    } else {
        false
    }

    @JvmStatic
    public fun browse(uri: URI): Boolean {
        return browseInternally(uri) || openWithCommand(uri.toString())
    }

    @JvmStatic
    public fun open(file: File): Boolean {
        return openInternally(file) || openWithCommand(file.absolutePath)
    }

    @JvmStatic
    public fun edit(file: File): Boolean {
        return editInternally(file) || openWithCommand(file.absolutePath)
    }

    private fun browseInternally(uri: URI): Boolean {
        if (!Desktop.isDesktopSupported()) {
            return false
        }

        return try {
            if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                return false
            }

            Desktop.getDesktop().browse(uri)
            true
        } catch (e: Exception) {
            LOGGER.error("Failed to browse URI", e)
            false
        }
    }

    private fun openInternally(file: File): Boolean {
        if (!Desktop.isDesktopSupported()) {
            return false
        }

        return try {
            if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                return false
            }

            Desktop.getDesktop().open(file)
            true
        } catch (e: Exception) {
            LOGGER.error("Failed to open file", e)
            false
        }
    }

    private fun editInternally(file: File): Boolean {
        if (!Desktop.isDesktopSupported()) {
            return false
        }

        return try {
            if (!Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {
                return false
            }

            Desktop.getDesktop().edit(file)
            true
        } catch (e: Exception) {
            LOGGER.error("Failed to edit file", e)
            false
        }
    }

    private fun openWithCommand(path: String): Boolean {
        return when {
            isWindows -> run("rundll32", "url.dll.FileProtocolHandler", path, checkExit = false)
            isMac -> run("open", path, checkExit = false)
            isLinux -> listOf(
                "xdg-open",
                "gnome-open",
                "kde-open"
            ).any { command -> run(command, path, checkExit = true) }

            else -> false
        }
    }

    private fun run(
        vararg command: String,
        checkExit: Boolean
    ): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(command) ?: return false
            if (checkExit) {
                if (process.waitFor(5, TimeUnit.SECONDS)) {
                    process.exitValue() == 0
                } else {
                    true
                }
            } else {
                process.isAlive
            }
        } catch (e: Exception) {
            LOGGER.error("Failed to run command", e)
            false
        }
    }

    override fun toString(): String {
        return buildString {
            append(this@OmniDesktop::class.java.simpleName).append("[")
            append("Windows=").append(isWindows).append(", ")
            append("macOS=").append(isMac).append(", ")
            append("Linux=").append(isLinux)
            append("]")
        }
    }
}