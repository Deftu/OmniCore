package dev.deftu.omnicore.common

import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side
import net.minecraft.util.math.BlockPos

/**
 * Utility object for creating and manipulating [BlockPos] instances.
 * Provides static methods and Kotlin extension functions for common operations.
 */
@GameSide(Side.BOTH)
public object OmniBlockPos {

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun from(x: Int, y: Int, z: Int): BlockPos {
        return BlockPos(x, y, z)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun offsetX(blockPos: BlockPos, x: Int): BlockPos {
        return blockPos.add(x, 0, 0)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun offsetY(blockPos: BlockPos, y: Int): BlockPos {
        return blockPos.add(0, y, 0)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun offsetZ(blockPos: BlockPos, z: Int): BlockPos {
        return blockPos.add(0, 0, z)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun offset(blockPos: BlockPos, offset: BlockPos): BlockPos {
        return blockPos.add(offset)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftUpBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.up(amount)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftUp(blockPos: BlockPos): BlockPos {
        return blockPos.up()
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftDownBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.down(amount)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftDown(blockPos: BlockPos): BlockPos {
        return blockPos.down()
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftNorthBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.north(amount)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftNorth(blockPos: BlockPos): BlockPos {
        return blockPos.north()
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftSouthBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.south(amount)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftSouth(blockPos: BlockPos): BlockPos {
        return blockPos.south()
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftEastBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.east(amount)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftEast(blockPos: BlockPos): BlockPos {
        return blockPos.east()
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftWestBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.west(amount)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun shiftWest(blockPos: BlockPos): BlockPos {
        return blockPos.west()
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun compress(blockPos: BlockPos): Long {
        return blockPos.asLong()
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun decompress(compressed: Long): BlockPos {
        return BlockPos.fromLong(compressed)
    }

}

@GameSide(Side.BOTH)
public fun BlockPos.offsetX(x: Int): BlockPos {
    return OmniBlockPos.offsetX(this, x)
}

@GameSide(Side.BOTH)
public fun BlockPos.offsetY(y: Int): BlockPos {
    return OmniBlockPos.offsetY(this, y)
}

@GameSide(Side.BOTH)
public fun BlockPos.offsetZ(z: Int): BlockPos {
    return OmniBlockPos.offsetZ(this, z)
}

@GameSide(Side.BOTH)
public fun BlockPos.offset(offset: BlockPos): BlockPos {
    return OmniBlockPos.offset(this, offset)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftUpBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftUpBy(this, amount)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftUp(): BlockPos {
    return OmniBlockPos.shiftUp(this)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftDownBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftDownBy(this, amount)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftDown(): BlockPos {
    return OmniBlockPos.shiftDown(this)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftNorthBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftNorthBy(this, amount)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftNorth(): BlockPos {
    return OmniBlockPos.shiftNorth(this)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftSouthBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftSouthBy(this, amount)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftSouth(): BlockPos {
    return OmniBlockPos.shiftSouth(this)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftEastBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftEastBy(this, amount)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftEast(): BlockPos {
    return OmniBlockPos.shiftEast(this)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftWestBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftWestBy(this, amount)
}

@GameSide(Side.BOTH)
public fun BlockPos.shiftWest(): BlockPos {
    return OmniBlockPos.shiftWest(this)
}

@GameSide(Side.BOTH)
public fun BlockPos.compress(): Long {
    return OmniBlockPos.compress(this)
}

@GameSide(Side.BOTH)
public fun Long.decompress(): BlockPos {
    return OmniBlockPos.decompress(this)
}
