package dev.deftu.omnicore.internal.client.image

import dev.deftu.omnicore.api.client.image.OmniImage
import java.io.File
import java.nio.file.Path
import kotlin.use

//#if MC >= 1.16.5
import net.minecraft.client.texture.NativeImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
//#else
//$$ import java.awt.image.BufferedImage
//#endif

public class OmniImageImpl(override val width: Int, override val height: Int) : OmniImage {
    //#if MC >= 1.16.5
    override val native: NativeImage = NativeImage(width, height, false)
    //#else
    //$$ override val native: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    //#endif

    override operator fun get(x: Int, y: Int): Int {
        //#if MC >= 1.21.2
        val color = native.getColorArgb(x, y)
        val red = OmniColor.Argb.getRed(color)
        val green = OmniColor.Argb.getGreen(color)
        val blue = OmniColor.Argb.getBlue(color)
        val alpha = OmniColor.Argb.getAlpha(color)
        return OmniColor.Rgba.getRgba(red, green, blue, alpha)
        //#elseif MC >= 1.16.5
        //$$ return native.getColor(x, y)
        //#else
        //$$ return native.getRGB(x, y)
        //#endif
    }

    override operator fun set(x: Int, y: Int, color: Int) {
        //#if MC >= 1.21.2
        val red = OmniColor.Rgba.getRed(color)
        val green = OmniColor.Rgba.getGreen(color)
        val blue = OmniColor.Rgba.getBlue(color)
        val alpha = OmniColor.Rgba.getAlpha(color)
        val color = OmniColor.Argb.getArgb(red, green, blue, alpha)
        native.setColorArgb(x, y, color)
        //#elseif MC >= 1.16.5
        //$$ native.setColor(x, y, color)
        //#else
        //$$ native.setRGB(x, y, color)
        //#endif
    }

    override fun flipX() {
        //#if MC >= 1.16.5
        MemoryStack.stackPush().use { memoryStack ->
            val channelCount = native.format.channelCount
            val rowSize = width * channelCount
            val rowBuffer = memoryStack.nmalloc(rowSize)

            for (k in 0..<width / 2) {
                val right = width - 1 - k

                for (i in 0 until height) {
                    val leftOffset = i * rowSize + k * channelCount
                    val rightOffset = i * rowSize + right * channelCount

                    MemoryUtil.memCopy(native.imageId() + leftOffset, rowBuffer, rowSize.toLong())
                    MemoryUtil.memCopy(native.imageId() + rightOffset, native.imageId() + leftOffset, rowSize.toLong())
                    MemoryUtil.memCopy(rowBuffer, native.imageId() + rightOffset, rowSize.toLong())
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

    override fun flipY() {
        //#if MC >= 1.16.5
        MemoryStack.stackPush().use { memoryStack ->
            val channelCount = native.format.channelCount
            val rowSize = width * channelCount
            val rowBuffer = memoryStack.nmalloc(rowSize)

            for (i in 0..<height / 2) {
                val bottom = height - 1 - i

                MemoryUtil.memCopy(native.imageId() + i * rowSize, rowBuffer, rowSize.toLong())
                MemoryUtil.memCopy(native.imageId() + bottom * rowSize, native.imageId() + i * rowSize, rowSize.toLong())
                MemoryUtil.memCopy(rowBuffer, native.imageId() + bottom * rowSize, rowSize.toLong())
            }
        }
        //#else
        //$$ val graphics = native.createGraphics()
        //$$ graphics.scale(1.0, -1.0)
        //$$ graphics.drawImage(native, 0, -height, null)
        //$$ graphics.dispose()
        //#endif
    }

    override fun saveTo(path: Path) {
        //#if MC >= 1.16.5
        native.writeTo(path)
        //#else
        //$$ ImageIO.write(native, "png", path.toFile())
        //#endif
    }

    override fun saveTo(file: File) {
        //#if MC >= 1.16.5
        native.writeTo(file)
        //#else
        //$$ ImageIO.write(native, "png", file)
        //#endif
    }

    override fun deepCopy(): OmniImage {
        //#if MC >= 1.16.5
        val copy = OmniImageImpl(width, height)
        copy.native.copyFrom(native)
        return copy
        //#else
        //$$ val copy = OmniImage(width, height)
        //$$ val graphics = copy.native.createGraphics()
        //$$ graphics.drawImage(native, 0, 0, null)
        //$$ graphics.dispose()
        //$$ return copy
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
