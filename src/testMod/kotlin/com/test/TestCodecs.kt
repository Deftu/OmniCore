package com.test

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

object TestCodecs {

    @JvmField
    val TEST_CODEC = RecordCodecBuilder.create { instance ->
        instance.group(
            Codec.STRING.fieldOf("name").forGetter(TestData::name),
            Codec.INT.fieldOf("value").forGetter(TestData::value)
        ).apply(instance, ::TestData)
    }

}
