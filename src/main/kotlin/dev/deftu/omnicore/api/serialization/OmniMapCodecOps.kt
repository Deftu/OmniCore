package dev.deftu.omnicore.api.serialization

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.EitherMapCodec

public object OmniMapCodecOps {
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
