package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.omnicore.api.sound.OmniPlayerSounds
import dev.deftu.omnicore.api.sound.OmniSound
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniPlayerSoundsImpl : OmniPlayerSounds {
    override val hurt: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("game.player.hurt"))
    }

    override val death: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("game.player.die"))
    }

    override val swim: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("game.player.swim"))
    }

    override val splash: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("game.neutral.swim.splash"))
    }

    override val burp: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.burp"))
    }

    override val fallBig: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("game.player.hurt.fall.big"))
    }

    override val fallSmall: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("game.player.hurt.fall.small"))
    }
}
