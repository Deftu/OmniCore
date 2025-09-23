package dev.deftu.omnicore.api.client.render.shader

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.state.OmniBlendState
import dev.deftu.omnicore.api.resources.findFirstOrNull
import dev.deftu.omnicore.api.resources.readString
import net.minecraft.util.Identifier

//#if MC >= 1.17.1 && MC < 1.21.5
//$$ import dev.deftu.omnicore.internal.client.render.shader.VanillaShaderFactory
//#else
import dev.deftu.omnicore.internal.client.render.shader.CompatibleShader
//#endif

public object OmniShaders {
    @JvmStatic
    @JvmOverloads
    public fun create(
        vertSource: String,
        fragSource: String,
        vertexFormat: VertexFormat?,
        schema: ShaderSchema,
        blendState: OmniBlendState = OmniBlendState.NORMAL,
    ): OmniShader {
        //#if MC >= 1.17.1 && MC < 1.21.5
        //$$ return VanillaShaderFactory.create(vertSource, fragSource, vertexFormat, schema, blendState)
        //#else
        return CompatibleShader(vertSource, fragSource, schema, blendState)
        //#endif
    }

    @JvmStatic
    @JvmOverloads
    public fun create(
        vertSource: String,
        fragSource: String,
        vertexFormat: DefaultVertexFormats?,
        schema: ShaderSchema,
        blendState: OmniBlendState = OmniBlendState.NORMAL,
    ): OmniShader {
        return create(vertSource, fragSource, vertexFormat?.vanilla, schema, blendState)
    }

    @JvmStatic
    public fun resource(
        vertexLocation: Identifier,
        fragmentLocation: Identifier,
        vertexFormat: VertexFormat?,
        schema: ShaderSchema,
        blendState: OmniBlendState = OmniBlendState.NORMAL,
    ): OmniShader {
        val resourceManager = client.resourceManager
        val vertexSource = resourceManager.findFirstOrNull(vertexLocation)?.readString()
            ?: throw IllegalArgumentException("Vertex shader not found: $vertexLocation")
        val fragmentSource = resourceManager.findFirstOrNull(fragmentLocation)?.readString()
            ?: throw IllegalArgumentException("Fragment shader not found: $fragmentLocation")
        return create(vertexSource, fragmentSource, vertexFormat, schema, blendState)
    }

    @JvmStatic
    public fun resource(
        vertexLocation: Identifier,
        fragmentLocation: Identifier,
        vertexFormat: DefaultVertexFormats?,
        schema: ShaderSchema,
        blendState: OmniBlendState = OmniBlendState.NORMAL,
    ): OmniShader {
        return resource(vertexLocation, fragmentLocation, vertexFormat?.vanilla, schema, blendState)
    }
}
