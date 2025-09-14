package dev.deftu.omnicore.internal.client.render

import dev.deftu.omnicore.api.client.client
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import org.jetbrains.annotations.ApiStatus
import java.util.IdentityHashMap
import java.util.function.Supplier
import kotlin.reflect.KFunction

@ApiStatus.Internal
public object DefaultShaders {
    private val defaults: IdentityHashMap<VertexFormat, Supplier<ShaderProgram>> by lazy {
        IdentityHashMap<VertexFormat, Supplier<ShaderProgram>>().apply {
            put(VertexFormats.POSITION, createSupplierNonnull(GameRenderer::getPositionProgram))
            put(VertexFormats.POSITION_COLOR, createSupplierNonnull(GameRenderer::getPositionColorProgram))
            put(VertexFormats.POSITION_TEXTURE, createSupplierNonnull(GameRenderer::getPositionTexProgram))
            put(VertexFormats.POSITION_TEXTURE_COLOR, createSupplierNonnull(GameRenderer::getPositionTexColorProgram))
            put(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, createSupplierNonnull(GameRenderer::getPositionColorTexLightmapProgram))
            put(VertexFormats.POSITION_COLOR_LIGHT, createSupplierNonnull(GameRenderer::getPositionColorLightmapProgram))
            put(VertexFormats.LINES, createSupplierNonnull(GameRenderer::getRenderTypeLinesProgram))
            put(VertexFormats.BLIT_SCREEN, Supplier { client.gameRenderer.blitScreenProgram!! })

            //#if MC < 1.20.6
            //$$ put(VertexFormats.POSITION_TEXTURE_LIGHT_COLOR, createSupplierNonnull(GameRenderer::getPositionTexLightmapColorProgram))
            //$$ put(VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, createSupplierNonnull(GameRenderer::getPositionTexColorNormalProgram))
            //#endif

            //#if MC < 1.21.1
            //$$ put(DefaultVertexFormat.POSITION_COLOR_TEX, createSupplierNonnull(GameRenderer::getPositionColorTexShader))
            //#endif
        }
    }

    @JvmStatic
    public operator fun get(format: VertexFormat): Supplier<ShaderProgram>? {
        return defaults[format]
    }

    private fun <T> createSupplierNonnull(function: KFunction<T?>): Supplier<T> {
        return Supplier { function.call() ?: throw IllegalStateException("Shader program returned by $function is null") }
    }
}
