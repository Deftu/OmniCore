package dev.deftu.omnicore.api.client.render.provider

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.gl.UniformType
import net.minecraft.util.Identifier

public sealed interface ShaderProvider {
    public class Compatible(
        public val vertexFormat: VertexFormat,

        public val vertexSource: String,
        public val fragmentSource: String,
    ) : ShaderProvider {
        //#if MC < 1.21.5
        //$$ internal lateinit var shader: OmniShader
        //$$     private set
        //$$
        //$$ override fun bind(blendState: OmniManagedBlendState) {
        //$$     if (!::shader.isInitialized) {
        //$$         shader = OmniShader.fromLegacyShader(vertexSource, fragmentSource, blendState, vertexFormat)
        //$$     }
        //$$
            //#if MC >= 1.17.1
            //$$ val vanillaShader = (shader as MinecraftShader).shader
            //#if MC >= 1.21.2
            //$$ RenderSystem.setShader(vanillaShader)
            //#else
            //$$ RenderSystem.setShader { vanillaShader }
            //#endif
            //#else
            //$$ (shader as GlShader).bind()
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
            //$$ (shader as GlShader).unbind()
            //#endif
        //$$ }
        //#endif
    }

    //#if MC >= 1.17.1
    public class Vanilla(
        //#if MC >= 1.21.5
        public val vertexLocation: Identifier,
        public val fragmentLocation: Identifier,
        public val samplers: List<String>,
        public val uniforms: Map<String, UniformType>
        //#else
        //$$ public val supplier: Supplier<CompiledShaderProgram>
        //#endif
    ) : ShaderProvider {
        //#if MC < 1.21.5
        //$$ override fun bind(blendState: OmniManagedBlendState) {
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
    //$$ public fun bind(blendState: OmniManagedBlendState)
    //$$
    //$$ public fun unbind()
    //#endif
}
