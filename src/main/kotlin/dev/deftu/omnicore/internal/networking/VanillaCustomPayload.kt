package dev.deftu.omnicore.internal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class VanillaCustomPayload(public val identifier: Identifier, public val data: PacketByteBuf) : CustomPayload {
    //#if MC >= 1.20.6
    public
    //#else
    //$$ override
    //#endif
    fun write(buf: PacketByteBuf) {
        val copy = data.copy()
        try {
            buf.writeBytes(copy)
        } finally {
            println("Releasing buffer in write")
            copy.release()
        }
    }

    //#if MC >= 1.20.6
    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return CustomPayload.Id(identifier)
    }
    //#else
    //$$ override fun id(): Identifier {
    //$$     return identifier
    //$$ }
    //#endif
}
