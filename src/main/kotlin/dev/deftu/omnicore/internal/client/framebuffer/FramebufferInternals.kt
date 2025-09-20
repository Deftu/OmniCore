package dev.deftu.omnicore.internal.client.framebuffer

import dev.deftu.omnicore.api.client.framebuffer.FramebufferTarget
import dev.deftu.omnicore.internal.client.exceptions.FramebufferStatusException
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//#endif

//#if MC < 1.21.5
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#endif

/**
 * Houses internal operations for framebuffers, mapping to the appropriate Minecraft version methods or OpenGL calls.
 *
 * This includes creating, binding, deleting framebuffers, clearing buffers, and checking framebuffer status.
 *
 * @since 1.0.0
 * @author Deftu
 */
@ApiStatus.Internal
public object FramebufferInternals {
    @JvmStatic
    public fun create(): Int {
        //#if MC >= 1.17.1
        return GL30.glGenFramebuffers()
        //#elseif MC >= 1.16.5
        //$$ return GlStateManager.genFramebuffers()
        //#else
        //$$ return OpenGlHelper.glGenFramebuffers()
        //#endif
    }

    @JvmStatic
    public fun bound(target: FramebufferTarget): Int {
        return GL11.glGetInteger(target.code)
    }

    @JvmStatic
    public fun bind0(target: FramebufferTarget, id: Int) {
        //#if MC >= 1.17.1
        GL30.glBindFramebuffer(target.code, id)
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.bindFramebuffer(target.code, id)
        //#else
        //$$ OpenGlHelper.glBindFramebuffer(target.code, id)
        //#endif
    }

    @JvmStatic
    public fun bind(target: FramebufferTarget, id: Int): () -> Unit {
        val prevReadFramebuffer = bound(FramebufferTarget.READ)
        val prevDrawFramebuffer = bound(FramebufferTarget.WRITE)
        bind0(target, id)
        return {
            bind0(FramebufferTarget.READ, prevReadFramebuffer)
            bind0(FramebufferTarget.WRITE, prevDrawFramebuffer)
        }
    }

    @JvmStatic
    public fun bind(id: Int): () -> Unit {
        return bind(FramebufferTarget.READ_WRITE, id)
    }

    @JvmStatic
    public fun delete(id: Int) {
        //#if MC >= 1.17.1
        GL30.glDeleteFramebuffers(id)
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.deleteFramebuffers(id)
        //#else
        //$$ OpenGlHelper.glDeleteFramebuffers(id)
        //#endif
    }

    @JvmStatic
    public fun checkStatus(target: FramebufferTarget = FramebufferTarget.READ_WRITE) {
        val statusCode = GL30.glCheckFramebufferStatus(target.code)
        if (statusCode != GL30.GL_FRAMEBUFFER_COMPLETE) {
            val status = FramebufferStatus.from(statusCode)
            throw FramebufferStatusException(statusCode, status)
        }
    }
}
