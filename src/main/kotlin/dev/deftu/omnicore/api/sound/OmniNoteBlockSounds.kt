package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.api.annotations.VersionedAbove

public interface OmniNoteBlockSounds {
    public val basedrum: OmniSound
    public val bass: OmniSound
    public val harp: OmniSound
    public val hat: OmniSound
    public val pling: OmniSound
    public val snare: OmniSound

    @VersionedAbove("1.12.2") public val bell: OmniSound
    @VersionedAbove("1.12.2") public val chime: OmniSound
    @VersionedAbove("1.12.2") public val flute: OmniSound
    @VersionedAbove("1.12.2") public val guitar: OmniSound
    @VersionedAbove("1.12.2") public val xylophone: OmniSound

    @VersionedAbove("1.16.5") public val ironXylophone: OmniSound
    @VersionedAbove("1.16.5") public val cowBell: OmniSound
    @VersionedAbove("1.16.5") public val didgeridoo: OmniSound
    @VersionedAbove("1.16.5") public val bit: OmniSound
    @VersionedAbove("1.16.5") public val banjo: OmniSound
}
