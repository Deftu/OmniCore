package dev.deftu.omnicore.internal.client.framebuffer

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.framebuffer.FramebufferTarget
import dev.deftu.omnicore.api.client.framebuffer.OmniFramebuffer
import dev.deftu.omnicore.api.client.framebuffer.ManagedFramebuffer
import dev.deftu.omnicore.api.client.framebuffer.WrappedFramebuffer
import dev.deftu.omnicore.internal.client.exceptions.FramebufferStatusException
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL30

//#if MC >= 1.21.6
import com.mojang.blaze3d.textures.GpuTextureView
//#else
//$$ import dev.deftu.omnicore.api.client.render.OmniResolution
//$$ import org.lwjgl.opengl.GL11
//#endif

//#if MC == 1.21.5
//$$ import com.mojang.blaze3d.textures.GpuTexture
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
    //#if MC >= 1.21.6
    public var colorTextureOverride: GpuTextureView? = null
    public var depthTextureOverride: GpuTextureView? = null
    //#elseif MC >= 1.21.5
    //$$ public var colorTextureOverride: GpuTexture? = null
    //$$ public var depthTextureOverride: GpuTexture? = null
    //#endif

    //#if MC >= 1.20.1 && MC <= 1.21.5
    //$$ public var isBoundOverride: Boolean = false
    //$$ public var currentBoundFramebuffer: OmniFramebuffer? = null
    //#endif

    @JvmStatic
    public fun create(): Int {
        //#if MC >= 1.21.5
        return GlStateManager.glGenFramebuffers()
        //#else
        //$$ return GL30.glGenFramebuffers()
        //#endif
    }

    @JvmStatic
    public fun bound(target: FramebufferTarget): Int {
        if (target == FramebufferTarget.READ_WRITE) {
            throw IllegalArgumentException("Use READ or WRITE targets when querying bound framebuffer.")
        }

        //#if MC >= 1.21.5
        return GlStateManager.getFrameBuffer(target.code)
        //#else
        //$$ return GL11.glGetInteger(target.binding)
        //#endif
    }

    @JvmStatic
    public fun bind0(target: FramebufferTarget, id: Int) {
        //#if MC >= 1.21.5
        GlStateManager._glBindFramebuffer(target.code, id)
        //#else
        //$$ GL30.glBindFramebuffer(target.code, id)
        //#endif
    }

    @JvmStatic
    public fun bind(target: FramebufferTarget, framebuffer: OmniFramebuffer): () -> Unit {
        //#if MC >= 1.21.5
        val prevColorOverride = colorTextureOverride
        val prevDepthOverride = depthTextureOverride

        //#if MC >= 1.21.6
        val colorTexture = framebuffer.vanillaColorTextureView
        val depthTexture = when (framebuffer) {
            is ManagedFramebuffer -> framebuffer.vanillaDepthStencilTextureView
            is WrappedFramebuffer -> framebuffer.vanillaDepthStencilTextureView
            else -> null
        }
        //#elseif MC >= 1.21.5
        //$$ val colorTexture = framebuffer.vanillaColorTexture
        //$$ val depthTexture = when (framebuffer) {
        //$$     is ManagedFramebuffer -> framebuffer.vanillaDepthStencilTexture
        //$$     is WrappedFramebuffer -> framebuffer.vanillaDepthStencilTexture
        //$$     else -> null
        //$$ }
        //#endif

        colorTextureOverride = colorTexture
        depthTextureOverride = depthTexture
        //#endif

        //#if MC >= 1.20.1 && MC <= 1.21.5
        //$$ isBoundOverride = true
        //$$ currentBoundFramebuffer = framebuffer
        //#endif

        val unbind = bind(target, framebuffer.id)
        return {
            //#if MC >= 1.21.5
            colorTextureOverride = prevColorOverride
            depthTextureOverride = prevDepthOverride
            //#endif

            //#if MC >= 1.20.1 && MC <= 1.21.5
            //$$ isBoundOverride = false
            //$$ currentBoundFramebuffer = null
            //#endif

            unbind()
        }
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
    public fun bind(framebuffer: OmniFramebuffer): () -> Unit {
        return bind(FramebufferTarget.READ_WRITE, framebuffer)
    }

    @JvmStatic
    public fun bind(id: Int): () -> Unit {
        return bind(FramebufferTarget.READ_WRITE, id)
    }

    @JvmStatic
    public fun delete(id: Int) {
        //#if MC >= 1.21.5
        GlStateManager._glDeleteFramebuffers(id)
        //#else
        //$$ GL30.glDeleteFramebuffers(id)
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
