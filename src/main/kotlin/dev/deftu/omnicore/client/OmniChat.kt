package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textile.TextHolder
import dev.deftu.textile.minecraft.toVanilla

/**
 * @since 0.2.2
 * @author Deftu
 */
@GameSide(Side.CLIENT)
public object OmniChat {

    /**
     * Displays a chat message to the client player locally, without sending it to the server.
     *
     * @since 0.2.2
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun showChatMessage(text: TextHolder) {
        val player = OmniClientPlayer.getInstance() ?: throw IllegalStateException("Player is null")
        player
            //#if MC >= 1.21.4
            //$$ .sendMessage(text.toVanilla(), false)
            //#elseif MC >= 1.19.2
            .sendMessage(text.toVanilla())
            //#elseif MC >= 1.16.5
            //$$ .sendMessage(text.toVanilla(), false)
            //#else
            //$$ .sendMessage(text.toVanilla())
            //#endif
    }

    /**
     * Displays a chat message to the client player locally, without sending it to the server.
     *
     * @since 0.2.2
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun showChatMessage(text: String) {
        showChatMessage(SimpleTextHolder(text))
    }

    /**
     * Sends a chat message to the server on behalf of the client player.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun sendChatMessage(text: String) {
        val player = OmniClientPlayer.getInstance() ?: throw IllegalStateException("Player is null")
        //#if MC >= 1.19.3
        player.networkHandler?.sendChatMessage(text)
        //#elseif MC >= 1.19.1
        //$$ player.chatSigned(text, null)
        //#else
        //$$ player.sendChatMessage(text)
        //#endif
    }

    /**
     * Sends a chat message to the server on behalf of the client player.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun sendChatMessage(text: TextHolder) {
        sendChatMessage(text.asString())
    }

}
