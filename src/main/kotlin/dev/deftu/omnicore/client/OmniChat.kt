package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Incubating
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.exceptions.UnavailableClientPlayerException
import dev.deftu.textile.Text
import dev.deftu.textile.impl.SimpleText
import dev.deftu.textile.toVanilla
import kotlin.jvm.Throws

@Incubating
@GameSide(Side.CLIENT)
public object OmniChat {

    @Incubating
    @JvmStatic
    @GameSide(Side.CLIENT)
    @Throws(UnavailableClientPlayerException::class)
    public fun showChatMessage(text: Text) {
        val player = OmniClient.requirePlayer()
        player
            //#if MC >= 1.19.2
            .sendMessage(text.toVanilla())
            //#elseif MC >= 1.16.5
            //$$ .sendMessage(text.toVanilla(), false)
            //#elseif MC >= 1.12.2
            //$$ .sendMessage(text.toVanilla())
            //#else
            //$$ .addChatMessage(text.toVanilla())
            //#endif
    }

    @Incubating
    @JvmStatic
    @GameSide(Side.CLIENT)
    @Throws(UnavailableClientPlayerException::class)
    public fun showChatMessage(text: String) {
        showChatMessage(SimpleText(text))
    }

}
