package dev.deftu.omnicore.client

import net.minecraft.client.network.ServerInfo

public data class OmniClientServerEntry(
    public val name: String,
    public val address: String,
    public val isLocal: Boolean,
) {

    public companion object {

        @JvmStatic
        public fun fromServerInfo(serverInfo: ServerInfo): OmniClientServerEntry {
            return OmniClientServerEntry(
                serverInfo.name,
                serverInfo.address,
                //#if MC >= 1.20.4
                serverInfo.serverType == ServerInfo.ServerType.LAN,
                //#else
                //$$ serverInfo.isLocal,
                //#endif
            )
        }

    }

    public fun connect() {
        OmniClientMultiplayer.connectTo(address, name, isLocal)
    }

    public fun toServerInfo(): ServerInfo {
        return ServerInfo(
            name,
            address,
            //#if MC >= 1.20.4
            if (isLocal) ServerInfo.ServerType.LAN else ServerInfo.ServerType.OTHER,
            //#else
            //$$ isLocal,
            //#endif
        )
    }

    public fun withAddress(address: String): OmniClientServerEntry {
        return copy(address = address)
    }

    override fun toString(): String {
        return "[$name] $address (local=$isLocal)"
    }

}
