package dev.deftu.omnicore.client.keybindings

import net.minecraft.client.option.KeyBinding

//#if MC >= 1.16.5
import dev.deftu.omnicore.client.OmniKeyboard
import dev.deftu.omnicore.mixins.client.Mixin_KeyBinding_AccessorBoundKey
//#endif

public class WrappedKeyBinding(override val vanillaKeyBinding: KeyBinding) : MCKeyBinding {
    override val name: String = this.vanillaKeyBinding.translationKey

    override val category: String = this.vanillaKeyBinding.category

    override val type: OmniKeyBinding.KeyBindingType
        get() {
            val keyCode =
                //#if MC >= 1.16.5
                (this.vanillaKeyBinding as Mixin_KeyBinding_AccessorBoundKey).boundKey?.code ?: OmniKeyboard.KEY_NONE
                //#else
                //$$ this.vanillaKeyBinding.keyCode
                //#endif
            return if (keyCode < 0) {
                OmniKeyBinding.KeyBindingType.MOUSE
            } else {
                OmniKeyBinding.KeyBindingType.KEY
            }
        }

    override val defaultValue: Int
        get() {
            //#if MC >= 1.16.5
            return this.vanillaKeyBinding.defaultKey.code
            //#else
            //$$ return this.vanillaKeyBinding.keyCodeDefault
            //#endif
        }

    override fun matchesMouse(button: Int): Boolean {
        //#if MC >= 1.16.5
        return this.vanillaKeyBinding.matchesMouse(button)
        //#else
        //$$ return button == this.vanillaKeyBinding.keyCode
        //#endif
    }

    override fun matchesKey(keyCode: Int, scancode: Int): Boolean {
        //#if MC >= 1.16.5
        return this.vanillaKeyBinding.matchesKey(keyCode, scancode)
        //#else
        //$$ return keyCode == this.vanillaKeyBinding.keyCode
        //#endif
    }
}
