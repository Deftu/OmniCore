package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.omnicore.api.client.input.OmniInputCode
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import net.minecraft.client.option.KeyBinding

public data class ManagedKeyBinding @JvmOverloads constructor(
    override val name: String,
    override val category: String,
    override val defaultValue: OmniInputCode,
    override val type: OmniKeyBinding.KeyBindingType = when (defaultValue) {
        is OmniKey -> OmniKeyBinding.KeyBindingType.KEY
        is OmniMouseButton -> OmniKeyBinding.KeyBindingType.MOUSE
    }
) : MCKeyBinding {
    override val vanillaKeyBinding: KeyBinding by lazy {
        KeyBinding(
            name,
            //#if MC >= 1.16.5
            type.vanilla,
            //#endif
            defaultValue.code,
            category
        )
    }

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
}
