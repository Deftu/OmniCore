package dev.deftu.omnicore.client.keybindings

import net.minecraft.client.option.KeyBinding

//#if MC <= 1.12.2
//$$ import dev.deftu.omnicore.client.OmniKeyboard
//#endif

public class WrappedKeyBinding(override val vanillaKeyBinding: KeyBinding) : MCKeyBinding {

    override val name: String = this.vanillaKeyBinding.translationKey

    override val category: String = this.vanillaKeyBinding.category

    override val defaultValue: Int
        get() {
            //#if MC >= 1.16.5
            return this.vanillaKeyBinding.defaultKey.code
            //#else
            //$$ return this.vanillaKeyBinding.keyCodeDefault
            //#endif
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

    override fun consume(): Boolean {
        return this.vanillaKeyBinding.wasPressed()
    }
}
