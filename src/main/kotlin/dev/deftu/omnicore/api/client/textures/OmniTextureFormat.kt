package dev.deftu.omnicore.api.client.textures

import org.lwjgl.opengl.GL30

//#if MC >= 1.21.5
import com.mojang.blaze3d.textures.TextureFormat
//#endif

public enum class OmniTextureFormat(public val isColor: Boolean = false) {
    RGBA8(isColor = true),
    DEPTH24_STENCIL8,
    DEPTH32;

    public val attachment: Int
        get() = when (this) {
            RGBA8 -> GL30.GL_COLOR_ATTACHMENT0
            DEPTH24_STENCIL8 -> GL30.GL_DEPTH_STENCIL_ATTACHMENT
            DEPTH32 -> GL30.GL_DEPTH_ATTACHMENT
        }

    public companion object {
        //#if MC >= 1.21.5
        @JvmStatic
        public fun from(format: TextureFormat): OmniTextureFormat {
            return when (format) {
                TextureFormat.RGBA8 -> RGBA8
                TextureFormat.DEPTH32 -> DEPTH32
                else -> throw IllegalArgumentException("Invalid texture format: $format")
            }
        }
        //#endif
    }
}
