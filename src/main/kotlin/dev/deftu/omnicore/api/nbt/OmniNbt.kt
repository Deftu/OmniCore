package dev.deftu.omnicore.api.nbt

import net.minecraft.nbt.*

//#if MC <= 1.21.4 && MC >= 1.16.5
//$$ import com.mojang.brigadier.StringReader
//#endif

//#if MC <= 1.12.2
//$$ import java.lang.invoke.MethodHandles
//$$ import java.lang.invoke.MethodType
//#endif

public object OmniNbt {
    //#if MC <= 1.12.2
    //$$ private val implLookup by lazy {
    //$$     val field = MethodHandles.Lookup::class.java.getDeclaredField("IMPL_LOOKUP")
    //$$     field.isAccessible = true
    //$$     field.get(null) as MethodHandles.Lookup
    //$$ }
    //#endif

    @JvmStatic
    public fun parseCompound(value: String): NbtCompound {
        //#if MC >= 1.21.5
        return StringNbtReader.readCompound(value)
        //#elseif MC >= 1.16.5
        //$$ return TagParser(StringReader(value)).readStruct()
        //#else
        //$$ return JsonToNBT.getTagFromJson(value)
        //#endif
    }

    @JvmStatic
    public fun parseElement(value: String): NbtElement {
        //#if MC >= 1.21.5
        return StringNbtReader.fromOps(NbtOps.INSTANCE).read(value)
        //#elseif MC >= 1.16.5
        //$$ return TagParser(StringReader(value)).readValue()
        //#else
        //$$ return try {
        //$$     val constructorHandle = MethodHandles.lookup().findConstructor(NBTBase::class.java, MethodType.methodType(Void.TYPE, String::class.java))
        //$$     val instance = constructorHandle.invokeExact(value)
        //$$
        //$$     val readValueHandle = implLookup.findVirtual(NBTBase::class.java, "readValue", MethodType.methodType(NBTBase::class.java))
        //$$     readValueHandle.invokeExact(instance) as NBTBase
        //$$ } catch (t: Throwable) {
        //$$     throw IllegalArgumentException("Invalid NBT value: $value", t)
        //$$ }
        //#endif
    }
}
