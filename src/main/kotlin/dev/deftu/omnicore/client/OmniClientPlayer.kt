package dev.deftu.omnicore.client

import com.mojang.authlib.GameProfile
import dev.deftu.omnicore.api.OmniGameMode
import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side
import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.common.*
import dev.deftu.omnicore.common.world.OmniBiomeData
import dev.deftu.omnicore.common.world.OmniChunkData
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.UUID

//#if MC >= 1.12.2
import net.minecraft.world.GameMode
import net.minecraft.util.Arm
//#elseif MC >= 1.8.9
//$$ import net.minecraft.world.level.LevelInfo.GameMode
//#endif

//#if MC == 1.16.5
//$$ import net.minecraft.util.math.ChunkSectionPos
//#endif

//#if MC <= 1.18.2
//#if MC >= 1.16.5
//$$ import net.minecraft.tag.BlockTags
//#endif
//$$
//$$ import net.minecraft.block.FenceGateBlock
//$$ import net.minecraft.block.BlockState
//$$ import kotlin.math.floor
//#endif

//#if MC <= 1.12.2
//$$ import net.minecraft.block.BlockFence
//$$ import net.minecraft.block.BlockWall
//$$ import net.minecraft.init.Blocks
//#endif

/**
 * A wrapping class for the client player entity and any data related to the player.
 *
 * @since 0.20.0
 * @author Deftu
 */
@GameSide(Side.CLIENT)
public object OmniClientPlayer {

    //#if MC <= 1.18.2
    //$$ private const val STOOD_BLOCK_OFFSET = 1.0E-5f
    //#endif

    /**
     * @return The current instance of any potentially existing client player entity.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getInstance(): ClientPlayerEntity? {
        return OmniClient.getInstance().player
    }

    /**
     * @return True if the player is in a world, false otherwise.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val hasPlayer: Boolean
        @JvmName("hasPlayer")
        get() = getInstance() != null

    /**
     * @return The current session's profile ID.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val uuid: UUID
        get() {
            //#if MC >= 1.20.1
            return OmniClient.getInstance().session.uuidOrNull ?: throw IllegalStateException("Player UUID is null")
            //#else
            //$$ return OmniClient.getInstance().session.profile.id
            //#endif
        }

    /**
     * @return The current session's username.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val name: String
        get() = OmniClient.getInstance().session.username

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val gameProfile: GameProfile
        get() {
            //#if MC >= 1.20.4
            return OmniClient.getInstance().gameProfile
            //#else
            //$$ return OmniClient.getInstance().user.gameProfile
            //#endif
        }

    /**
     * @return The player's current game mode, or [dev.deftu.omnicore.api.OmniGameMode.UNKNOWN] if the player is not in a world.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val gameMode: OmniGameMode
        get() {
            val player = getInstance() ?: return OmniGameMode.UNKNOWN
            val gameMode = OmniClient.networkHandler?.getPlayerListEntry(player.uuid)?.gameMode ?: return OmniGameMode.UNKNOWN

            @Suppress("REDUNDANT_ELSE_IN_WHEN")
            return when (gameMode) {
                GameMode.ADVENTURE -> OmniGameMode.ADVENTURE
                GameMode.CREATIVE -> OmniGameMode.CREATIVE
                GameMode.SPECTATOR -> OmniGameMode.SPECTATOR
                GameMode.SURVIVAL -> OmniGameMode.SURVIVAL
                else -> OmniGameMode.UNKNOWN
            }
        }

    /**
     * @return True if the player is in adventure mode, false otherwise.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isAdventure: Boolean
        get() = gameMode == OmniGameMode.ADVENTURE

    /**
     * @return True if the player is in creative mode, false otherwise.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isCreative: Boolean
        get() = gameMode == OmniGameMode.CREATIVE

    /**
     * @return True if the player is in spectator mode, false otherwise.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isSpectator: Boolean
        get() = gameMode == OmniGameMode.SPECTATOR

    /**
     * @return True if the player is in survival mode, false otherwise.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isSurvival: Boolean
        get() = gameMode == OmniGameMode.SURVIVAL

    //#if MC >= 1.12.2
    @JvmStatic
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.12.2")
    public val vanillaMainArm: Arm
        get() = getInstance()?.mainArm ?: Arm.RIGHT
    //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.12.2")
    public val mainArm: OmniEquipment.EquipmentType
        get() {
            //#if MC >= 1.12.2
            return when (vanillaMainArm) {
                Arm.LEFT -> OmniEquipment.EquipmentType.OFF_HAND
                Arm.RIGHT -> OmniEquipment.EquipmentType.MAIN_HAND
            }
            //#else
            //$$ return OmniEquipment.EquipmentType.MAIN_HAND
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var posX: Double
        get() {
            //#if MC >= 1.16.5
            return getInstance()?.x ?: 0.0
            //#else
            //$$ return getInstance()?.posX ?: 0.0
            //#endif
        }
        set(value) {
            getInstance()?.setPos(value, posY, posZ)
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val prevPosX: Double
        get() {
            //#if MC >= 1.16.5
            return getInstance()?.lastX ?: 0.0
            //#else
            //$$ return getInstance()?.prevPosX ?: 0.0
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var posY: Double
        get() {
            //#if MC >= 1.16.5
            return getInstance()?.y ?: 0.0
            //#else
            //$$ return getInstance()?.posY ?: 0.0
            //#endif
        }
        set(value) {
            getInstance()?.setPos(posX, value, posZ)
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val prevPosY: Double
        get() {
            //#if MC >= 1.16.5
            return getInstance()?.lastY ?: 0.0
            //#else
            //$$ return getInstance()?.prevPosY ?: 0.0
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var posZ: Double
        get() {
            //#if MC >= 1.16.5
            return getInstance()?.z ?: 0.0
            //#else
            //$$ return getInstance()?.posZ ?: 0.0
            //#endif
        }
        set(value) {
            getInstance()?.setPos(posX, posY, value)
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val prevPosZ: Double
        get() {
            //#if MC >= 1.16.5
            return getInstance()?.lastZ ?: 0.0
            //#else
            //$$ return getInstance()?.prevPosZ ?: 0.0
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var pos: Vec3d
        get() {
            //#if MC >= 1.16.5
            return getInstance()?.pos ?: OmniCommonDefaults.defaultVec3d
            //#else
            //#if FABRIC
            //$$ return getInstance()?.getPos() ?: OmniCommonDefaults.defaultVec3d
            //#else
            //$$ return getInstance()?.positionVector ?: OmniCommonDefaults.defaultVec3d
            //#endif
            //#endif
        }
        set(value) {
            getInstance()?.setPos(value.x, value.y, value.z)
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val blockPos: BlockPos
        get() {
            //#if MC >= 1.16.5
            return getInstance()?.blockPos ?: BlockPos.ORIGIN
            //#else
            //$$ return getInstance()?.position ?: BlockPos.ORIGIN
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val blockX: Int
        get() {
            //#if MC >= 1.17.1
            return getInstance()?.blockX ?: 0
            //#else
            //$$ return blockPos.x
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val blockY: Int
        get() {
            //#if MC >= 1.17.1
            return getInstance()?.blockY ?: 0
            //#else
            //$$ return blockPos.y
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val blockZ: Int
        get() {
            //#if MC >= 1.17.1
            return getInstance()?.blockZ ?: 0
            //#else
            //$$ return blockPos.z
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val currentChunk: OmniChunkData?
        get() {
            return OmniClient.currentWorld?.getChunkAt(blockPos)
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val currentBiome: OmniBiomeData?
        get() = currentChunk?.getBiomeAt(blockPos)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var pitch: Float
        get() = getInstance()?.pitch ?: 0f
        set(value) {
            getInstance()?.pitch = value
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val prevPitch: Float
        get() = getInstance()?.lastPitch ?: 0f

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var yaw: Float
        get() = getInstance()?.yaw ?: 0f
        set(value) {
            getInstance()?.yaw = value
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val prevYaw: Float
        get() = getInstance()?.lastYaw ?: 0f

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val stoodBlockPos: BlockPos
        get() {
            //#if MC >= 1.19.2
            return getInstance()?.steppingPos ?: BlockPos.ORIGIN
            //#else
            //$$ val world = OmniClient.world ?: return BlockPos.ORIGIN
            //$$
            //$$ val x = floor(posX).toInt()
            //$$ val y = floor(posY - STOOD_BLOCK_OFFSET).toInt()
            //$$ val z = floor(posZ).toInt()
            //$$
            //$$ val blockPos = OmniBlockPos.from(x, y, z)
            //$$ if (world.getBlockState(blockPos).isAir) {
            //$$     // The player is standing atop a block with a gap above it (f.ex, a fence)
            //$$     val offsetBlockPos = blockPos.shiftDown()
            //$$     val newBlockState = world.getBlockState(offsetBlockPos)
            //$$     if (newBlockState.hasAdditionalCollission()) {
            //$$         return offsetBlockPos
            //$$     }
            //$$ }
            //$$
            //$$ return blockPos
            //#endif
        }

    //#if MC <= 1.18.2
    //$$ private fun BlockState.hasAdditionalCollission(): Boolean {
    //#if MC >= 1.16.5
    //$$     return setOf(BlockTags.FENCES, BlockTags.WALLS).any { tag ->
    //#if FABRIC
    //$$         this.isIn(tag)
    //#else
    //$$         this.`is`(tag)
    //#endif
    //$$     } || this.block is FenceGateBlock
    //#else
    //$$     val block = this.block
    //$$     return block is BlockFence || block is BlockWall || block is BlockFenceGate
    //#endif
    //$$ }
    //#endif

    //#if MC <= 1.12.2
    //$$ private val IBlockState.isAir: Boolean
    //$$     get() = this.block == Blocks.AIR
    //#endif

    /**
     * @return The player's current health, 0 if the player is not in a world.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val health: Float
        get() = getInstance()?.health ?: 0f

    /**
     * @return The player's current hunger, 0 if the player is not in a world.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val hunger: Int
        get() = getInstance()?.hungerManager?.foodLevel ?: 0

    /**
     * @return The player's current saturation, 0 if the player is not in a world.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val saturation: Float
        get() = getInstance()?.hungerManager?.saturationLevel ?: 0f

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val snapshot: OmniPlayerState
        get() {
            return OmniPlayerState(
                uuid = uuid,
                name = name,
                prevPos = Vec3d(prevPosX, prevPosY, prevPosZ),
                pos = Vec3d(posX, posY, posZ),
                blockPos = blockPos,
                gameMode = gameMode,
                currentChunk = currentChunk,
                prevYaw = prevYaw,
                yaw = yaw,
                prevPitch = prevPitch,
                pitch = pitch,
                health = health,
                hunger = hunger,
                saturation = saturation
            )
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    @Deprecated("Use hasPlayer() instead", ReplaceWith("hasPlayer()"))
    public fun getHasPlayer(): Boolean {
        return hasPlayer
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getEquipment(type: OmniEquipment.EquipmentType): ItemStack? {
        return getInstance()?.getEquipment(type)
    }

}
