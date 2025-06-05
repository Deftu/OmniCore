package dev.deftu.omnicore.client.events

import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.client.render.OmniMatrixStack
import dev.deftu.omnicore.common.events.TickEvent

//#if FABRIC
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
//#else
//$$ import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//$$ import net.legacyfabric.fabric.api.client.rendering.v1.HudRenderCallback
//#endif
//#elseif FORGE
//$$ import net.minecraftforge.common.MinecraftForge
//#if MC >= 1.19.2
//$$ import net.minecraftforge.client.event.RenderGuiEvent
//#else
//$$ import net.minecraftforge.client.event.RenderGameOverlayEvent
//#endif
//#if MC <= 1.12.2
//$$ import dev.deftu.omnicore.client.OmniKeyboard
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//$$ import net.minecraftforge.client.event.GuiScreenEvent
//$$ import org.lwjgl.input.Keyboard
//$$ import org.lwjgl.input.Mouse
//#endif
//#else
//$$ import net.neoforged.neoforge.client.event.RenderGuiEvent
//$$ import net.neoforged.neoforge.common.NeoForge
//#endif

//#if MC >= 1.21.1
//$$ import dev.deftu.omnicore.client.render.OmniGameRendering
//#endif

//#if MC >= 1.16.5
import dev.deftu.omnicore.client.toKeyboardModifiers
//#endif

//#if FORGE-LIKE
//$$ private typealias EventHolder =
//#if FORGE
//$$     MinecraftForge
//#elseif NEOFORGE
//$$     NeoForge
//#endif
//$$
//#if MC >= 1.16.5
//$$ // I HATE this.
//$$ private typealias LoaderPreInitScreenEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.Init.Pre
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.InitScreenEvent.Pre
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre
//#endif
//$$ private typealias LoaderPostInitScreenEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.Init.Post
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.InitScreenEvent.Post
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post
//#endif
//$$ private typealias LoaderPreRenderScreenEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.Render.Pre
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent.Pre
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Pre
//#endif
//$$ private typealias LoaderPostRenderScreenEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.Render.Post
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent.Post
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post
//#endif
//$$ private typealias LoaderPreKeyPressEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.KeyPressed.Pre
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.KeyboardKeyPressedEvent.Pre
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyPressedEvent.Pre
//#endif
//$$ private typealias LoaderPostKeyPressEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.KeyPressed.Post
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.KeyboardKeyPressedEvent.Post
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyPressedEvent.Post
//#endif
//$$ private typealias LoaderPreKeyReleaseEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.KeyReleased.Pre
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.KeyboardKeyReleasedEvent.Pre
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyReleasedEvent.Pre
//#endif
//$$ private typealias LoaderPostKeyReleaseEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.KeyReleased.Post
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.KeyboardKeyReleasedEvent.Post
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyReleasedEvent.Post
//#endif
//$$ private typealias LoaderPreMouseClickEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.MouseButtonPressed.Pre
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.MouseClickedEvent.Pre
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent.Pre
//#endif
//$$ private typealias LoaderPostMouseClickEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.MouseButtonPressed.Post
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.MouseClickedEvent.Post
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent.Post
//#endif
//$$ private typealias LoaderPreMouseReleaseEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.MouseButtonReleased.Pre
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.MouseReleasedEvent.Pre
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.MouseReleasedEvent.Pre
//#endif
//$$ private typealias LoaderPostMouseReleaseEvent =
//#if MC >= 1.19.2
//$$     net.minecraftforge.client.event.ScreenEvent.MouseButtonReleased.Post
//#elseif MC >= 1.18.2
//$$     net.minecraftforge.client.event.ScreenEvent.MouseReleasedEvent.Post
//#else
//$$     net.minecraftforge.client.event.GuiScreenEvent.MouseReleasedEvent.Post
//#endif
//#endif
//#endif

public object OmniClientEventPassthrough {

    private var isInitialized = false

    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC
        ClientTickEvents.START_CLIENT_TICK.register { _ ->
            OmniCore.eventBus.post(TickEvent.Client.Pre)
        }

        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            OmniCore.eventBus.post(TickEvent.Client.Post)
        }

        HudRenderCallback.EVENT.register { ctx, tickDelta ->
            val matrixStack = OmniMatrixStack.vanilla(
                //#if MC >= 1.16.5
                ctx
                //#endif
            )

            //#if MC >= 1.21.1
            //$$ val tickDelta = OmniGameRendering.getTickDelta(false)
            //#endif
            OmniCore.eventBus.post(HudRenderEvent(matrixStack, tickDelta))
        }

        //#if MC >= 1.16.5
        ScreenEvents.BEFORE_INIT.register { _, screen, _, _ ->
            OmniCore.eventBus.post(ScreenEvent.Init.Pre(screen))

            ScreenKeyboardEvents.allowKeyPress(screen).register { _, keyCode, scancode, modifiers ->
                val event = ScreenEvent.KeyPress.Pre(screen, keyCode, scancode, modifiers.toKeyboardModifiers())
                OmniCore.eventBus.post(event)
                !event.isCancelled
            }

            ScreenKeyboardEvents.allowKeyRelease(screen).register { _, keyCode, scancode, modifiers ->
                val event = ScreenEvent.KeyRelease.Pre(screen, keyCode, scancode, modifiers.toKeyboardModifiers())
                OmniCore.eventBus.post(event)
                !event.isCancelled
            }

            ScreenMouseEvents.allowMouseClick(screen).register { _, x, y, button ->
                val event = ScreenEvent.MouseClick.Pre(screen, button, x, y)
                OmniCore.eventBus.post(event)
                !event.isCancelled
            }

            ScreenMouseEvents.allowMouseRelease(screen).register { _, x, y, button ->
                val event = ScreenEvent.MouseRelease.Pre(screen, button, x, y)
                OmniCore.eventBus.post(event)
                !event.isCancelled
            }

            ScreenEvents.beforeRender(screen).register { _, ctx, _, _, tickDelta ->
                val matrixStack = OmniMatrixStack.vanilla(ctx)
                val event = ScreenEvent.Render.Pre(screen, matrixStack, tickDelta)
                OmniCore.eventBus.post(event)
            }
        }

        ScreenEvents.AFTER_INIT.register { _, screen, _, _ ->
            OmniCore.eventBus.post(ScreenEvent.Init.Post(screen))

            ScreenKeyboardEvents.afterKeyPress(screen).register { _, keyCode, scancode, modifiers ->
                val event = ScreenEvent.KeyPress.Post(screen, keyCode, scancode, modifiers.toKeyboardModifiers())
                OmniCore.eventBus.post(event)
            }

            ScreenKeyboardEvents.afterKeyRelease(screen).register { _, keyCode, scancode, modifiers ->
                val event = ScreenEvent.KeyRelease.Post(screen, keyCode, scancode, modifiers.toKeyboardModifiers())
                OmniCore.eventBus.post(event)
            }

            ScreenMouseEvents.afterMouseClick(screen).register { _, x, y, button ->
                val event = ScreenEvent.MouseClick.Post(screen, button, x, y)
                OmniCore.eventBus.post(event)
            }

            ScreenMouseEvents.afterMouseRelease(screen).register { _, x, y, button ->
                val event = ScreenEvent.MouseRelease.Post(screen, button, x, y)
                OmniCore.eventBus.post(event)
            }

            ScreenEvents.afterRender(screen).register { _, ctx, _, _, tickDelta ->
                val matrixStack = OmniMatrixStack.vanilla(ctx)
                val event = ScreenEvent.Render.Post(screen, matrixStack, tickDelta)
                OmniCore.eventBus.post(event)
            }
        }
        //#endif
        //#elseif MC >= 1.16.5
        //#if MC >= 1.20.6
        //$$ EventHolder.EVENT_BUS.addListener<net.neoforged.neoforge.client.event.ClientTickEvent.Pre> { event ->
        //$$     OmniCore.eventBus.post(TickEvent.Client.Pre)
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<net.neoforged.neoforge.client.event.ClientTickEvent.Post> { event ->
        //$$     OmniCore.eventBus.post(TickEvent.Client.Post)
        //$$ }
        //#elseif MC >= 1.16.5
        //$$ EventHolder.EVENT_BUS.addListener<net.minecraftforge.event.TickEvent.ClientTickEvent> { event ->
        //$$         when (event.phase) {
        //$$             net.minecraftforge.event.TickEvent.Phase.START -> OmniCore.eventBus.post(TickEvent.Client.Pre)
        //$$             net.minecraftforge.event.TickEvent.Phase.END -> OmniCore.eventBus.post(TickEvent.Client.Post)
        //$$         }
        //$$     }
        //#endif
        //$$
        //#if MC >= 1.19.2
        //$$ EventHolder.EVENT_BUS.addListener<RenderGuiEvent.Post> { event ->
        //$$         val matrixStack = OmniMatrixStack.vanilla(
        //#if MC >= 1.20.1
        //$$              event.guiGraphics
        //#else
        //$$              event.poseStack
        //#endif
        //$$          )
        //$$
        //#if MC >= 1.21.1
        //$$         val tickDelta = OmniGameRendering.getTickDelta(false)
        //#else
        //$$         val tickDelta = event.partialTick
        //#endif
        //$$          OmniCore.eventBus.post(HudRenderEvent(matrixStack, tickDelta))
        //$$     }
        //#elseif MC >= 1.16.5
        //$$ EventHolder.EVENT_BUS.addListener<RenderGameOverlayEvent> { event ->
        //$$     val matrixStack = OmniMatrixStack.vanilla(event.matrixStack)
        //$$     OmniCore.eventBus.post(HudRenderEvent(matrixStack, event.partialTicks))
        //$$ }
        //#endif
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPreInitScreenEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     OmniCore.eventBus.post(ScreenEvent.Init.Pre(screen))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPostInitScreenEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     OmniCore.eventBus.post(ScreenEvent.Init.Post(screen))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPreRenderScreenEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val matrixStack = OmniMatrixStack.vanilla(
        //#if MC >= 1.20.1
        //$$         event.guiGraphics
        //#elseif MC >= 1.18.2
        //$$         event.poseStack
        //#else
        //$$         event.matrixStack
        //#endif
        //$$     )
        //$$
        //#if MC >= 1.19.2
        //$$     val partialTicks = event.partialTick
        //#elseif MC >= 1.18.2
        //$$     val partialTicks = event.partialTicks
        //#else
        //$$     val partialTicks = event.renderPartialTicks
        //#endif
        //$$     OmniCore.eventBus.post(ScreenEvent.Render.Pre(screen, matrixStack, partialTicks))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPostRenderScreenEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val matrixStack = OmniMatrixStack.vanilla(
        //#if MC >= 1.20.1
        //$$         event.guiGraphics
        //#elseif MC >= 1.18.2
        //$$         event.poseStack
        //#else
        //$$         event.matrixStack
        //#endif
        //$$     )
        //$$
        //#if MC >= 1.19.2
        //$$     val partialTicks = event.partialTick
        //#elseif MC >= 1.18.2
        //$$     val partialTicks = event.partialTicks
        //#else
        //$$     val partialTicks = event.renderPartialTicks
        //#endif
        //$$     OmniCore.eventBus.post(ScreenEvent.Render.Post(screen, matrixStack, partialTicks))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPreKeyPressEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val keyCode = event.keyCode
        //$$     val scanCode = event.scanCode
        //$$     val modifiers = event.modifiers.toKeyboardModifiers()
        //$$     OmniCore.eventBus.post(ScreenEvent.KeyPress.Pre(screen, keyCode, scanCode, modifiers))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPostKeyPressEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val keyCode = event.keyCode
        //$$     val scanCode = event.scanCode
        //$$     val modifiers = event.modifiers.toKeyboardModifiers()
        //$$     OmniCore.eventBus.post(ScreenEvent.KeyRelease.Pre(screen, keyCode, scanCode, modifiers))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPreKeyReleaseEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val keyCode = event.keyCode
        //$$     val scanCode = event.scanCode
        //$$     val modifiers = event.modifiers.toKeyboardModifiers()
        //$$     OmniCore.eventBus.post(ScreenEvent.KeyRelease.Pre(screen, keyCode, scanCode, modifiers))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPostKeyReleaseEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val keyCode = event.keyCode
        //$$     val scanCode = event.scanCode
        //$$     val modifiers = event.modifiers.toKeyboardModifiers()
        //$$     OmniCore.eventBus.post(ScreenEvent.KeyRelease.Post(screen, keyCode, scanCode, modifiers))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPreMouseClickEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val button = event.button
        //$$     val x = event.mouseX
        //$$     val y = event.mouseY
        //$$     OmniCore.eventBus.post(ScreenEvent.MouseClick.Pre(screen, button, x, y))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPostMouseClickEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val button = event.button
        //$$     val x = event.mouseX
        //$$     val y = event.mouseY
        //$$     OmniCore.eventBus.post(ScreenEvent.MouseClick.Post(screen, button, x, y))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPreMouseReleaseEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val button = event.button
        //$$     val x = event.mouseX
        //$$     val y = event.mouseY
        //$$     OmniCore.eventBus.post(ScreenEvent.MouseRelease.Pre(screen, button, x, y))
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<LoaderPostMouseReleaseEvent> { event ->
        //#if MC >= 1.18.2
        //$$     val screen = event.screen
        //#else
        //$$     val screen = event.gui
        //#endif
        //$$     val button = event.button
        //$$     val x = event.mouseX
        //$$     val y = event.mouseY
        //$$     OmniCore.eventBus.post(ScreenEvent.MouseRelease.Post(screen, button, x, y))
        //$$ }
        //$$
        //#if MC >= 1.20.6
        //$$ EventHolder.EVENT_BUS.addListener<net.neoforged.neoforge.client.event.RenderFrameEvent.Pre> { event ->
        //$$     OmniCore.eventBus.post(RenderTickEvent.Pre)
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<net.neoforged.neoforge.client.event.RenderFrameEvent.Post> { event ->
        //$$     OmniCore.eventBus.post(RenderTickEvent.Post)
        //$$ }
        //#else
        //$$ EventHolder.EVENT_BUS.addListener<net.minecraftforge.event.TickEvent.RenderTickEvent> { event ->
        //$$     when (event.phase) {
        //$$         net.minecraftforge.event.TickEvent.Phase.START -> OmniCore.eventBus.post(RenderTickEvent.Pre)
        //$$         net.minecraftforge.event.TickEvent.Phase.END -> OmniCore.eventBus.post(RenderTickEvent.Post)
        //$$         else -> {  } // no-op
        //$$     }
        //$$ }
        //#endif
        //#else
        //$$ EventHolder.EVENT_BUS.register(this)
        //#endif

        isInitialized = true
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onClientTick(event: net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent) {
    //$$     when (event.phase) {
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START -> OmniCore.eventBus.post(TickEvent.Client.Pre)
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END -> OmniCore.eventBus.post(TickEvent.Client.Post)
    //$$         else -> {
    //$$             // no-op
    //$$         }
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onRenderGameOverlay(event: RenderGameOverlayEvent.Post) {
    //$$     val matrixStack = OmniMatrixStack()
    //$$     OmniCore.eventBus.post(HudRenderEvent(matrixStack, event.partialTicks))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPreInit(event: GuiScreenEvent.InitGuiEvent.Pre) {
    //$$     OmniCore.eventBus.post(ScreenEvent.Init.Pre(event.gui))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPostInit(event: GuiScreenEvent.InitGuiEvent.Post) {
    //$$     OmniCore.eventBus.post(ScreenEvent.Init.Post(event.gui))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPreRender(event: GuiScreenEvent.DrawScreenEvent.Pre) {
    //$$     val matrixStack = OmniMatrixStack()
    //$$     OmniCore.eventBus.post(ScreenEvent.Render.Pre(event.gui, matrixStack, event.renderPartialTicks))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPostRender(event: GuiScreenEvent.DrawScreenEvent.Post) {
    //$$     val matrixStack = OmniMatrixStack()
    //$$     OmniCore.eventBus.post(ScreenEvent.Render.Post(event.gui, matrixStack, event.renderPartialTicks))
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPreKeyInput(event: GuiScreenEvent.KeyboardInputEvent.Pre) {
    //$$     val keyCode = Keyboard.getEventKey()
    //$$     val modifiers = OmniKeyboard.modifiers
    //$$     val action = if (Keyboard.getEventKeyState()) 1 else 0
    //$$     when (action) {
    //$$         1 -> OmniCore.eventBus.post(ScreenEvent.KeyPress.Pre(event.gui, keyCode, action, modifiers))
    //$$         0 -> OmniCore.eventBus.post(ScreenEvent.KeyRelease.Pre(event.gui, keyCode, action, modifiers))
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onGuiPostKeyInput(event: GuiScreenEvent.KeyboardInputEvent.Post) {
    //$$     val keyCode = Keyboard.getEventKey()
    //$$     val modifiers = OmniKeyboard.modifiers
    //$$     val action = if (Keyboard.getEventKeyState()) 1 else 0
    //$$     when (action) {
    //$$         1 -> OmniCore.eventBus.post(ScreenEvent.KeyPress.Post(event.gui, keyCode, action, modifiers))
    //$$         0 -> OmniCore.eventBus.post(ScreenEvent.KeyRelease.Post(event.gui, keyCode, action, modifiers))
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
    //$$         1 -> OmniCore.eventBus.post(ScreenEvent.MouseClick.Pre(event.gui, button, x, y))
    //$$         0 -> OmniCore.eventBus.post(ScreenEvent.MouseRelease.Pre(event.gui, button, x, y))
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
    //$$         1 -> OmniCore.eventBus.post(ScreenEvent.MouseClick.Post(event.gui, button, x, y))
    //$$         0 -> OmniCore.eventBus.post(ScreenEvent.MouseRelease.Post(event.gui, button, x, y))
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onRenderTick(event: net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent) {
    //$$     when (event.phase) {
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START -> OmniCore.eventBus.post(RenderTickEvent.Pre)
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END -> OmniCore.eventBus.post(RenderTickEvent.Post)
    //$$         else -> {  } // no-op
    //$$     }
    //$$ }
    //#endif

}
