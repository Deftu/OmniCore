package dev.deftu.omnicore.server

import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.server.network.ServerPlayerEntity
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvent
//#else
//$$ import net.minecraft.util.Identifier
//#endif

public object OmniServerSound {

    @JvmStatic
    @ApiStatus.Internal
    public fun play(
        player: ServerPlayerEntity,
        //#if MC >= 1.12.2
        event: SoundEvent,
        //#else
        //$$ event: Identifier,
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

    @JvmStatic
    public fun play(player: ServerPlayerEntity, sound: OmniSound, volume: Float, pitch: Float) {
        play(
            player,
            //#if MC >= 1.12.2
            sound.event,
            //#else
            //$$ sound.identifier,
            //#endif
            volume,
            pitch
        )
    }

}
