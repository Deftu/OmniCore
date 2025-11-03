package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.api.sound.OmniMiscellaneousSounds
import dev.deftu.omnicore.api.sound.OmniSound
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniMiscellaneousSoundsImpl : OmniMiscellaneousSounds {
    override val buttonClick: OmniSound by lazy {
        OmniSound.of(locationOrThrow("gui.button.press"))
    }
}
