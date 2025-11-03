@file:JvmName("OmniNbtList")

package dev.deftu.omnicore.api.nbt

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.nbt.ListTag

public val ListTag.length: Int
    get() {
        //#if MC >= 1.16.5
        return this.size
        //#else
        //$$ return this.tagCount()
        //#endif
    }

public fun ListTag.addElement(element: Tag) {
    //#if MC >= 1.16.5
    this.add(element)
    //#else
    //$$ this.appendTag(element)
    //#endif
}

public fun ListTag.forEachCompound(action: (CompoundTag) -> Unit) {
    for (i in 0 until length) {
        val element = this[i]
        if (element is CompoundTag) {
            action(element)
        }
    }
}

public fun ListTag.getCompoundOrNull(index: Int): CompoundTag? {
    val element = this[index]
    return element as? CompoundTag
}

public fun ListTag.mapCompounds(transform: (CompoundTag) -> Tag): ListTag {
    val result = ListTag()
    forEachCompound {
        result.addElement(transform(it))
    }

    return result
}

public fun ListTag.filterCompounds(predicate: (CompoundTag) -> Boolean): ListTag {
    val result = ListTag()
    forEachCompound {
        if (predicate(it)) {
            result.addElement(it)
        }
    }

    return result
}

public fun ListTag.findCompound(predicate: (CompoundTag) -> Boolean): CompoundTag? {
    for (i in 0 until length) {
        val element = this[i]
        if (element is CompoundTag && predicate(element)) {
            return element
        }
    }

    return null
}
