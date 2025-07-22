package dev.deftu.omnicore.client.keybindings

import net.minecraft.client.option.KeyBinding

//#if MC >= 1.16.5
import net.minecraft.client.util.InputUtil
//#else
//$$ import dev.deftu.omnicore.client.OmniKeyboard
//#endif

public data class ManagedKeyBinding @JvmOverloads constructor(
    override val name: String,
    override val category: String,
    override val defaultValue: Int,
    val type: OmniKeyBinding.KeyBindingType = OmniKeyBinding.KeyBindingType.KEY
) : MCKeyBinding {

    override val vanillaKeyBinding: KeyBinding by lazy {
        KeyBinding(
            name,
            //#if MC >= 1.16.5
            if (type == OmniKeyBinding.KeyBindingType.KEY) InputUtil.Type.KEYSYM else InputUtil.Type.MOUSE,
            //#endif
            defaultValue,
            category
        )
    }

    override val isDefault: Boolean
        get() {
            //#if MC >= 1.16.5
            return this.vanillaKeyBinding.isDefault
            //#else
            //$$ return this.vanillaKeyBinding.keyCode == this.defaultValue
            //#endif
        }

    override val isUnbound: Boolean
        get() {
            //#if MC >= 1.16.5
            return this.vanillaKeyBinding.isUnbound
            //#else
            //$$ return this.vanillaKeyBinding.keyCode == OmniKeyboard.KEY_NONE
            //#endif
        }

    override val isPressed: Boolean
        get() = !isUnbound && this.vanillaKeyBinding.isPressed

    override fun matchesMouse(button: Int): Boolean {
        return this.type == OmniKeyBinding.KeyBindingType.MOUSE && (
            //#if MC >= 1.16.5
            this.vanillaKeyBinding.matchesMouse(button)
            //#else
            //$$ button == this.vanillaKeyBinding.keyCode
            //#endif
        )
    }

    override fun matchesKey(keyCode: Int, scancode: Int): Boolean {
        return this.type == OmniKeyBinding.KeyBindingType.KEY && (
            //#if MC >= 1.16.5
            this.vanillaKeyBinding.matchesKey(keyCode, scancode)
            //#else
            //$$ keyCode == this.vanillaKeyBinding.keyCode
            //#endif
        )
    }

    override fun consume(): Boolean {
        return this.vanillaKeyBinding.wasPressed()
    }

}
