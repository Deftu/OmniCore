@file:JvmName("OmniScreens")

package dev.deftu.omnicore.api.client.screen

import dev.deftu.omnicore.api.client.client
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.Screen

//#if MC >= 1.16.5
import net.minecraft.client.gui.screen.ingame.HandledScreen
//#else
//$$ import net.minecraft.client.gui.inventory.GuiContainer
//#endif

public var currentScreen: Screen?
    get() = client.currentScreen
    set(value) {
        client.setScreen(value)
    }

public val isInScreen: Boolean
    get() = currentScreen != null

public val isInChatScreen: Boolean
    get() = currentScreen is ChatScreen

public val isInInventoryLikeScreen: Boolean
    get() {
        //#if MC >= 1.16.5
        return currentScreen is HandledScreen<*>
        //#else
        //$$ return currentScreen is GuiContainer
        //#endif
    }

public val Class<out Screen>.isCurrentScreen: Boolean
    get() = this == currentScreen?.javaClass

public fun closeScreen() {
    currentScreen = null
}
