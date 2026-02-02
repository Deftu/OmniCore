package dev.deftu.omnicore.internal.client.framebuffer

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.framebuffer.FramebufferTarget
import dev.deftu.omnicore.api.client.framebuffer.OmniFramebuffer
import dev.deftu.omnicore.api.client.textures.OmniTextureFormat
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

/**
 * A collection of small non-essential helper functions for working with framebuffers.
 * Calls [FramebufferInternals] functions internally where appropriate.
 *
 * @since 1.0.0
 * @author Deftu
 */
@ApiStatus.Internal
public object FramebufferHelper {
    @JvmStatic
    public fun <T> with(target: FramebufferTarget, framebuffer: OmniFramebuffer, block: () -> T): T {
        val unbind = FramebufferInternals.bind(target, framebuffer)
        try {
            return block()
        } finally {
            unbind()
        }
    }

    @JvmStatic
    public fun <T> with(target: FramebufferTarget, fbo: Int, block: () -> T): T {
        val unbind = FramebufferInternals.bind(target, fbo)
        try {
            return block()
        } finally {
            unbind()
        }
    }

    @JvmStatic
    public fun <T> with(framebuffer: OmniFramebuffer, block: () -> T): T {
        return with(FramebufferTarget.READ_WRITE, framebuffer, block)
    }

    @JvmStatic
    public fun <T> with(fbo: Int, block: () -> T): T {
        return with(FramebufferTarget.READ_WRITE, fbo, block)
    }

    @JvmStatic
    public fun attach(target: FramebufferTarget, attachment: Int, texId: Int) {
        //#if MC >= 1.21.6
        GlStateManager._glFramebufferTexture2D(target.code, attachment, GL11.GL_TEXTURE_2D, texId, 0)
        //#else
        //$$ GL30.glFramebufferTexture2D(target.code, attachment, GL11.GL_TEXTURE_2D, texId, 0)
        //#endif
    }

    @JvmStatic
    public fun detach(target: FramebufferTarget, attachment: Int) {
        //#if MC >= 1.21.6
        GlStateManager._glFramebufferTexture2D(target.code, attachment, GL11.GL_TEXTURE_2D, 0, 0)
        //#else
        //$$ GL30.glFramebufferTexture2D(target.code, attachment, GL11.GL_TEXTURE_2D, 0, 0)
        //#endif
    }

    @JvmStatic
    @JvmOverloads
    public fun attachColor(
        id: Int,
        texture: Int,
        target: FramebufferTarget = FramebufferTarget.READ_WRITE
    ) {
        with(target, id) {
            //#if MC >= 1.21.6
            GlStateManager._glFramebufferTexture2D(
                target.code,
                GL30.GL_COLOR_ATTACHMENT0,
                GL11.GL_TEXTURE_2D,
                texture,
                0
            )
            //#else
            //$$ GL30.glFramebufferTexture2D(
            //$$     target.code,
            //$$     GL30.GL_COLOR_ATTACHMENT0,
            //$$     GL11.GL_TEXTURE_2D,
            //$$     texture,
            //$$     0
            //$$ )
            //#endif
        }
    }

    @JvmStatic
    @JvmOverloads
    public fun attachDepth(
        id: Int,
        texture: Int,
        attachment: OmniTextureFormat = OmniTextureFormat.DEPTH24_STENCIL8,
        target: FramebufferTarget = FramebufferTarget.READ_WRITE
    ) {
        with(target, id) {
            //#if MC >= 1.21.6
            GlStateManager._glFramebufferTexture2D(
                target.code,
                attachment.attachment,
                GL11.GL_TEXTURE_2D,
                texture,
                0
            )
            //#else
            //$$ GL30.glFramebufferTexture2D(
            //$$     target.code,
            //$$     attachment.attachment,
            //$$     GL11.GL_TEXTURE_2D,
            //$$     texture,
            //$$     0
            //$$ )
            //#endif
        }
    }
}
