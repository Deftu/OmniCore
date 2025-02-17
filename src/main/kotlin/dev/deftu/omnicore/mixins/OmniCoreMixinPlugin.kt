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
            //#if FABRIC && MC <= 1.12.2 || FORGE && MC >= 1.16.5 && MC <= 1.17.1
            //$$ result.add("client.Mixin_ChatScreen_CommandAutoComplete")
            //$$ result.add("client.Mixin_Screen_CommandExecution")
            //#endif
        }

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
