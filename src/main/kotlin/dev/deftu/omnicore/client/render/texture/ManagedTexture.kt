package dev.deftu.omnicore.client.render.texture

import dev.deftu.omnicore.client.render.OmniTextureManager
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30
import java.nio.ByteBuffer

public class ManagedTexture(
    initialWidth: Int,
    initialHeight: Int,
    override val format: GpuTexture.TextureFormat
) : GlTexture(format) {

    override var id: Int = -1
        private set

    override var width: Int = initialWidth
        private set

    override var height: Int = initialHeight
        private set

    init {
        this.createTexture()
    }

    override fun resize(width: Int, height: Int) {
        if (
            this.width == width &&
            this.height == height &&
            this.id != -1
        ) {
            return
        }

        this.width = width
        this.height = height
        this.close()
        this.createTexture()
    }

    override fun close() {
        if (this.id == -1) {
            return
        }

        OmniTextureManager.deleteTexture(id)
        this.id = -1
    }

    private fun createTexture() {
        this.id = GL11.glGenTextures()
        OmniTextureManager.configureTexture(this.id) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)

            @Suppress("CAST_NEVER_SUCCEEDS")
            GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0,
                when (this.format) {
                    GpuTexture.TextureFormat.RGBA8 -> GL11.GL_RGBA
                    GpuTexture.TextureFormat.DEPTH24_STENCIL8 -> GL30.GL_DEPTH24_STENCIL8
                    GpuTexture.TextureFormat.DEPTH32 -> GL14.GL_DEPTH_COMPONENT32
                },
                this.width, this.height, 0,
                when (this.format) {
                    GpuTexture.TextureFormat.RGBA8 -> GL11.GL_RGBA
                    GpuTexture.TextureFormat.DEPTH24_STENCIL8 -> GL30.GL_DEPTH_STENCIL
                    GpuTexture.TextureFormat.DEPTH32 -> GL11.GL_DEPTH_COMPONENT
                },
                when (this.format) {
                    GpuTexture.TextureFormat.RGBA8 -> GL11.GL_UNSIGNED_BYTE
                    GpuTexture.TextureFormat.DEPTH24_STENCIL8 -> GL30.GL_UNSIGNED_INT_24_8
                    GpuTexture.TextureFormat.DEPTH32 -> GL11.GL_FLOAT
                },
                null as? ByteBuffer
            )
        }
    }

}
