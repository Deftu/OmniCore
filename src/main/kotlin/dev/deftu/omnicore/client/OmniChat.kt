package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Incubating
import dev.deftu.omnicore.annotations.Side
import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textile.TextHolder
import dev.deftu.textile.minecraft.toVanilla

@Incubating
@GameSide(Side.CLIENT)
public object OmniChat {

    @Incubating
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

    @Incubating
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun showChatMessage(text: String) {
        showChatMessage(SimpleTextHolder(text))
    }

}
