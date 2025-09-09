package dev.deftu.omnicore.api.serialization

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec

//#if MC < 1.20.6
//$$ import java.util.function.Function
//#endif

public object OmniCodecOps {
    @JvmStatic
    public fun <T> unwrapEither(either: Either<out T, out T>): T {
        //#if MC >= 1.20.6
        return Either.unwrap(either)
        //#else
        //$$ return either.map(Function.identity(), Function.identity())
        //#endif
    }

    @JvmStatic
    public fun <T> withAlternative(primary: Codec<T>, alternative: Codec<T>): Codec<T> {
        //#if MC >= 1.20.6
        return Codec.withAlternative(primary, alternative)
        //#else
        //$$ return Codec.either(primary, alternative).xmap(::unwrapEither, Either<T, T>::left)
        //#endif
    }
}
