package dev.deftu.omnicore.api.client.compat.config

import org.jetbrains.annotations.ApiStatus

//#if FORGE-LIKE && MC >= 1.16.5
//$$ import dev.deftu.omnicore.internal.client.compat.config.ModernForgeConfigCompat
//#endif

@ApiStatus.Experimental
public object ConfigScreenRegistry {
    @Volatile private var factories = mutableMapOf<String, ConfigScreenProvider>()

    @Synchronized
    public fun register(modId: String, provider: ConfigScreenProvider) {
        factories[modId] = provider

        //#if FORGE-LIKE && MC >= 1.16.5
        //$$ // Register in-place on this mod's context
        //$$ ModernForgeConfigCompat.register(provider)
        //#endif
    }

    public fun get(modId: String): ConfigScreenProvider? {
        return factories[modId]
    }

    public fun all(): Map<String, ConfigScreenProvider> {
        return factories.toMap()
    }
}
