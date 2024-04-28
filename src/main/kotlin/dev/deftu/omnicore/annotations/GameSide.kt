package dev.deftu.omnicore.annotations

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FILE
)
public annotation class GameSide(val side: Side)

public enum class Side {
    CLIENT,
    SERVER,
    BOTH
}
