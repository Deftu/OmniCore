package dev.deftu.omnicore.internal.time

import dev.deftu.omnicore.api.time.TickSource
import net.minecraft.world.entity.Entity

public class EntityTickSource(private val entity: Entity) : TickSource {
    override fun ticks(): Long {
        return entity.tickCount.toLong()
    }
}
