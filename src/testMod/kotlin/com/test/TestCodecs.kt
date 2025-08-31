package com.test

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deftu.omnicore.api.serialization.OmniCodecs

object TestCodecs {
    @JvmField
    val TEST_CODEC: Codec<TestData> = RecordCodecBuilder.create { instance ->
        instance.group(
            Codec.STRING.fieldOf("name").forGetter(TestData::name),
            Codec.INT.fieldOf("value").forGetter(TestData::value),
            OmniCodecs.IDENTIFIER.fieldOf("id").forGetter(TestData::id),
            OmniCodecs.PATTERN.fieldOf("pattern").forGetter(TestData::pattern),
            OmniCodecs.UUID_STRICT.fieldOf("strict_uuid").forGetter(TestData::strictUuid),
            OmniCodecs.UUID_UNDASHED.fieldOf("undashed_uuid").forGetter(TestData::undashedUuid),
            OmniCodecs.UUID_LENIENT.fieldOf("lenient_uuid").forGetter(TestData::lenientUuid),
            OmniCodecs.UUID_LENIENT.fieldOf("other_lenient_uuid").forGetter(TestData::otherLenientUuid),
            OmniCodecs.BLOCK_POS.fieldOf("block_pos").forGetter(TestData::blockPos),
            OmniCodecs.INSTANT.fieldOf("instant").forGetter(TestData::instant)
        ).apply(instance, ::TestData)
    }
}
