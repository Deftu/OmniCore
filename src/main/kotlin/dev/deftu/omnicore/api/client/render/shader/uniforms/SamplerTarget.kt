package dev.deftu.omnicore.api.client.render.shader.uniforms

public enum class SamplerTarget(public val shaderName: String) {
    TEX_1D("sampler1D"),
    TEX_2D("sampler2D"),
    TEX_3D("sampler3D"),
    TEX_CUBE("samplerCube"),
}
