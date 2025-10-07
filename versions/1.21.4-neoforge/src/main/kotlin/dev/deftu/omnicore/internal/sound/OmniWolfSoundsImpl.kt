package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.omnicore.api.sound.OmniWolfSounds
import net.minecraft.sounds.SoundEvents

public object OmniWolfSoundsImpl : OmniWolfSounds {
    override val shake: OmniSound by CommonWolfSounds.shake
    
    override val step: OmniSound by CommonWolfSounds.step
    
    override val bark: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOLF_AMBIENT))
    }
    
    override val death: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOLF_DEATH))
    }
    
    override val growl: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOLF_GROWL))
    }
    
    override val hurt: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOLF_HURT))
    }
    
    override val panting: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOLF_PANT))
    }
    
    override val whine: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOLF_WHINE))
    }
}
