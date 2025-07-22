package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.sound.SoundCategory as VanillaSoundCategory

@GameSide(Side.CLIENT)
public object OmniSoundSettings {

    @GameSide(Side.CLIENT)
    public object Capabilities {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isSubtitlesSupported: Boolean
            get() {
                //#if MC >= 1.9
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isNarratorSupported: Boolean
            get() {
                //#if MC >= 1.12.2
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isSoundDeviceSupported: Boolean
            get() {
                //#if MC >= 1.18
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isDirectionalAudioSupported: Boolean
            get() {
                //#if MC >= 1.19
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isMusicFrequencySupported: Boolean
            get() {
                //#if MC >= 1.21.6
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isNowPlayingToastSupported: Boolean
            get() {
                //#if MC >= 1.21.6
                return true
                //#else
                //$$ return false
                //#endif
            }

    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isSubtitlesEnabled: Boolean
        get() {
            //#if MC >= 1.9
            return unwrap(options.showSubtitles)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val narratorMode: NarratorMode
        get() {
            //#if MC >= 1.16.2
            return NarratorMode.from(unwrap(options.narrator))
            //#else
            //$$ return NarratorMode.OFF
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isNarratorEnabled: Boolean
        get() {
            //#if MC >= 1.16.2
            return narratorMode != NarratorMode.OFF
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val soundDevice: String?
        get() {
            //#if MC >= 1.18
            return unwrap(options.soundDevice)
            //#else
            //$$ return null
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isDirectionalAudioEnabled: Boolean
        get() {
            //#if MC >= 1.19
            return unwrap(options.directionalAudio)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val musicFrequency: MusicFrequency
        get() {
            //#if MC >= 1.21.6
            return MusicFrequency.from(unwrap(options.musicFrequency))
            //#else
            //$$ return MusicFrequency.DEFAULT
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isNowPlayingToastEnabled: Boolean
        get() {
            //#if MC >= 1.21.6
            return unwrap(options.showNowPlayingToast)
            //#else
            //$$ return false
            //#endif
        }

    /**
     * @return The volume for this category alone, without factoring in the master volume.
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getCategoryVolume(category: VanillaSoundCategory): Float {
        return options.getCategorySoundVolume(category)
    }

    /**
     * @return The volume for this category alone, without factoring in the master volume.
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getCategoryVolume(category: SoundCategory): Float {
        return getCategoryVolume(category.vanilla)
    }

    /**
     * @return The volume for this category, multiplied by the value of the master volume, giving you the effective volume.
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getVolume(category: VanillaSoundCategory): Float {
        return options.getSoundVolume(category)
    }

    /**
     * @return The volume for this category, multiplied by the value of the master volume, giving you the effective volume.
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getVolume(category: SoundCategory): Float {
        return getVolume(category.vanilla)
    }

}
