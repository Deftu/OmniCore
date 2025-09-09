package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.api.annotations.VersionedBelow

//#if MC >= 1.21.5
import net.minecraft.entity.passive.WolfSoundVariant
import net.minecraft.entity.passive.WolfSoundVariants
//#endif

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvents
//#else
//$$ import dev.deftu.omnicore.common.OmniIdentifier
//#endif

public object OmniSounds {
    //#if MC >= 1.21.5
    private val classicWolfSounds: WolfSoundVariant by lazy {
        SoundEvents.WOLF_SOUNDS[WolfSoundVariants.Type.CLASSIC]!!
    }
    //#endif

    @JvmField
    public val BUTTON_PRESS: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.UI_BUTTON_CLICK
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
            //#else
            //$$ OmniIdentifier.create("gui.button.press")
            //#endif
        )
    }

    @JvmField
    public val EXPERIENCE_ORB_PICKUP: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP
            //#else
            //$$ OmniIdentifier.create("random.orb")
            //#endif
        )
    }

    @JvmField
    public val ITEM_PICKUP: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_ITEM_PICKUP
            //#else
            //$$ OmniIdentifier.create("random.pop")
            //#endif
        )
    }

    @JvmField
    public val LEVEL_UP: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_PLAYER_LEVELUP
            //#else
            //$$ OmniIdentifier.create("random.levelup")
            //#endif
        )
    }

    @JvmField
    public val ITEM_BREAK: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_ITEM_BREAK
                //#if MC >= 1.21.5
                .comp_349()
                //#endif
            //#else
            //$$ OmniIdentifier.create("random.break")
            //#endif
        )
    }

    @JvmField
    public val WOLF_AMBIENT: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.21.5
            classicWolfSounds.comp_3779().comp_349()
            //#elseif MC >= 1.12.2
            //$$ SoundEvents.WOLF_AMBIENT
            //#else
            //$$ OmniIdentifier.create("entity.wolf.ambient")
            //#endif
        )
    }

    @JvmField
    public val WOLF_DEATH: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.21.5
            classicWolfSounds.comp_3780().comp_349()
            //#elseif MC >= 1.12.2
            //$$ SoundEvents.WOLF_DEATH
            //#else
            //$$ OmniIdentifier.create("entity.wolf.death")
            //#endif
        )
    }

    @JvmField
    public val WOLF_GROWL: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.21.5
            classicWolfSounds.comp_3781().comp_349()
            //#elseif MC >= 1.12.2
            //$$ SoundEvents.WOLF_GROWL
            //#else
            //$$ OmniIdentifier.create("entity.wolf.growl")
            //#endif
        )
    }

    @JvmField
    @VersionedBelow("1.21.5")
    public val WOLF_HOWL: OmniSound = noInline {
        //#if MC >= 1.21.5
        OmniSound.invalid()
        //#else
        //$$ OmniSound(
            //#if MC >= 1.12.2
            //$$ SoundEvents.WOLF_HOWL
            //#else
            //$$ OmniIdentifier.create("entity.wolf.howl")
            //#endif
        //$$ )
        //#endif
    }

    @JvmField
    public val WOLF_HURT: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.21.5
            classicWolfSounds.comp_3782().comp_349()
            //#elseif MC >= 1.12.2
            //$$ SoundEvents.WOLF_HURT
            //#else
            //$$ OmniIdentifier.create("entity.wolf.hurt")
            //#endif
        )
    }

    @JvmField
    public val WOLF_PANT: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.21.5
            classicWolfSounds.comp_3783().comp_349()
            //#elseif MC >= 1.12.2
            //$$ SoundEvents.WOLF_PANT
            //#else
            //$$ OmniIdentifier.create("entity.wolf.pant")
            //#endif
        )
    }

    @JvmField
    public val WOLF_SHAKE: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_SHAKE
            //#else
            //$$ OmniIdentifier.create("entity.wolf.shake")
            //#endif
        )
    }

    @JvmField
    public val WOLF_STEP: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_STEP
            //#else
            //$$ OmniIdentifier.create("entity.wolf.step")
            //#endif
        )
    }

    @JvmField
    public val WOLF_WHINE: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.21.5
            classicWolfSounds.comp_3784().comp_349()
            //#elseif MC >= 1.12.2
            //$$ SoundEvents.WOLF_WHINE
            //#else
            //$$ OmniIdentifier.create("entity.wolf.whine")
            //#endif
        )
    }

    @JvmField
    public val NOTE_BLOCK_BASEDRUM: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
            //#else
            //$$ OmniIdentifier.create("note.bd")
            //#endif
        )
    }

    @JvmField
    public val NOTE_BLOCK_BASS: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.BLOCK_NOTE_BLOCK_BASS
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
            //#else
            //$$ OmniIdentifier.create("note.bass")
            //#endif
        )
    }

    @JvmField
    public val NOTE_BLOCK_HARP: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.BLOCK_NOTE_BLOCK_HARP
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
            //#else
            //$$ OmniIdentifier.create("note.harp")
            //#endif
        )
    }

    @JvmField
    public val NOTE_BLOCK_HAT: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.BLOCK_NOTE_BLOCK_HAT
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
            //#else
            //$$ OmniIdentifier.create("note.hat")
            //#endif
        )
    }

    @JvmField
    public val NOTE_BLOCK_PLING: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.BLOCK_NOTE_BLOCK_PLING
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
            //#else
            //$$ OmniIdentifier.create("note.pling")
            //#endif
        )
    }

    @JvmField
    public val NOTE_BLOCK_SNARE: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.BLOCK_NOTE_BLOCK_SNARE
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
            //#else
            //$$ OmniIdentifier.create("note.snare")
            //#endif
        )
    }

    @JvmField
    @VersionedAbove("1.12.2")
    public val NOTE_BLOCK_BELL: OmniSound = noInline {
        //#if MC >= 1.12.2
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_BELL
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.12.2")
    public val NOTE_BLOCK_CHIME: OmniSound = noInline {
        //#if MC >= 1.12.2
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_CHIME
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.12.2")
    public val NOTE_BLOCK_FLUTE: OmniSound = noInline {
        //#if MC >= 1.12.2
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_FLUTE
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.12.2")
    public val NOTE_BLOCK_GUITAR: OmniSound = noInline {
        //#if MC >= 1.12.2
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_GUITAR
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.12.2")
    public val NOTE_BLOCK_XYLOPHONE: OmniSound = noInline {
        //#if MC >= 1.12.2
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.16.5")
    public val NOTE_BLOCK_IRON_XYLOPHONE: OmniSound = noInline {
        //#if MC >= 1.16.5
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.16.5")
    public val NOTE_BLOCK_COW_BELL: OmniSound = noInline {
        //#if MC >= 1.16.5
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.16.5")
    public val NOTE_BLOCK_DIDGERIDOO: OmniSound = noInline {
        //#if MC >= 1.16.5
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.16.5")
    public val NOTE_BLOCK_BIT: OmniSound = noInline {
        //#if MC >= 1.16.5
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_BIT
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    @JvmField
    @VersionedAbove("1.16.5")
    public val NOTE_BLOCK_BANJO: OmniSound = noInline {
        //#if MC >= 1.16.5
        OmniSound(
            SoundEvents.BLOCK_NOTE_BLOCK_BANJO
                //#if MC >= 1.19.4
                .comp_349()
                //#endif
        )
        //#else
        //$$ OmniSound.invalid()
        //#endif
    }

    private inline fun <T> noInline(block: () -> T): T {
        return block()
    }
}
