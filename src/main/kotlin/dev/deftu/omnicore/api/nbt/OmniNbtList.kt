@file:JvmName("OmniNbtList")

package dev.deftu.omnicore.api.nbt

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList

public val NbtList.length: Int
    get() {
        //#if MC >= 1.16.5
        return this.size
        //#else
        //$$ return this.tagCount()
        //#endif
    }

public fun NbtList.addElement(element: NbtElement) {
    //#if MC >= 1.16.5
    this.add(element)
    //#else
    //$$ this.appendTag(element)
    //#endif
}

public fun NbtList.forEachCompound(action: (NbtCompound) -> Unit) {
    for (i in 0 until length) {
        val element = this[i]
        if (element is NbtCompound) {
            action(element)
        }
    }
}

public fun NbtList.getCompoundOrNull(index: Int): NbtCompound? {
    val element = this[index]
    return element as? NbtCompound
}

public fun NbtList.mapCompounds(transform: (NbtCompound) -> NbtElement): NbtList {
    val result = NbtList()
    forEachCompound {
        result.addElement(transform(it))
    }

    return result
}

public fun NbtList.filterCompounds(predicate: (NbtCompound) -> Boolean): NbtList {
    val result = NbtList()
    forEachCompound {
        if (predicate(it)) {
            result.addElement(it)
        }
    }

    return result
}

public fun NbtList.findCompound(predicate: (NbtCompound) -> Boolean): NbtCompound? {
    for (i in 0 until length) {
        val element = this[i]
        if (element is NbtCompound && predicate(element)) {
            return element
        }
    }

    return null
}
