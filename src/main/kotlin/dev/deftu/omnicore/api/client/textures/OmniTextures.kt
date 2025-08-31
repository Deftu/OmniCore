package dev.deftu.omnicore.api.client.textures

//#if MC >= 1.21.6
import com.mojang.blaze3d.textures.GpuTexture
import com.mojang.blaze3d.textures.GpuTextureView
import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side
import net.minecraft.client.texture.GlTextureView
//#endif

//#if MC >= 1.21.5
import net.minecraft.client.texture.GlTexture
//#endif

@GameSide(Side.CLIENT)
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
