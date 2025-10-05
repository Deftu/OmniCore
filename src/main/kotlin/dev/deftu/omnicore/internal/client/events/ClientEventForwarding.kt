package dev.deftu.omnicore.internal.client.events

public object ClientEventForwarding {
    public fun initialize() {
        ClientTickEventForwarding.initialize()
        HudRenderEventForwarding.initialize()
        RenderTickEventForwarding.initialize()
        ScreenEventForwarding.initialize()
    }
}
