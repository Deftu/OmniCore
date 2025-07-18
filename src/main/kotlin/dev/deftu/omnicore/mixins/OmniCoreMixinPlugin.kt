package dev.deftu.omnicore.mixins

//#if FABRIC || MC >= 1.16.5
import dev.deftu.omnicore.common.OmniLoader
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

public class OmniCoreMixinPlugin : IMixinConfigPlugin {

    override fun getMixins(): MutableList<String> {
        val result = mutableListOf<String>()

        if (OmniLoader.isPhysicalClient) {
            //#if FABRIC
            result.add("client.Mixin_MinecraftClient_RenderTickEvent")
            //#endif

            //#if FABRIC || MC >= 1.16.5
            result.add("client.Mixin_ClientPlayNetworkHandler_CaptureCustomPayloads")
            //#endif

            //#if MC >= 1.16.5
            result.add("client.Mixin_NativeImage_Accessor")
            //#endif

            //#if FABRIC && MC <= 1.12.2 || FABRIC && MC >= 1.16.5 && MC <= 1.18.2 || FORGE && MC >= 1.16.5 && MC <= 1.17.1
            //$$ result.add("client.Mixin_ChatScreen_CommandAutoComplete")
            //#endif

            //#if FABRIC && MC <= 1.12.2 || MC >= 1.16.5
            result.add("client.Mixin_Screen_CommandExecution")
            //#endif

            //#if FABRIC && MC <= 1.12.2
            //$$ result.add("client.Mixin_Screen_InitScreenEvent")
            //$$ result.add("client.Mixin_GameRenderer_RenderScreenEvent")
            //$$ result.add("client.Mixin_Screen_KeyboardScreenEvent")
            //$$ result.add("client.Mixin_Screen_MouseScreenEvent")
            //#endif

            //#if FABRIC && MC == 1.8.9
            //$$ result.add("client.Mixin_MinecraftClient_TimerAccessor")
            //#endif
        }

        //#if MC >= 1.20.6
        result.add("common.Mixin_CustomPayload_CaptureCodecBuffer")
        result.add("common.Mixin_CustomPayload_BypassOmniPackets")
        //#endif

        //#if MC == 1.20.4
        //$$ result.add("common.Mixin_CustomPayloadC2SPacket_CapturePayloadData")
        //$$ result.add("common.Mixin_CustomPayloadS2CPacket_CapturePayloadData")
        //#endif

        //#if MC >= 1.20.4
        result.add("common.Mixin_UnknownCustomPayload_CapturePayloadData")
        //#endif

        //#if FABRIC || MC >= 1.16.5
        //#if MC == 1.16.5
        //$$ result.add("server.Mixin_CustomPayloadC2SPacket_FieldAccessor")
        //#endif

        //#if MC >= 1.20.6
        result.add("server.Mixin_ServerPlayNetworkHandler_CaptureOverridenCustomPayloads")
        //#endif

        result.add("server.Mixin_ServerPlayNetworkHandler_CaptureCustomPayloads")
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
