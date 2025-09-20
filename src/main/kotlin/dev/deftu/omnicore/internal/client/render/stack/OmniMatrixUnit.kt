package dev.deftu.omnicore.internal.client.render.stack

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.math.OmniMatrix3f
import dev.deftu.omnicore.api.math.OmniMatrix4f

public data object OmniMatrixUnit : OmniMatrixStack {
    private val entry = OmniMatrixStack.Entry(OmniMatrix4f.identity(), OmniMatrix3f.identity())

    override val isEmpty: Boolean
        get() = true

    override val current: OmniMatrixStack.Entry
        get() = entry

    override fun deepCopy(): OmniMatrixStack {
        return this
    }

    override fun push() {
        // no-op
    }

    override fun pop(): OmniMatrixStack.Entry {
        return entry
    }

    override fun translate(x: Float, y: Float, z: Float) {
        // no-op
    }

    override fun scale(x: Float, y: Float, z: Float) {
        // no-op
    }

    override fun rotate(angle: Float, axisX: Float, axisY: Float, axisZ: Float, isDegrees: Boolean) {
        // no-op
    }

    override fun <R> runWithGlobalState(block: () -> R): R {
        return block()
    }

    override fun <R> runReplacingGlobalState(block: () -> R): R {
        return block()
    }
}
