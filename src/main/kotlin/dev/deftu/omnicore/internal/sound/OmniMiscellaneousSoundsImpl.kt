package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniMiscellaneousSounds
import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sounds.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniMiscellaneousSoundsImpl : OmniMiscellaneousSounds {
    override val buttonClick: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.UI_BUTTON_CLICK))
    }
}
