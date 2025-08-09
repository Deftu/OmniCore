package com.test

import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.time.Instant
import java.util.UUID
import java.util.regex.Pattern

data class TestData(
    val name: String,
    val value: Int,
    val id: Identifier,
    val pattern: Pattern,
    val strictUuid: UUID,
    val undashedUuid: UUID,
    val lenientUuid: UUID,
    val otherLenientUuid: UUID,
    val blockPos: BlockPos,
    val instant: Instant,
)
