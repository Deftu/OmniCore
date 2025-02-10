package dev.deftu.omnicore.client

//#if MC >= 1.16.5
import net.minecraft.client.util.InputUtil
//#endif

//#if FABRIC
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
//#else
//$$ import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper
//#endif
//#elseif FORGE && MC >= 1.19.2
//$$ import net.minecraftforge.client.event.RegisterKeyMappingsEvent
//#elseif NEOFORGE && MC >= 1.19.2
//$$ import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
//#endif

//#if FORGE && MC <= 1.18.2
//#if MC <= 1.16.5
//$$ import net.minecraftforge.fml.client.registry.ClientRegistry
//#elseif MC <= 1.17.1
//$$ import net.minecraftforge.fmlclient.registry.ClientRegistry
//#else
//$$ import net.minecraftforge.client.ClientRegistry
//#endif
//#endif

import net.minecraft.client.option.KeyBinding

public data class OmniKeyBinding @JvmOverloads constructor(
    val name: String,
    val category: String,
    val defaultValue: Int,
    val type: Type = Type.KEY
) {

    public enum class Type {
        KEY,
        MOUSE
    }

    public fun matchesMouse(button: Int): Boolean {
        return type == Type.MOUSE && (
                //#if MC >= 1.16.5
                toVanilla().matchesMouse(button)
                //#else
                //$$ button == toVanilla().getKeyCode()
                //#endif
        )
    }

    @JvmOverloads
    public fun matchesKey(keyCode: Int, scancode: Int = -1): Boolean {
        return type == Type.KEY && (
                //#if MC >= 1.16.5
                toVanilla().matchesKey(keyCode, scancode)
                //#else
                //$$ keyCode == toVanilla().getKeyCode()
                //#endif
        )
    }

    public fun toVanilla(): KeyBinding {
        return KeyBinding(
            name,
            //#if MC >= 1.16.5
            if (type == Type.KEY) InputUtil.Type.KEYSYM else InputUtil.Type.MOUSE,
            //#endif
            defaultValue,
            category
        )
    }

    public fun attemptRegister() {
        //#if FABRIC
        KeyBindingHelper.registerKeyBinding(this.toVanilla())
        //#elseif FORGE && MC <= 1.18.2
        //$$ ClientRegistry.registerKeyBinding(this.toVanilla())
        //#else
        //$$ println("Attempted to register keybinding using attemptRegister on Forge 1.19.2+. Use register on the relevant event instead.")
        //#endif
    }

    //#if FORGE-LIKE && MC >= 1.19.2
    //$$ public fun register(event: RegisterKeyMappingsEvent) {
    //$$     event.register(this.toVanilla())
    //$$ }
    //#endif

}
