package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniEntitySounds
import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sound.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniEntitySoundsImpl : OmniEntitySounds {
    override val experienceOrb: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP))
    }

    override val levelUp: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_PLAYER_LEVELUP))
    }

    override val itemPickUp: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_ITEM_PICKUP))
    }

    override val itemBreak: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_ITEM_BREAK))
    }
}
