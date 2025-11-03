package dev.deftu.omnicore.api.client.render.provider

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.shader.ShaderSchema

//#if MC >= 1.21.5
import com.mojang.blaze3d.shaders.UniformType
import net.minecraft.resources.ResourceLocation
//#else
//$$ import dev.deftu.omnicore.api.client.render.state.OmniBlendState
//$$ import dev.deftu.omnicore.api.client.render.shader.OmniShader
//$$ import dev.deftu.omnicore.api.client.render.shader.OmniShaders
//#endif

//#if MC >= 1.17.1 && MC < 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//$$ import dev.deftu.omnicore.internal.client.render.shader.VanillaShader
//$$ import net.minecraft.client.renderer.CompiledShaderProgram
//$$ import java.util.function.Supplier
//#else
import dev.deftu.omnicore.internal.client.render.shader.CompatibleShader
//#endif

public sealed interface ShaderProvider {
    public class Compatible(
        public val vertexFormat: VertexFormat,

        public val vertexSource: String,
        public val fragmentSource: String,
        public val schema: ShaderSchema,
    ) : ShaderProvider {
        //#if MC < 1.21.5
        //$$ internal lateinit var shader: OmniShader
        //$$     private set
        //$$
        //$$ override fun bind(blendState: OmniBlendState) {
        //$$     if (!::shader.isInitialized) {
        //$$         shader = OmniShaders.create(vertexSource, fragmentSource, vertexFormat, schema, blendState)
        //$$     }
        //$$
        //#if MC >= 1.17.1
        //$$ val vanillaShader = (shader as VanillaShader).program
        //#if MC >= 1.21.2
        //$$ RenderSystem.setShader(vanillaShader)
        //#else
        //$$ RenderSystem.setShader { vanillaShader }
        //#endif
        //#else
        //$$ (shader as CompatibleShader).bind()
        //#endif
        //$$ }
        //$$
        //$$ override fun unbind() {
        //$$     if (!::shader.isInitialized) {
        //$$         return
        //$$     }
        //$$
        //#if MC >= 1.21.2
        //$$ RenderSystem.clearShader()
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.setShader { null }
        //#else
        //$$ (shader as CompatibleShader).unbind()
        //#endif
        //$$ }
        //#endif
    }

    //#if MC >= 1.17.1
    public class Vanilla(
        //#if MC >= 1.21.5
        public val vertexLocation: ResourceLocation,
        public val fragmentLocation: ResourceLocation,
        public val samplers: List<String>,
        public val uniforms: Map<String, UniformType>
        //#else
        //$$ public val supplier: Supplier<CompiledShaderProgram>
        //#endif
    ) : ShaderProvider {
        //#if MC < 1.21.5
        //$$ override fun bind(blendState: OmniBlendState) {
        //#if MC >= 1.21.2
        //$$ RenderSystem.setShader(supplier.get())
        //#else
        //$$ RenderSystem.setShader(supplier)
        //#endif
        //$$ }
        //$$
        //$$ override fun unbind() {
        //#if MC >= 1.21.2
        //$$ RenderSystem.clearShader()
        //#else
        //$$ RenderSystem.setShader { null }
        //#endif
        //$$ }
        //#endif
    }
    //#endif

    //#if MC < 1.21.5
    //$$ public fun bind(blendState: OmniBlendState)
    //$$ public fun unbind()
    //#endif
}
