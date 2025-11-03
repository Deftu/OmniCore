@file:JvmName("OmniCore")

package dev.deftu.omnicore.internal

import dev.deftu.omnicore.api.ID
import dev.deftu.omnicore.api.locationOrThrow
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus

@get:ApiStatus.Internal
public inline val isDebug: Boolean
    get() = System.getProperty("omnicore.debug")?.toBoolean() ?: false

internal fun internalLocationOf(path: String): ResourceLocation {
    return locationOrThrow(ID, path)
}
