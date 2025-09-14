package dev.deftu.omnicore.api.network

import dev.deftu.omnicore.api.Side
import dev.deftu.omnicore.api.network.codec.StreamCodec
import dev.deftu.omnicore.internal.networking.NetworkingInternals
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.function.BiConsumer

//#if MC >= 1.19.4
import dev.deftu.omnicore.internal.mixins.Mixin_ConnectionAccessor
//#endif

public object OmniNetworking {
    private data class Entry<out P : PacketPayload>(
        val codec: StreamCodec<@UnsafeVariance P, PacketByteBuf>,
        val handler: BiConsumer<NetworkContext, @UnsafeVariance P>
    )

    private val entries = mutableMapOf<Identifier, Entry<PacketPayload>>()

    @JvmStatic
    public fun <P : PacketPayload> register(
        id: Identifier,
        codec: StreamCodec<P, PacketByteBuf>,
        handler: BiConsumer<NetworkContext, P>
    ) {
        check(entries.putIfAbsent(id, Entry(codec, handler::accept)) == null) {
            "Packet with id $id is already registered"
        }
    }

    @JvmStatic
    public fun <P : PacketPayload> register(
        type: PacketType<P, PacketByteBuf>,
        handler: BiConsumer<NetworkContext, P>
    ) {
        register(type.id, type.codec, handler)
    }

    public inline fun <reified P : PacketPayload> register(
        id: Identifier,
        codec: StreamCodec<P, PacketByteBuf>,
        noinline handler: NetworkContext.(P) -> Unit
    ) {
        register(id, codec, BiConsumer(handler))
    }

    public inline fun <reified P : PacketPayload> register(
        type: PacketType<P, PacketByteBuf>,
        noinline handler: NetworkContext.(P) -> Unit
    ) {
        register(type.id, type.codec, BiConsumer(handler))
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    public fun send(player: ServerPlayerEntity, payload: PacketPayload) {
        val id = payload.id
        val entry = entries[id] ?: error("No packet registered with ID '$id'")
        val buf = PacketByteBuf(Unpooled.buffer())
        entry.codec.encode(buf, payload)
        NetworkingInternals.send(player, id, buf)
    }

    @JvmStatic
    public fun handle(
        id: Identifier,
        buf: PacketByteBuf,
        player: ServerPlayerEntity
    ) {
        val entry = entries[id] ?: error("No packet registered with ID '$id'")
        val payload = entry.codec.decode(buf)
        //#if MC >= 1.19.4
        val connection = (player.networkHandler as Mixin_ConnectionAccessor).connection
        //#else
        //$$ val connection = player.connection.connection
        //#endif
        val context = NetworkContext(Side.SERVER, connection, player)
        entry.handler.accept(context, payload)
    }
}
