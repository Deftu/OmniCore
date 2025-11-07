package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.internal.sound.ServerSoundInternals
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

public object OmniServerSounds {
    @JvmStatic
    public fun play(player: ServerPlayer, sound: OmniSound, volume: Float, pitch: Float) {
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

    @JvmStatic
    public fun play(server: MinecraftServer, sound: OmniSound, volume: Float, pitch: Float) {
        for (player in server.playerList.players) {
            play(player, sound, volume, pitch)
        }
    }
}
