package dev.deftu.omnicore.api.client.network

import net.minecraft.client.multiplayer.ServerData

public data class OmniServerInfo(
    public val name: String,
    public val address: String,
    public val isLocal: Boolean,
) {
    public companion object {
        @JvmStatic
        public fun fromServerInfo(serverInfo: ServerData): OmniServerInfo {
            return OmniServerInfo(
                serverInfo.name,
                serverInfo.ip,
                //#if MC >= 1.20.4
                serverInfo.type() == ServerData.Type.LAN,
                //#else
                //$$ serverInfo.isLan,
                //#endif
            )
        }
    }

    public fun connect() {
        connectTo(address, name, isLocal)
    }

    public fun toServerData(): ServerData {
        return ServerData(
            name,
            address,
            //#if MC >= 1.20.4
            if (isLocal) ServerData.Type.LAN else ServerData.Type.OTHER,
            //#else
            //$$ isLocal,
            //#endif
        )
    }

    public fun withAddress(address: String): OmniServerInfo {
        return copy(address = address)
    }

    override fun toString(): String {
        return "[$name] $address (local=$isLocal)"
    }
}
