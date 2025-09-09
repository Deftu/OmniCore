@file:JvmName("OmniCore")

package dev.deftu.omnicore.api

import dev.deftu.eventbus.EventBus
import dev.deftu.eventbus.bus
import dev.deftu.eventbus.invokers.LMFInvoker

//#if MC >= 1.16.5
import net.minecraft.SharedConstants
//#elseif FORGE
//$$ import net.minecraftforge.common.ForgeVersion
//#elseif FABRIC
//$$ import net.fabricmc.loader.api.FabricLoader
//#endif

private val VERSION_REGEX = "(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?".toRegex()

public const val ID: String = "@MOD_ID@"
public const val VERSION: String = "@MOD_VERSION@"
public const val GIT_BRANCH: String = "@GIT_BRANCH@"
public const val GIT_COMMIT: String = "@GIT_COMMIT@"
public const val GIT_URL: String = "@GIT_URL@"

public var minecraftVersion: String = "unknown"
    private set
    get() {
        if (field != "unknown") {
            return field
        }

        //#if MC >= 1.16.5
        val version = SharedConstants.getGameVersion().comp_4025()
        //#else
        //#if FORGE
        //$$ val version = ForgeVersion.mcVersion
        //#else
        //$$ val version = FabricLoader.getInstance().getModContainer("minecraft").orElseThrow {
        //$$     RuntimeException("Minecraft mod container not found")
        //$$ }.metadata.version.friendlyString
        //#endif
        //#endif

        field = version
        return version
    }

public var paddedMinecraftVersion: Int = -1
    private set
    get() {
        if (field != -1) {
            return field
        }

        val version = minecraftVersion
        val match = VERSION_REGEX.find(version) ?: throw IllegalArgumentException("Invalid version format, could not match to regex: $version")
        val groups = match.groups

        val major = groups["major"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing major version: $version")
        val minor = groups["minor"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing minor version: $version")
        val patch = groups["patch"]?.value?.toInt() ?: 0

        val finalVersion = major * 10000 + minor * 100 + patch
        field = finalVersion
        return finalVersion
    }

public val eventBus: EventBus = bus {
    invoker = LMFInvoker()
    threadSafety = true // Slightly decreases performance, but ensures stability
}
