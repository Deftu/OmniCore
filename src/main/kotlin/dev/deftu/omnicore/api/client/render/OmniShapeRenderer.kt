package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.internal.client.render.shape.BoxOutlineShapeRenderer
import dev.deftu.omnicore.internal.client.render.shape.FilledBoxShapeRenderer
import dev.deftu.omnicore.internal.client.render.shape.ShapeOutlineShapeRenderer
import dev.deftu.omnicore.internal.client.render.shape.VectorShapeRenderer
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public object OmniShapeRenderer {
    @JvmField public val BOX_OUTLINE: BoxOutlineShapeRenderer = BoxOutlineShapeRenderer
    @JvmField public val FILLED_BOX: FilledBoxShapeRenderer = FilledBoxShapeRenderer
    @JvmField public val SHAPE_OUTLINE: ShapeOutlineShapeRenderer = ShapeOutlineShapeRenderer
    @JvmField public val VECTOR: VectorShapeRenderer = VectorShapeRenderer
}