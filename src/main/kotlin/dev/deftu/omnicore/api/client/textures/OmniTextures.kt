package dev.deftu.omnicore.api.client.textures

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.AbstractTexture
import net.minecraft.util.Identifier

//#if MC >= 1.21.6
import com.mojang.blaze3d.textures.GpuTextureView
import net.minecraft.client.texture.GlTextureView
//#endif

//#if MC >= 1.21.5
import net.minecraft.client.texture.GlTexture
import com.mojang.blaze3d.textures.GpuTexture
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
        return wrap(texture.glTextureView)
    }

    @JvmStatic
    public fun wrap(identifier: Identifier): OmniTextureHandle {
        return wrap(MinecraftClient.getInstance().textureManager.getTexture(identifier))
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
