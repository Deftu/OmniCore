package dev.deftu.omnicore.internal.client.render.vertex

//#if MC >= 1.21.1
import com.mojang.blaze3d.vertex.MeshData as VanillaMeshData
//#elseif MC >= 1.19.2
//$$ import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer as VanillaMeshData
//#else
//$$ import com.mojang.blaze3d.vertex.BufferBuilder as VanillaMeshData
//#endif

internal typealias VanillaMeshData = VanillaMeshData
