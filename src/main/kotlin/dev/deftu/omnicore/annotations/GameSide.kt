package dev.deftu.omnicore.annotations

/**
 * Any functionality annotated with [GameSide] will only be executed on the specified side.
 *
 * @since 0.2.2
 * @author Deftu
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FILE,
)
public annotation class GameSide(val side: Side)

public enum class Side {
    CLIENT,
    SERVER,
    BOTH
}
