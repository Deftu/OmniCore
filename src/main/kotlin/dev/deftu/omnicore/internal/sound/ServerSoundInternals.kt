package dev.deftu.omnicore.internal.sound

import net.minecraft.server.level.ServerPlayer
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.12.2
import net.minecraft.sounds.SoundEvent
//#else
//$$ import net.minecraft.util.ResourceLocation
//#endif

public object ServerSoundInternals {
    @JvmStatic
    @ApiStatus.Internal
    public fun play(
        player: ServerPlayer,
        //#if MC >= 1.12.2
        event: SoundEvent,
        //#else
        //$$ event: ResourceLocation,
        //#endif
        volume: Float,
        pitch: Float
    ) {
        player.playSound(
            //#if MC >= 1.12.2
            event,
            //#else
            //$$ event.toString(),
            //#endif
            volume,
            pitch
        )
    }
}
