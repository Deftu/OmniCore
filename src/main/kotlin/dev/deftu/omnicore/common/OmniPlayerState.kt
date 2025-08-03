package dev.deftu.omnicore.common

import dev.deftu.omnicore.common.world.OmniChunkData
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.UUID

public data class OmniPlayerState(
    val uuid: UUID,
    val name: String,

    val prevPos: Vec3d,
    val pos: Vec3d,

    val blockPos: BlockPos,
    val gameMode: OmniGameMode,
    val currentChunk: OmniChunkData?,

    val prevYaw: Float,
    val yaw: Float,

    val prevPitch: Float,
    val pitch: Float,

    val health: Float,
    val hunger: Int,
    val saturation: Float,
)
