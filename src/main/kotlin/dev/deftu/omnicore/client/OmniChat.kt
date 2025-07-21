package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.textile.TextHolder
import dev.deftu.textile.minecraft.*

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
    public fun displayClientMessage(text: MCTextHolder<*>) {
        val player = OmniClientPlayer.getInstance() ?: throw IllegalStateException("Player is null")
        player
            //#if MC >= 1.21.2
            .sendMessage(text.asVanilla(), false)
            //#elseif MC >= 1.19.2
            //$$ .sendMessage(text.asVanilla())
            //#elseif MC >= 1.16.5
            //$$ .sendMessage(text.asVanilla(), false)
            //#else
            //$$ .sendMessage(text.asVanilla())
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
    public fun displayClientMessage(text: String) {
        displayClientMessage(MCSimpleTextHolder(text))
    }

    @JvmStatic
    @JvmOverloads
    @GameSide(Side.CLIENT)
    public fun displayErrorMessage(throwable: Throwable, isDetailed: Boolean = true) {
        val text = MCSimpleMutableTextHolder(throwable.message ?: "An error occurred")
            .addFormatting(MCTextFormat.RED)
        if (isDetailed) {
            text.setHoverEvent(MCHoverEvent.ShowText(throwable.toReadableStackTrace()))
        }

        displayClientMessage(text)
    }

    @JvmStatic
    @JvmOverloads
    @GameSide(Side.CLIENT)
    public fun displayErrorMessage(text: MCTextHolder<*>, throwable: Throwable, isDetailed: Boolean = true) {
        val displayedText = MCSimpleMutableTextHolder("")
            .append(text)
        if (isDetailed) {
            displayedText.setHoverEvent(MCHoverEvent.ShowText(throwable.toReadableStackTrace()))
        }

        displayClientMessage(displayedText)
    }

    /**
     * Sends a chat message to the server on behalf of the client player.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun sendPlayerMessage(text: String) {
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
    public fun sendPlayerMessage(text: TextHolder<*, *>) {
        sendPlayerMessage(text.asString())
    }

    private fun Throwable.toReadableStackTrace(): String {
        return this.stackTraceToString()
            .replace("\r\n", "\n")
            .replace("\t", " ".repeat(4))
    }

}
