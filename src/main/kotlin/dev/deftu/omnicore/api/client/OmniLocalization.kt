package dev.deftu.omnicore.api.client

import net.minecraft.client.resource.language.I18n

public object OmniLocalization {
    @JvmStatic
    public operator fun get(key: String, vararg args: Any): String {
        return I18n.translate(key, args)
    }

    @JvmStatic
    public operator fun contains(key: String): Boolean {
        //#if MC >= 1.12.2
        return I18n.hasTranslation(key)
        //#else
        //$$ val translated = I18n.translate(key)
        //$$ return translated != key
        //#endif
    }
}
