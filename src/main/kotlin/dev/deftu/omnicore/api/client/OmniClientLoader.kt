@file:JvmName("OmniClientLoader")

package dev.deftu.omnicore.api.client

import dev.deftu.omnicore.api.loader.ModInfo
import dev.deftu.omnicore.api.loader.OmniLoader
import java.io.InputStream
import kotlin.jvm.optionals.getOrNull

public fun ModInfo.getIconResourcePath(size: Int): String? {
    val container = container.getOrNull() ?: throw IllegalStateException("ModInfo is not loaded from a container")

    //#if FABRIC
    return container.metadata.getIconPath(size).getOrNull()
    //#else
    //#if MC >= 1.15.2
    //#if MC >= 1.17.1
    //$$ val logoFile = container.modInfo?.logoFile
    //#else
    //$$ val modInfo = container.modInfo
    //$$ val logoFile = if (modInfo is net.minecraftforge.fml.loading.moddiscovery.ModInfo) modInfo.logoFile else null
    //#endif
    //$$ return logoFile?.getOrNull()
    //#else
    //$$ return container.metadata?.logoFile
    //#endif
    //#endif
}

public fun ModInfo.getIconResource(size: Int): InputStream? {
    val iconPath = getIconResourcePath(size) ?: return null
    return OmniLoader.getResourceOrNull(id, iconPath)
}
