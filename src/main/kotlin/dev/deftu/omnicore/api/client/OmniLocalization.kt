package dev.deftu.omnicore.api.client

import net.minecraft.client.resources.language.I18n

public object OmniLocalization {
    @JvmStatic
    public operator fun get(key: String, vararg args: Any): String {
        return I18n.get(key, args)
    }

    @JvmStatic
    public operator fun contains(key: String): Boolean {
        //#if MC >= 1.12.2
        return I18n.exists(key)
        //#else
        //$$ val translated = I18n.format(key)
        //$$ return translated != key
        //#endif
    }
}
