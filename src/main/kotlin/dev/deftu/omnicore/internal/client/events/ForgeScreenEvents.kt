package dev.deftu.omnicore.internal.client.events

// This... This is awful.
// I hate this.

//#if FORGE-LIKE
//#if MC >= 1.16.5
//$$ internal typealias LoaderPreInitScreenEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.Init.Pre
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.InitScreenEvent.Pre
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre
    //#endif
//$$ internal typealias LoaderPostInitScreenEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.Init.Post
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.InitScreenEvent.Post
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post
    //#endif
//$$ internal typealias LoaderPreRenderScreenEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.Render.Pre
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent.Pre
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Pre
    //#endif
//$$ internal typealias LoaderPostRenderScreenEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.Render.Post
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent.Post
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post
    //#endif
//$$ internal typealias LoaderPreKeyPressEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.KeyPressed.Pre
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.KeyboardKeyPressedEvent.Pre
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyPressedEvent.Pre
    //#endif
//$$ internal typealias LoaderPostKeyPressEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.KeyPressed.Post
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.KeyboardKeyPressedEvent.Post
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyPressedEvent.Post
    //#endif
//$$ internal typealias LoaderPreKeyReleaseEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.KeyReleased.Pre
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.KeyboardKeyReleasedEvent.Pre
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyReleasedEvent.Pre
    //#endif
//$$ internal typealias LoaderPostKeyReleaseEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.KeyReleased.Post
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.KeyboardKeyReleasedEvent.Post
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyReleasedEvent.Post
    //#endif
//$$ internal typealias LoaderPreMouseClickEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.MouseButtonPressed.Pre
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.MouseClickedEvent.Pre
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent.Pre
    //#endif
//$$ internal typealias LoaderPostMouseClickEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.MouseButtonPressed.Post
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.MouseClickedEvent.Post
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent.Post
    //#endif
//$$ internal typealias LoaderPreMouseReleaseEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.MouseButtonReleased.Pre
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.MouseReleasedEvent.Pre
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.MouseReleasedEvent.Pre
    //#endif
//$$ internal typealias LoaderPostMouseReleaseEvent =
    //#if MC >= 1.19.2
    //$$ net.neoforged.neoforge.client.event.ScreenEvent.MouseButtonReleased.Post
    //#elseif MC >= 1.18.2
    //$$ net.minecraftforge.client.event.ScreenEvent.MouseReleasedEvent.Post
    //#else
    //$$ net.minecraftforge.client.event.GuiScreenEvent.MouseReleasedEvent.Post
    //#endif
//#endif
//#endif
