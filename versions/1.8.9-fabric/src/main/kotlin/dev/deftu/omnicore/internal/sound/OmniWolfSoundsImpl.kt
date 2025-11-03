package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.omnicore.api.sound.OmniWolfSounds

public object OmniWolfSoundsImpl : OmniWolfSounds {
    override val shake: OmniSound by CommonWolfSounds.shake
    
    override val step: OmniSound by CommonWolfSounds.step
    
    override val bark: OmniSound by lazy {
        OmniSound.of(locationOrThrow("mob.wolf.bark"))
    }
    
    override val death: OmniSound by lazy {
        OmniSound.of(locationOrThrow("mob.wolf.death"))
    }
    
    override val growl: OmniSound by lazy {
        OmniSound.of(locationOrThrow("mob.wolf.growl"))
    }
    
    override val hurt: OmniSound by lazy {
        OmniSound.of(locationOrThrow("mob.wolf.hurt"))
    }
    
    override val panting: OmniSound by lazy {
        OmniSound.of(locationOrThrow("mob.wolf.panting"))
    }
    
    override val whine: OmniSound by lazy {
        OmniSound.of(locationOrThrow("mob.wolf.whine"))
    }
}
