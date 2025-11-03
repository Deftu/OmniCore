package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.omnicore.api.client.input.OmniInputCode
import dev.deftu.omnicore.api.client.input.OmniInputs
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniKeys
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import net.minecraft.client.KeyMapping
import net.minecraft.resources.ResourceLocation

//#if MC >= 1.21.9
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.client.input.MouseButtonInfo
//#else
//$$ import dev.deftu.omnicore.api.locationOrThrow
//#endif

//#if MC >= 1.16.5
import dev.deftu.omnicore.internal.mixins.client.Mixin_AccessBoundKey
//#endif

public class WrappedKeyBinding(override val vanillaKeyBinding: KeyMapping) : MCKeyBinding {
    override val name: String = this.vanillaKeyBinding.name

    override val category: ResourceLocation =
        //#if MC >= 1.21.9
        this.vanillaKeyBinding.category.id
        //#else
        //$$ locationOrThrow(this.vanillaKeyBinding.category)
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
            return this.type.code(this.vanillaKeyBinding.defaultKey.value)
            //#else
            //$$ return this.type.code(this.vanillaKeyBinding.keyCodeDefault)
            //#endif
        }

    override var boundValue: OmniInputCode
        get() {
            val code =
                //#if MC >= 1.16.5
                (this.vanillaKeyBinding as Mixin_AccessBoundKey).key?.value ?: return OmniKeys.KEY_NONE
                //#else
                //$$ this.vanillaKeyBinding.keyCode
                //#endif

            return OmniInputs.get(code)
        }
        set(value) {
            super.boundValue = value
        }

    override fun matchesMouse(button: Int): Boolean {
        //#if MC >= 1.21.9
        return this.vanillaKeyBinding.matchesMouse(MouseButtonEvent(0.0, 0.0, MouseButtonInfo(button, 0)))
        //#elseif MC >= 1.16.5
        //$$ return this.vanillaKeyBinding.matchesMouse(button)
        //#else
        //$$ return button == this.vanillaKeyBinding.keyCode
        //#endif
    }

    override fun matchesKey(keyCode: Int, scancode: Int): Boolean {
        //#if MC >= 1.21.9
        return this.vanillaKeyBinding.matches(KeyEvent(keyCode, scancode, 0))
        //#elseif MC >= 1.16.5
        //$$ return this.vanillaKeyBinding.matches(keyCode, scancode)
        //#else
        //$$ return keyCode == this.vanillaKeyBinding.keyCode
        //#endif
    }
}
