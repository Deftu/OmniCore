package dev.deftu.omnicore.client.render.pipeline

//#if MC >= 1.17.1
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats

//#if MC < 1.21.5
//#if MC >= 1.21.2
//$$ import net.minecraft.client.gl.ShaderProgramKey
//$$ import net.minecraft.client.gl.ShaderProgramKeys
//#elseif MC >= 1.16.5
import dev.deftu.omnicore.client.OmniClient
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.GameRenderer
import java.util.IdentityHashMap
import java.util.function.Supplier
import kotlin.reflect.KFunction
//#endif
//#endif

//#if MC >= 1.21.5
//$$ import dev.deftu.omnicore.common.OmniIdentifier
//$$ import net.minecraft.resources.ResourceLocation
//#endif

public object DefaultShaders {

    //#if MC >= 1.21.5
    //$$ private val defaults: Map<VertexFormat, ResourceLocation> by lazy {
    //$$     mapOf(
    //$$         DefaultVertexFormat.POSITION to OmniIdentifier.create("core/position"),
    //$$         DefaultVertexFormat.POSITION_COLOR to OmniIdentifier.create("core/position_color"),
    //$$         DefaultVertexFormat.POSITION_TEX to OmniIdentifier.create("core/position_tex"),
    //$$         DefaultVertexFormat.POSITION_TEX_COLOR to OmniIdentifier.create("core/position_tex_color"),
    //$$         DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP to OmniIdentifier.create("core/position_color_tex_lightmap"),
    //$$         DefaultVertexFormat.POSITION_COLOR_LIGHTMAP to OmniIdentifier.create("core/position_color_lightmap"),
    //$$         DefaultVertexFormat.POSITION_COLOR_NORMAL to OmniIdentifier.create("core/rendertype_lines"),
    //$$         DefaultVertexFormat.BLIT_SCREEN to OmniIdentifier.create("core/blit_screen"),
    //$$     )
    //$$ }
    //#elseif MC >= 1.21.2
    //$$ private val defaults: Map<VertexFormat, ShaderProgramKey> by lazy {
    //$$     mapOf(
    //$$         VertexFormats.POSITION to ShaderProgramKeys.POSITION,
    //$$         VertexFormats.POSITION_COLOR to ShaderProgramKeys.POSITION_COLOR,
    //$$         VertexFormats.POSITION_TEXTURE to ShaderProgramKeys.POSITION_TEX,
    //$$         VertexFormats.POSITION_TEXTURE_COLOR to ShaderProgramKeys.POSITION_TEX_COLOR,
    //$$         VertexFormats.POSITION_COLOR_TEXTURE_LIGHT to ShaderProgramKeys.POSITION_COLOR_TEX_LIGHTMAP,
    //$$         VertexFormats.POSITION_COLOR_LIGHT to ShaderProgramKeys.POSITION_COLOR_LIGHTMAP,
    //$$         VertexFormats.LINES to ShaderProgramKeys.RENDERTYPE_LINES,
    //$$         VertexFormats.BLIT_SCREEN to ShaderProgramKeys.BLIT_SCREEN,
    //$$     )
    //$$ }
    //#elseif MC >= 1.17.1
    private val defaults: IdentityHashMap<VertexFormat, Supplier<ShaderProgram>> by lazy {
        IdentityHashMap<VertexFormat, Supplier<ShaderProgram>>().apply {
            put(VertexFormats.POSITION, createSupplierNonnull(GameRenderer::getPositionProgram))
            put(VertexFormats.POSITION_COLOR, createSupplierNonnull(GameRenderer::getPositionColorProgram))
            put(VertexFormats.POSITION_TEXTURE, createSupplierNonnull(GameRenderer::getPositionTexProgram))
            put(VertexFormats.POSITION_TEXTURE_COLOR, createSupplierNonnull(GameRenderer::getPositionTexColorProgram))
            put(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, createSupplierNonnull(GameRenderer::getPositionColorTexLightmapProgram))
            put(VertexFormats.POSITION_COLOR_LIGHT, createSupplierNonnull(GameRenderer::getPositionColorLightmapProgram))
            put(VertexFormats.LINES, createSupplierNonnull(GameRenderer::getRenderTypeLinesProgram))
            put(VertexFormats.BLIT_SCREEN, Supplier { OmniClient.getInstance().gameRenderer.blitScreenProgram })

            //#if MC < 1.20.6
            put(VertexFormats.POSITION_TEXTURE_LIGHT_COLOR, createSupplierNonnull(GameRenderer::getPositionTexLightmapColorProgram))
            put(VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, createSupplierNonnull(GameRenderer::getPositionTexColorNormalProgram))
            //#endif

            //#if MC < 1.21
            put(VertexFormats.POSITION_COLOR_TEXTURE, createSupplierNonnull(GameRenderer::getPositionColorTexProgram))
            //#endif
        }
    }
    //#endif

    @JvmStatic
    public operator fun get(format: VertexFormat):
            //#if MC >= 1.21.5
            //$$ ResourceLocation?
            //#elseif MC >= 1.21.2
            //$$ ShaderProgramKey?
            //#else
            Supplier<ShaderProgram>?
            //#endif
    {
        return defaults[format]
    }

    //#if MC < 1.21.2
    private fun <T> createSupplierNonnull(function: KFunction<T?>): Supplier<T> {
        return Supplier { function.call() ?: throw IllegalStateException("Shader program returned by $function is null") }
    }
    //#endif

}
//#endif
