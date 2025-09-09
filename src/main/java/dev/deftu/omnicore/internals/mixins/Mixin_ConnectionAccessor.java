package dev.deftu.omnicore.internals.mixins;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerCommonNetworkHandler.class)
public interface Mixin_ConnectionAccessor {
    @Accessor
    ClientConnection getConnection();
}
