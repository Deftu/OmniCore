package dev.deftu.omnicore.api.client.textures

import dev.deftu.omnicore.api.client.client
import net.minecraft.util.Identifier

//#if MC >= 1.21.6
import com.mojang.blaze3d.textures.GpuTextureView
import net.minecraft.client.texture.GlTextureView
//#endif

//#if MC >= 1.21.5
import net.minecraft.client.texture.GlTexture
import com.mojang.blaze3d.textures.GpuTexture
//#endif

//#if MC < 1.21.5
//$$ import dev.deftu.omnicore.internal.client.textures.TextureInternals
//$$ import org.lwjgl.opengl.GL11
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.texture.AbstractTexture
//#else
//$$ import net.minecraft.client.renderer.texture.ITextureObject
//#endif

public object OmniTextures {
    @JvmStatic
    public fun create(
        width: Int,
        height: Int,
        format: OmniTextureFormat
    ): ManagedTexture {
        return ManagedTexture(width, height, format)
    }

    @JvmStatic
    public fun wrap(
        id: Int,
        width: Int,
        height: Int,
        format: OmniTextureFormat
    ): WrappedTexture {
        return WrappedTexture(id, width, height, format)
    }

    @JvmStatic
    public fun wrap(
        //#if MC >= 1.16.5
        texture: AbstractTexture,
        //#else
        //$$ texture: ITextureObject,
        //#endif
    ): WrappedTexture {
        //#if MC >= 1.21.5
        return wrap(texture.glTexture)
        //#else
        //$$ val id =
            //#if MC >= 1.16.5
            //$$ texture.id
            //#else
            //$$ texture.glTextureId
            //#endif
        //$$
        //$$ val unbind = TextureInternals.bind(id)
        //$$ try {
        //$$     val width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH).coerceAtLeast(1)
        //$$     val height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT).coerceAtLeast(1)
        //$$     val internalFormat = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT)
        //$$     val format = OmniTextureFormat.from(internalFormat)
        //$$     return WrappedTexture(id, width, height, format)
        //$$ } finally {
        //$$     unbind()
        //$$ }
        //#endif
    }

    @JvmStatic
    public fun wrap(location: Identifier): OmniTextureHandle {
        val texture = client.textureManager.getTexture(location)
            ?: throw IllegalArgumentException("Texture $location is not loaded")
        return wrap(texture)
    }

    //#if MC >= 1.21.5
    @JvmStatic
    public fun wrap(vanillaTexture: GlTexture): WrappedTexture {
        return WrappedTexture(
            vanillaTexture.glId,
            vanillaTexture.getWidth(0),
            vanillaTexture.getHeight(0),
            OmniTextureFormat.from(vanillaTexture.format)
        )
    }

    @JvmStatic
    public fun wrap(vanillaTexture: GpuTexture): WrappedTexture {
        return wrap(
            vanillaTexture as? GlTexture
                ?: throw IllegalArgumentException("Provided texture is not a GL texture")
        )
    }
    //#endif

    //#if MC >= 1.21.6
    @JvmStatic
    public fun wrap(vanillaTexture: GlTextureView): WrappedTexture {
        val texture = vanillaTexture.texture()
        return WrappedTexture(
            texture.glId,
            vanillaTexture.getWidth(0),
            vanillaTexture.getHeight(0),
            OmniTextureFormat.from(texture.format)
        )
    }

    @JvmStatic
    public fun wrap(vanillaTexture: GpuTextureView): WrappedTexture {
        return wrap(
            vanillaTexture as? GlTextureView
                ?: throw IllegalArgumentException("Provided texture view is not a GL texture view")
        )
    }
    //#endif
}
