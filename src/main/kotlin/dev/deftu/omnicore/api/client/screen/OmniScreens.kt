@file:JvmName("OmniScreens")

package dev.deftu.omnicore.api.client.screen

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.scheduling.TickSchedulers
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.gui.screens.Screen

//#if MC >= 1.16.5
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
//#else
//$$ import net.minecraft.client.gui.inventory.GuiContainer
//#endif

public var currentScreen: Screen?
    get() = client.screen
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
        return currentScreen is AbstractContainerScreen<*>
        //#else
        //$$ return currentScreen is GuiContainer
        //#endif
    }

public val Class<out Screen>.isCurrentScreen: Boolean
    get() = this == currentScreen?.javaClass

@JvmOverloads
public fun Screen.openScreen(delayTicks: Int = 0) {
    if (delayTicks <= 0) {
        currentScreen = this
    } else {
        TickSchedulers.client.after(delayTicks) {
            currentScreen = this
        }
    }
}

public fun closeScreen() {
    currentScreen = null
}
