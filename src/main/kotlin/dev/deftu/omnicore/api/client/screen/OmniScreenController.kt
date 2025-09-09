package dev.deftu.omnicore.api.client.screen

import dev.deftu.omnicore.api.client.input.KeyboardModifiers
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.client.render.OmniRenderingContext

public interface OmniScreenController {
    public fun onInitialize(width: Int, height: Int)
    public fun onResize(width: Int, height: Int)
    public fun onKeyPress(key: OmniKey, scanCode: Int, typedChar: Char, modifiers: KeyboardModifiers, event: KeyPressEvent): Boolean
    public fun onKeyRelease(key: OmniKey, scanCode: Int, modifiers: KeyboardModifiers): Boolean
    public fun onMouseClick(button: OmniMouseButton, x: Double, y: Double): Boolean
    public fun onMouseRelease(button: OmniMouseButton, x: Double, y: Double): Boolean
    public fun onMouseDrag(button: OmniMouseButton, x: Double, y: Double, deltaX: Double, deltaY: Double, clickTime: Long): Boolean
    public fun onMouseScroll(x: Double, y: Double, amount: Double, horizontalAmount: Double): Boolean
    public fun onScreenTick()
    public fun onScreenClose()
    public fun onRender(ctx: OmniRenderingContext, mouseX: Int, mouseY: Int, tickDelta: Float)
    public fun onBackgroundRender(ctx: OmniRenderingContext, mouseX: Int, mouseY: Int, tickDelta: Float)
}
