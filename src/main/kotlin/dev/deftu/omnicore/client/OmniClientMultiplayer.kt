package dev.deftu.omnicore.client

import net.minecraft.client.gui.screen.multiplayer.ConnectScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.network.ServerAddress
import net.minecraft.client.network.ServerInfo

public object OmniClientMultiplayer {

    /**
     * @return The server brand, null if the player is not in a server.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val serverBrand: String?
        get() {
            //#if MC >= 1.20.4
            return OmniClient.networkHandler?.brand
            //#else
            //$$ return OmniClientPlayer.getInstance()?.serverBrand
            //#endif
        }

    /**
     * @return The server info, null if the player is not in a server.
     *
     * @since 0.16.0
     * @author Deftu
     */
    @JvmStatic
    public val currentServer: OmniClientServerEntry?
        get() = OmniClient.currentServerInfo?.let(OmniClientServerEntry::fromServerInfo)

    /**
     * @return The server name, null if the player is not in a server.
     *
     * @since 0.16.0
     * @author Deftu
     */
    @JvmStatic
    public val currentServerName: String?
        get() = currentServer?.name

    /**
     * @return The server address, null if the player is not in a server.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val currentServerAddress: String?
        get() = currentServer?.address

    /**
     * @return Whether the player is in a LAN server.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    public val isInLan: Boolean
        get() = currentServer?.isLocal ?: false

    /**
     * @return Whether the player has multiplayer enabled, is allowed to join servers, and is not banned from multiplayer.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val isMultiplayerEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return OmniClient.getInstance().isMultiplayerEnabled
            //#else
            //$$ return true // TODO - Find a way to fetch this value in earlier versions
            //#endif
        }

    /**
     * @return Whether the player is banned from multiplayer.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val isMultiplayerBanned: Boolean
        get() {
            //#if MC >= 1.20.4
            return OmniClient.getInstance().multiplayerBanDetails != null
            //#elseif MC >= 1.19.2
            //$$ return OmniClient.getInstance().shouldShowBanNotice()
            //#else
            //$$ return false // TODO - Find a way to fetch this value in earlier versions
            //#endif
        }

    /**
     * @return Whether the player is in a singleplayer environment.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    public val isInSingleplayer: Boolean
        get() = OmniClient.getInstance().isInSingleplayer

    /**
     * @return Whether the player is in a multiplayer environment.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val isInMultiplayer: Boolean
        get() = OmniClient.world != null && !isInSingleplayer && isMultiplayerEnabled && !isMultiplayerBanned && currentServer?.address != null

    @JvmStatic
    public fun connectTo(entry: OmniClientServerEntry) {
        val serverInfo = entry.toServerInfo()

        //#if MC >= 1.17.1
        val serverAddress = ServerAddress.parse(entry.address)
        ConnectScreen.connect(
            MultiplayerScreen(OmniScreen.currentScreen),
            OmniClient.getInstance(),
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
        //$$ OmniScreen.currentScreen = ConnectScreen(MultiplayerScreen(OmniScreen.currentScreen), OmniClient.getInstance(), serverInfo)
        //#endif
    }

    @JvmStatic
    @JvmOverloads
    public fun connectTo(hostname: String, name: String = hostname, isLocal: Boolean = false) {
        val entry = OmniClientServerEntry(name, hostname, isLocal)
        connectTo(entry)
    }

}
