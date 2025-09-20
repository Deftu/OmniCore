package dev.deftu.omnicore.internal.client.textures
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL13

@ApiStatus.Internal
public object TextureHelper {
    @JvmStatic
    public fun with(id: Int, block: Runnable) {
        val unbind = TextureInternals.bind(id)
        try {
            block.run()
        } finally {
            unbind()
        }
    }

    @JvmStatic
    public fun withUnit(unit: OmniTextureUnit, block: Runnable) {
        val prevActiveTextureUnit = TextureInternals.activeUnit
        TextureInternals.activeUnit = unit
        block.run()
        TextureInternals.activeUnit = prevActiveTextureUnit
    }
}
