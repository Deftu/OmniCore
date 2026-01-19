package dev.deftu.omnicore.internal.client.compat.config

//#if FABRIC && MC >= 1.16.5 && MC < 26.1
import com.terraformersmc.modmenu.api.ConfigScreenFactory as ModMenuConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.deftu.omnicore.api.client.compat.config.ConfigScreenRegistry
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class ModMenuCompat : ModMenuApi {
    override fun getProvidedConfigScreenFactories(): Map<String?, ModMenuConfigScreenFactory<*>?>? {
        return buildMap {
            for ((modId, factory) in ConfigScreenRegistry.all()) {
                this[modId] = ModMenuConfigScreenFactory { parent ->
                    factory.build(parent)
                }
            }
        }
    }
}
//#endif
