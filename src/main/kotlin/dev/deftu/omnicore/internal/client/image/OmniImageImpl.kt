package dev.deftu.omnicore.internal.client.image

import dev.deftu.omnicore.api.client.image.OmniImage
import dev.deftu.omnicore.api.color.ColorFormat
import dev.deftu.omnicore.api.color.OmniColor
import java.io.File
import java.nio.file.Path
import kotlin.use

//#if MC >= 1.16.5
import com.mojang.blaze3d.platform.NativeImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
//#else
//$$ import java.awt.image.BufferedImage
//$$ import javax.imageio.ImageIO
//#endif

public class OmniImageImpl(override val width: Int, override val height: Int) : OmniImage {
    //#if MC >= 1.16.5
    override val native: NativeImage = NativeImage(width, height, false)
    //#else
    //$$ override val native: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    //#endif

    override operator fun get(x: Int, y: Int): OmniColor {
        //#if MC >= 1.21.2
        val color = native.getPixel(x, y)
        return OmniColor(ColorFormat.ARGB, color)
        //#elseif MC >= 1.16.5
        //$$ return OmniColor(ColorFormat.ABGR, native.getPixelRGBA(x, y))
        //#else
        //$$ return OmniColor(ColorFormat.ARGB, native.getRGB(x, y))
        //#endif
    }

    override operator fun set(x: Int, y: Int, color: OmniColor) {
        //#if MC >= 1.21.2
        native.setPixel(x, y, color.pack(ColorFormat.ARGB))
        //#elseif MC >= 1.16.5
        //$$ native.setPixelRGBA(x, y, color.pack(ColorFormat.ABGR))
        //#else
        //$$ native.setRGB(x, y, color.pack(ColorFormat.ARGB))
        //#endif
    }

    override fun flipX() {
        //#if MC >= 1.16.5
        MemoryStack.stackPush().use { memoryStack ->
            val channelCount = native.format().components()
            val rowSize = width * channelCount
            val rowBuffer = memoryStack.nmalloc(rowSize)

            for (k in 0..<width / 2) {
                val right = width - 1 - k

                for (i in 0 until height) {
                    val leftOffset = i * rowSize + k * channelCount
                    val rightOffset = i * rowSize + right * channelCount

                    //#if MC >= 1.21.5
                    val pointer = native.pointer
                    //#else
                    //$$ val pointer = ImageInternals.pointer(native)
                    //#endif
                    MemoryUtil.memCopy(pointer + leftOffset, rowBuffer, rowSize.toLong())
                    MemoryUtil.memCopy(pointer + rightOffset, pointer + leftOffset, rowSize.toLong())
                    MemoryUtil.memCopy(rowBuffer, pointer + rightOffset, rowSize.toLong())
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
            val channelCount = native.format().components()
            val rowSize = width * channelCount
            val rowBuffer = memoryStack.nmalloc(rowSize)

            for (i in 0..<height / 2) {
                val bottom = height - 1 - i

                //#if MC >= 1.21.5
                val pointer = native.pointer
                //#else
                //$$ val pointer = ImageInternals.pointer(native)
                //#endif
                MemoryUtil.memCopy(pointer + i * rowSize, rowBuffer, rowSize.toLong())
                MemoryUtil.memCopy(pointer + bottom * rowSize, pointer + i * rowSize, rowSize.toLong())
                MemoryUtil.memCopy(rowBuffer, pointer + bottom * rowSize, rowSize.toLong())
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
        native.writeToFile(path)
        //#else
        //$$ ImageIO.write(native, "png", path.toFile())
        //#endif
    }

    override fun saveTo(file: File) {
        //#if MC >= 1.16.5
        native.writeToFile(file)
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
        //$$ val copy = OmniImageImpl(width, height)
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
