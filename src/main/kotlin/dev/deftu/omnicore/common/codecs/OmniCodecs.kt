package dev.deftu.omnicore.common.codecs

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.EitherMapCodec
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.common.OmniUuid
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Arrays
import java.util.UUID
import java.util.function.Function
import java.util.regex.Pattern

//#if MC <= 1.19.2
//$$ import dev.deftu.omnicore.common.OmniIdentifier
//#endif

//#if MC < 1.16.5
//$$ import java.util.stream.IntStream
//#endif

@GameSide(Side.BOTH)
public object OmniCodecs {

    private val UUID_INT_STREAM: Codec<UUID> = Codec.INT_STREAM.comapFlatMap(
        { stream ->
            OmniDataResult
                .decodeFixedLengthArray(stream, 4)
                .map(OmniUuid::fromIntArray)
        },
        { uuid ->
            Arrays.stream(OmniUuid.toIntArray(uuid))
        }
    )

    @JvmField
    public val IDENTIFIER: Codec<Identifier> =
        //#if MC >= 1.19.4
        Identifier.CODEC
        //#else
        //$$ Codec.STRING.comapFlatMap<ResourceLocation>(
        //$$     {
        //$$         try {
        //$$             DataResult.success(OmniIdentifier.create(it))
        //$$         } catch (e: Exception) {
        //$$             DataResult.error("Not a valid resource location: $it ${e.message}")
        //$$         }
        //$$     },
        //$$     ResourceLocation::toString
        //$$ ).stable()
        //#endif

    @JvmField
    public val PATTERN: Codec<Pattern> = Codec.STRING.comapFlatMap(
        { string ->
            try {
                DataResult.success(Pattern.compile(string))
            } catch (e: Exception) {
                OmniDataResult.error("Invalid regex pattern $string: ${e.message}")
            }
        },
        Pattern::pattern
    )

    @JvmField
    public val UUID_STRICT: Codec<UUID> = withAlternative(Codec.STRING.comapFlatMap({ string ->
        try {
            DataResult.success(UUID.fromString(string))
        } catch (e: Exception) {
            OmniDataResult.error("Invalid UUID $string: ${e.message}")
        }
    }, UUID::toString), UUID_INT_STREAM)

    @JvmField
    public val UUID_UNDASHED: Codec<UUID> = withAlternative(Codec.STRING.comapFlatMap({ string ->
        try {
            DataResult.success(OmniUuid.fromUndashed(string))
        } catch (e: Exception) {
            OmniDataResult.error("Invalid UUID $string: ${e.message}")
        }
    }, OmniUuid::toUndashed), UUID_INT_STREAM)

    @JvmField
    public val BLOCK_POS: Codec<BlockPos> =
        //#if MC >= 1.16.5
        BlockPos.CODEC
        //#else
        //$$ Codec.INT_STREAM.comapFlatMap(
        //$$     { stream ->
        //$$         OmniDataResult
        //$$             .decodeFixedLengthArray(stream, 3)
        //$$             .map { arr -> BlockPos(arr[0], arr[1], arr[2]) }
        //$$     },
        //$$     { pos ->
        //$$         IntStream.of(pos.x, pos.y, pos.z)
        //$$     }
        //$$ ).stable()
        //#endif

    @JvmField
    public val INSTANT: Codec<Instant> = time(DateTimeFormatter.ISO_INSTANT).xmap(Instant::from, Function.identity())

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun <T> unwrapEither(either: Either<out T, out T>): T {
        //#if MC >= 1.20.6
        return Either.unwrap(either)
        //#else
        //$$ return either.map(Function.identity(), Function.identity())
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun <T> withAlternative(primary: Codec<T>, alternative: Codec<T>): Codec<T> {
        //#if MC >= 1.20.6
        return Codec.withAlternative(primary, alternative)
        //#else
        //$$ return Codec.either(primary, alternative).xmap(::unwrapEither, Either<T, T>::left)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun time(formatter: DateTimeFormatter): Codec<TemporalAccessor> {
        return Codec.STRING.comapFlatMap({ string ->
            try {
                DataResult.success(formatter.parse(string))
            } catch (e: Exception) {
                OmniDataResult.error(e::message)
            }
        }, formatter::format)
    }

    /**
     * Accepts either:
     * - list field (e.g. "items": [ ... ])
     * - single field (e.g. "item":  "...")
     * and always decodes to List<T>.
     *
     * Encoding picks Right (single) when size==1, otherwise Left (list).
     *
     * @since 0.42.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public fun <T> singleOrList(
        single: MapCodec<T>,
        list: MapCodec<List<T>>
    ): MapCodec<List<T>> {
        return EitherMapCodec(list, single).xmap(
            { e -> e.map({ it }, { listOf(it) }) },
            { xs -> if (xs.size == 1) Either.right(xs[0]) else Either.left(xs) }
        )
    }

    /**
     * Convenience function to automatically build the map-codecs from names + element codec.
     *
     * @since 0.42.0
     * @author Deftu
     */
    public fun <T> singleOrList(
        singleName: String,
        listName: String,
        element: Codec<T>
    ): MapCodec<List<T>> {
        return singleOrList(
            single = element.fieldOf(singleName),
            list = element.listOf().fieldOf(listName)
        )
    }

}
