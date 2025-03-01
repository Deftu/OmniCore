package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.textile.MutableTextHolder
import dev.deftu.textile.SimpleMutableTextHolder
import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textile.TextHolder
import dev.deftu.textile.minecraft.MinecraftTextFormat
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import java.util.function.Consumer

/**
 * The source given to commands executed under OmniCore's client-sided command dispatcher.
 *
 * Only provided so that the command source can be consistent across all versions, effectively providing the same functionality as any given loader's default client-sided command source (if present).
 *
 * @since 0.19.0
 * @author Deftu
 */
@GameSide(Side.CLIENT)
public class OmniClientCommandSource {

    public companion object {

        /**
         * Singleton instance of the client-sided command source, given to all client-sided command execution contexts.
         *
         * @since 0.19.0
         * @author Deftu
         */
        @JvmField
        @GameSide(Side.CLIENT)
        public val UNIT: OmniClientCommandSource = OmniClientCommandSource()

    }

    /**
     * @since 0.19.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public val client: MinecraftClient
        get() = OmniClient.getInstance()

    /**
     * @since 0.19.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public val player: ClientPlayerEntity
        get() = OmniClient.player ?: throw IllegalAccessException("Player is null when it shouldn't be")

    /**
     * @since 0.19.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public val world: ClientWorld
        get() = OmniClient.world ?: throw IllegalAccessException("World is null when it shouldn't be")

    /**
     * Displays a message to the player. This message is ONLY displayed in the client's chat, and is not sent to the server.
     *
     * @since 0.19.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun showMessage(text: TextHolder) {
        OmniChat.showChatMessage(text)
    }

    /**
     * Displays a message to the player. This message is ONLY displayed in the client's chat, and is not sent to the server.
     *
     * @since 0.19.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun showMessage(text: String) {
        OmniChat.showChatMessage(text)
    }

    /**
     * Displays a red formatted error message to the player. This message is ONLY displayed in the client's chat, and is not sent to the server.
     *
     * @param block Allows for further formatting of the error message.
     * @since 0.19.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun showError(error: Throwable, block: Consumer<MutableTextHolder> = Consumer {  }) {
        val text = SimpleMutableTextHolder(error.message ?: "An error occurred")
            .format(MinecraftTextFormat.RED)
        block.accept(text)
        showMessage(text)
    }

    /**
     * Displays a red formatted error message to the player. This message is ONLY displayed in the client's chat, and is not sent to the server.
     *
     * @since 0.19.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun showError(text: TextHolder) {
        showMessage(text.formatted(MinecraftTextFormat.RED))
    }

    /**
     * Displays a red formatted error message to the player. This message is ONLY displayed in the client's chat, and is not sent to the server.
     *
     * @since 0.19.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun showError(text: String) {
        showError(SimpleTextHolder(text))
    }

}
