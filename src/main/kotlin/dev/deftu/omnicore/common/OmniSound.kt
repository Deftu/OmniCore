package dev.deftu.omnicore.common

import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.OmniClientSound
import dev.deftu.omnicore.server.OmniServerSound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvent
//#endif

@GameSide(Side.BOTH)
public class OmniSound {

    public companion object {

        @JvmStatic
        @GameSide(Side.BOTH)
        public fun invalid(): OmniSound {
            return OmniSound(OmniIdentifier.create(OmniCore.ID, "invalid"))
        }

    }

    @GameSide(Side.BOTH)
    public val identifier: Identifier

    //#if MC >= 1.12.2
    @GameSide(Side.BOTH)
    public val event: SoundEvent
    //#endif

    @GameSide(Side.BOTH)
    public constructor(identifier: Identifier) {
        this.identifier = identifier
        //#if MC >= 1.12.2
        //#if MC >= 1.19.4
        this.event = SoundEvent.of(identifier)
        //#else
        //$$ this.event = SoundEvent(identifier)
        //#endif
        //#endif
    }

    //#if MC >= 1.12.2
    @GameSide(Side.BOTH)
    public constructor(event: SoundEvent) {
        this.identifier = event.id
        this.event = event
    }
    //#endif

    @GameSide(Side.CLIENT)
    public fun playForClient(volume: Float, pitch: Float) {
        OmniClientSound.play(this, volume, pitch)
    }

    @GameSide(Side.SERVER)
    public fun playForServer(player: ServerPlayerEntity, volume: Float, pitch: Float) {
        OmniServerSound.play(player, this, volume, pitch)
    }

}
