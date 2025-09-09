@file:JvmName("OmniClientServers")

package dev.deftu.omnicore.api.client.network

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.world
import dev.deftu.omnicore.client.OmniScreen
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.network.ServerAddress

//#if MC < 1.20.4
//$$ import dev.deftu.omnicore.api.client.player
//#endif

public val serverBrand: String?
    get() {
        //#if MC >= 1.20.4
        return client.networkHandler?.brand
        //#else
        //$$ return player?.serverBrand
        //#endif
    }

public val currentServer: OmniServerInfo?
    get() = client.currentServerEntry?.let(OmniServerInfo::fromServerInfo)

public val currentServerName: String?
    get() = currentServer?.name

public val currentServerAddress: String?
    get() = currentServer?.address

public val isInLan: Boolean
    get() = currentServer?.isLocal ?: false

public val isInSingleplayer: Boolean
    get() = client.isInSingleplayer

public val isInMultiplayer: Boolean
    get() = (world != null) && !isInSingleplayer && isMultiplayerEnabled && !isMultiplayerBanned && currentServerAddress != null

public val isMultiplayerEnabled: Boolean
    get() {
        //#if MC >= 1.19.2
        return client.isMultiplayerEnabled
        //#else
        //$$ // No direct equivalent, just assume true
        //$$ return true
        //#endif
    }

public val isMultiplayerBanned: Boolean
    get() {
        //#if MC >= 1.20.4
        return client.multiplayerBanDetails != null
        //#elseif MC >= 1.19.2
        //$$ return client.shouldShowBanNotice()
        //#else
        //$$ // No direct equivalent, just assume false
        //$$ return false
        //#endif
    }

public fun connectTo(entry: OmniServerInfo) {
    val serverInfo = entry.toServerInfo()

    //#if MC >= 1.17.1
    val serverAddress = ServerAddress.parse(entry.address)
    ConnectScreen.connect(
        MultiplayerScreen(OmniScreen.currentScreen),
        client,
        serverAddress,
        serverInfo,
        //#if MC >= 1.20.1
        false,
        //#endif
        //#if MC >= 1.20.6
        null,
        //#endif
    )
    //#else
    //$$ OmniScreen.currentScreen = ConnectScreen(MultiplayerScreen(OmniScreen.currentScreen), client, serverInfo)
    //#endif
}

@JvmOverloads
public fun connectTo(hostname: String, name: String = hostname, isLocal: Boolean = false) {
    val entry = OmniServerInfo(name, hostname, isLocal)
    connectTo(entry)
}
