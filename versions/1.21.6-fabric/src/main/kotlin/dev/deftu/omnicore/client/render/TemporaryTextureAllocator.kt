package dev.deftu.omnicore.client.render

import com.mojang.blaze3d.systems.RenderSystem

internal class TemporaryTextureAllocator(
    private val onClose: () -> Unit = {},
) : AutoCloseable {

    private val usedAllocations = mutableListOf<TextureAllocation>()
    private val reusableAllocations = mutableListOf<TextureAllocation>()

    fun allocate(width: Int, height: Int): TextureAllocation {
        var texture = reusableAllocations.removeLastOrNull()

        if (texture != null && (texture.width != width || texture.height != height)) {
            texture.close()
            texture = null
        }

        if (texture == null) {
            texture = TextureAllocation(width, height)
        }

        val device = RenderSystem.getDevice()
        device.createCommandEncoder().clearColorAndDepthTextures(texture.colorTexture, 0, texture.depthTexture, 1.0)
        usedAllocations.add(texture)
        return texture
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
