package dev.deftu.omnicore.internal.networking

//#if FORGE
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.minecraft.network.Packet

public abstract class OmniPacketHandler : SimpleChannelInboundHandler<Packet<*>>() {
    public abstract fun read(ctx: ChannelHandlerContext, packet: Packet<*>?)

    public final override fun channelRead0(ctx: ChannelHandlerContext, packet: Packet<*>?) {
        read(ctx, packet)
        ctx.fireChannelRead(packet)
    }
}
//#endif
