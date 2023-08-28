package xyz.deftu.multi

//#if MC >= 1.17
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gl.ShaderProgram
//#endif

import net.minecraft.util.Identifier
import java.util.function.Supplier

object MultiRenderSystem {
    //#if MC >= 1.17
    fun setShader(supplier: Supplier<ShaderProgram?>) {
        RenderSystem.setShader(supplier)
    }

    fun removeShader() {
        setShader { null }
    }

    fun setShaderTexture(index: Int, texture: Identifier) {
        RenderSystem.setShaderTexture(index, texture)
    }
    //#endif

    fun setTexture(index: Int, texture: Identifier) {
        //#if MC >= 1.17
        setShaderTexture(index, texture)
        //#else
        //$$ MultiTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + index)
        //$$ MultiClient.getTextureManager().bindTexture(texture)
        //#endif
    }
}
