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

    /**
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val errorCode: Int
        get() = GL11.glGetError()

    /**
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val error: GlError
        @Suppress("EnumValuesSoftDeprecate")
        get() = GlError.values().firstOrNull { error -> error.value == errorCode } ?: GlError.NO_ERROR

    /**
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isGl21Available: Boolean
        get() {
            //#if MC >= 1.15.2
            return true
            //#else
            //$$ return OpenGlHelper.openGL21
            //#endif
        }

    /**
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isFramebufferEnabled: Boolean
        get() {
            //#if MC >= 1.15.2
            return true
            //#else
            //$$ return OpenGlHelper.isFramebufferEnabled()
            //#endif
        }

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
        INVALID_FRAMEBUFFER_OPERATION(GL30.GL_INVALID_FRAMEBUFFER_OPERATION);

        public val isPresent: Boolean
            get() = this != NO_ERROR

        public fun ifPresent(block: (GlError) -> Unit) {
            if (isPresent && error == this) {
                block(this)
            }
        }

    }

}
