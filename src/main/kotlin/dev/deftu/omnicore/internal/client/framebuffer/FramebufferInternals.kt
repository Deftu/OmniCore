package dev.deftu.omnicore.internal.client.framebuffer

import dev.deftu.omnicore.api.client.framebuffer.FramebufferTarget
import dev.deftu.omnicore.internal.client.exceptions.FramebufferStatusException
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

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
        return GL30.glGenFramebuffers()
    }

    @JvmStatic
    public fun bound(target: FramebufferTarget): Int {
        return GL11.glGetInteger(target.binding)
    }

    @JvmStatic
    public fun bind0(target: FramebufferTarget, id: Int) {
        GL30.glBindFramebuffer(target.code, id)
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
        GL30.glDeleteFramebuffers(id)
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
