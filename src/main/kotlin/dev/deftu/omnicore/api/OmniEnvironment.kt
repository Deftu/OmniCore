@file:JvmName("OmniEnvironment")

package dev.deftu.omnicore.api

//#if MC >= 1.16.5
import net.minecraft.SharedConstants
//#elseif FORGE
//$$ import net.minecraftforge.common.ForgeVersion
//#elseif FABRIC
//$$ import net.fabricmc.loader.api.FabricLoader
//#endif

private val VERSION_REGEX = "(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?".toRegex()

public val minecraftVersion: String by lazy {
    //#if MC >= 1.16.5
    SharedConstants.getGameVersion().name()
    //#else
    //#if FORGE
    //$$ ForgeVersion.mcVersion
    //#else
    //$$ FabricLoader.getInstance().getModContainer("minecraft").orElseThrow {
    //$$     RuntimeException("Minecraft mod container not found")
    //$$ }.metadata.version.friendlyString
    //#endif
    //#endif
}

public val paddedMinecraftVersion: Int by lazy {
    val version = minecraftVersion
    val match = VERSION_REGEX.find(version) ?: throw IllegalArgumentException("Invalid version format, could not match to regex: $version")
    val groups = match.groups

    val major = groups["major"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing major version: $version")
    val minor = groups["minor"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing minor version: $version")
    val patch = groups["patch"]?.value?.toInt() ?: 0

    major * 10000 + minor * 100 + patch
}
