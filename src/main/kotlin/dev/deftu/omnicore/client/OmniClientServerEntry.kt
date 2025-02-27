package dev.deftu.omnicore.client

public data class OmniClientServerEntry(
    public val name: String,
    public val address: String,
    public val isLocal: Boolean,
) {

    public fun connect() {
        OmniClientMultiplayer.connectTo(address, name, isLocal)
    }

}
