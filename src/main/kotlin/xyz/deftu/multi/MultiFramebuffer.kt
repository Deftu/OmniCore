package xyz.deftu.multi

//#if MC<=11202
//$$ import net.minecraft.client.renderer.OpenGlHelper;
//#endif

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.platform.TextureUtil
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30
import java.nio.ByteBuffer

class MultiFramebuffer(
    init: Boolean = true,
    private var width: Int = MultiResolution.viewportWidth,
    private var height: Int = MultiResolution.viewportHeight,
    private val depth: Boolean = true
) {
    companion object {
        private val clearColor = floatArrayOf(1f, 1f, 1f, 0f)

        @JvmStatic fun genFramebuffers() =
            //#if MC>=11700
            GlStateManager.glGenFramebuffers()
            //#elseif MC>=11500
            //$$ GlStateManager.genFramebuffers()
            //#else
            //$$ OpenGlHelper.glGenFramebuffers()
            //#endif

        @JvmStatic fun bindFramebuffer(id: Int) {
            //#if MC>=11700
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, id)
            //#elseif MC>=11500
            //$$ GlStateManager.bindFramebuffer(GL30.GL_FRAMEBUFFER, id)
            //#else
            //$$ OpenGlHelper.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id)
            //#endif
        }

        @JvmStatic fun deleteFramebuffers(id: Int) {
            //#if MC>=11700
            GlStateManager._glDeleteFramebuffers(id)
            //#elseif MC>=11500
            //$$ GlStateManager.deleteFramebuffers(id)
            //#else
            //$$ OpenGlHelper.glDeleteFramebuffers(id)
            //#endif
        }

        @JvmStatic fun framebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int) {
            //#if MC>=11700
            GlStateManager._glFramebufferTexture2D(target, attachment, textarget, texture, level)
            //#elseif MC>=11500
            //$$ GlStateManager.framebufferTexture2D(target, attachment, textarget, texture, level)
            //#else
            //$$ OpenGlHelper.glFramebufferTexture2D(target, attachment, textarget, texture, level)
            //#endif
        }

        @JvmStatic fun framebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: Int) {
            //#if MC>=11700
            GlStateManager._glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer)
            //#elseif MC>=11500
            //$$ GL30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer)
            //#else
            //$$ OpenGlHelper.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer)
            //#endif
        }

        @JvmStatic fun genTexture() =
            //#if MC>=11700
            TextureUtil.generateTextureId()
            //#elseif MC>=11600
            //$$ TextureUtil.generateId()
            //#elseif MC>=11500
            //$$ TextureUtil.generateTextureId()
            //#else
            //$$ TextureUtil.glGenTextures()
            //#endif

        @JvmStatic fun genRenderbuffers() =
            //#if MC>=11700
            GlStateManager.glGenRenderbuffers()
            //#elseif MC>=11500
            //$$ GL30.glGenRenderbuffers()
            //#else
            //$$ OpenGlHelper.glGenRenderbuffers()
            //#endif

        @JvmStatic fun getBoundFramebuffer() =
            //#if MC>=11700
            GlStateManager._getInteger(GL30.GL_FRAMEBUFFER_BINDING)
            //#elseif MC>=11500
            //$$ GlStateManager.getInteger(GL30.GL_FRAMEBUFFER_BINDING)
            //#else
            //$$ GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING)
            //#endif

        @JvmStatic fun getMinecraftFramebuffer() =
            MultiClient.getInstance().framebuffer.fbo
    }

    private var framebuffer = -1
    private var colorAttachment = -1
    private var depthAttachment = -1

    private val initialized: Boolean
        get() = framebuffer != -1
    val isBound: Boolean
        get() = getBoundFramebuffer() == framebuffer

    init {
        if (init) {
            initialize()
        }
    }

    fun initialize() {
        println("Trying to initialize framebuffer - $framebuffer")
        if (initialized) return
        println("Initializing framebuffer - $framebuffer")
        framebuffer = genFramebuffers()
        println("Initialized framebuffer - $framebuffer")
        if (colorAttachment == -1) colorAttachment = genTexture()
        if (depth) {
            if (depthAttachment == -1) depthAttachment = genTexture()
            enableDepth()
        }

        setupColorAttachment()
        bind(false)
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorAttachment, 0)
        if (depth) GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthAttachment, 0)
        checkStatus()
        clear()
        push()
    }

    private fun setupColorAttachment() {
        setTexFilter(GL11.GL_NEAREST)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorAttachment)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?)
    }

    private fun checkStatus() {
        val status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER)
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            val message = when (status) {
                GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT -> "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT"
                GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT -> "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT"
                GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER -> "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER"
                GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER -> "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER"
                GL30.GL_FRAMEBUFFER_UNSUPPORTED -> "GL_FRAMEBUFFER_UNSUPPORTED"
                GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE -> "GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE"
                else -> "Unknown error"
            }

            error("Framebuffer is not complete: $message (${status.toString(16)})")
        }
    }

    fun bind(modifyViewport: Boolean) {
        bindFramebuffer(framebuffer)
        if (modifyViewport) {
            GL11.glViewport(0, 0, width, height)
        }
    }

    fun unbind() {
        bindFramebuffer(getMinecraftFramebuffer())
    }

    fun push() {
        bindFramebuffer(0)
    }

    fun clear() {
        bind(true)
        GL11.glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3])
        var i = GL11.GL_COLOR_BUFFER_BIT
        if (depth) {
            GL11.glClearDepth(1.0)
            i = i or GL11.GL_DEPTH_BUFFER_BIT
        }

        GL11.glClear(i)
        push()
    }

    fun delete() {
        if (colorAttachment > -1) {
            GL11.glDeleteTextures(colorAttachment)
            colorAttachment = -1
        }

        if (depthAttachment > -1) {
            GL11.glDeleteTextures(depthAttachment)
            depthAttachment = -1
        }

        unbind()
        deleteFramebuffers(framebuffer)
        framebuffer = -1
    }

    fun resize(width: Int, height: Int) {
        if (framebuffer >= 0) delete()
        this.width = width
        this.height = height
        initialize()
    }

    fun enableDepth() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthAttachment)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, 0)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, null as ByteBuffer?)
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthAttachment, 0)
    }

    fun disableDepth() {
        framebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, 0)
    }

    fun toggleDepth(depth: Boolean) {
        if (depth) {
            enableDepth()
        } else {
            disableDepth()
        }
    }

    fun toggleDepth() {
        toggleDepth(GL30.glGetFramebufferAttachmentParameteri(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE) != 0)
    }


    fun enableStencil() {
        //#if MC<11500
        // TODO
        //#endif
    }

    fun disableStencil() {
        //#if MC<11500
        // TODO
        //#endif
    }

    fun toggleStencil(stencil: Boolean) {
        if (stencil) {
            enableStencil()
        } else {
            disableStencil()
        }
    }

    fun toggleStencil() {
        //#if MC<11500
        // TODO
        //#endif
    }

    fun setTexFilter(filter: Int) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorAttachment)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
    }

    fun getColorAttachment() = colorAttachment
    fun getDepthAttachment() = depthAttachment

    fun getFramebuffer() = framebuffer
    fun getWidth() = width
    fun getHeight() = height
}
