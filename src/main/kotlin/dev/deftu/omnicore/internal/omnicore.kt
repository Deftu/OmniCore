@file:JvmName("OmniCore")

package dev.deftu.omnicore.internal

import dev.deftu.omnicore.api.identifierOrThrow
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

public const val ID: String = "@MOD_ID@"
public const val VERSION: String = "@MOD_VERSION@"
public const val GIT_BRANCH: String = "@GIT_BRANCH@"
public const val GIT_COMMIT: String = "@GIT_COMMIT@"
public const val GIT_URL: String = "@GIT_URL@"

@get:ApiStatus.Internal
public inline val isDebug: Boolean
    get() = System.getProperty("omnicore.debug")?.toBoolean() ?: false

internal fun identifierOf(path: String): Identifier {
    return identifierOrThrow(ID, path)
}
