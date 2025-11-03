package dev.deftu.omnicore.api.network

import dev.deftu.omnicore.api.Side
import dev.deftu.omnicore.api.network.codec.StreamCodec
import dev.deftu.omnicore.internal.networking.NetworkingInternals
import io.netty.buffer.Unpooled
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.resources.ResourceLocation
import java.util.function.BiConsumer
import org.apache.logging.log4j.LogManager

//#if MC >= 1.19.4
import dev.deftu.omnicore.internal.mixins.Mixin_ConnectionAccessor
//#endif

public object OmniNetworking {
    private data class Entry<out P : PacketPayload>(
        val codec: StreamCodec<@UnsafeVariance P, FriendlyByteBuf>,
        val handler: BiConsumer<NetworkContext, @UnsafeVariance P>
    )

    private val entries = mutableMapOf<ResourceLocation, Entry<PacketPayload>>()

    @JvmStatic
    public fun <P : PacketPayload> register(
        id: ResourceLocation,
        codec: StreamCodec<P, FriendlyByteBuf>,
        handler: BiConsumer<NetworkContext, P>
    ) {
        check(entries.putIfAbsent(id, Entry(codec, handler::accept)) == null) {
            "Packet with id $id is already registered"
        }
    }

    @JvmStatic
    public fun <P : PacketPayload> register(
        type: PacketType<P, FriendlyByteBuf>,
        handler: BiConsumer<NetworkContext, P>
    ) {
        register(type.id, type.codec, handler)
    }

    public inline fun <reified P : PacketPayload> register(
        id: ResourceLocation,
        codec: StreamCodec<P, FriendlyByteBuf>,
        noinline handler: NetworkContext.(P) -> Unit
    ) {
        register(id, codec, BiConsumer(handler))
    }

    public inline fun <reified P : PacketPayload> register(
        type: PacketType<P, FriendlyByteBuf>,
        noinline handler: NetworkContext.(P) -> Unit
    ) {
        register(type.id, type.codec, BiConsumer(handler))
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    public fun send(player: ServerPlayer, payload: PacketPayload) {
        val id = payload.id
        val entry = entries[id] ?: error("No packet registered with ID '$id'")
        val buf = FriendlyByteBuf(Unpooled.buffer())
        entry.codec.encode(buf, payload)
        NetworkingInternals.send(player, id, buf)
    }

    @JvmStatic
    public fun handle(
        id: ResourceLocation,
        buf: FriendlyByteBuf,
        player: ServerPlayer
    ) {
        val entry = entries[id] ?: error("No packet registered with ID '$id'")
        val payload = entry.codec.decode(buf)
        //#if MC >= 1.19.4
        val connection = (player.connection as Mixin_ConnectionAccessor).connection
        //#else
        //$$ val connection = player.connection.connection
        //#endif
        val context = NetworkContext(Side.SERVER, connection, player)
        entry.handler.accept(context, payload)
    }

    @JvmStatic
    public fun isRegisteredChannel(id: ResourceLocation): Boolean {
        return entries.containsKey(id)
    }
}
