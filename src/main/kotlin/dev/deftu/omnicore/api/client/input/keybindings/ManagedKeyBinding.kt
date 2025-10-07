package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.omnicore.api.client.input.OmniInputCode
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.translationKey
import net.minecraft.client.option.KeyBinding
import net.minecraft.util.Identifier

//#if MC >= 1.21.9
import net.minecraft.client.input.KeyInput
import net.minecraft.client.gui.Click
import net.minecraft.client.input.MouseInput
//#endif

public data class ManagedKeyBinding @JvmOverloads constructor(
    override val name: String,
    override val category: Identifier,
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
            //#if MC >= 1.21.9
            KeyBinding.Category(category),
            //#else
            //$$ category.translationKey("key.category"),
            //#endif
        )
    }

    override fun matchesMouse(button: Int): Boolean {
        return this.type == OmniKeyBinding.KeyBindingType.MOUSE && (
            //#if MC >= 1.21.9
            this.vanillaKeyBinding.matchesMouse(Click(0.0, 0.0, MouseInput(button, 0)))
            //#elseif MC >= 1.16.5
            //$$ this.vanillaKeyBinding.matchesMouse(button)
            //#else
            //$$ button == this.vanillaKeyBinding.keyCode
            //#endif
        )
    }

    override fun matchesKey(keyCode: Int, scancode: Int): Boolean {
        return this.type == OmniKeyBinding.KeyBindingType.KEY && (
            //#if MC >= 1.21.9
            this.vanillaKeyBinding.matchesKey(KeyInput(keyCode, scancode, 0))
            //#elseif MC >= 1.16.5
            //$$ this.vanillaKeyBinding.matches(keyCode, scancode)
            //#else
            //$$ keyCode == this.vanillaKeyBinding.keyCode
            //#endif
        )
    }
}
