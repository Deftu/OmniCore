@file:JvmName("OmniCore")

package dev.deftu.omnicore.internal

import dev.deftu.omnicore.api.ID
import dev.deftu.omnicore.api.identifierOrThrow
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

@get:ApiStatus.Internal
public inline val isDebug: Boolean
    get() = System.getProperty("omnicore.debug")?.toBoolean() ?: false

internal fun identifierOf(path: String): Identifier {
    return identifierOrThrow(ID, path)
}
