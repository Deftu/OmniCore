package dev.deftu.omnicore.client.render

//#if MC >= 1.16.5
//#if MC < 1.21.5
import dev.deftu.omnicore.mixins.client.Mixin_NativeImage_Accessor
//#endif
import net.minecraft.client.texture.NativeImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
//#else
//$$ import org.lwjgl.BufferUtils
//$$ import org.lwjgl.opengl.GL11
//$$ import java.awt.image.BufferedImage
//$$ import javax.imageio.ImageIO
//#endif

import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

public class OmniImage(
    public val width: Int,
    public val height: Int,
) : AutoCloseable {

    public companion object {

        @JvmStatic
        public fun from(
            //#if MC >= 1.16.5
            native: NativeImage,
            //#else
            //$$ native: BufferedImage,
            //#endif
        ): OmniImage {
            return OmniImage(native.width, native.height).apply {
                //#if MC >= 1.16.5
                this.native.copyFrom(native)
                //#else
                //$$ val graphics = this.native.createGraphics()
                //$$ graphics.drawImage(native, 0, 0, null)
                //$$ graphics.dispose()
                //#endif
            }
        }

        @JvmStatic
        public fun read(path: Path): OmniImage {
            //#if MC >= 1.16.5
            val data = Files.readAllBytes(path)
            val inputStream = ByteArrayInputStream(data)
            return from(NativeImage.read(inputStream))
            //#else
            //$$ return from(ImageIO.read(path.toFile()))
            //#endif
        }

        @JvmStatic
        public fun read(file: File): OmniImage {
            //#if MC >= 1.16.5
            val data = file.readBytes()
            val inputStream = ByteArrayInputStream(data)
            return from(NativeImage.read(inputStream))
            //#else
            //$$ return from(ImageIO.read(file))
            //#endif
        }

    }

    //#if MC >= 1.16.5
    public val native: NativeImage = NativeImage(width, height, false)
    //#else
    //$$ public val native: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    //#endif

    public fun getPixel(x: Int, y: Int): Int {
        //#if MC >= 1.21.2
        //$$ val color = native.getColorArgb(x, y)
        //$$ val red = OmniColor.Argb.getRed(color)
        //$$ val green = OmniColor.Argb.getGreen(color)
        //$$ val blue = OmniColor.Argb.getBlue(color)
        //$$ val alpha = OmniColor.Argb.getAlpha(color)
        //$$ return OmniColor.Rgba.getRgba(red, green, blue, alpha)
        //#elseif MC >= 1.16.5
        return native.getColor(x, y)
        //#else
        //$$ return native.getRGB(x, y)
        //#endif
    }

    public fun setPixel(x: Int, y: Int, color: Int) {
        //#if MC >= 1.21.2
        //$$ val red = OmniColor.Rgba.getRed(color)
        //$$ val green = OmniColor.Rgba.getGreen(color)
        //$$ val blue = OmniColor.Rgba.getBlue(color)
        //$$ val alpha = OmniColor.Rgba.getAlpha(color)
        //$$ val color = OmniColor.Argb.getArgb(red, green, blue, alpha)
        //$$ native.setColorArgb(x, y, color)
        //#elseif MC >= 1.16.5
        native.setColor(x, y, color)
        //#else
        //$$ native.setRGB(x, y, color)
        //#endif
    }

    @JvmOverloads
    public fun loadFromBoundTexture(level: Int = 0) {
        //#if MC >= 1.21.5
        //$$ TODO()
        //#elseif MC >= 1.16.5
        native.loadFromTextureImage(level, false)
        //#else
        //$$ val buffer = BufferUtils.createByteBuffer(width * height * 4)
        //$$ GL11.glGetTexImage(GL11.GL_TEXTURE_2D, level, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)
        //$$ for (y in 0 until height) {
        //$$     for (x in 0 until width) {
        //$$         val index = (y * width + x) * 4
        //$$         val r = buffer.get(index).toInt() and 0xFF
        //$$         val g = buffer.get(index + 1).toInt() and 0xFF
        //$$         val b = buffer.get(index + 2).toInt() and 0xFF
        //$$         val a = buffer.get(index + 3).toInt() and 0xFF
        //$$         setPixel(x, y, (a shl 24) or (r shl 16) or (g shl 8) or b)
        //$$     }
        //$$ }
        //#endif
    }

    public fun flipX() {
        //#if MC >= 1.21.5
        // TODO()
        //#elseif MC >= 1.16.5
        MemoryStack.stackPush().use { memoryStack ->
            val channelCount = native.format.channelCount
            val rowSize = width * channelCount
            val rowBuffer = memoryStack.nmalloc(rowSize)

            for (k in 0..<width / 2) {
                val left = k
                val right = width - 1 - k

                for (i in 0 until height) {
                    val leftOffset = i * rowSize + left * channelCount
                    val rightOffset = i * rowSize + right * channelCount

                    MemoryUtil.memCopy(native.pointer + leftOffset, rowBuffer, rowSize.toLong())
                    MemoryUtil.memCopy(native.pointer + rightOffset, native.pointer + leftOffset, rowSize.toLong())
                    MemoryUtil.memCopy(rowBuffer, native.pointer + rightOffset, rowSize.toLong())
                }
            }
        }
        //#else
        //$$ val graphics = native.createGraphics()
        //$$ graphics.scale(-1.0, 1.0)
        //$$ graphics.drawImage(native, -width, 0, null)
        //$$ graphics.dispose()
        //#endif
    }

    public fun flipY() {
        //#if MC >= 1.21.5
        //$$ TODO()
        //#elseif MC >= 1.16.5
        native.mirrorVertically()
        //#else
        //$$ val graphics = native.createGraphics()
        //$$ graphics.scale(1.0, -1.0)
        //$$ graphics.drawImage(native, 0, -height, null)
        //$$ graphics.dispose()
        //#endif
    }

    public fun saveTo(path: Path) {
        //#if MC >= 1.16.5
        native.writeTo(path)
        //#else
        //$$ ImageIO.write(native, "png", path.toFile())
        //#endif
    }

    public fun saveTo(file: File) {
        //#if MC >= 1.16.5
        native.writeTo(file)
        //#else
        //$$ ImageIO.write(native, "png", file)
        //#endif
    }

    override fun close() {
        //#if MC >= 1.16.5
        native.close()
        //#else
        //$$ native.flush()
        //#endif
    }

}

//#if MC >= 1.16.5 && MC < 1.21.5
public val NativeImage.pointer: Long
    get() = (this as Mixin_NativeImage_Accessor).pointer

public fun NativeImage.checkAllocated() {
    (this as Mixin_NativeImage_Accessor).invokeCheckAllocated()
}
//#endif
