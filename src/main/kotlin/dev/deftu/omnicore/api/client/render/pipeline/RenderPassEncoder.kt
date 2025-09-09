package dev.deftu.omnicore.api.client.render.pipeline

public interface RenderPassEncoder {
    public fun texture(name: String, id: Int): RenderPassEncoder
    public fun texture(index: Int, id: Int): RenderPassEncoder

    public fun uniform(name: String, vararg values: Float): RenderPassEncoder
    public fun uniform(name: String, vararg values: Int): RenderPassEncoder

    public fun enableScissor(x: Int, y: Int, width: Int, height: Int): RenderPassEncoder
    public fun disableScissor(): RenderPassEncoder

    /** Encode and submit your pass. One shot; any subsequent calls will throw. */
    public fun submit()
}
