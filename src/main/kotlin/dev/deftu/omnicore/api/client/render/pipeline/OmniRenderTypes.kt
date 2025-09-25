package dev.deftu.omnicore.api.client.render.pipeline

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import org.jetbrains.annotations.ApiStatus

//#if MC < 1.21.5
//$$ import dev.deftu.omnicore.internal.client.render.pipeline.OmniRenderPipelineImpl
//#endif

@ApiStatus.Experimental
public object OmniRenderTypes {
    public class Builder internal constructor(private val pipeline: OmniRenderPipeline) {
        @JvmField public var texture: RenderPhase.TextureBase? = null
        @JvmField public var lightmap: RenderPhase.Lightmap? = null
        @JvmField public var overlay: RenderPhase.Overlay? = null
        @JvmField public var layering: RenderPhase.Layering? = null
        @JvmField public var target: RenderPhase.Target? = null

        public fun setTexture(texture: RenderPhase.TextureBase): Builder {
            this.texture = texture
            return this
        }

        public fun setLightmap(lightmap: RenderPhase.Lightmap): Builder {
            this.lightmap = lightmap
            return this
        }

        public fun setOverlay(overlay: RenderPhase.Overlay): Builder {
            this.overlay = overlay
            return this
        }

        public fun setLayering(layering: RenderPhase.Layering): Builder {
            this.layering = layering
            return this
        }

        public fun setTarget(target: RenderPhase.Target): Builder {
            this.target = target
            return this
        }

        public fun build(): RenderLayer {
            return factory(pipeline) { builder ->
                texture?.let(builder::texture)
                lightmap?.let(builder::lightmap)
                overlay?.let(builder::overlay)
                layering?.let(builder::layering)
                target?.let(builder::target)
            }
        }
    }

    @JvmStatic
    public fun builder(pipeline: OmniRenderPipeline): Builder {
        return Builder(pipeline)
    }

    @JvmStatic
    @Suppress("ControlFlowWithEmptyBody")
    public fun factory(
        pipeline: OmniRenderPipeline,
        builder: (RenderLayer.MultiPhaseParameters.Builder) -> Unit
    ): RenderLayer {
        return RenderLayer.of(
            pipeline.location.toString(),
            //#if MC < 1.21.5
            //$$ pipeline.vertexFormat,
            //$$ pipeline.drawMode.vanilla,
            //#endif
            RenderLayer.DEFAULT_BUFFER_SIZE,
            true,
            false,
            //#if MC >= 1.21.5
            pipeline.vanilla,
            //#endif
            RenderLayer.MultiPhaseParameters.builder()
                .apply(builder)
                .also { builder ->
                    //#if MC < 1.21.5
                    //$$ val pipeline = pipeline as OmniRenderPipelineImpl
                    //$$
                    //$$
                    //#endif
                }.build(true)
        )
    }
}
