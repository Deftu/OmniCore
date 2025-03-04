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
     * @since 0.2.2
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun showChatMessage(text: TextHolder) {
        val player = OmniClient.player ?: throw IllegalStateException("Player is null")
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
     * @since 0.2.2
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun showChatMessage(text: String) {
        showChatMessage(SimpleTextHolder(text))
    }

}
