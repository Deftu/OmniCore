package dev.deftu.omnicore.api.client.utils

public interface GlConstEnum {
    public val const: Int

    public companion object {
        @JvmStatic
        public fun <E : GlConstEnum> findOrNull(all: Collection<E>, const: Int): E? {
            return all.associateBy(GlConstEnum::const)[const]
        }

        @JvmStatic
        public fun <E : GlConstEnum> findOrThrow(clz: Class<E>, all: Collection<E>, const: Int): E {
            return findOrNull(all, const) ?:
            throw IllegalArgumentException(buildString {
                append("Unknown GL const for ")
                append(clz.simpleName)

                append("(")
                buildList {
                    all.mapTo(this, GlConstEnum::const)
                        .sorted()
                        .map(Int::toString)
                }.forEach(::append)
                append(")")

                append(": ")
                append(const)
            })
        }

        @JvmStatic
        public inline fun <reified E : GlConstEnum> findOrThrow(all: Collection<E>, const: Int): E {
            return findOrThrow(E::class.java, all, const)
        }
    }
}
