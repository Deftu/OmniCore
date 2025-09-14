package dev.deftu.omnicore.internal.client.sound

import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvent
//#else
//$$ import net.minecraft.client.sound.SoundInstance
//$$ import net.minecraft.util.Identifier
//$$ import java.lang.invoke.MethodHandle
//$$ import java.lang.invoke.MethodHandles
//#endif

@ApiStatus.Internal
public object ClientSoundInternals {
    //#if MC < 1.12.2
    //$$ private val positionedSoundRecordConstructor: MethodHandle? by lazy {
    //$$     try {
    //$$         val constructor = PositionedSoundInstance::class.java.getDeclaredConstructor(Identifier::class.java, Float::class.java, Float::class.java, Boolean::class.java, Int::class.java, SoundInstance.AttenuationType::class.java, Float::class.java, Float::class.java, Float::class.java)
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
        //$$ event: Identifier,
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
        //$$ event: Identifier,
        //#endif
        volume: Float,
        pitch: Float
    ): PositionedSoundInstance? {
        //#if MC >= 1.12.2
        return PositionedSoundInstance.master(event, pitch, volume)
        //#else
        //$$ return positionedSoundRecordConstructor?.invokeExact(event, volume, pitch, false, 0, SoundInstance.AttenuationType.NONE, 0.0f, 0.0f, 0.0f) as? PositionedSoundInstance
        //#endif
    }
}
