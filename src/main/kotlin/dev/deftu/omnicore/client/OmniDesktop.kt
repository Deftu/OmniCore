package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import org.apache.logging.log4j.LogManager
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit

@GameSide(Side.CLIENT)
public object OmniDesktop {

    private val logger = LogManager.getLogger()

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var isWindows: Boolean = false
        private set

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var isMac: Boolean = false
        private set

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var isLinux: Boolean = false
        private set

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var isXdg: Boolean = false
        private set

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var isKde: Boolean = false
        private set

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var isGnome: Boolean = false
        private set

    init {
        val osName = try {
            System.getProperty("os.name").lowercase()
        } catch (e: Exception) {
            logger.error("Failed to get OS name", e)
            null
        }

        if (osName != null) {
            isWindows = osName.startsWith("Windows")
            isMac = osName.startsWith("Mac")
            isLinux = osName.startsWith("Linux") || osName.startsWith("LINUX")
            if (isLinux) {
                isXdg = System.getenv("XDG_SESSION_ID")?.isNotEmpty() == true
                val gnomeSession = System.getenv("GDMSESSION")?.lowercase()
                isGnome = gnomeSession?.contains("gnome") == true
                isKde = gnomeSession?.contains("kde") == true
            }
        } else logger.error("Couldn't assign OS variables because the OS name is null")
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun browse(uri: URI): Boolean =
        browseInternally(uri) || openWithCommand(uri.toString())

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun open(file: File): Boolean =
        openInternally(file) || openWithCommand(file.absolutePath)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun edit(file: File): Boolean =
        editInternally(file) || openWithCommand(file.absolutePath)

    private fun browseInternally(uri: URI): Boolean {
        if (!Desktop.isDesktopSupported())
            return false

        return try {
            if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
                return false

            Desktop.getDesktop().browse(uri)
            true
        } catch (e: Exception) {
            logger.error("Failed to browse URI", e)
            false
        }
    }

    private fun openInternally(file: File): Boolean {
        if (!Desktop.isDesktopSupported())
            return false

        return try {
            if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
                return false

            Desktop.getDesktop().open(file)
            true
        } catch (e: Exception) {
            logger.error("Failed to open file", e)
            false
        }
    }

    private fun editInternally(file: File): Boolean {
        if (!Desktop.isDesktopSupported())
            return false

        return try {
            if (!Desktop.getDesktop().isSupported(Desktop.Action.EDIT))
                return false

            Desktop.getDesktop().edit(file)
            true
        } catch (e: Exception) {
            logger.error("Failed to edit file", e)
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
                } else true
            } else process.isAlive
        } catch (e: Exception) {
            logger.error("Failed to run command", e)
            false
        }
    }


}
