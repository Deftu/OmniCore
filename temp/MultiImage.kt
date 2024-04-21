package dev.deftu.omnicore

//#if MC >= 1.16
import net.minecraft.client.texture.NativeImage
//#else
//$$ import java.awt.image.BufferedImage
//#endif

import java.awt.Color

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
public class MultiImage(
    //#if MC >= 1.16
    public val image: NativeImage,
    //#else
    //$$ public val image: BufferedImage,
    //#endif
) {
    public companion object {
        @JvmStatic
        @JvmOverloads
        public fun create(
            width: Int,
            height: Int,
            clear: Boolean = true
        ): MultiImage {
            //#if MC >= 1.16
            return MultiImage(NativeImage(width, height, clear))
            //#else
            //$$ return MultiImage(BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))
            //#endif
        }
    }

    public fun copyFrom(other: MultiImage) {
        //#if MC >= 1.16
        image.copyFrom(other.image)
        //#else
        //$$ image.graphics.drawImage(other.image, 0, 0, other.image.width, other.image.height, null)
        //#endif
    }

    public fun copy(): MultiImage {
        //#if MC >= 1.16
        return MultiImage(NativeImage(getWidth(), getHeight(), image.format.hasAlpha()))
        //#else
        //$$ return MultiImage(BufferedImage(getWidth(), getHeight(), image.type))
        //#endif
    }

    public fun deepCopy(): MultiImage {
        //#if MC >= 1.16
        return MultiImage(NativeImage(image.format, getWidth(), getHeight(), image.format.hasAlpha()).also { it.copyFrom(image) })
        //#else
        //$$ return MultiImage(BufferedImage(getWidth(), getHeight(), image.type).also { it.graphics.drawImage(image, 0, 0, image.width, image.height, null) })
        //#endif
    }

    public fun close() {
        //#if MC >= 1.16
        image.close()
        //#endif
    }

    public fun getPixel(x: Int, y: Int): Int {
        //#if MC >= 1.16
        return image.getColor(x, y)
        //#else
        //$$ return Integer.rotateLeft(image.getRGB(x, y), 8)
        //#endif
    }

    public fun setPixel(x: Int, y: Int, color: Int) {
        //#if MC >= 1.16
        image.setColor(x, y, color)
        //#else
        //$$ image.setRGB(x, y, Integer.rotateRight(color, 8))
        //#endif
    }

    public fun setPixel(x: Int, y: Int, color: Color): Unit = setPixel(x, y, color.rgb)

    public fun getWidth(): Int = image.width
    public fun getHeight(): Int = image.height
}
