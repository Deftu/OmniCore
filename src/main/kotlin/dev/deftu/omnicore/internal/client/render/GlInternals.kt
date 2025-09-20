package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.render.ClearMask
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL21

@ApiStatus.Internal
public object GlInternals {
    @JvmStatic
    public fun clear0(bits: Int) {
        //#if MC >= 1.21.5
        GL11.glClear(bits)
        //#else
        //$$ GlStateManager._clear(
        //$$     bits,
        //#if MC >= 1.16.5 && MC < 1.21.2
        //$$ false,
        //#endif
        //$$ )
        //#endif
    }

    @JvmStatic
    public fun clear(mask: ClearMask) {
        clear0(mask.bits)
    }

    @JvmStatic
    public fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        //#if MC >= 1.21.5
        GL11.glClearColor(red, green, blue, alpha)
        //#else
        //$$ GlStateManager._clearColor(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic
    public fun clearDepth(depth: Double) {
        //#if MC >= 1.21.5
        GL11.glClearDepth(depth)
        //#else
        //$$ GlStateManager._clearDepth(depth)
        //#endif
    }

    @JvmStatic
    public fun clearStencil(stencil: Int) {
        //#if MC >= 1.21.5 || MC <= 1.12.2
        GL11.glClearStencil(stencil)
        //#else
        //$$ GlStateManager._clearStencil(stencil)
        //#endif
    }

    @JvmStatic
    public fun depthMask(flag: Boolean) {
        GlStateManager._depthMask(flag)
    }

    // Okay so this is stupid as hell but whatever
    // I hate OpenGL

    @JvmStatic
    public inline fun withUnpackState(
        width: Int, comps: Int, bytesPerComp: Int,
        crossinline block: () -> Unit
    ) {
        val rowBytes = width * comps * bytesPerComp
        val wantAlign = when {
            rowBytes % 8 == 0 -> 8
            rowBytes % 4 == 0 -> 4
            rowBytes % 2 == 0 -> 2
            else -> 1
        }

        val prevAlign = GL11.glGetInteger(GL11.GL_UNPACK_ALIGNMENT)
        val prevRowLen = GL11.glGetInteger(GL11.GL_UNPACK_ROW_LENGTH)
        val prevSkipRows = GL11.glGetInteger(GL11.GL_UNPACK_SKIP_ROWS)
        val prevSkipPx = GL11.glGetInteger(GL11.GL_UNPACK_SKIP_PIXELS)
        val prevImgH = GL11.glGetInteger(GL12.GL_UNPACK_IMAGE_HEIGHT)
        val prevSkipImg = GL11.glGetInteger(GL12.GL_UNPACK_SKIP_IMAGES)
        val prevPbo = GL11.glGetInteger(GL21.GL_PIXEL_UNPACK_BUFFER_BINDING)

        if (prevPbo != 0) GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, 0)
        if (prevAlign != wantAlign) GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, wantAlign)
        if (prevRowLen != 0) GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0)
        if (prevSkipRows != 0) GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0)
        if (prevSkipPx != 0) GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0)
        if (prevImgH != 0) GL11.glPixelStorei(GL12.GL_UNPACK_IMAGE_HEIGHT, 0)
        if (prevSkipImg != 0) GL11.glPixelStorei(GL12.GL_UNPACK_SKIP_IMAGES, 0)

        try {
            block()
        } finally {
            if (prevPbo != 0) GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, prevPbo)
            if (prevAlign != wantAlign) GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, prevAlign)
            if (prevRowLen != 0) GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, prevRowLen)
            if (prevSkipRows != 0) GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, prevSkipRows)
            if (prevSkipPx != 0) GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, prevSkipPx)
            if (prevImgH != 0) GL11.glPixelStorei(GL12.GL_UNPACK_IMAGE_HEIGHT, prevImgH)
            if (prevSkipImg != 0) GL11.glPixelStorei(GL12.GL_UNPACK_SKIP_IMAGES, prevSkipImg)
        }
    }

    @JvmStatic
    public inline fun withPackState(
        width: Int, comps: Int, bytesPerComp: Int,
        crossinline block: () -> Unit
    ) {
        val rowBytes = width * comps * bytesPerComp
        val wantAlign = when {
            rowBytes % 8 == 0 -> 8
            rowBytes % 4 == 0 -> 4
            rowBytes % 2 == 0 -> 2
            else -> 1
        }

        val prevAlign = GL11.glGetInteger(GL11.GL_PACK_ALIGNMENT)
        val prevRowLen = GL11.glGetInteger(GL11.GL_PACK_ROW_LENGTH)
        val prevSkipRows = GL11.glGetInteger(GL11.GL_PACK_SKIP_ROWS)
        val prevSkipPx = GL11.glGetInteger(GL11.GL_PACK_SKIP_PIXELS)
        val prevImgH = GL11.glGetInteger(GL12.GL_PACK_IMAGE_HEIGHT)
        val prevSkipImg = GL11.glGetInteger(GL12.GL_PACK_SKIP_IMAGES)
        val prevPbo = GL11.glGetInteger(GL21.GL_PIXEL_PACK_BUFFER_BINDING)

        if (prevPbo != 0) GL15.glBindBuffer(GL21.GL_PIXEL_PACK_BUFFER, 0)
        if (prevAlign != wantAlign) GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, wantAlign)
        if (prevRowLen != 0) GL11.glPixelStorei(GL11.GL_PACK_ROW_LENGTH, 0)
        if (prevSkipRows != 0) GL11.glPixelStorei(GL11.GL_PACK_SKIP_ROWS, 0)
        if (prevSkipPx != 0) GL11.glPixelStorei(GL11.GL_PACK_SKIP_PIXELS, 0)
        if (prevImgH != 0) GL11.glPixelStorei(GL12.GL_PACK_IMAGE_HEIGHT, 0)
        if (prevSkipImg != 0) GL11.glPixelStorei(GL12.GL_PACK_SKIP_IMAGES, 0)

        try {
            block()
        } finally {
            if (prevPbo != 0) GL15.glBindBuffer(GL21.GL_PIXEL_PACK_BUFFER, prevPbo)
            if (prevAlign != wantAlign) GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, prevAlign)
            if (prevRowLen != 0) GL11.glPixelStorei(GL11.GL_PACK_ROW_LENGTH, prevRowLen)
            if (prevSkipRows != 0) GL11.glPixelStorei(GL11.GL_PACK_SKIP_ROWS, prevSkipRows)
            if (prevSkipPx != 0) GL11.glPixelStorei(GL11.GL_PACK_SKIP_PIXELS, prevSkipPx)
            if (prevImgH != 0) GL11.glPixelStorei(GL12.GL_PACK_IMAGE_HEIGHT, prevImgH)
            if (prevSkipImg != 0) GL11.glPixelStorei(GL12.GL_PACK_SKIP_IMAGES, prevSkipImg)
        }
    }

}
