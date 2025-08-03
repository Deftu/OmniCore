package dev.deftu.omnicore.common

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.util.Identifier

public object OmniIdentifier {

    public const val MINECRAFT_NAMESPACE: String = "minecraft"

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun create(namespace: String, path: String): Identifier {
        //#if MC >= 1.19.2
        return Identifier.tryParse(namespace, path) ?: throw IllegalArgumentException("Invalid identifier")
        //#else
        //$$ return Identifier(namespace, path)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun create(path: String): Identifier {
        //#if MC >= 1.19.2
        return Identifier.tryParse(path) ?: throw IllegalArgumentException("Invalid identifier")
        //#else
        //$$ return Identifier(path)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun toTranslationKey(identifier: Identifier): String {
        return "${identifier.namespace}.${identifier.path}"
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun toTranslationKey(identifier: Identifier, prefix: String): String {
        return "${prefix}.${toTranslationKey(identifier)}"
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun toShortTranslationKey(identifier: Identifier): String {
        return if (identifier.namespace.equals(MINECRAFT_NAMESPACE)) identifier.path else toTranslationKey(identifier)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun withNamespace(identifier: Identifier, namespace: String): Identifier {
        return create(namespace, identifier.path)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun withPath(identifier: Identifier, path: String): Identifier {
        return create(identifier.namespace, path)
    }

}

public fun Identifier.translationKey(): String {
    return OmniIdentifier.toTranslationKey(this)
}

public fun Identifier.translationKey(prefix: String): String {
    return OmniIdentifier.toTranslationKey(this, prefix)
}

public fun Identifier.shortTranslationKey(): String {
    return OmniIdentifier.toShortTranslationKey(this)
}
