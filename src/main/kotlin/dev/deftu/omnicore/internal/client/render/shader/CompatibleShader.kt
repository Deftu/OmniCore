package dev.deftu.omnicore.internal.client.render.shader

import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.shader.OmniShader
import dev.deftu.omnicore.api.client.render.shader.ShaderSchema
import dev.deftu.omnicore.api.client.render.shader.uniforms.Uniform
import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformKind
import dev.deftu.omnicore.api.client.render.state.OmniBlendState
import dev.deftu.omnicore.api.client.render.state.OmniRenderStates
import dev.deftu.omnicore.internal.client.textures.TextureInternals
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13

public class CompatibleShader(
    public val vertSource: String,
    public val fragSource: String,
    public val schema: ShaderSchema,
    public val blendState: OmniBlendState,
) : OmniShader {
    private var program = ShaderInternals.program()
    private var vertShader = ShaderInternals.vertexShader()
    private var fragShader = ShaderInternals.fragmentShader()

    private val usedKinds = linkedMapOf<String, UniformKind>()
    private val uniformCache = linkedMapOf<Pair<String, UniformKind>, Uniform>()
    private val samplerCache = linkedMapOf<String, CompatibleSamplerUniform>()

    private var prevTexture = GL11.GL_NONE
    private var prevTextureBindings = mutableMapOf<Int, Int>()
    private var prevBlendState: OmniBlendState? = null

    public var isBound: Boolean = false
        private set

    override var isUsable: Boolean = false
        private set

    init {
        initialize()
    }

    override fun bind() {
        prevTexture = TextureInternals.active
        samplerCache.values.forEach { sampler -> bindSampler(sampler.index, sampler.texture) }
        prevBlendState = OmniRenderStates.blend
        prevBlendState?.submit()
        ShaderInternals.useProgram(program)
        isBound = true
    }

    override fun unbind() {
        for ((textureUnit, texture) in prevTextureBindings) {
            TextureInternals.bindOnUnit(OmniTextureUnit.from(textureUnit) ?: error("Invalid texture unit: $textureUnit"), texture)
        }

        prevTextureBindings.clear()
        TextureInternals.bind(prevTexture)
        prevBlendState?.submit()

        ShaderInternals.useProgram(GL11.GL_NONE)
        isBound = false
    }

    override fun uniformOrNull(
        name: String,
        kind: UniformKind
    ): Uniform? {
        val declaredKind = usedKinds[name] ?: return null
        if (!declaredKind.isCompatibleWith(kind)) {
            return null
        }

        if (kind is UniformKind.Sampler) {
            val sampler = samplerCache[name] ?: return null
            if (sampler.target != kind.target) {
                return null
            }

            return sampler
        }

        val key = name to kind
        uniformCache[key]?.let { return it }

        val location = location(name) ?: return null
        val uniform = when (kind) {
            is UniformKind.Float1 -> CompatibleFloat1Uniform(location)
            is UniformKind.Int1 -> CompatibleInt1Uniform(location)
            is UniformKind.Bool1 -> CompatibleBool1Uniform(location)
            is UniformKind.Vec2f -> CompatibleVec2fUniform(location)
            is UniformKind.Vec3f -> CompatibleVec3fUniform(location)
            is UniformKind.Vec4f -> CompatibleVec4fUniform(location)
            is UniformKind.Mat2f -> CompatibleMat2fUniform(location)
            is UniformKind.Mat3f -> CompatibleMat3fUniform(location)
            is UniformKind.Mat4f -> CompatibleMat4fUniform(location)
            else -> return null
        }

        uniformCache[key] = uniform
        return uniform
    }

    override fun uniformOrThrow(
        name: String,
        kind: UniformKind
    ): Uniform {
        return uniformOrNull(name, kind)
            ?: error("Uniform '$name' not found, or kind mismatch (expected $kind, found ${usedKinds[name]}).")
    }

    internal fun bindSampler(index: Int, id: Int) {
        TextureInternals.activeUnit = OmniTextureUnit.from(index) ?: OmniTextureUnit.TEXTURE0
        prevTextureBindings.computeIfAbsent(index) { TextureInternals.active }
        TextureInternals.bind(id)
    }

    private fun initialize() {
        // Compile and link our shader
        isUsable = ShaderInternals.compileAndLink(program, vertShader, fragShader, vertSource, fragSource)
        if (!isUsable) {
            // Clean up if compilation/linking failed
            ShaderInternals.deleteShader(vertShader)
            ShaderInternals.deleteShader(fragShader)
            ShaderInternals.deleteProgram(program)
            program = GL11.GL_NONE
            vertShader = GL11.GL_NONE
            fragShader = GL11.GL_NONE
            return
        }

        // Cache uniform kinds and prepare sampler uniforms
        for (definition in schema.uniforms) {
            val name = definition.name
            val kind = definition.kind

            usedKinds[name] = kind

            // Pre-allocate sampler uniforms
            if (kind is UniformKind.Sampler) {
                val location = location(name) ?: continue
                val index = samplerCache.size
                val sampler = CompatibleSamplerUniform(this, location, kind.target)
                sampler.setTextureIndex(index)
                samplerCache[name] = sampler
                bindSampler(index, 0)
            }
        }
    }

    private fun location(name: String): Int? {
        val location = ShaderInternals.uniformLocation(program, name)
        if (location < 0) {
            return null
        }

        return location
    }

    private fun UniformKind.isCompatibleWith(other: UniformKind): Boolean {
        return when (other) {
            is UniformKind.Sampler -> this is UniformKind.Sampler && this.target == other.target
            else -> this == other
        }
    }
}
