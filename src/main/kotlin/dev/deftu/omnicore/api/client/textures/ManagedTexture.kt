package dev.deftu.omnicore.api.client.textures

import dev.deftu.omnicore.internal.client.textures.TextureHelper
import dev.deftu.omnicore.internal.client.textures.TextureInternals
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30
import java.nio.ByteBuffer

public class ManagedTexture(
    initialWidth: Int,
    initialHeight: Int,
    override val format: OmniTextureFormat
) : AbstractGlTexture(format) {
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

        TextureInternals.delete(id)
        this.id = -1
    }

    private fun createTexture() {
        this.id = TextureInternals.create()
        TextureHelper.with(this.id) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)

            @Suppress("CAST_NEVER_SUCCEEDS")
            GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0,
                when (this.format) {
                    OmniTextureFormat.RGBA8 -> GL11.GL_RGBA
                    OmniTextureFormat.DEPTH24_STENCIL8 -> GL30.GL_DEPTH24_STENCIL8
                    OmniTextureFormat.DEPTH32 -> GL14.GL_DEPTH_COMPONENT32
                },
                this.width, this.height, 0,
                when (this.format) {
                    OmniTextureFormat.RGBA8 -> GL11.GL_RGBA
                    OmniTextureFormat.DEPTH24_STENCIL8 -> GL30.GL_DEPTH_STENCIL
                    OmniTextureFormat.DEPTH32 -> GL11.GL_DEPTH_COMPONENT
                },
                when (this.format) {
                    OmniTextureFormat.RGBA8 -> GL11.GL_UNSIGNED_BYTE
                    OmniTextureFormat.DEPTH24_STENCIL8 -> GL30.GL_UNSIGNED_INT_24_8
                    OmniTextureFormat.DEPTH32 -> GL11.GL_FLOAT
                },
                null as? ByteBuffer
            )
        }
    }
}
