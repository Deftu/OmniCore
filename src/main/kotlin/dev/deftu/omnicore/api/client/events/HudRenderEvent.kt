package dev.deftu.omnicore.api.client.events

import dev.deftu.omnicore.api.client.render.OmniRenderingContext

public data class HudRenderEvent(
    public val context: OmniRenderingContext,
    public val tickDelta: Float
)
