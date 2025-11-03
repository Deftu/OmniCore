package dev.deftu.omnicore.internal.sound

import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent

@Suppress("UNCHECKED_CAST")
internal fun unwrapSoundEvent(value: Any): SoundEvent {
    return when (value) {
        is Holder<*> -> (value as Holder<SoundEvent>).value()
        else -> value
    } as SoundEvent
}
