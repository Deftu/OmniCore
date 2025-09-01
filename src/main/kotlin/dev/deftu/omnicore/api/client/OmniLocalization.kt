package dev.deftu.omnicore.api.client

import net.minecraft.client.resource.language.I18n

public object OmniLocalization {
    @JvmStatic
    public fun get(key: String, vararg args: Any): String {
        return I18n.translate(key, args)
    }

    @JvmStatic
    public fun has(key: String): Boolean {
        return I18n.hasTranslation(key)
    }
}
