package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.internal.sound.ServerSoundInternals
import net.minecraft.server.network.ServerPlayerEntity

public object OmniServerSounds {
    @JvmStatic
    public fun play(player: ServerPlayerEntity, sound: OmniSound, volume: Float, pitch: Float) {
        ServerSoundInternals.play(
            player,
            //#if MC >= 1.12.2
            sound.event,
            //#else
            //$$ sound.location,
            //#endif
            volume,
            pitch
        )
    }
}
