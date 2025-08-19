package dev.deftu.omnicore.client.keybindings

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.client.option.KeyBinding

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
        @GameSide(Side.CLIENT)
        public fun create(
            name: String,
            category: String,
            defaultValue: Int,
            type: KeyBindingType = KeyBindingType.KEY
        ): MCKeyBinding {
            return ManagedKeyBinding(name, category, defaultValue, type)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
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
    }

    public val name: String

    public val category: String

    public val type: KeyBindingType

    public val defaultValue: Int

    public var boundValue: Int

    public val isDefault: Boolean

    public val isUnbound: Boolean

    public val isPressed: Boolean

    public fun matchesMouse(button: Int): Boolean

    public fun matchesKey(keyCode: Int, scancode: Int): Boolean

    public fun matchesKey(keyCode: Int): Boolean {
        return matchesKey(keyCode, -1)
    }

    /**
     * Effectively acts as a `wasPressed` property, permitting you to view a toggle-like state of this
     * key binding if it's been pressed since the last time this method was called.
     */
    public fun consume(): Boolean
}
