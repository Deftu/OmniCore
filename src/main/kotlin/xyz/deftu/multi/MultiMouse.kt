package xyz.deftu.multi

//#if MC<=11202
//$$ import org.lwjgl.input.Mouse
//#endif

import kotlin.math.max

object MultiMouse {
    @JvmStatic
    val rawX: Double
        get() {
            //#if MC>=11400
            return MultiClient.getInstance().mouse.x
            //#else
            //$$ return Mouse.getX().toDouble()
            //#endif
        }

    @JvmStatic
    val rawY: Double
        get() {
            //#if MC>=11400
            return MultiClient.getInstance().mouse.y
            //#else
            //$$ return Mouse.getY().toDouble()
            //#endif
        }

    @JvmStatic
    val scaledX: Double
        get() = rawX * MultiResolution.scaledWidth / max(1, MultiResolution.screenWidth)

    @JvmStatic
    val scaledY: Double
        get() = rawY * MultiResolution.scaledHeight / max(1, MultiResolution.screenHeight)

    @JvmStatic
    val isCursorGrabbed: Boolean
        get() {
            //#if MC>=11400
            return MultiClient.getInstance().mouse.isCursorLocked
            //#else
            //$$ return Mouse.isGrabbed()
            //#endif
        }

    @JvmStatic
    fun setCursorGrabbed(grabbed: Boolean) {
        //#if MC>=11400
        if (grabbed) MultiClient.getInstance().mouse.lockCursor()
        else MultiClient.getInstance().mouse.unlockCursor()
        //#else
        //$$ Mouse.setGrabbed(grabbed)
        //#endif
    }
}
