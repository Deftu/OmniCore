package dev.deftu.omnicore

//#if MC <= 1.16.5
//$$ import org.lwjgl.opengl.GL13
//#endif

//#if MC >= 1.17
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gl.ShaderProgram
import java.util.function.Supplier
//#endif

import net.minecraft.util.Identifier

public object MultiRenderSystem {
    //#if MC >= 1.17
    public fun setShader(supplier: Supplier<ShaderProgram?>) {
        RenderSystem.setShader(supplier)
    }

    public fun removeShader() {
        setShader { null }
    }

    public fun setShaderTexture(index: Int, texture: Identifier) {
        RenderSystem.setShaderTexture(index, texture)
    }

    public fun setShaderTexture(index: Int, texture: Int) {
        RenderSystem.setShaderTexture(index, texture)
    }
    //#endif

    public fun setTexture(index: Int, texture: Identifier) {
        //#if MC >= 1.17
        setShaderTexture(index, texture)
        //#else
        //$$ MultiTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + index)
        //$$ MultiClient.getTextureManager().bindTexture(texture)
        //#endif
    }

    public fun setTexture(index: Int, texture: Int) {
        //#if MC >= 1.17
        setShaderTexture(index, texture)
        //#else
        //$$ MultiTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + index)
        //$$ MultiTextureManager.bindTexture(texture)
        //#endif
    }
}
