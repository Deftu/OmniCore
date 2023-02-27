package xyz.deftu.multi

//#if MC>=11700
import net.minecraft.client.gl.ShaderProgram
//#endif

//#if MC>=11500
import com.mojang.blaze3d.systems.RenderSystem
//#endif

import java.util.function.Supplier

object MultiRenderSystem {
    //#if MC>=11700
    fun setShader(supplier: Supplier<ShaderProgram?>) {
        RenderSystem.setShader(supplier)
    }
    //#endif
}
