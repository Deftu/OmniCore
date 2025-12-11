@file:JvmName("OmniClientServers")

package dev.deftu.omnicore.api.client.network

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.screen.currentScreen
import dev.deftu.omnicore.api.client.world
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen

//#if MC >= 1.17.1
import net.minecraft.client.multiplayer.resolver.ServerAddress
//#endif

//#if MC < 1.20.4
//$$ import dev.deftu.omnicore.api.client.player
//#endif

public val serverBrand: String?
    get() {
        //#if MC >= 1.20.4
        return client.connection?.serverBrand()
        //#else
        //$$ return player?.serverBrand
        //#endif
    }

public val currentServer: OmniServerInfo?
    get() = client.currentServer?.let(OmniServerInfo::fromServerInfo)

public val currentServerName: String?
    get() = currentServer?.name

public val currentServerAddress: String?
    get() = currentServer?.address

public val isInLan: Boolean
    get() = currentServer?.isLocal ?: false

public val isInSingleplayer: Boolean
    get() {
        //#if MC >= 1.19.4
        return client.isSingleplayer
        //#else
        //$$ val localServer = client.singleplayerServer
        //#if MC >= 1.16.5
        //$$ val isPublic = localServer?.isPublished ?: false
        //#else
        //$$ val isPublic = localServer?.public ?: false
        //#endif
        //$$ return localServer != null && !isPublic
        //#endif
    }

public val isInMultiplayer: Boolean
    get() = (world != null) && !isInSingleplayer && isMultiplayerEnabled && !isMultiplayerBanned && currentServerAddress != null

public val isMultiplayerEnabled: Boolean
    get() {
        //#if MC >= 1.19.2
        return client.allowsMultiplayer()
        //#else
        //$$ // No direct equivalent, just assume true
        //$$ return true
        //#endif
    }

public val isMultiplayerBanned: Boolean
    get() {
        //#if MC >= 1.20.4
        return client.multiplayerBan() != null
        //#elseif MC >= 1.19.2
        //$$ return client.shouldShowBanNotice()
        //#else
        //$$ // No direct equivalent, just assume false
        //$$ return false
        //#endif
    }

@JvmOverloads
public fun connectTo(entry: OmniServerInfo, screen: Screen = defaultScreen()) {
    val serverInfo = entry.toServerData()

    //#if MC >= 1.17.1
    val serverAddress = ServerAddress.parseString(entry.address)
    ConnectScreen.startConnecting(
        JoinMultiplayerScreen(screen),
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
    //$$ currentScreen = ConnectScreen(JoinMultiplayerScreen(screen), client, serverInfo)
    //#endif
}

@JvmOverloads
public fun connectTo(hostname: String, name: String = hostname, isLocal: Boolean = false, screen: Screen = defaultScreen()) {
    val entry = OmniServerInfo(name, hostname, isLocal)
    connectTo(entry)
}

private fun defaultScreen(): Screen {
    return currentScreen ?: TitleScreen()
}
