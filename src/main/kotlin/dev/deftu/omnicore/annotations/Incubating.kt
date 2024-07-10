package dev.deftu.omnicore.annotations

/**
 * Any functionality annotated with [Incubating] is subject to change and may be removed in the future.
 *
 * Stability of any functionality annotated with [Incubating] is not guaranteed.
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
    AnnotationTarget.FILE
)
public annotation class Incubating
