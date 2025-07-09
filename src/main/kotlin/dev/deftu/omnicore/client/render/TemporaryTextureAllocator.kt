package dev.deftu.omnicore.client.render

import com.mojang.blaze3d.systems.RenderSystem

internal class TemporaryTextureAllocator(
    private val onClose: () -> Unit = {},
) : AutoCloseable {

    private val usedAllocations = mutableListOf<TextureAllocation>()
    private val reusableAllocations = mutableListOf<TextureAllocation>()

    fun allocate(width: Int, height: Int): TextureAllocation {
        var lastTexture = reusableAllocations.removeLastOrNull()

        if (lastTexture != null && (lastTexture.width != width || lastTexture.height != height)) {
            lastTexture.close()
            lastTexture = null
        }

        if (lastTexture == null) {
            lastTexture = TextureAllocation(width, height)
        }

        val device = RenderSystem.getDevice()
        device.createCommandEncoder().clearColorAndDepthTextures(lastTexture.colorTexture, 0, lastTexture.depthTexture, 1.0)
        usedAllocations.add(lastTexture)
        return lastTexture
    }

    fun tick() {
        reusableAllocations.forEach(TextureAllocation::close)
        reusableAllocations.clear()
        reusableAllocations.addAll(usedAllocations)
        usedAllocations.clear()

        if (reusableAllocations.isEmpty()) {
            onClose()
        }
    }

    override fun close() {
        // Ensure all allocations are released
        tick()
        tick()

        assert(usedAllocations.isEmpty())
        assert(reusableAllocations.isEmpty())
    }

}
