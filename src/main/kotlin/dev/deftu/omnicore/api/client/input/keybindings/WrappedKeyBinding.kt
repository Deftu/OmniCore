package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.omnicore.api.client.input.OmniInputCode
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import net.minecraft.client.option.KeyBinding

public class WrappedKeyBinding(override val vanillaKeyBinding: KeyBinding) : MCKeyBinding {
    override val name: String = this.vanillaKeyBinding.translationKey

    override val category: String = this.vanillaKeyBinding.category

    override val type: OmniKeyBinding.KeyBindingType
        get() {
            return when (val code = boundValue) {
                is OmniKey -> OmniKeyBinding.KeyBindingType.KEY
                is OmniMouseButton -> OmniKeyBinding.KeyBindingType.MOUSE
                else -> throw IllegalStateException("Unknown key binding type for code: $code")
            }
        }

    override val defaultValue: OmniInputCode
        get() {
            //#if MC >= 1.16.5
            return this.type.code(this.vanillaKeyBinding.defaultKey.code)
            //#else
            //$$ return this.type.code(this.vanillaKeyBinding.keyCodeDefault)
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
