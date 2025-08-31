package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

//#if MC >= 1.16.5
import org.lwjgl.opengl.GL
//#else
//$$ import org.lwjgl.opengl.GLContext
//#endif

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//#endif

/**
 * A utility class which provides a means of checking various OpenGL related states and capabilities.
 *
 * @since 0.1.0
 * @author Deftu
 */
@GameSide(Side.CLIENT)
public object OmniRenderEnv {

    /**
     * Representation of an OpenGL error code.
     *
     * @since 0.1.0
     * @author Deftu
     */
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

    /**
     * Returns the current OpenGL error code.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val errorCode: Int
        get() = GL11.glGetError()

    /**
     * Returns the current OpenGL error.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val error: GlError
        @Suppress("EnumValuesSoftDeprecate")
        get() = GlError.values().firstOrNull { error -> error.value == errorCode } ?: GlError.NO_ERROR

    /**
     * Returns whether OpenGL 2.1 features are available.
     *
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
     * Returns whether OpenGL 3.0 features are available.
     *
     * @since 0.19.1
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isGl3Available: Boolean
        get() {
            //#if MC >= 1.15.2
            return GL.getCapabilities().OpenGL30
            //#else
            //$$ return GLContext.getCapabilities().OpenGL30
            //#endif
        }

    /**
     * Returns whether the client has framebuffers enabled and if the OpenGL 3.0 features required for them are available.
     *
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

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isShaderSupported: Boolean
        get() {
            //#if MC >= 1.15.2
            return true
            //#else
            //$$ return OpenGlHelper.shadersSupported
            //#endif
        }

}
