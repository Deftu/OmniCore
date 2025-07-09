package dev.deftu.omnicore.client.render.pipeline

//#if MC >= 1.17.1
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.render.VertexFormats

//#if MC < 1.21.5
//#if MC >= 1.21.2
//$$ import net.minecraft.client.renderer.ShaderProgram
//$$ import net.minecraft.client.renderer.CoreShaders
//#elseif MC >= 1.16.5
//$$ import dev.deftu.omnicore.client.OmniClient
//$$ import net.minecraft.client.gl.ShaderProgram
//$$ import net.minecraft.client.render.GameRenderer
//$$ import java.util.IdentityHashMap
//$$ import java.util.function.Supplier
//$$ import kotlin.reflect.KFunction
//#endif
//#endif

//#if MC >= 1.21.5
import dev.deftu.omnicore.common.OmniIdentifier
import net.minecraft.util.Identifier
//#endif

public object DefaultShaders {

    //#if MC >= 1.21.5
    private val defaults: Map<VertexFormat, Identifier> by lazy {
        mapOf(
            VertexFormats.POSITION to OmniIdentifier.create("core/position"),
            VertexFormats.POSITION_COLOR to OmniIdentifier.create("core/position_color"),
            VertexFormats.POSITION_TEXTURE to OmniIdentifier.create("core/position_tex"),
            VertexFormats.POSITION_TEXTURE_COLOR to OmniIdentifier.create("core/position_tex_color"),
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT to OmniIdentifier.create("core/position_color_tex_lightmap"),
            VertexFormats.POSITION_COLOR_LIGHT to OmniIdentifier.create("core/position_color_lightmap"),
            VertexFormats.POSITION_COLOR_NORMAL to OmniIdentifier.create("core/rendertype_lines"),
            VertexFormats.BLIT_SCREEN to OmniIdentifier.create("core/blit_screen"),
        )
    }
    //#elseif MC >= 1.21.2
    //$$ private val defaults: Map<VertexFormat, ShaderProgram> by lazy {
    //$$     mapOf(
    //$$         DefaultVertexFormat.POSITION to CoreShaders.POSITION,
    //$$         DefaultVertexFormat.POSITION_COLOR to CoreShaders.POSITION_COLOR,
    //$$         DefaultVertexFormat.POSITION_TEX to CoreShaders.POSITION_TEX,
    //$$         DefaultVertexFormat.POSITION_TEX_COLOR to CoreShaders.POSITION_TEX_COLOR,
    //$$         DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP to CoreShaders.POSITION_COLOR_TEX_LIGHTMAP,
    //$$         DefaultVertexFormat.POSITION_COLOR_LIGHTMAP to CoreShaders.POSITION_COLOR_LIGHTMAP,
    //$$         DefaultVertexFormat.POSITION_COLOR_NORMAL to CoreShaders.RENDERTYPE_LINES,
    //$$         DefaultVertexFormat.BLIT_SCREEN to CoreShaders.BLIT_SCREEN,
    //$$     )
    //$$ }
    //#elseif MC >= 1.17.1
    //$$ private val defaults: IdentityHashMap<VertexFormat, Supplier<ShaderProgram>> by lazy {
    //$$     IdentityHashMap<VertexFormat, Supplier<ShaderProgram>>().apply {
    //$$         put(VertexFormats.POSITION, createSupplierNonnull(GameRenderer::getPositionProgram))
    //$$         put(VertexFormats.POSITION_COLOR, createSupplierNonnull(GameRenderer::getPositionColorProgram))
    //$$         put(VertexFormats.POSITION_TEXTURE, createSupplierNonnull(GameRenderer::getPositionTexProgram))
    //$$         put(VertexFormats.POSITION_TEXTURE_COLOR, createSupplierNonnull(GameRenderer::getPositionTexColorProgram))
    //$$         put(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, createSupplierNonnull(GameRenderer::getPositionColorTexLightmapProgram))
    //$$         put(VertexFormats.POSITION_COLOR_LIGHT, createSupplierNonnull(GameRenderer::getPositionColorLightmapProgram))
    //$$         put(VertexFormats.LINES, createSupplierNonnull(GameRenderer::getRenderTypeLinesProgram))
    //$$         put(VertexFormats.BLIT_SCREEN, Supplier { OmniClient.getInstance().gameRenderer.blitScreenProgram })
    //$$
            //#if MC < 1.20.6
            //$$ put(VertexFormats.POSITION_TEXTURE_LIGHT_COLOR, createSupplierNonnull(GameRenderer::getPositionTexLightmapColorProgram))
            //$$ put(VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, createSupplierNonnull(GameRenderer::getPositionTexColorNormalProgram))
            //#endif
    //$$
            //#if MC < 1.21
            //$$ put(DefaultVertexFormat.POSITION_COLOR_TEX, createSupplierNonnull(GameRenderer::getPositionColorTexShader))
            //#endif
    //$$     }
    //$$ }
    //#endif

    @JvmStatic
    public operator fun get(format: VertexFormat):
            //#if MC >= 1.21.5
            Identifier?
            //#elseif MC >= 1.21.2
            //$$ ShaderProgram?
            //#else
            //$$ Supplier<ShaderProgram>?
            //#endif
    {
        return defaults[format]
    }

    //#if MC < 1.21.2
    //$$ private fun <T> createSupplierNonnull(function: KFunction<T?>): Supplier<T> {
    //$$     return Supplier { function.call() ?: throw IllegalStateException("Shader program returned by $function is null") }
    //$$ }
    //#endif

}
//#endif
