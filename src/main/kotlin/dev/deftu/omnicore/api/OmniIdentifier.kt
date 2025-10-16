@file:JvmName("OmniIdentifier")

package dev.deftu.omnicore.api

import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCText
import net.minecraft.util.Identifier

public const val DEFAULT_NAMESPACE: String = "minecraft"

public inline val Identifier.translationKey: String
    @JvmName("translationKey")
    get() = "${namespace}.${path}"

public inline val Identifier.shortTranslationKey: String
    @JvmName("shortTranslationKey")
    get() {
        if (namespace == DEFAULT_NAMESPACE) {
            return path
        }

        return translationKey
    }

public inline val Identifier.translatedText: Text
    @JvmName("createTranslatedText")
    get() = MCText.translatable(translationKey)

public inline val Identifier.shortTranslatedText: Text
    @JvmName("createShortTranslatedText")
    get() = MCText.translatable(shortTranslationKey)

public inline val Identifier.isMinecraft: Boolean
    get() = namespace == DEFAULT_NAMESPACE

@JvmName("createOrNull")
public fun identifierOrNull(namespace: String, path: String): Identifier? {
    //#if MC >= 1.19.2
    return Identifier.tryParse(namespace, path)
    //#else
    //$$ return try {
    //$$     Identifier(namespace, path)
    //$$ } catch (_: Exception) {
    //$$     null
    //$$ }
    //#endif
}

@JvmName("createOrNull")
public fun identifierOrNull(path: String): Identifier? {
    //#if MC >= 1.19.2
    return Identifier.tryParse(path)
    //#else
    //$$ return try {
    //$$     Identifier(path)
    //$$ } catch (_: Exception) {
    //$$     null
    //$$ }
    //#endif
}

@JvmName("createOrThrow")
public fun identifierOrThrow(namespace: String, path: String): Identifier {
    return identifierOrNull(namespace, path) ?: throw IllegalArgumentException("Invalid identifier")
}

@JvmName("createOrThrow")
public fun identifierOrThrow(path: String): Identifier {
    return identifierOrNull(path) ?: throw IllegalArgumentException("Invalid identifier")
}

public fun Identifier.translationKey(prefix: String): String {
    return "$prefix.$translationKey"
}

public fun Identifier.shortTranslationKey(prefix: String): String {
    if (namespace == DEFAULT_NAMESPACE) {
        return "$prefix.$path"
    }

    return "$prefix.$translationKey"
}

@JvmName("createTranslatedText")
public fun Identifier.translatedText(prefix: String): Text {
    return MCText.translatable(translationKey(prefix))
}

@JvmName("createShortTranslatedText")
public fun Identifier.shortTranslatedText(prefix: String): Text {
    return MCText.translatable(shortTranslationKey(prefix))
}

public fun Identifier.withNamespace(namespace: String): Identifier {
    return identifierOrThrow(namespace, path)
}

public fun Identifier.withPath(path: String): Identifier {
    return identifierOrThrow(namespace, path)
}
