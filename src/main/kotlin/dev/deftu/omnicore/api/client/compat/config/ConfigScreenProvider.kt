package dev.deftu.omnicore.api.client.compat.config

import net.minecraft.client.gui.screen.Screen
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public fun interface ConfigScreenProvider {
    public fun build(parentScreen: Screen?): Screen
}
