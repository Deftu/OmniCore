package dev.deftu.omnicore.internal.client.sound

import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.sounds.SoundManager
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.12.2
import net.minecraft.sounds.SoundEvent
//#else
//$$ import net.minecraft.client.audio.ISound
//$$ import net.minecraft.util.ResourceLocation
//$$ import java.lang.invoke.MethodHandle
//$$ import java.lang.invoke.MethodHandles
//#endif

@ApiStatus.Internal
public object ClientSoundInternals {
    //#if MC < 1.12.2
    //$$ private val positionedSoundRecordConstructor: MethodHandle? by lazy {
    //$$     try {
    //$$         val constructor = PositionedSoundRecord::class.java.getDeclaredConstructor(ResourceLocation::class.java, Float::class.java, Float::class.java, Boolean::class.java, Int::class.java, ISound.AttenuationType::class.java, Float::class.java, Float::class.java, Float::class.java)
    //$$         constructor.isAccessible = true
    //$$         MethodHandles.lookup().unreflectConstructor(constructor)
    //$$     } catch (e: Throwable) {
    //$$         e.printStackTrace()
    //$$         null
    //$$     }
    //$$ }
    //#endif

    @JvmStatic
    public fun play(
        soundManager: SoundManager,
        //#if MC >= 1.12.2
        event: SoundEvent,
        //#else
        //$$ event: ResourceLocation,
        //#endif
        volume: Float,
        pitch: Float
    ) {
        createPositionedSoundRecord(event, volume, pitch)?.let(soundManager::play)
    }

    @JvmStatic
    public fun createPositionedSoundRecord(
        //#if MC >= 1.12.2
        event: SoundEvent,
        //#else
        //$$ event: ResourceLocation,
        //#endif
        volume: Float,
        pitch: Float
    ): SimpleSoundInstance? {
        //#if MC >= 1.12.2
        return SimpleSoundInstance.forUI(event, pitch, volume)
        //#else
        //$$ return positionedSoundRecordConstructor?.invokeExact(event, volume, pitch, false, 0, ISound.AttenuationType.NONE, 0.0f, 0.0f, 0.0f) as? PositionedSoundRecord
        //#endif
    }
}
