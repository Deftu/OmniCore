package dev.deftu.omnicore.api.client.network

import dev.deftu.omnicore.api.Side
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.network.NetworkContext
import dev.deftu.omnicore.api.network.PacketPayload
import dev.deftu.omnicore.api.network.PacketType
import dev.deftu.omnicore.api.network.codec.StreamCodec
import dev.deftu.omnicore.internal.client.networking.ClientNetworkingInternals
import io.netty.buffer.Unpooled
import net.minecraft.network.ClientConnection
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.util.function.BiConsumer

public object OmniClientNetworking {
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
    public fun send(payload: PacketPayload) {
        val id = payload.id
        val entry = entries[id] ?: error("No packet registered with ID '${id}'")
        val buf = PacketByteBuf(Unpooled.buffer())
        entry.codec.encode(buf, payload)
        ClientNetworkingInternals.send(id, buf)
    }

    @JvmStatic
    public fun handle(
        id: Identifier,
        buf: PacketByteBuf,
        connection: ClientConnection,
    ) {
        val entry = entries[id] ?: return // ignore unknown
        val payload = entry.codec.decode(buf)
        val context = NetworkContext(Side.CLIENT, connection, client.player)
        entry.handler.accept(context, payload)
    }
}
