package dev.deftu.omnicore.internal.mixins;

import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 1.20.4
import net.minecraft.server.network.ServerCommonPacketListenerImpl;

@Mixin(ServerCommonPacketListenerImpl.class)
//#else
//$$ import net.minecraft.server.network.ServerGamePacketListenerImpl;
//$$
//$$ @Mixin(ServerGamePacketListenerImpl.class)
//#endif
public interface Mixin_ConnectionAccessor {
    @Accessor Connection getConnection();
}
