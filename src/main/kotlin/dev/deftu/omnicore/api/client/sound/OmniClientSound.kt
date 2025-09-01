package dev.deftu.omnicore.api.client.sound

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.omnicore.internal.client.sound.ClientSoundInternals
import net.minecraft.client.sound.SoundManager

public object OmniClientSound {
    @JvmStatic
    public inline val soundManager: SoundManager?
        get() = client.soundManager

    @JvmStatic
    public fun play(sound: OmniSound, volume: Float, pitch: Float) {
        val soundManager = soundManager ?: return
        ClientSoundInternals.play(
            soundManager = soundManager,
            //#if MC >= 1.12.2
            event = sound.event,
            //#else
            //$$ event = sound.identifier,
            //#endif
            volume = volume,
            pitch = pitch
        )
    }
}
