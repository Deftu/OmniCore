package dev.deftu.omnicore.internal.client.textures

import com.mojang.blaze3d.textures.TextureFormat
import com.mojang.blaze3d.opengl.GlTexture
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
@Suppress("InconsistentCommentForJavaParameter")
public class WrappedGlTexture(id: Int) : GlTexture(
    //#if MC >= 1.21.6
    /* usage = */USAGE_TEXTURE_BINDING,
    //#endif
    /* label = */"",
    TextureFormat.RGBA8,
    /* width = */0,
    /* height = */0,
    /* depth = */0,
    //#if MC >= 1.21.6
    /* mipLevels = */1,
    //#endif
    /* glId = */id
) {
    init {
        this.modesDirty = false
    }
}
