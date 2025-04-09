package dev.deftu.omnicore.client.render

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11

public class OmniManagedRenderState(
    isDepthTest: Boolean,
    depthFunc: Int,
    isDepthMask: Boolean,
    isCullFace: Boolean,
    blendState: OmniManagedBlendState,
    colorMask: ColorMask,
    colorLogicOp: Boolean,
    colorLogicOpMode: Int,
    isPolygonOffsetFill: Boolean,
    polygonOffsetFactor: Float,
    polygonOffsetUnits: Float,
    private val _textureStates: MutableList<Boolean>,
    isAlphaTest: Boolean,
    alphaTestFunc: Int,
    alphaTestRef: Float,
) {

    public var isDepthTest: Boolean = isDepthTest
        set(value) {
            if (value) {
                GlStateManager._enableDepthTest()
            } else {
                GlStateManager._disableDepthTest()
            }

            field = value
        }

    public var depthFunc: Int = depthFunc
        set(value) {
            GlStateManager._depthFunc(value)
            field = value
        }

    public var isDepthMask: Boolean = isDepthMask
        set(value) {
            GlStateManager._depthMask(value)
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

    public var blendState: OmniManagedBlendState = blendState
        set(value) {
            value.activate()
            field = value
        }

    public var colorMask: ColorMask = colorMask
        set(value) {
            GlStateManager._colorMask(value.red, value.green, value.blue, value.alpha)
            field = value
        }

    public var colorLogicOp: Boolean = colorLogicOp
        set(value) {
            if (value) {
                GlStateManager._enableColorLogicOp()
            } else {
                GlStateManager._disableColorLogicOp()
            }

            field = value
        }

    public var colorLogicOpMode: Int = colorLogicOpMode
        set(value) {
            GlStateManager._logicOp(value)
            field = value
        }

    public var isPolygonOffsetFill: Boolean = isPolygonOffsetFill
        set(value) {
            if (value) {
                GlStateManager._enablePolygonOffset()
            } else {
                GlStateManager._disablePolygonOffset()
            }

            field = value
        }

    public var polygonOffsetFactor: Float = polygonOffsetFactor
        set(value) {
            GlStateManager._polygonOffset(value, polygonOffsetUnits)
            field = value
        }

    public var polygonOffsetUnits: Float = polygonOffsetUnits
        set(value) {
            GlStateManager._polygonOffset(polygonOffsetFactor, value)
            field = value
        }

    public val textureStates: List<Boolean>
        get() = _textureStates.toList()

    public var isAlphaTest: Boolean = isAlphaTest
        set(value) {
            //#if MC <= 1.16.5
            //$$ if (value) {
            //$$     GlStateManager._enableAlphaTest()
            //$$ } else {
            //$$     GlStateManager._disableAlphaTest()
            //$$ }
            //#endif

            field = value
        }

    public var alphaTestFunc: Int = alphaTestFunc
        set(value) {
            //#if MC <= 1.16.5
            //$$ GlStateManager._alphaFunc(value, alphaTestRef)
            //#endif

            field = value
        }

    public var alphaTestRef: Float = alphaTestRef
        set(value) {
            //#if MC <= 1.16.5
            //$$ GlStateManager._alphaFunc(alphaTestFunc, value)
            //#endif

            field = value
        }

    public constructor(other: OmniManagedRenderState) : this(
        isDepthTest = other.isDepthTest,
        depthFunc = other.depthFunc,
        isCullFace = other.isCullFace,
        colorLogicOp = other.colorLogicOp,
        colorLogicOpMode = other.colorLogicOpMode,
        blendState = other.blendState,
        colorMask = other.colorMask,
        isDepthMask = other.isDepthMask,
        isPolygonOffsetFill = other.isPolygonOffsetFill,
        polygonOffsetFactor = other.polygonOffsetFactor,
        polygonOffsetUnits = other.polygonOffsetUnits,
        _textureStates = other._textureStates.toMutableList(),
        isAlphaTest = other.isAlphaTest,
        alphaTestFunc = other.alphaTestFunc,
        alphaTestRef = other.alphaTestRef
    )

    @JvmOverloads
    public fun apply(
        active: OmniManagedRenderState,
        updateUnused: Boolean = false,
    ) {
        if (active.isDepthTest != isDepthTest) {
            active.isDepthTest = isDepthTest
        }

        if ((isDepthTest || updateUnused) && active.depthFunc != depthFunc) {
            active.depthFunc = depthFunc
        }

        if (active.isCullFace != isCullFace) {
            active.isCullFace = isCullFace
        }

        if (active.colorLogicOp != colorLogicOp) {
            active.colorLogicOp = colorLogicOp
        }

        if ((colorLogicOp || updateUnused) && active.colorLogicOpMode != colorLogicOpMode) {
            active.colorLogicOpMode = colorLogicOpMode
        }

        if (active.blendState != blendState) {
            active.blendState = blendState
        }

        if (active.colorMask != colorMask) {
            active.colorMask = colorMask
        }

        if (active.isDepthMask != isDepthMask) {
            active.isDepthMask = isDepthMask
        }

        if (active.isPolygonOffsetFill != isPolygonOffsetFill) {
            active.isPolygonOffsetFill = isPolygonOffsetFill
        }

        if ((isPolygonOffsetFill || updateUnused) && active.polygonOffsetFactor != polygonOffsetFactor) {
            active.polygonOffsetFactor = polygonOffsetFactor
        }

        if ((isPolygonOffsetFill || updateUnused) && active.polygonOffsetUnits != polygonOffsetUnits) {
            active.polygonOffsetUnits = polygonOffsetUnits
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
        //$$             GlStateManager._enableTexture()
        //$$         } else {
        //$$             GlStateManager._disableTexture()
        //$$         }
        //$$     }
        //$$ }
        //$$
        //$$ if (active.isAlphaTest != isAlphaTest) {
        //$$     active.isAlphaTest = isAlphaTest
        //$$ }
        //$$
        //$$ if ((isAlphaTest || updateUnused) && active.alphaTestFunc != alphaTestFunc) {
        //$$     active.alphaTestFunc = alphaTestFunc
        //$$ }
        //$$
        //$$ if ((isAlphaTest || updateUnused) && active.alphaTestRef != alphaTestRef) {
        //$$     active.alphaTestRef = alphaTestRef
        //$$ }
        //#endif
    }

    public companion object {

        public fun active(): OmniManagedRenderState {
            return OmniManagedRenderState(
                isDepthTest = GL11.glGetBoolean(GL11.GL_DEPTH_TEST),
                depthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC),
                isDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK),
                isCullFace = GL11.glGetBoolean(GL11.GL_CULL_FACE),
                blendState = OmniManagedBlendState.active(),
                colorMask = ColorMask.active(),
                colorLogicOp = GL11.glGetBoolean(GL11.GL_COLOR_LOGIC_OP),
                colorLogicOpMode = GL11.glGetInteger(GL11.GL_LOGIC_OP_MODE),
                isPolygonOffsetFill = GL11.glGetBoolean(GL11.GL_POLYGON_OFFSET_FILL),
                polygonOffsetFactor = GL11.glGetFloat(GL11.GL_POLYGON_OFFSET_FACTOR),
                polygonOffsetUnits = GL11.glGetFloat(GL11.GL_POLYGON_OFFSET_UNITS),
                _textureStates = mutableListOf(),
                //#if MC <= 1.16.5
                //$$ isAlphaTest = GL11.glGetBoolean(GL11.GL_ALPHA_TEST),
                //$$ alphaTestFunc = GL11.glGetInteger(GL11.GL_ALPHA_FUNC),
                //$$ alphaTestRef = GL11.glGetFloat(GL11.GL_ALPHA_REF),
                //#else
                isAlphaTest = false,
                alphaTestFunc = 0,
                alphaTestRef = 0f,
                //#endif
            )
        }

    }

}
