@file:Suppress("FunctionName")

package dev.deftu.omnicore.internal.networking

import net.minecraft.network.FriendlyByteBuf
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public interface UnknownPayloadDataSmuggler {
    public fun `omnicore$getData`(): FriendlyByteBuf
    public fun `omnicore$setData`(data: FriendlyByteBuf)
}
