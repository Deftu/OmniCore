package dev.deftu.omnicore.client.render.vertex

//#if MC >= 1.21.1
//$$ import com.mojang.blaze3d.vertex.MeshData as VanillaBuiltBuffer
//#elseif MC >= 1.19.2
import net.minecraft.client.render.BufferBuilder.BuiltBuffer as VanillaBuiltBuffer
//#else
//$$ import net.minecraft.client.render.BufferBuilder as VanillaBuiltBuffer
//#endif

public typealias VanillaBuiltBuffer = VanillaBuiltBuffer
