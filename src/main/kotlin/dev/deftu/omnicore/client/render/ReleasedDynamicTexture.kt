package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.OmniClient
import net.minecraft.client.texture.AbstractTexture
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
    data: OmniImage?
//#if MC >= 1.14
) : AbstractTexture() {
    //#else
//$$ ) : AbstractTexture() {
//#endif

    public constructor(width: Int, height: Int) : this(width, height, null)
    public constructor(image: OmniImage) : this(image.width, image.height, image)

    private var resources = Resources(this)
    private var data by resources::data

    init {
        resources.data = data ?: OmniImage(width, height)
    }

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
            data?.prepareTexture(allocGlId())
            data?.uploadTexture(allocGlId())

            data = null
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
        var data: OmniImage? = null
            set(value) {
                field?.close()
                field = value
            }

        init {
            toBeCleanedUp.add(this)
        }

        override fun close() {
            toBeCleanedUp.remove(this)
            if (glId != -1) {
                OmniClient.textureManager.deleteTexture(glId)
                glId = -1
            }

            data = null
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
