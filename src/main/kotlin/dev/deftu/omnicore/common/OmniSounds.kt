package dev.deftu.omnicore.common

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.annotations.VersionedAbove

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvents
//#else
//$$ import dev.deftu.omnicore.common.OmniIdentifier
//#endif

public object OmniSounds {

    @JvmField
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
    public val ITEM_BREAK: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_ITEM_BREAK
            //#else
            //$$ OmniIdentifier.create("random.break")
            //#endif
        )
    }

    @JvmField
    @GameSide(Side.CLIENT)
    public val WOLF_AMBIENT: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_AMBIENT
            //#else
            //$$ OmniIdentifier.create("entity.wolf.ambient")
            //#endif
        )
    }

    @JvmField
    @GameSide(Side.CLIENT)
    public val WOLF_DEATH: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_DEATH
            //#else
            //$$ OmniIdentifier.create("entity.wolf.death")
            //#endif
        )
    }

    @JvmField
    @GameSide(Side.CLIENT)
    public val WOLF_GROWL: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_GROWL
            //#else
            //$$ OmniIdentifier.create("entity.wolf.growl")
            //#endif
        )
    }

    @JvmField
    @GameSide(Side.CLIENT)
    public val WOLF_HOWL: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_HOWL
            //#else
            //$$ OmniIdentifier.create("entity.wolf.howl")
            //#endif
        )
    }

    @JvmField
    @GameSide(Side.CLIENT)
    public val WOLF_HURT: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_HURT
            //#else
            //$$ OmniIdentifier.create("entity.wolf.hurt")
            //#endif
        )
    }

    @JvmField
    @GameSide(Side.CLIENT)
    public val WOLF_PANT: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_PANT
            //#else
            //$$ OmniIdentifier.create("entity.wolf.pant")
            //#endif
        )
    }

    @JvmField
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
    public val WOLF_WHINE: OmniSound = noInline {
        OmniSound(
            //#if MC >= 1.12.2
            SoundEvents.ENTITY_WOLF_WHINE
            //#else
            //$$ OmniIdentifier.create("entity.wolf.whine")
            //#endif
        )
    }

    @JvmField
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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

    /**
     * Adapted from EssentialGG UniversalCraft under LGPL-3.0
     * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
     */
    private inline fun <T> noInline(init: () -> T): T = init()

}
