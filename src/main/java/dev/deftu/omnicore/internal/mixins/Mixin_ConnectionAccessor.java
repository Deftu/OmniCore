package dev.deftu.omnicore.internal.mixins;

import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 1.20.4
import net.minecraft.server.network.ServerCommonNetworkHandler;

@Mixin(ServerCommonNetworkHandler.class)
//#else
//$$ import net.minecraft.server.network.ServerGamePacketListenerImpl;
//$$
//$$ @Mixin(ServerGamePacketListenerImpl.class)
//#endif
public interface Mixin_ConnectionAccessor {
    @Accessor ClientConnection getConnection();
}
