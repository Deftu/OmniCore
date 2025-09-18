package dev.deftu.omnicore.internal.client.image

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.image.OmniImage
import dev.deftu.omnicore.api.client.textures.OmniTextureFormat
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.internal.client.textures.TextureInternals
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11

//#if MC >= 1.21.9
//$$ import java.nio.ByteBuffer
//#elseif MC >= 1.21.5
import java.nio.IntBuffer
//#else
//$$ import com.mojang.blaze3d.platform.TextureUtil
//#endif

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
        GL11.glGetTexImage(
            GL11.GL_TEXTURE_2D, 0,
            getFormat(native),
            GL11.GL_UNSIGNED_BYTE,
            native.imageId()
        )
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

    @JvmStatic
    public fun loadTextureFrom(image: OmniImage): OmniTextureHandle {
        val texture = OmniTextures.create(image.width, image.height, OmniTextureFormat.RGBA8)
        prepareImage(texture.id, image.width, image.height, GL11.GL_RGBA)
        uploadTexture(image, texture.id, image.width, image.height)
        return texture
    }

    @JvmStatic
    public fun prepareImage(id: Int, width: Int, height: Int, format: Int) {
        //#if MC >= 1.21.5
        val unbind = TextureInternals.bind(id)
        GlStateManager._texImage2D(
            GL11.GL_TEXTURE_2D, 0,
            format,
            width, height,
            0,
            format,
            GL11.GL_UNSIGNED_BYTE,
            //#if MC >= 1.21.9
            //$$ null as ByteBuffer?
            //#else
            null as IntBuffer?
            //#endif
        )
        unbind()
        //#elseif MC >= 1.17.1
        //$$ TextureUtil.prepareImage(id, width, height)
        //#elseif MC >= 1.16.5
        //$$ TextureUtil.allocate(id, width, height)
        //#else
        //$$ TextureUtil.allocateTexture(id, width, height)
        //#endif
    }

    @JvmStatic
    public fun uploadTexture(image: OmniImage, id: Int, width: Int, height: Int) {
        //#if MC >= 1.16.5
        val native = image.native
        checkAllocated(native)

        val unbind = TextureInternals.bind(id)
        with(GL11.GL_NEAREST) {
            GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this)
            GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this)
        }

        val unpackRowLength = if (width == native.width) 0 else width
        GlStateManager._pixelStore(GL11.GL_UNPACK_ROW_LENGTH, unpackRowLength)

        GlStateManager._pixelStore(GL11.GL_UNPACK_SKIP_PIXELS, 0)
        GlStateManager._pixelStore(GL11.GL_UNPACK_SKIP_ROWS, 0)
        setUnpackAlignment(native)

        GlStateManager._texSubImage2D(
            GL11.GL_TEXTURE_2D,
            0, 0, 0,
            width,
            height,
            getFormat(native),
            GL11.GL_UNSIGNED_BYTE,
            //#if MC >= 1.21.5
            native.imageId()
            //#else
            //$$ pointer(native)
            //#endif
        )

        unbind()
        //#else
        //$$ val data = image.native.getRGB(0, 0, width, height, null, 0, width)
        //$$ TextureUtil.uploadTexture(id, data, width, height)
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
