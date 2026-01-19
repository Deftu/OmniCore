package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.omnicore.api.sound.OmniWolfSounds
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants
import net.minecraft.sounds.SoundEvents

public object OmniWolfSoundsImpl : OmniWolfSounds {
    private val classicWolfSounds: WolfSoundVariant.WolfSoundSet by lazy {
        SoundEvents.WOLF_SOUNDS[WolfSoundVariants.SoundSet.CLASSIC]?.adultSounds ?:
            error("Unable to find classic wolf sounds")
    }

    override val shake: OmniSound by CommonWolfSounds.shake

    override val step: OmniSound by CommonWolfSounds.step

    override val bark: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(classicWolfSounds.ambientSound))
    }

    override val death: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(classicWolfSounds.deathSound))
    }

    override val growl: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(classicWolfSounds.growlSound))
    }

    override val hurt: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(classicWolfSounds.hurtSound))
    }

    override val panting: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(classicWolfSounds.pantSound))
    }

    override val whine: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(classicWolfSounds.whineSound))
    }
}
