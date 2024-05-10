package dev.deftu.omnicore.client.render

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//#endif

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

@GameSide(Side.CLIENT)
public object OmniRenderEnv {

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getErrorCode(): Int =
        GL11.glGetError()

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getError(): GlError =
        @Suppress("EnumValuesSoftDeprecate")
        GlError.values().firstOrNull { it.value == getErrorCode() } ?: GlError.NO_ERROR

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun isGl21Available(): Boolean =
        //#if MC >= 1.15.2
        true
        //#else
        //$$ OpenGlHelper.openGL21
        //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun isFramebufferEnabled(): Boolean =
        //#if MC >= 1.15.2
        true
        //#else
        //$$ OpenGlHelper.isFramebufferEnabled()
        //#endif

    @GameSide(Side.CLIENT)
    public enum class GlError(
        public val value: Int
    ) {
        NO_ERROR(GL11.GL_NO_ERROR),
        INVALID_ENUM(GL11.GL_INVALID_ENUM),
        INVALID_VALUE(GL11.GL_INVALID_VALUE),
        INVALID_OPERATION(GL11.GL_INVALID_OPERATION),
        STACK_OVERFLOW(GL11.GL_STACK_OVERFLOW),
        STACK_UNDERFLOW(GL11.GL_STACK_UNDERFLOW),
        OUT_OF_MEMORY(GL11.GL_OUT_OF_MEMORY),
        INVALID_FRAMEBUFFER_OPERATION(GL30.GL_INVALID_FRAMEBUFFER_OPERATION)
    }

}
