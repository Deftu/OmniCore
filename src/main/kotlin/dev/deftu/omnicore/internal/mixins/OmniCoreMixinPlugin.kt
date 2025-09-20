package dev.deftu.omnicore.internal.mixins

//#if FABRIC || MC >= 1.16.5
import dev.deftu.omnicore.api.loader.OmniLoader
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

public class OmniCoreMixinPlugin : IMixinConfigPlugin {
    override fun getMixins(): MutableList<String> {
        val result = mutableListOf<String>()

        // Events
        if (OmniLoader.isPhysicalClient) {
            //#if FABRIC
            //#if MC >= 1.21.3
            result.add("client.events.Mixin_HudRenderEvent")
            //#endif

            //#if MC <= 1.12.2
            //$$ result.add("client.events.Mixin_ScreenEvent\$Init")
            //$$ result.add("client.events.Mixin_ScreenEvent\$Key")
            //$$ result.add("client.events.Mixin_ScreenEvent\$MouseButton")
            //$$ result.add("client.events.Mixin_ScreenEvent\$Render")
            //#endif

            result.add("client.events.Mixin_InputEvent\$Key")
            result.add("client.events.Mixin_InputEvent\$MouseButton")
            result.add("client.events.Mixin_RenderTickEvent")
            //#endif
        }

        // Accessors
        if (OmniLoader.isPhysicalClient) {
            //#if FABRIC
            //#if MC <= 1.8.9
            //$$ result.add("client.Mixin_AccessDeltaTickTracker")
            //#endif
            //#endif

            //#if MC == 1.16.5
            //$$ result.add("Mixin_Matrix3fGetters")
            //$$ result.add("Mixin_Matrix4fSetters")
            //#endif

            //#if MC >= 1.16.5
            result.add("client.Mixin_AccessBoundKey")
            result.add("client.Mixin_NativeImageAllocation")
            //#endif
        }

        //#if MC >= 1.19.4
        result.add("Mixin_ConnectionAccessor")
        //#endif

        //#if MC == 1.16.5
        //$$ result.add("Mixin_CustomPayloadDataAccessor")
        //#endif

        // Functionality
        if (OmniLoader.isPhysicalClient) {
            //#if FABRIC
            result.add("client.Mixin_ExecuteClientCommands")
            //#endif

            //#if FABRIC || MC >= 1.16.5
            result.add("client.Mixin_ForwardClientCustomPayloads")
            //#endif

            // What a mess...
            //#if FABRIC && MC <= 1.12.2 || FABRIC && MC >= 1.16.5 && MC <= 1.18.2 || FORGE && MC >= 1.16.5 && MC <= 1.17.1
            //$$ result.add("client.Mixin_MergeClientCommandsForAutoComplete")
            //#endif
        }

        //#if MC >= 1.20.6
        result.add("Mixin_SetUnknownPayloadData")
        result.add("Mixin_WriteCustomPacketBuffers")
        //#endif

        //#if MC >= 1.20.4
        result.add("Mixin_SmuggleUnknownPayloadData")
        //#endif

        //#if FABRIC || MC >= 1.16.5
        //#if MC >= 1.20.6
        result.add("Mixin_ForwardServerCustomPayload2EletricBoogaloo")
        //#endif

        result.add("Mixin_ForwardServerCustomPayloads")
        //#endif

        return result
    }

    override fun getRefMapperConfig(): String? = null
    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean = true

    override fun onLoad(mixinPackage: String) {
        // no-op
    }

    override fun acceptTargets(myTargets: MutableSet<String>, otherTargets: MutableSet<String>) {
        // no-op
    }

    override fun preApply(
        targetClassName: String,
        //#if FABRIC || MC >= 1.16.5
        targetClass: ClassNode,
        //#else
        //$$ targetClass: org.spongepowered.asm.lib.tree.ClassNode,
        //#endif
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
        // no-op
    }

    override fun postApply(
        targetClassName: String,
        //#if FABRIC || MC >= 1.16.5
        targetClass: ClassNode,
        //#else
        //$$ targetClass: org.spongepowered.asm.lib.tree.ClassNode,
        //#endif
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
        // no-op
    }
}
//#endif
