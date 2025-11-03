package dev.deftu.omnicore.internal.networking

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class VanillaCustomPacketPayload(public val identifier: ResourceLocation, public val data: FriendlyByteBuf) : CustomPacketPayload {
    //#if MC >= 1.20.6
    public
    //#else
    //$$ override
    //#endif
    fun write(buf: FriendlyByteBuf) {
        val copy = data.copy()
        try {
            buf.writeBytes(copy)
        } finally {
            println("Releasing buffer in write")
            copy.release()
        }
    }

    //#if MC >= 1.20.6
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return CustomPacketPayload.Type(identifier)
    }
    //#else
    //$$ override fun id(): ResourceLocation {
    //$$     return identifier
    //$$ }
    //#endif
}
