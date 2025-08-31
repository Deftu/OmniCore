package dev.deftu.omnicore.client.shaders

import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side

@GameSide(Side.CLIENT)
public interface ShaderUniform {

    @GameSide(Side.CLIENT)
    public val location: Int

}

@GameSide(Side.CLIENT)
public interface IntUniform : ShaderUniform {

    @GameSide(Side.CLIENT)
    public fun setValue(value: Int)

}

@GameSide(Side.CLIENT)
public interface VecUniform : ShaderUniform {

    @GameSide(Side.CLIENT)
    public fun setValue(a: Float)

}

@GameSide(Side.CLIENT)
public interface Vec2Uniform : ShaderUniform {

    @GameSide(Side.CLIENT)
    public fun setValue(a: Float, b: Float)

}

@GameSide(Side.CLIENT)
public interface Vec3Uniform : ShaderUniform {

    @GameSide(Side.CLIENT)
    public fun setValue(a: Float, b: Float, c: Float)

}

@GameSide(Side.CLIENT)
public interface Vec4Uniform : ShaderUniform {

    @GameSide(Side.CLIENT)
    public fun setValue(a: Float, b: Float, c: Float, d: Float)

}

@GameSide(Side.CLIENT)
public interface MatrixUniform : ShaderUniform {

    @GameSide(Side.CLIENT)
    public fun setValue(matrix: FloatArray)

}

@GameSide(Side.CLIENT)
public interface SamplerUniform : ShaderUniform {

    @GameSide(Side.CLIENT)
    public fun setValue(value: Int)

}
