@file:Suppress("FunctionName")

package dev.deftu.omnicore.internal.networking

import net.minecraft.network.PacketByteBuf
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public interface UnknownPayloadDataSmuggler {
    public fun `omnicore$getData`(): PacketByteBuf
    public fun `omnicore$setData`(data: PacketByteBuf)
}
