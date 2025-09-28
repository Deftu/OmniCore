package dev.deftu.omnicore.api.client.render.pipeline

//#if MC >= 1.21.5
import com.mojang.blaze3d.pipeline.RenderPipeline
import dev.deftu.omnicore.api.loader.OmniLoader
import net.irisshaders.iris.api.v0.IrisApi
import net.irisshaders.iris.api.v0.IrisProgram
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
//#endif

public enum class IrisShaderType {
    BASIC,
    TEXTURED,
    TERRAIN,
    TERRAIN_SOLID,
    TERRAIN_CUTOUT,
    TRANSLUCENT,
    SKY_BASIC,
    SKY_TEXTURED,
    ARMOR_GLINT,
    ENTITIES,
    ENTITIES_TRANSLUCENT,
    CLOUDS,
    BLOCK,
    BLOCK_TRANSLUCENT,
    HAND,
    HAND_TRANSLUCENT,
    PARTICLES,
    PARTICLES_TRANSLUCENT,
    EMISSIVE_ENTITIES,
    BEACON_BEAM,
    LINES;

    //#if MC >= 1.21.5
    public val iris: IrisProgram
        get() {
            return IrisProgram.valueOf(this.name)
        }

    public fun assign(vanilla: RenderPipeline) {
        if (!OmniLoader.isLoaded("iris")) {
            return
        }

        IrisApi.getInstance().assignPipeline(vanilla, iris)
    }

    public companion object {
        private val logger = LogManager.getLogger("OmniCore/Iris Compatibility")

        @JvmStatic
        public fun warnIfNecessary(location: Identifier) {
            if (!OmniLoader.isDevelopment && !OmniLoader.isLoaded("iris")) {
                return
            }

            logger.warn(
                "Iris is loaded. The OmniCore pipeline registered @ $location likely won't render as intended. " +
                "Please define an ${IrisShaderType::class.java.simpleName} to create your desired pipeline."
            )
        }
    }
    //#endif
}
