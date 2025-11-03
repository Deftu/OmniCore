@file:JvmName("OmniResourceLocation")

package dev.deftu.omnicore.api

import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCText
import net.minecraft.resources.ResourceLocation

public const val DEFAULT_NAMESPACE: String = "minecraft"

public inline val ResourceLocation.translationKey: String
    @JvmName("translationKey")
    get() = "${namespace}.${path}"

public inline val ResourceLocation.shortTranslationKey: String
    @JvmName("shortTranslationKey")
    get() {
        if (namespace == DEFAULT_NAMESPACE) {
            return path
        }

        return translationKey
    }

public inline val ResourceLocation.translatedText: Text
    @JvmName("createTranslatedText")
    get() = MCText.translatable(translationKey)

public inline val ResourceLocation.shortTranslatedText: Text
    @JvmName("createShortTranslatedText")
    get() = MCText.translatable(shortTranslationKey)

public inline val ResourceLocation.isMinecraft: Boolean
    get() = namespace == DEFAULT_NAMESPACE

@JvmName("createOrNull")
public fun locationOrNull(namespace: String, path: String): ResourceLocation? {
    //#if MC >= 1.19.2
    return ResourceLocation.tryBuild(namespace, path)
    //#else
    //$$ return try {
    //$$     ResourceLocation(namespace, path)
    //$$ } catch (_: Exception) {
    //$$     null
    //$$ }
    //#endif
}

@JvmName("createOrNull")
public fun locationOrNull(path: String): ResourceLocation? {
    //#if MC >= 1.19.2
    return ResourceLocation.tryParse(path)
    //#else
    //$$ return try {
    //$$     ResourceLocation(path)
    //$$ } catch (_: Exception) {
    //$$     null
    //$$ }
    //#endif
}

@JvmName("createOrThrow")
public fun locationOrThrow(namespace: String, path: String): ResourceLocation {
    return locationOrNull(namespace, path) ?: throw IllegalArgumentException("Invalid identifier")
}

@JvmName("createOrThrow")
public fun locationOrThrow(path: String): ResourceLocation {
    return locationOrNull(path) ?: throw IllegalArgumentException("Invalid identifier")
}

public fun ResourceLocation.translationKey(prefix: String): String {
    return "$prefix.$translationKey"
}

public fun ResourceLocation.shortTranslationKey(prefix: String): String {
    if (namespace == DEFAULT_NAMESPACE) {
        return "$prefix.$path"
    }

    return "$prefix.$translationKey"
}

@JvmName("createTranslatedText")
public fun ResourceLocation.translatedText(prefix: String): Text {
    return MCText.translatable(translationKey(prefix))
}

@JvmName("createShortTranslatedText")
public fun ResourceLocation.shortTranslatedText(prefix: String): Text {
    return MCText.translatable(shortTranslationKey(prefix))
}

public fun ResourceLocation.withNamespace(namespace: String): ResourceLocation {
    return locationOrThrow(namespace, path)
}

public fun ResourceLocation.withPath(path: String): ResourceLocation {
    return locationOrThrow(namespace, path)
}
