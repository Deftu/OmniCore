package dev.deftu.omnicore.common

//#if MC >= 1.20.4
import net.minecraft.network.PacketByteBuf

internal interface OmniCustomPayloadDataHolder {

    @Suppress("FunctionName")
    fun `omnicore$getData`(): PacketByteBuf?

    @Suppress("FunctionName")
    fun `omnicore$setData`(data: PacketByteBuf)

}
//#endif
