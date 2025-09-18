package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.omnicore.api.client.input.OmniInputCode
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.identifierOrThrow
import net.minecraft.client.option.KeyBinding
import net.minecraft.util.Identifier

//#if MC >= 1.21.9
//$$ import net.minecraft.client.input.KeyEvent
//$$ import net.minecraft.client.input.MouseButtonEvent
//$$ import net.minecraft.client.input.MouseButtonInfo
//#endif

public class WrappedKeyBinding(override val vanillaKeyBinding: KeyBinding) : MCKeyBinding {
    override val name: String = this.vanillaKeyBinding.translationKey

    override val category: Identifier =
        //#if MC >= 1.21.9
        //$$ this.vanillaKeyBinding.category.id
        //#else
        identifierOrThrow(this.vanillaKeyBinding.category)
        //#endif

    override val type: OmniKeyBinding.KeyBindingType
        get() {
            return when (this.boundValue) {
                is OmniKey -> OmniKeyBinding.KeyBindingType.KEY
                is OmniMouseButton -> OmniKeyBinding.KeyBindingType.MOUSE
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
        //#if MC >= 1.21.9
        //$$ return this.vanillaKeyBinding.matchesMouse(MouseButtonEvent(0.0, 0.0, MouseButtonInfo(button, 0)))
        //#elseif MC >= 1.16.5
        return this.vanillaKeyBinding.matchesMouse(button)
        //#else
        //$$ return button == this.vanillaKeyBinding.keyCode
        //#endif
    }

    override fun matchesKey(keyCode: Int, scancode: Int): Boolean {
        //#if MC >= 1.21.9
        //$$ return this.vanillaKeyBinding.matches(KeyEvent(keyCode, scancode, 0))
        //#elseif MC >= 1.16.5
        return this.vanillaKeyBinding.matchesKey(keyCode, scancode)
        //#else
        //$$ return keyCode == this.vanillaKeyBinding.keyCode
        //#endif
    }
}
