package dev.deftu.omnicore.client

import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.client.sound.PositionedSoundInstance
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvent
//#else
//$$ import net.minecraft.client.sound.SoundInstance
//$$ import net.minecraft.util.Identifier
//$$ import java.lang.invoke.MethodHandle
//$$ import java.lang.invoke.MethodHandles
//#endif

public object OmniClientSound {
    @JvmStatic
    @ApiStatus.Internal
    public fun play(
        //#if MC >= 1.12.2
        event: SoundEvent,
        //#else
        //$$ event: Identifier,
        //#endif
        volume: Float,
        pitch: Float
    ) {
        val soundManager = OmniClient.soundManager ?: return // null whenever switching sound devices, or in legacy, when OpenAL fails to initialize
        createPositionedSoundRecord(event, volume, pitch)?.let(soundManager::play)
    }

    @JvmStatic
    public fun play(sound: OmniSound, volume: Float, pitch: Float) {
        play(
            //#if MC >= 1.12.2
            sound.event,
            //#else
            //$$ sound.identifier,
            //#endif
            volume,
            pitch
        )
    }

    //#if MC >= 1.12.2
    private fun createPositionedSoundRecord(event: SoundEvent, volume: Float, pitch: Float): PositionedSoundInstance? {
        return PositionedSoundInstance.master(event, pitch, volume)
    }
    //#else
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
    //$$
    //$$ private fun createPositionedSoundRecord(event: Identifier, volume: Float, pitch: Float): PositionedSoundInstance? {
    //$$     return positionedSoundRecordConstructor?.invokeExact(event, volume, pitch, false, 0, SoundInstance.AttenuationType.NONE, 0.0f, 0.0f, 0.0f) as? PositionedSoundInstance
    //$$ }
    //#endif
}
