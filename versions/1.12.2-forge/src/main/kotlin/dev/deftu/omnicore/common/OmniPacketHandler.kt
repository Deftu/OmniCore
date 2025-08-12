package dev.deftu.omnicore.common

//#if FORGE
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.minecraft.network.Packet

public abstract class OmniPacketHandler : SimpleChannelInboundHandler<Packet<*>>() {

    public abstract fun handleRead(ctx: ChannelHandlerContext, packet: Packet<*>?): Boolean

    public final override fun channelRead0(ctx: ChannelHandlerContext, packet: Packet<*>?) {
        if (handleRead(ctx, packet)) {
            return
        }

        ctx.fireChannelRead(packet)
    }

}
//#endif
