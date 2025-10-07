package dev.deftu.omnicore.internal.client.events

import dev.deftu.omnicore.api.client.events.ScreenEvent
import dev.deftu.omnicore.api.client.input.KeyboardModifiers
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks
import dev.deftu.omnicore.api.eventBus
import org.jetbrains.annotations.ApiStatus

//#if FABRIC && MC >= 1.16.5
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
//#elseif FORGE-LIKE
//#if MC <= 1.12.2
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//$$ import net.minecraftforge.client.event.GuiScreenEvent
//$$ import org.lwjgl.input.Keyboard
//$$ import org.lwjgl.input.Mouse
//#endif
//$$
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//#endif

@ApiStatus.Internal
public object ScreenEventForwarding {
    public fun initialize() {
        //#if FABRIC && MC >= 1.16.5
        ScreenEvents.BEFORE_INIT.register { _, screen, _, _ ->
            eventBus.post(ScreenEvent.Init.Pre(screen))

            //#if MC >= 1.21.9
            ScreenKeyboardEvents.allowKeyPress(screen).register { _, event ->
                val event = ScreenEvent.KeyPress.Pre(screen, event.keycode, event.scancode, KeyboardModifiers.wrap(event.modifiers))
                eventBus.post(event)
                !event.isCancelled
            }

            ScreenKeyboardEvents.allowKeyRelease(screen).register { _, event ->
                val event = ScreenEvent.KeyRelease.Pre(screen, event.keycode, event.scancode, KeyboardModifiers.wrap(event.modifiers))
                eventBus.post(event)
                !event.isCancelled
            }

            ScreenMouseEvents.allowMouseClick(screen).register { _, event ->
                val event = ScreenEvent.MouseClick.Pre(screen, event.buttonInfo.button, event.x, event.y)
                eventBus.post(event)
                !event.isCancelled
            }

            ScreenMouseEvents.allowMouseRelease(screen).register { _, event ->
                val event = ScreenEvent.MouseRelease.Pre(screen, event.buttonInfo.button, event.x, event.y)
                eventBus.post(event)
                !event.isCancelled
            }
            //#else
            //$$ ScreenKeyboardEvents.allowKeyPress(screen).register { _, keyCode, scancode, modifiers ->
            //$$     val event = ScreenEvent.KeyPress.Pre(screen, keyCode, scancode, KeyboardModifiers.wrap(modifiers))
            //$$     eventBus.post(event)
            //$$     !event.isCancelled
            //$$ }
            //$$
            //$$ ScreenKeyboardEvents.allowKeyRelease(screen).register { _, keyCode, scancode, modifiers ->
            //$$     val event = ScreenEvent.KeyRelease.Pre(screen, keyCode, scancode, KeyboardModifiers.wrap(modifiers))
            //$$     eventBus.post(event)
            //$$     !event.isCancelled
            //$$ }
            //$$
            //$$ ScreenMouseEvents.allowMouseClick(screen).register { _, x, y, button ->
            //$$     val event = ScreenEvent.MouseClick.Pre(screen, button, x, y)
            //$$     eventBus.post(event)
            //$$     !event.isCancelled
            //$$ }
            //$$
            //$$ ScreenMouseEvents.allowMouseRelease(screen).register { _, x, y, button ->
            //$$     val event = ScreenEvent.MouseRelease.Pre(screen, button, x, y)
            //$$     eventBus.post(event)
            //$$     !event.isCancelled
            //$$ }
            //#endif

            ScreenEvents.beforeRender(screen).register { _, ctx, _, _, tickDelta ->
                val matrixStack = OmniMatrixStacks.vanilla(ctx)
                eventBus.post(ScreenEvent.Render.Post(
                    screen,
                    OmniRenderingContext(
                        //#if MC >= 1.20.1
                        ctx,
                        //#endif
                        matrixStack,
                    ),
                    tickDelta
                ))
            }
        }

        ScreenEvents.AFTER_INIT.register { _, screen, _, _ ->
            eventBus.post(ScreenEvent.Init.Post(screen))

            //#if MC >= 1.21.9
            ScreenKeyboardEvents.afterKeyPress(screen).register { _, event ->
                val event = ScreenEvent.KeyPress.Post(screen, event.keycode, event.scancode, KeyboardModifiers.wrap(event.modifiers))
                eventBus.post(event)
            }

            ScreenKeyboardEvents.afterKeyRelease(screen).register { _, event ->
                val event = ScreenEvent.KeyRelease.Post(screen, event.keycode, event.scancode, KeyboardModifiers.wrap(event.modifiers))
                eventBus.post(event)
            }

            ScreenMouseEvents.afterMouseClick(screen).register { _, event, _ ->
                val event = ScreenEvent.MouseClick.Post(screen, event.buttonInfo.button, event.x, event.y)
                eventBus.post(event)
                true
            }

            ScreenMouseEvents.afterMouseRelease(screen).register { _, event, _ ->
                val event = ScreenEvent.MouseRelease.Post(screen, event.buttonInfo.button, event.x, event.y)
                eventBus.post(event)
                true
            }
            //#else
            //$$ ScreenKeyboardEvents.afterKeyPress(screen).register { _, keyCode, scancode, modifiers ->
            //$$     val event = ScreenEvent.KeyPress.Post(screen, keyCode, scancode, KeyboardModifiers.wrap(modifiers))
            //$$     eventBus.post(event)
            //$$ }
            //$$
            //$$ ScreenKeyboardEvents.afterKeyRelease(screen).register { _, keyCode, scancode, modifiers ->
            //$$     val event = ScreenEvent.KeyRelease.Post(screen, keyCode, scancode, KeyboardModifiers.wrap(modifiers))
            //$$     eventBus.post(event)
            //$$ }
            //$$
            //$$ ScreenMouseEvents.afterMouseClick(screen).register { _, x, y, button ->
            //$$     val event = ScreenEvent.MouseClick.Post(screen, button, x, y)
            //$$     eventBus.post(event)
            //$$ }
            //$$
            //$$ ScreenMouseEvents.afterMouseRelease(screen).register { _, x, y, button ->
            //$$     val event = ScreenEvent.MouseRelease.Post(screen, button, x, y)
            //$$     eventBus.post(event)
            //$$ }
            //#endif

            ScreenEvents.afterRender(screen).register { _, ctx, _, _, tickDelta ->
                val matrixStack = OmniMatrixStacks.vanilla(ctx)
                eventBus.post(ScreenEvent.Render.Post(
                    screen,
                    OmniRenderingContext(
                        //#if MC >= 1.20.1
                        ctx,
                        //#endif
                        matrixStack,
                    ),
                    tickDelta
                ))
            }
        }
        //#elseif FORGE-LIKE && MC >= 1.16.5
        //$$ forgeEventBus.addListener<LoaderPreInitScreenEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     eventBus.post(ScreenEvent.Init.Pre(screen))
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPostInitScreenEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     eventBus.post(ScreenEvent.Init.Post(screen))
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPreRenderScreenEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     eventBus.post(ScreenEvent.Render.Pre(
        //$$         screen,
        //$$         OmniRenderingContext(
                    //#if MC >= 1.20.1
                    //$$ graphics = event.guiGraphics,
                    //#endif
        //$$             matrices = OmniMatrixStacks.vanilla(
                        //#if MC >= 1.20.1
                        //$$ event.guiGraphics,
                        //#elseif MC >= 1.18.2
                        //$$ event.poseStack
                        //#else
                        //$$ event.matrixStack
                        //#endif
        //$$             )
        //$$         ),
                    //#if MC >= 1.19.2
                    //$$ tickDelta = event.partialTick,
                    //#elseif MC >= 1.18.2
                    //$$ tickDelta = event.partialTicks,
                    //#else
                    //$$ tickDelta = event.renderPartialTicks,
                    //#endif
        //$$     ))
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPostRenderScreenEvent> { event ->
                //#if MC >= 1.18.2
                //$$ val screen = event.screen
                //#else
                //$$ val screen = event.gui
                //#endif
        //$$     eventBus.post(ScreenEvent.Render.Post(
        //$$         screen,
        //$$         OmniRenderingContext(
                    //#if MC >= 1.20.1
                    //$$ graphics = event.guiGraphics,
                    //#endif
        //$$             matrices = OmniMatrixStacks.vanilla(
                            //#if MC >= 1.20.1
                            //$$ event.guiGraphics,
                            //#elseif MC >= 1.18.2
                            //$$ event.poseStack
                            //#else
                            //$$ event.matrixStack
                            //#endif
        //$$             )
        //$$         ),
                    //#if MC >= 1.19.2
                    //$$ tickDelta = event.partialTick,
                    //#elseif MC >= 1.18.2
                    //$$ tickDelta = event.partialTicks,
                    //#else
                    //$$ tickDelta = event.renderPartialTicks,
                    //#endif
        //$$     ))
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPreKeyPressEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     val ev = ScreenEvent.KeyPress.Pre(screen, event.keyCode, event.scanCode, KeyboardModifiers.wrap(event.modifiers))
        //$$     eventBus.post(ev)
        //$$     event.isCanceled = ev.isCancelled
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPostKeyPressEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     eventBus.post(ScreenEvent.KeyPress.Post(screen, event.keyCode, event.scanCode, KeyboardModifiers.wrap(event.modifiers)))
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPreKeyReleaseEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     val ev = ScreenEvent.KeyRelease.Pre(screen, event.keyCode, event.scanCode, KeyboardModifiers.wrap(event.modifiers))
        //$$     eventBus.post(ev)
        //$$     event.isCanceled = ev.isCancelled
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPostKeyReleaseEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     eventBus.post(ScreenEvent.KeyRelease.Post(screen, event.keyCode, event.scanCode, KeyboardModifiers.wrap(event.modifiers)))
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPreMouseClickEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     val ev = ScreenEvent.MouseClick.Pre(screen, event.button, event.mouseX, event.mouseY)
        //$$     eventBus.post(ev)
        //$$     event.isCanceled = ev.isCancelled
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPostMouseClickEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     eventBus.post(ScreenEvent.MouseClick.Post(screen, event.button, event.mouseX, event.mouseY))
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPreMouseReleaseEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     val ev = ScreenEvent.MouseRelease.Pre(screen, event.button, event.mouseX, event.mouseY)
        //$$     eventBus.post(ev)
        //$$     event.isCanceled = ev.isCancelled
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<LoaderPostMouseReleaseEvent> { event ->
            //#if MC >= 1.18.2
            //$$ val screen = event.screen
            //#else
            //$$ val screen = event.gui
            //#endif
        //$$     eventBus.post(ScreenEvent.MouseRelease.Post(screen, event.button, event.mouseX, event.mouseY))
        //$$ }
        //#elseif FORGE
        //$$ forgeEventBus.register(this)
        //#endif
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onGuiPreInit(event: GuiScreenEvent.InitGuiEvent.Pre) {
    //$$     eventBus.post(ScreenEvent.Init.Pre(event.gui))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPostInit(event: GuiScreenEvent.InitGuiEvent.Post) {
    //$$     eventBus.post(ScreenEvent.Init.Post(event.gui))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPreRender(event: GuiScreenEvent.DrawScreenEvent.Pre) {
    //$$     val context = OmniRenderingContext.from()
    //$$     eventBus.post(ScreenEvent.Render.Pre(event.gui, context, event.renderPartialTicks))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPostRender(event: GuiScreenEvent.DrawScreenEvent.Post) {
    //$$     val context = OmniRenderingContext.from()
    //$$     eventBus.post(ScreenEvent.Render.Post(event.gui, context, event.renderPartialTicks))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPreKeyInput(event: GuiScreenEvent.KeyboardInputEvent.Pre) {
    //$$     val keyCode = Keyboard.getEventKey()
    //$$     val modifiers = KeyboardModifiers.current
    //$$     val action = if (Keyboard.getEventKeyState()) 1 else 0
    //$$     when (action) {
    //$$         1 -> eventBus.post(ScreenEvent.KeyPress.Post(event.gui, keyCode, action, modifiers))
    //$$         0 -> eventBus.post(ScreenEvent.KeyRelease.Pre(event.gui, keyCode, action, modifiers))
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPostKeyInput(event: GuiScreenEvent.KeyboardInputEvent.Post) {
    //$$     val keyCode = Keyboard.getEventKey()
    //$$     val modifiers = KeyboardModifiers.current
    //$$     val action = if (Keyboard.getEventKeyState()) 1 else 0
    //$$     when (action) {
    //$$         1 -> eventBus.post(ScreenEvent.KeyPress.Post(event.gui, keyCode, action, modifiers))
    //$$         0 -> eventBus.post(ScreenEvent.KeyRelease.Post(event.gui, keyCode, action, modifiers))
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPreMouseInput(event: GuiScreenEvent.MouseInputEvent.Pre) {
    //$$     val button = Mouse.getEventButton()
    //$$     val x = Mouse.getEventX().toDouble()
    //$$     val y = Mouse.getEventY().toDouble()
    //$$     val action = if (Mouse.getEventButtonState()) 1 else 0
    //$$     when (action) {
    //$$         1 -> eventBus.post(ScreenEvent.MouseClick.Pre(event.gui, button, x, y))
    //$$         0 -> eventBus.post(ScreenEvent.MouseRelease.Pre(event.gui, button, x, y))
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPostMouseInput(event: GuiScreenEvent.MouseInputEvent.Post) {
    //$$     val button = Mouse.getEventButton()
    //$$     val x = Mouse.getEventX().toDouble()
    //$$     val y = Mouse.getEventY().toDouble()
    //$$     val action = if (Mouse.getEventButtonState()) 1 else 0
    //$$     when (action) {
    //$$         1 -> eventBus.post(ScreenEvent.MouseClick.Post(event.gui, button, x, y))
    //$$         0 -> eventBus.post(ScreenEvent.MouseRelease.Post(event.gui, button, x, y))
    //$$     }
    //$$ }
    //#endif
}
