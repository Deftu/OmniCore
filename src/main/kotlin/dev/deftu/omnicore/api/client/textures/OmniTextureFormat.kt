package dev.deftu.omnicore.api.client.textures

import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14

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

    //#if MC >= 1.21.5
    public val vanilla: TextureFormat
        get() = when (this) {
            RGBA8 -> TextureFormat.RGBA8
            DEPTH32 -> TextureFormat.DEPTH32
            else -> throw IllegalArgumentException("No vanilla TextureFormat for $this")
        }
    //#endif

    public companion object {
        @JvmStatic
        public fun from(internalFormat: Int): OmniTextureFormat {
            return when (internalFormat) {
                GL11.GL_RGBA8 -> RGBA8
                GL30.GL_DEPTH24_STENCIL8 -> DEPTH24_STENCIL8
                GL14.GL_DEPTH_COMPONENT32 -> DEPTH32
                else -> throw IllegalArgumentException("Invalid internal format: $internalFormat")
            }
        }

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
