package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.client
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.ShaderInstance
import org.jetbrains.annotations.ApiStatus
import java.util.IdentityHashMap
import java.util.function.Supplier

@ApiStatus.Internal
public object DefaultShaders {
    private val defaults: IdentityHashMap<VertexFormat, Supplier<ShaderInstance>> by lazy {
        IdentityHashMap<VertexFormat, Supplier<ShaderInstance>>().apply {
            put(DefaultVertexFormat.POSITION, createSupplierNonnull(GameRenderer::getPositionShader))
            put(DefaultVertexFormat.POSITION_COLOR, createSupplierNonnull(GameRenderer::getPositionColorShader))
            put(DefaultVertexFormat.POSITION_TEX, createSupplierNonnull(GameRenderer::getPositionTexShader))
            put(DefaultVertexFormat.POSITION_TEX_COLOR, createSupplierNonnull(GameRenderer::getPositionTexColorShader))
            put(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, createSupplierNonnull(GameRenderer::getPositionColorTexLightmapShader))
            put(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, createSupplierNonnull(GameRenderer::getPositionColorLightmapShader))
            put(DefaultVertexFormat.POSITION_COLOR_NORMAL, createSupplierNonnull(GameRenderer::getRendertypeLinesShader))
            put(DefaultVertexFormat.BLIT_SCREEN, Supplier { client.gameRenderer.blitShader!! })

            //#if MC < 1.20.6
            //$$ put(DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR, createSupplierNonnull(GameRenderer::getPositionTexLightmapColorShader))
            //$$ put(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, createSupplierNonnull(GameRenderer::getPositionTexColorNormalShader))
            //#endif

            //#if MC < 1.21.1
            //$$ put(DefaultVertexFormat.POSITION_COLOR_TEX, createSupplierNonnull(GameRenderer::getPositionColorTexShader))
            //#endif
        }
    }

    @JvmStatic
    public operator fun get(format: VertexFormat): Supplier<ShaderInstance>? {
        return defaults[format]
    }

    private fun <T> createSupplierNonnull(supplier: Supplier<T?>): Supplier<T> {
        return Supplier { supplier.get() ?: throw IllegalStateException("Shader program returned by $supplier is null") }
    }
}
