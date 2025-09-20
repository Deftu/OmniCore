package dev.deftu.omnicore.api.client.render

/**
 * Provides you with a means of bypassing Minecraft's new render layering system as of Minecraft 1.21.6.
 * Done by overriding the current render target's output color and depth buffers with our own texture(s), drawing your content,
 * to it/them, restoring the original buffers, and then submitting those overriden texture(s) to the renderer to be drawn.
 *
 * It should be noted that this is a very low-level renderer, and as such, it is not recommended to use it unless you know what you are doing,
 * and that it is automatically used to wrap the rendering of [dev.deftu.omnicore.api.client.screen.OmniScreen]s, allowing you to use the [dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilder] API to render whatever
 * you please. Use it as you wish elsewhere should you need to.
 */
public object ImmediateScreenRenderer {
    @JvmStatic
    public fun initialize() {
        // no-op below 1.21.6
    }

    @JvmStatic
    public fun render(ctx: OmniRenderingContext, block: () -> Unit) {
        block()
    }

    @JvmStatic
    public fun render(ctx: OmniRenderingContext, runnable: Runnable) {
        runnable.run()
    }
}
