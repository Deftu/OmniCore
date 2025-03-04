package dev.deftu.omnicore.common

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.util.math.BlockPos

@GameSide(Side.CLIENT)
public object OmniBlockPos {

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun from(x: Int, y: Int, z: Int): BlockPos {
        return BlockPos(x, y, z)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun offsetX(blockPos: BlockPos, x: Int): BlockPos {
        return blockPos.add(x, 0, 0)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun offsetY(blockPos: BlockPos, y: Int): BlockPos {
        return blockPos.add(0, y, 0)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun offsetZ(blockPos: BlockPos, z: Int): BlockPos {
        return blockPos.add(0, 0, z)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun offset(blockPos: BlockPos, offset: BlockPos): BlockPos {
        return blockPos.add(offset)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftUpBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.up(amount)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftUp(blockPos: BlockPos): BlockPos {
        return blockPos.up()
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftDownBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.down(amount)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftDown(blockPos: BlockPos): BlockPos {
        return blockPos.down()
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftNorthBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.north(amount)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftNorth(blockPos: BlockPos): BlockPos {
        return blockPos.north()
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftSouthBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.south(amount)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftSouth(blockPos: BlockPos): BlockPos {
        return blockPos.south()
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftEastBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.east(amount)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftEast(blockPos: BlockPos): BlockPos {
        return blockPos.east()
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftWestBy(blockPos: BlockPos, amount: Int): BlockPos {
        return blockPos.west(amount)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun shiftWest(blockPos: BlockPos): BlockPos {
        return blockPos.west()
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun compress(blockPos: BlockPos): Long {
        return blockPos.asLong()
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun decompress(compressed: Long): BlockPos {
        return BlockPos.fromLong(compressed)
    }

}

public fun BlockPos.offsetX(x: Int): BlockPos {
    return OmniBlockPos.offsetX(this, x)
}

public fun BlockPos.offsetY(y: Int): BlockPos {
    return OmniBlockPos.offsetY(this, y)
}

public fun BlockPos.offsetZ(z: Int): BlockPos {
    return OmniBlockPos.offsetZ(this, z)
}

public fun BlockPos.offset(offset: BlockPos): BlockPos {
    return OmniBlockPos.offset(this, offset)
}

public fun BlockPos.shiftUpBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftUpBy(this, amount)
}

public fun BlockPos.shiftUp(): BlockPos {
    return OmniBlockPos.shiftUp(this)
}

public fun BlockPos.shiftDownBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftDownBy(this, amount)
}

public fun BlockPos.shiftDown(): BlockPos {
    return OmniBlockPos.shiftDown(this)
}

public fun BlockPos.shiftNorthBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftNorthBy(this, amount)
}

public fun BlockPos.shiftNorth(): BlockPos {
    return OmniBlockPos.shiftNorth(this)
}

public fun BlockPos.shiftSouthBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftSouthBy(this, amount)
}

public fun BlockPos.shiftSouth(): BlockPos {
    return OmniBlockPos.shiftSouth(this)
}

public fun BlockPos.shiftEastBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftEastBy(this, amount)
}

public fun BlockPos.shiftEast(): BlockPos {
    return OmniBlockPos.shiftEast(this)
}

public fun BlockPos.shiftWestBy(amount: Int): BlockPos {
    return OmniBlockPos.shiftWestBy(this, amount)
}

public fun BlockPos.shiftWest(): BlockPos {
    return OmniBlockPos.shiftWest(this)
}

public fun BlockPos.compress(): Long {
    return OmniBlockPos.compress(this)
}

public fun Long.decompress(): BlockPos {
    return OmniBlockPos.decompress(this)
}
