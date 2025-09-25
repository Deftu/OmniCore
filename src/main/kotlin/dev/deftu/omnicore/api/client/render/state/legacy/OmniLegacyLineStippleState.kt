package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.api.client.render.state.TrackedState

//#if MC <= 1.16.5
//$$ import org.lwjgl.opengl.GL11
//#endif

public data class OmniLegacyLineStippleState(
    public val isEnabled: Boolean,
    public val factor: Int,
    public val pattern: Short,
) : TrackedState<OmniLegacyLineStippleState> {
    override var prevState: OmniLegacyLineStippleState? = null
        private set

    override fun submit(saveLast: Boolean) {
        //#if MC <= 1.16.5
        //$$ if (saveLast) {
        //$$     prevState = OmniLegacyRenderStates.lineStipple
        //$$ }
        //$$
        //$$ if (isEnabled) {
        //$$     GL11.glEnable(GL11.GL_LINE_STIPPLE)
        //$$     GL11.glLineStipple(factor, pattern)
        //$$ } else {
        //$$     GL11.glDisable(GL11.GL_LINE_STIPPLE)
        //$$ }
        //#endif
    }

    public fun asEnabled(): OmniLegacyLineStippleState {
        return if (isEnabled) this else OmniLegacyLineStippleState(true, factor, pattern)
    }

    public fun asDisabled(): OmniLegacyLineStippleState {
        return if (!isEnabled) this else OmniLegacyLineStippleState(false, factor, pattern)
    }

    public fun withState(isEnabled: Boolean): OmniLegacyLineStippleState {
        return if (this.isEnabled == isEnabled) this else OmniLegacyLineStippleState(isEnabled, factor, pattern)
    }

    public fun withParams(factor: Int, pattern: Short): OmniLegacyLineStippleState {
        return if (this.factor == factor && this.pattern == pattern) this else OmniLegacyLineStippleState(isEnabled, factor, pattern)
    }
}
