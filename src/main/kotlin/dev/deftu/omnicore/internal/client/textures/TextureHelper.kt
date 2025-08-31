package dev.deftu.omnicore.internal.client.textures
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL13

@ApiStatus.Internal
public object TextureHelper {
    @JvmStatic
    public fun with(id: Int, block: Runnable) {
        val prevActiveTexture = TextureInternals.bound()
        TextureInternals.bind(id)
        try {
            block.run()
        } finally {
            TextureInternals.bind(prevActiveTexture)
        }
    }

    @JvmStatic
    public fun withUnit(index: Int, block: Runnable) {
        val prevActiveTextureUnit = TextureInternals.activeUnit
        TextureInternals.activeUnit = GL13.GL_TEXTURE0 + index
        block.run()
        TextureInternals.activeUnit = prevActiveTextureUnit
    }
}
