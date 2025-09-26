@file:JvmName("OmniEntities")

package dev.deftu.omnicore.api.entity

import dev.deftu.omnicore.api.data.aabb.OmniAABB
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import net.minecraft.entity.Entity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

public var Entity.currentX: Double
    get() {
        //#if MC >= 1.16.5
        return x
        //#else
        //$$ return posX
        //#endif
    }
    set(value) {
        setPos(value, currentY, currentZ)
    }

public var Entity.currentY: Double
    get() {
        //#if MC >= 1.16.5
        return y
        //#else
        //$$ return posY
        //#endif
    }
    set(value) {
        setPos(currentX, value, currentZ)
    }

public var Entity.currentZ: Double
    get() {
        //#if MC >= 1.16.5
        return z
        //#else
        //$$ return posZ
        //#endif
    }
    set(value) {
        setPos(currentX, currentY, value)
    }

public var Entity.prevX: Double
    get() {
        //#if MC >= 1.16.5
        return lastX
        //#else
        //$$ return prevPosX
        //#endif
    }
    set(value) {
        //#if MC >= 1.16.5
        lastX = value
        //#else
        //$$ prevPosX = value
        //#endif
    }

public var Entity.prevY: Double
    get() {
        //#if MC >= 1.16.5
        return lastY
        //#else
        //$$ return prevPosY
        //#endif
    }
    set(value) {
        //#if MC >= 1.16.5
        lastY = value
        //#else
        //$$ prevPosY = value
        //#endif
    }

public var Entity.prevZ: Double
    get() {
        //#if MC >= 1.16.5
        return lastZ
        //#else
        //$$ return prevPosZ
        //#endif
    }
    set(value) {
        //#if MC >= 1.16.5
        lastZ = value
        //#else
        //$$ prevPosZ = value
        //#endif
    }

public var Entity.currentPos: OmniVec3d
    get() {
        return OmniVec3d(this.currentX, this.currentY, this.currentZ)
    }
    set(value) {
        setPos(value.x, value.y, value.z)
    }

public var Entity.prevPos: OmniVec3d
    get() {
        return OmniVec3d(this.prevX, this.prevY, this.prevZ)
    }
    set(value) {
        this.prevX = value.x
        this.prevY = value.y
        this.prevZ = value.z
    }

public var Entity.currentYaw: Float
    get() = yaw
    set(value) {
        yaw = value
    }

public var Entity.prevYaw: Float
    get() = lastYaw
    set(value) {
        lastYaw = value
    }

public var Entity.currentPitch: Float
    get() = pitch
    set(value) {
        pitch = value
    }

public val Entity.currentRotationVector: OmniVec3d
    get() = computeRotationVector(currentYaw, currentPitch)

public val Entity.entityBoundingBox: OmniAABB
    get() {
        //#if MC >= 1.16.5
        val box = this.boundingBox
        //#else
        //$$ val box = this.entityBoundingBox
        //#endif
        return OmniAABB(box)
    }

@Suppress("RedundantNullableReturnType")
public val Entity.collisionBoundingBox: OmniAABB?
    get() {
        //#if MC >= 1.16.5
        val box = this.boundingBox
        //#else
        //$$ val box = this.collisionBoundingBox ?: return null
        //#endif
        return OmniAABB(box)
    }

public val Entity.renderBoundingBox: OmniAABB
    get() {
        //#if MC >= 1.16.5
        val box = this.boundingBox
        //#elseif MC >= 1.12.2
        //$$ val box = this.renderBoundingBox
        //#else
        //$$ val box = this.boundingBox
        //#endif
        return OmniAABB(box)
    }

public fun Entity.computeRotationVector(yaw: Float, pitch: Float): OmniVec3d {
    val yawRadians = yaw * (PI / 180)
    val pitchRadians = pitch * (PI / 180)

    val yawCos = cos(yawRadians)
    val yawSin = sin(yawRadians)
    val pitchCos = cos(pitchRadians)
    val pitchSin = sin(pitchRadians)

    val x = pitchSin * yawCos
    val y = -yawSin
    val z = pitchCos * yawCos
    return OmniVec3d(x, y, z)
}
