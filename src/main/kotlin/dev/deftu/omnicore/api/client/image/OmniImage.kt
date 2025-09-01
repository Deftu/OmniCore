package dev.deftu.omnicore.api.client.image

import java.io.File
import java.nio.file.Path

//#if MC >= 1.16.5
import net.minecraft.client.texture.NativeImage
//#else
//$$ import java.awt.image.BufferedImage
//#endif

public interface OmniImage : AutoCloseable {
    public val width: Int
    public val height: Int

    //#if MC >= 1.16.5
    public val native: NativeImage
    //#else
    //$$ public val native: BufferedImage
    //#endif

    public operator fun get(x: Int, y: Int): Int
    public operator fun set(x: Int, y: Int, color: Int)

    public fun flipX()
    public fun flipY()

    public fun saveTo(path: Path)
    public fun saveTo(file: File)

    public fun deepCopy(): OmniImage
}
