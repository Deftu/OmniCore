package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.locationOrThrow
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object DefaultShaders {
    private val defaults: Map<VertexFormat, ResourceLocation> by lazy {
        mapOf(
            DefaultVertexFormat.POSITION to locationOrThrow("core/position"),
            DefaultVertexFormat.POSITION_COLOR to locationOrThrow("core/position_color"),
            DefaultVertexFormat.POSITION_TEX to locationOrThrow("core/position_tex"),
            DefaultVertexFormat.POSITION_TEX_COLOR to locationOrThrow("core/position_tex_color"),
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP to locationOrThrow("core/position_color_tex_lightmap"),
            DefaultVertexFormat.POSITION_COLOR_LIGHTMAP to locationOrThrow("core/position_color_lightmap"),
            DefaultVertexFormat.POSITION_COLOR_NORMAL to locationOrThrow("core/rendertype_lines"),
            //#if MC < 1.21.9
            //$$ DefaultVertexFormat.BLIT_SCREEN to locationOrThrow("core/blit_screen"),
            //#endif
        )
    }

    @JvmStatic
    public operator fun get(format: VertexFormat): ResourceLocation? {
        return defaults[format]
    }
}
