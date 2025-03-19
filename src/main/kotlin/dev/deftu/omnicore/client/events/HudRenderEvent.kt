package dev.deftu.omnicore.client.events

import dev.deftu.omnicore.client.render.OmniMatrixStack

public data class HudRenderEvent(public val matrixStack: OmniMatrixStack, public val tickDelta: Float)
