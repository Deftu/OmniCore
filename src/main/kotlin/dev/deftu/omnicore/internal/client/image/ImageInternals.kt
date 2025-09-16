package dev.deftu.omnicore.internal.client.image

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.image.OmniImage
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11

//#if MC >= 1.16.5
import dev.deftu.omnicore.internal.mixins.client.Mixin_NativeImageAllocation
import net.minecraft.client.texture.NativeImage
//#else
//$$ import dev.deftu.omnicore.api.color.OmniColor
//$$ import org.lwjgl.BufferUtils
//#endif

@ApiStatus.Internal
public object ImageInternals {
    @JvmStatic
    public fun loadTextureInto(
        texture: OmniTextureHandle,
        image: OmniImage
    ) {
        val native = image.native

        //#if MC >= 1.21.5
        checkAllocated(native)
        setPackAlignment(native)
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, getFormat(native), GL11.GL_UNSIGNED_BYTE, native.imageId())
        //#elseif MC >= 1.16.5
        //$$ native.downloadTexture(0, false)
        //#else
        //$$ val width = image.width
        //$$ val height = image.height
        //$$ val buffer = BufferUtils.createByteBuffer(width * height * 4)
        //$$ GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)
        //$$ for (y in 0 until height) {
        //$$     for (x in 0 until width) {
        //$$         val index = (y * width + x) * 4
        //$$         val r = buffer.get(index).toInt() and 0xFF
        //$$         val g = buffer.get(index + 1).toInt() and 0xFF
        //$$         val b = buffer.get(index + 2).toInt() and 0xFF
        //$$         val a = buffer.get(index + 3).toInt() and 0xFF
        //$$         image[x, y] = OmniColor(r, g, b, a)
        //$$     }
        //$$ }
        //#endif
    }

    //#if MC >= 1.16.5
    @JvmStatic
    public fun getFormat(nativeImage: NativeImage): Int {
        return when (nativeImage.format) {
            NativeImage.Format.RGBA -> GL11.GL_RGBA
            NativeImage.Format.RGB -> GL11.GL_RGB
            NativeImage.Format.LUMINANCE_ALPHA -> GL11.GL_LUMINANCE_ALPHA
            NativeImage.Format.LUMINANCE -> GL11.GL_LUMINANCE
            else -> throw IllegalArgumentException("Unsupported format: ${nativeImage.format}")
        }
    }

    //#if MC < 1.21.5
    //$$ @JvmStatic
    //$$ public fun pointer(image: NativeImage): Long {
    //$$     return (image as Mixin_NativeImageAllocation).pixels
    //$$ }
    //#endif

    @JvmStatic
    public fun checkAllocated(image: NativeImage) {
        (image as Mixin_NativeImageAllocation).invokeCheckAllocated()
    }

    @JvmStatic
    public fun setUnpackAlignment(image: NativeImage) {
        GlStateManager._pixelStore(GL11.GL_UNPACK_ALIGNMENT, image.format.channelCount)
    }

    @JvmStatic
    public fun setPackAlignment(image: NativeImage) {
        GlStateManager._pixelStore(GL11.GL_PACK_ALIGNMENT, image.format.channelCount)
    }
    //#endif
}
