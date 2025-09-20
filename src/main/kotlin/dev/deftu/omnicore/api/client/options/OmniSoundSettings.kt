package dev.deftu.omnicore.api.client.options

import net.minecraft.sound.SoundCategory as VanillaSoundCategory

public object OmniSoundSettings {
    public object Capabilities {
        @JvmStatic
        public val isSubtitlesSupported: Boolean
            get() {
                //#if MC >= 1.9
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isNarratorSupported: Boolean
            get() {
                //#if MC >= 1.12.2
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isSoundDeviceSupported: Boolean
            get() {
                //#if MC >= 1.18
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isDirectionalAudioSupported: Boolean
            get() {
                //#if MC >= 1.19
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isMusicFrequencySupported: Boolean
            get() {
                //#if MC >= 1.21.6
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
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
    public val isSubtitlesEnabled: Boolean
        get() {
            //#if MC >= 1.9
            return unwrap(options.showSubtitles)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val narratorMode: NarratorMode
        get() {
            //#if MC >= 1.12.2
            return NarratorMode.from(unwrap(options.narrator))
            //#else
            //$$ return NarratorMode.OFF
            //#endif
        }

    @JvmStatic
    public val isNarratorEnabled: Boolean
        get() {
            //#if MC >= 1.12.2
            return narratorMode != NarratorMode.OFF
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val soundDevice: String?
        get() {
            //#if MC >= 1.18
            return unwrap(options.soundDevice)
            //#else
            //$$ return null
            //#endif
        }

    @JvmStatic
    public val isDirectionalAudioEnabled: Boolean
        get() {
            //#if MC >= 1.19
            return unwrap(options.directionalAudio)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val musicFrequency: MusicFrequency
        get() {
            //#if MC >= 1.21.6
            return MusicFrequency.from(unwrap(options.musicFrequency))
            //#else
            //$$ return MusicFrequency.DEFAULT
            //#endif
        }

    @JvmStatic
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
    public fun getCategoryVolume(category: VanillaSoundCategory): Float {
        return options.getCategorySoundVolume(category)
    }

    /**
     * @return The volume for this category alone, without factoring in the master volume.
     */
    @JvmStatic
    public fun getCategoryVolume(category: SoundCategory): Float {
        return getCategoryVolume(category.vanilla)
    }

    /**
     * @return The volume for this category, multiplied by the value of the master volume, giving you the effective volume.
     */
    @JvmStatic
    public fun getVolume(category: VanillaSoundCategory): Float {
        return options.getSoundVolume(category)
    }

    /**
     * @return The volume for this category, multiplied by the value of the master volume, giving you the effective volume.
     */
    @JvmStatic
    public fun getVolume(category: SoundCategory): Float {
        return getVolume(category.vanilla)
    }
}
