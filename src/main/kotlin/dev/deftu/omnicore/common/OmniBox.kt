package dev.deftu.omnicore.common

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d

//#if MC <= 1.8.9
//$$ import kotlin.math.max
//$$ import kotlin.math.min
//#endif

@GameSide(Side.BOTH)
public object OmniBox {

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun from(
        minX: Double,
        minY: Double,
        minZ: Double,
        maxX: Double,
        maxY: Double,
        maxZ: Double
    ): Box {
        return Box(minX, minY, minZ, maxX, maxY, maxZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun from(pos: BlockPos): Box {
        //#if MC >= 1.12.2
        return Box(pos)
        //#else
        //$$ return Box(
        //$$     pos.x.toDouble(),
        //$$     pos.y.toDouble(),
        //$$     pos.z.toDouble(),
        //$$     pos.x + 1.0,
        //$$     pos.y + 1.0,
        //$$     pos.z + 1.0
        //$$ )
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun usingMinX(box: Box, minX: Double): Box {
        return Box(minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun usingMinY(box: Box, minY: Double): Box {
        return Box(box.minX, minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun usingMinZ(box: Box, minZ: Double): Box {
        return Box(box.minX, box.minY, minZ, box.maxX, box.maxY, box.maxZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun usingMaxX(box: Box, maxX: Double): Box {
        return Box(box.minX, box.minY, box.minZ, maxX, box.maxY, box.maxZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun usingMaxY(box: Box, maxY: Double): Box {
        return Box(box.minX, box.minY, box.minZ, box.maxX, maxY, box.maxZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun usingMaxZ(box: Box, maxZ: Double): Box {
        return Box(box.minX, box.minY, box.minZ, box.maxX, box.maxY, maxZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun usingMin(box: Box, min: Vec3d): Box {
        return Box(min.x, min.y, min.z, box.maxX, box.maxY, box.maxZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun usingMax(box: Box, max: Vec3d): Box {
        return Box(box.minX, box.minY, box.minZ, max.x, max.y, max.z)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun offsetBy(box: Box, offsetX: Double, offsetY: Double, offsetZ: Double): Box {
        return box.offset(offsetX, offsetY, offsetZ)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun intersect(box1: Box, box2: Box): Box? {
        //#if MC >= 1.12.2
        return box1.intersection(box2)
        //#else
        //$$ val minX = max(box1.minX, box2.minX)
        //$$ val minY = max(box1.minY, box2.minY)
        //$$ val minZ = max(box1.minZ, box2.minZ)
        //$$ val maxX = min(box1.maxX, box2.maxX)
        //$$ val maxY = min(box1.maxY, box2.maxY)
        //$$ val maxZ = min(box1.maxZ, box2.maxZ)
        //$$ return Box(
        //$$     minX, minY, minZ,
        //$$     maxX, maxY, maxZ
        //$$ )
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun isIntersecting(box1: Box, box2: Box): Boolean {
        return box1.intersects(box2)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun unionize(box1: Box, box2: Box): Box {
        return box1.union(box2)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun doesContain(x: Double, y: Double, z: Double, box: Box): Boolean {
        //#if MC >= 1.16.5
        return box.contains(x, y, z)
        //#else
        //$$ return box.contains(Vec3d(x, y, z))
        //#endif
    }

}

@GameSide(Side.BOTH)
public fun Box.usingMinX(minX: Double): Box {
    return OmniBox.usingMinX(this, minX)
}

@GameSide(Side.BOTH)
public fun Box.usingMinY(minY: Double): Box {
    return OmniBox.usingMinY(this, minY)
}

@GameSide(Side.BOTH)
public fun Box.usingMinZ(minZ: Double): Box {
    return OmniBox.usingMinZ(this, minZ)
}

@GameSide(Side.BOTH)
public fun Box.usingMaxX(maxX: Double): Box {
    return OmniBox.usingMaxX(this, maxX)
}

@GameSide(Side.BOTH)
public fun Box.usingMaxY(maxY: Double): Box {
    return OmniBox.usingMaxY(this, maxY)
}

@GameSide(Side.BOTH)
public fun Box.usingMaxZ(maxZ: Double): Box {
    return OmniBox.usingMaxZ(this, maxZ)
}

@GameSide(Side.BOTH)
public fun Box.usingMin(min: Vec3d): Box {
    return OmniBox.usingMin(this, min)
}

@GameSide(Side.BOTH)
public fun Box.usingMax(max: Vec3d): Box {
    return OmniBox.usingMax(this, max)
}

@GameSide(Side.BOTH)
public fun Box.offsetBy(offsetX: Double, offsetY: Double, offsetZ: Double): Box {
    return OmniBox.offsetBy(this, offsetX, offsetY, offsetZ)
}

@GameSide(Side.BOTH)
public fun Box.intersect(box: Box): Box? {
    return OmniBox.intersect(this, box)
}

@GameSide(Side.BOTH)
public fun Box.isIntersecting(box: Box): Boolean {
    return OmniBox.isIntersecting(this, box)
}

@GameSide(Side.BOTH)
public fun Box.unionize(box: Box): Box {
    return OmniBox.unionize(this, box)
}

@GameSide(Side.BOTH)
public fun Box.doesContain(x: Double, y: Double, z: Double): Boolean {
    return OmniBox.doesContain(x, y, z, this)
}
