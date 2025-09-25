package dev.deftu.omnicore.api.client.render.pipeline

import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.math.OmniMatrix4f
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.ScissorBox

//#if MC >= 1.21.5
import com.mojang.blaze3d.systems.RenderPass
//#endif

public interface RenderPassEncoder {
    //#if MC >= 1.21.5
    public val vanilla: RenderPass
    //#endif

    public fun texture(name: String, id: Int): RenderPassEncoder
    public fun texture(unit: OmniTextureUnit, id: Int): RenderPassEncoder

    public fun uniform(name: String, vararg values: Float): RenderPassEncoder
    public fun uniform(name: String, vararg values: Int): RenderPassEncoder

    public fun getShaderColor(): OmniColor?
    public fun setShaderColor(red: Float, green: Float, blue: Float, alpha: Float): RenderPassEncoder

    public fun setShaderColor(red: Float, green: Float, blue: Float): RenderPassEncoder {
        return setShaderColor(red, green, blue, 1.0f)
    }

    public fun setShaderColor(color: OmniColor): RenderPassEncoder {
        return setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
    }

    public fun setLineWidth(width: Float): RenderPassEncoder

    public fun setLineStipple(factor: Int, pattern: Short): RenderPassEncoder

    public fun setTextureMatrix(matrix: OmniMatrix4f): RenderPassEncoder
    public fun resetTextureMatrix(): RenderPassEncoder

    public fun setModelViewMatrix(matrix: OmniMatrix4f): RenderPassEncoder
    public fun resetModelViewMatrix(): RenderPassEncoder

    public fun enableScissor(box: ScissorBox): RenderPassEncoder
    public fun disableScissor(): RenderPassEncoder

    public fun enableScissor(x: Int, y: Int, width: Int, height: Int): RenderPassEncoder {
        return enableScissor(ScissorBox(x, y, width, height))
    }

    /** Encode and submit your pass. One shot; any subsequent calls will throw. */
    public fun submit()
}
