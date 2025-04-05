package dev.deftu.omnicore.client.render

import com.mojang.blaze3d.platform.TextureUtil
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.OmniClient
import net.minecraft.client.texture.AbstractTexture
import net.minecraft.client.texture.NativeImage
import net.minecraft.resource.ResourceManager
import java.io.Closeable
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
@GameSide(Side.CLIENT)
public class ReleasedDynamicTexture(
    public val width: Int,
    public val height: Int,
    //#if MC >= 1.14
    data: NativeImage?
    //#else
    //$$ data: IntArray?
    //#endif
//#if MC >= 1.14
) : AbstractTexture() {
    //#else
//$$ ) : AbstractTexture() {
//#endif
    public constructor(width: Int, height: Int) : this(width, height, null)
    //#if MC >= 1.14
    public constructor(image: NativeImage) : this(image.width, image.height, image)
    //#else
    //$$ public constructor(image: BufferedImage) : this(image.width, image.height, image.getRGB(0, 0, image.width, image.height, null, 0, image.width))
    //#endif

    private var resources = Resources(this)
    //#if MC >= 1.14
    private var data by resources::data

    init {
        resources.data = data ?: NativeImage(width, height, true)
    }
    //#else
    //$$ private var data = data ?: IntArray(width * height)
    //#endif

    @GameSide(Side.CLIENT)
    public var uploaded: Boolean = false

    //#if MC <= 1.21.3
    override fun load(resourceManager: ResourceManager?) {
    }
    //#endif

    private fun allocGlId() = super.getGlId()

    @GameSide(Side.CLIENT)
    public fun upload() {
        if (!uploaded) {
            //#if MC >= 1.17
            TextureUtil.prepareImage(allocGlId(), width, height)
            //#elseif MC >= 1.16
            //$$ TextureUtil.allocate(allocGlId(), width, height)
            //#elseif MC >= 1.14
            //$$ TextureUtil.prepareImage(allocGlId(), width, height)
            //#else
            //$$ TextureUtil.allocateTexture(allocGlId(), width, height)
            //#endif

            //#if MC >= 1.14
            OmniTextureManager.configureTexture(allocGlId()) {
                data?.upload(0, 0, 0, false)
            }
            data = null
            //#else
            //$$ TextureUtil.uploadTexture(allocGlId(), data, width, height)
            //$$ data = IntArray(0)
            //#endif

            uploaded = true
            resources.glId = allocGlId()
            Resources.drainCleanupQueue()
        }
    }

    @GameSide(Side.CLIENT)
    override fun getGlId(): Int {
        upload()
        return super.getGlId()
    }

    @GameSide(Side.CLIENT)
    override fun clearGlId() {
        super.clearGlId()
        resources.glId = -1
    }

    private class Resources(
        referent: ReleasedDynamicTexture
    ) : PhantomReference<ReleasedDynamicTexture>(referent, referenceQueue), Closeable {
        var glId: Int = -1
        //#if MC >= 1.14
        var data: NativeImage? = null
            set(value) {
                field?.close()
                field = value
            }
        //#endif

        init {
            toBeCleanedUp.add(this)
        }

        override fun close() {
            toBeCleanedUp.remove(this)
            if (glId != -1) {
                OmniClient.textureManager.deleteTexture(glId)
                glId = -1
            }

            //#if MC >= 1.14
            data = null
            //#endif
        }

        companion object {
            val referenceQueue = ReferenceQueue<ReleasedDynamicTexture>()
            val toBeCleanedUp: MutableSet<Resources> = Collections.newSetFromMap(ConcurrentHashMap())

            fun drainCleanupQueue() {
                while (true) {
                    ((referenceQueue.poll() ?: break) as Resources).close()
                }
            }
        }
    }
}
