package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.api.sound.OmniBlockSounds
import dev.deftu.omnicore.api.sound.OmniSound
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniBlockSoundsImpl : OmniBlockSounds {
    override val anvilUse: OmniSound by lazy {
        OmniSound.of(locationOrThrow("random.anvil_use"))
    }

    override val anvilBreak: OmniSound by lazy {
        OmniSound.of(locationOrThrow("random.anvil_break"))
    }

    override val anvilFall: OmniSound by lazy {
        OmniSound.of(locationOrThrow("random.anvil_land"))
    }

    override val anvilLand: OmniSound by lazy {
        OmniSound.of(locationOrThrow("random.anvil_fall"))
    }

    override val anvilPlace: OmniSound by lazy {
        OmniSound.of(locationOrThrow("dig.stone"))
    }

    override val doorOpen: OmniSound by lazy {
        OmniSound.of(locationOrThrow("random.door_open"))
    }

    override val doorClose: OmniSound by lazy {
        OmniSound.of(locationOrThrow("random.door_close"))
    }

    override val chestOpen: OmniSound by lazy {
        OmniSound.of(locationOrThrow("random.chestopen"))
    }

    override val chestClose: OmniSound by lazy {
        OmniSound.of(locationOrThrow("random.chestclosed"))
    }
}
