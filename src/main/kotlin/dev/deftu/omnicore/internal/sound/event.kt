package dev.deftu.omnicore.internal.sound

import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent

@Suppress("UNCHECKED_CAST")
internal fun unwrapSoundEvent(value: Any): SoundEvent {
    return when (value) {
        is RegistryEntry<*> -> (value as RegistryEntry<SoundEvent>).value()
        else -> value
    } as SoundEvent
}
