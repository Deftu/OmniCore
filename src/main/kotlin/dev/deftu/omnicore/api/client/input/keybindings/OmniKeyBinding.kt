package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.omnicore.api.client.input.OmniInputCode
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import net.minecraft.client.option.KeyBinding
import net.minecraft.util.Identifier

//#if MC >= 1.16.5
import net.minecraft.client.util.InputUtil
//#endif

/**
 * A contract for key bindings in OmniCore. Declared so that we can
 * have a common interface should the consumer want to use their own
 * key binding system. This also allows us to intuitively have a common
 * vanilla key binding implementation, which backs our own managed key bindings
 * and any which may be wrapped by our consumer.
 */
public interface OmniKeyBinding {
    public companion object {
        @JvmStatic
        @JvmOverloads
        public fun create(
            name: String,
            category: Identifier,
            defaultValue: OmniInputCode,
            type: KeyBindingType = KeyBindingType.KEY
        ): MCKeyBinding {
            return ManagedKeyBinding(name, category, defaultValue, type)
        }

        @JvmStatic
        public fun wrap(vanillaKeyBinding: KeyBinding): MCKeyBinding {
            return WrappedKeyBinding(vanillaKeyBinding)
        }
    }

    public enum class KeyBindingType {
        KEY,
        MOUSE;

        //#if MC >= 1.16.5
        public val vanilla: InputUtil.Type
            get() = when (this) {
                KEY -> InputUtil.Type.KEYSYM
                MOUSE -> InputUtil.Type.MOUSE
            }
        //#endif

        public fun code(code: Int): OmniInputCode {
            return when (this) {
                KEY -> OmniKey(code)
                MOUSE -> OmniMouseButton(code)
            }
        }
    }

    public val name: String

    public val category: Identifier

    public val type: KeyBindingType

    public val defaultValue: OmniInputCode

    public var boundValue: OmniInputCode

    public val isDefault: Boolean

    public val isUnbound: Boolean

    public val isPressed: Boolean

    public fun matchesMouse(button: Int): Boolean

    public fun matchesMouse(button: OmniInputCode): Boolean {
        return matchesMouse(button.code)
    }

    public fun matchesKey(keyCode: Int, scancode: Int): Boolean

    public fun matchesKey(keyCode: Int): Boolean {
        return matchesKey(keyCode, -1)
    }

    public fun matchesKey(key: OmniInputCode): Boolean {
        return matchesKey(key.code)
    }

    /**
     * Effectively acts as a `wasPressed` property, permitting you to view a toggle-like state of this
     * key binding if it's been pressed since the last time this method was called.
     */
    public fun consume(): Boolean
}
