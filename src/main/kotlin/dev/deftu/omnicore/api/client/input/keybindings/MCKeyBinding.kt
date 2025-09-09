package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.omnicore.api.client.OmniLocalization
import dev.deftu.omnicore.api.client.input.OmniInputCode
import dev.deftu.omnicore.api.client.input.OmniKeys
import net.minecraft.client.option.KeyBinding

//#if FORGE-LIKE && MC >= 1.19.2
//$$ import dev.deftu.omnicore.common.OmniLoader
//#endif

//#if MC >= 1.16.5
import dev.deftu.omnicore.internal.mixins.client.Mixin_AccessBoundKey
//#endif

//#if FABRIC
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
//#else
//$$ import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper
//#endif
//#elseif FORGE && MC >= 1.19.2
//$$ import net.minecraftforge.client.event.RegisterKeyMappingsEvent
//#elseif NEOFORGE && MC >= 1.19.2
//$$ import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
//#endif

//#if FORGE && MC <= 1.18.2
//#if MC <= 1.16.5
//$$ import net.minecraftforge.fml.client.registry.ClientRegistry
//#elseif MC <= 1.17.1
//$$ import net.minecraftforge.fmlclient.registry.ClientRegistry
//#else
//$$ import net.minecraftforge.client.ClientRegistry
//#endif
//#endif

public interface MCKeyBinding : OmniKeyBinding {
    public companion object {
        //#if FORGE-LIKE && MC >= 1.19.2
        //$$ internal val registeredBindings = mutableListOf<MCKeyBinding>()
        //#endif

        @JvmStatic // Just so that we don't have that ugly `.Companion` suggestion when referencing `MCKeyBinding`.
        public fun initialize() {
            //#if FORGE-LIKE && MC >= 1.19.2
            //$$ OmniLoader.modEventBus.addListener(::registerAll)
            //#endif
        }

        //#if FORGE-LIKE && MC >= 1.19.2
        //$$ internal fun registerAll(event: RegisterKeyMappingsEvent) {
        //$$     registeredBindings.map(MCKeyBinding::vanillaKeyBinding).forEach(event::register)
        //$$ }
        //#endif
    }

    public val vanillaKeyBinding: KeyBinding

    public val translatedName: String
        get() {
            //#if MC >= 1.16.5
            return OmniLocalization[this.vanillaKeyBinding.translationKey]
            //#else
            //$$ return OmniLocalization[this.vanillaKeyBinding.keyDescription]
            //#endif
        }

    override var boundValue: OmniInputCode
        get() {
            //#if MC >= 1.16.5
            return (this.vanillaKeyBinding as Mixin_AccessBoundKey).boundKey?.code?.let(type::code) ?: OmniKeys.KEY_NONE
            //#else
            //$$ return type.code(this.vanillaKeyBinding.keyCode)
            //#endif
        }
        set(value) {
            //#if MC >= 1.16.5
            this.vanillaKeyBinding.setBoundKey(type.vanilla.createFromCode(value.code))
            //#else
            //$$ this.vanillaKeyBinding.keyCode = value.code
            //#endif
        }

    override val isDefault: Boolean
        get() {
            //#if MC >= 1.16.5
            return this.vanillaKeyBinding.isDefault
            //#else
            //$$ return this.vanillaKeyBinding.keyCode == this.defaultValue.code
            //#endif
        }

    override val isUnbound: Boolean
        get() {
            //#if MC >= 1.16.5
            return this.vanillaKeyBinding.isUnbound
            //#else
            //$$ return this.vanillaKeyBinding.keyCode == OmniKeys.KEY_NONE.code
            //#endif
        }

    override val isPressed: Boolean
        get() = !isUnbound && this.vanillaKeyBinding.isPressed

    public fun register(): MCKeyBinding = this@MCKeyBinding.apply {
        //#if FABRIC
        KeyBindingHelper.registerKeyBinding(this.vanillaKeyBinding)
        //#elseif FORGE && MC <= 1.18.2
        //$$ ClientRegistry.registerKeyBinding(this.vanillaKeyBinding)
        //#else
        //$$ registeredBindings.add(this)
        //#endif
    }

    override fun consume(): Boolean {
        return this.vanillaKeyBinding.wasPressed()
    }
}
