package dev.deftu.omnicore.client.render.pipeline

import net.minecraft.client.render.VertexFormat

//#if MC >= 1.21.5
//$$ import com.mojang.blaze3d.shaders.UniformType
//$$ import net.minecraft.resources.ResourceLocation
//#else
import dev.deftu.omnicore.client.render.state.OmniManagedBlendState
import dev.deftu.omnicore.client.shaders.OmniShader
//#endif

//#if MC >= 1.17.1 && MC < 1.21.5
import com.mojang.blaze3d.systems.RenderSystem
import dev.deftu.omnicore.client.shaders.MinecraftShader
import net.minecraft.client.gl.ShaderProgram
import java.util.function.Supplier
//#else
//$$ import dev.deftu.omnicore.client.shaders.GlShader
//#endif

public sealed interface ShaderSource {

    //#if MC < 1.21.5
    public fun bind(blendState: OmniManagedBlendState)

    public fun unbind()
    //#endif

}

public class LegacyShaderSource(
    public val vertexFormat: VertexFormat,

    public val vertexSource: String,
    public val fragmentSource: String,
) : ShaderSource {

    //#if MC < 1.21.5
    internal lateinit var shader: OmniShader
        private set

    override fun bind(blendState: OmniManagedBlendState) {
        if (!::shader.isInitialized) {
            shader = OmniShader.fromLegacyShader(vertexSource, fragmentSource, blendState, vertexFormat)
        }

        //#if MC >= 1.17.1
        val vanillaShader = (shader as MinecraftShader).shader
        //#if MC >= 1.21.2
        //$$ RenderSystem.setShader(vanillaShader)
        //#else
        RenderSystem.setShader { vanillaShader }
        //#endif
        //#else
        //$$ (shader as GlShader).bind()
        //#endif
    }

    override fun unbind() {
        if (!::shader.isInitialized) {
            return
        }

        //#if MC >= 1.21.2
        //$$ RenderSystem.clearShader()
        //#elseif MC >= 1.17.1
        RenderSystem.setShader { null }
        //#else
        //$$ (shader as GlShader).unbind()
        //#endif
    }
    //#endif

}

//#if MC >= 1.17.1
public class VanillaShaderSource(
    //#if MC >= 1.21.5
    //$$ public val vertexIdentifier: ResourceLocation,
    //$$ public val fragmentIdentifier: ResourceLocation,
    //$$ public val samplers: List<String>,
    //$$ public val uniforms: Map<String, UniformType>
    //#else
    public val supplier: Supplier<ShaderProgram>
    //#endif
) : ShaderSource {

    //#if MC < 1.21.5
    override fun bind(blendState: OmniManagedBlendState) {
        //#if MC >= 1.21.2
        //$$ RenderSystem.setShader(supplier.get())
        //#else
        RenderSystem.setShader(supplier)
        //#endif
    }

    override fun unbind() {
        //#if MC >= 1.21.2
        //$$ RenderSystem.clearShader()
        //#else
        RenderSystem.setShader { null }
        //#endif
    }
    //#endif

}
//#endif
