package dev.deftu.omnicore.client.render.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11

//#if MC <= 1.16.5
//$$ import dev.deftu.omnicore.client.render.OmniTextureManager
//#endif

public class OmniManagedRenderState(
    blendState: OmniManagedBlendState,
    depthState: OmniManagedDepthState,
    alphaState: OmniManagedAlphaState,
    colorLogic: OmniManagedColorLogic,
    colorMask: OmniManagedColorMask,
    polygonOffset: OmniManagedPolygonOffset,
    isCullFace: Boolean,
    textureStates: List<Boolean>,
) {

    public var blendState: OmniManagedBlendState = blendState
        set(value) {
            value.activate()
            field = value
        }

    public var depthState: OmniManagedDepthState = depthState
        set(value) {
            value.activate()
            field = value
        }

    public var alphaState: OmniManagedAlphaState = alphaState
        set(value) {
            value.activate()
            field = value
        }

    public var colorLogic: OmniManagedColorLogic = colorLogic
        set(value) {
            value.activate()
            field = value
        }

    public var colorMask: OmniManagedColorMask = colorMask
        set(value) {
            value.activate()
            field = value
        }

    public var polygonOffset: OmniManagedPolygonOffset = polygonOffset
        set(value) {
            value.activate()
            field = value
        }

    public var isCullFace: Boolean = isCullFace
        set(value) {
            if (value) {
                GlStateManager._enableCull()
            } else {
                GlStateManager._disableCull()
            }

            field = value
        }

    private val _textureStates = textureStates.toMutableList()

    public val textureStates: List<Boolean>
        get() = _textureStates.toList()

    public constructor(other: OmniManagedRenderState) : this(
        blendState = other.blendState,
        depthState = other.depthState,
        alphaState = other.alphaState,
        colorLogic = other.colorLogic,
        colorMask = other.colorMask,
        polygonOffset = other.polygonOffset,
        isCullFace = other.isCullFace,
        textureStates = other.textureStates.toMutableList(),
    )

    public fun apply(active: OmniManagedRenderState) {
        if (active.blendState != blendState) {
            active.blendState = blendState
        }

        if (active.depthState != depthState) {
            active.depthState = depthState
        }

        if (active.alphaState != alphaState) {
            active.alphaState = alphaState
        }

        if (active.colorLogic != colorLogic) {
            active.colorLogic = colorLogic
        }

        if (active.polygonOffset != polygonOffset) {
            active.polygonOffset = polygonOffset
        }

        if (active.isCullFace != isCullFace) {
            active.isCullFace = isCullFace
        }

        //#if MC <= 1.16.5
        //$$ for ((index, requestedState) in textureStates.withIndex()) {
        //$$     val isEnabled = active.textureStates.getOrNull(index)
        //$$     if (isEnabled == requestedState) {
        //$$         continue
        //$$     }
        //$$
        //$$     OmniTextureManager.configureTextureUnit(index) {
        //$$         if (isEnabled == null) {
        //$$             active._textureStates += requestedState
        //$$         } else {
        //$$             active._textureStates[index] = requestedState
        //$$         }
        //$$
        //$$         if (requestedState) {
        //$$             GlStateManager.enableTexture()
        //$$         } else {
        //$$             GlStateManager.disableTexture()
        //$$         }
        //$$     }
        //$$ }
        //#endif
    }

    public companion object {

        public fun active(): OmniManagedRenderState {
            return OmniManagedRenderState(
                blendState = OmniManagedBlendState.active(),
                depthState = OmniManagedDepthState.active(),
                alphaState = OmniManagedAlphaState.active(),
                colorLogic = OmniManagedColorLogic.active(),
                colorMask = OmniManagedColorMask.active(),
                polygonOffset = OmniManagedPolygonOffset.active(),
                isCullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE),
                textureStates = emptyList()
            )
        }

    }

}
