package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.omnicore.api.sound.OmniEntitySounds
import dev.deftu.omnicore.api.sound.OmniSound
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniEntitySoundsImpl : OmniEntitySounds {
    override val experienceOrb: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.orb"))
    }

    override val levelUp: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.levelup"))
    }
    
    override val itemPickUp: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.pop"))
    }
    
    override val itemBreak: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.break"))
    }
}
