package dev.deftu.omnicore.api.client.render.state

//#if MC <= 1.16.5
//$$ import dev.deftu.omnicore.internal.client.render.state.AlphaStateInternals
//#endif

public data class OmniAlphaState(
    public val isEnabled: Boolean,
    public val func: Int,
    public val ref: Float,
) : TrackedState<OmniAlphaState> {
    override var prevState: OmniAlphaState? = null
        private set

    /** Globally applies this state. */
    override fun submit(saveLast: Boolean) {
        //#if MC <= 1.16.5
        //$$ if (saveLast) {
        //$$     prevState = OmniRenderStates.alpha // Capture previous state for restoration if needed
        //$$ }
        //$$
        //$$ if (isEnabled) {
        //$$     AlphaStateInternals.enableAlpha()
        //$$     AlphaStateInternals.alphaFunc(func, ref)
        //$$ } else {
        //$$     AlphaStateInternals.disableAlpha()
        //$$ }
        //#endif
    }

    public companion object {
        @JvmField public val DISABLED: OmniAlphaState = OmniAlphaState(false, 0, 0f)

        @JvmStatic
        public fun asEnabled(function: Int, reference: Float): OmniAlphaState {
            return OmniAlphaState(
                isEnabled = true,
                func = function,
                ref = reference
            )
        }
    }
}
